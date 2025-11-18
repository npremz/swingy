package be.npremont.swingy.model.event;

import be.npremont.swingy.model.event.enums.EventType;
import be.npremont.swingy.model.event.events.*;
import java.util.ArrayList;
import java.util.List;

public class EventFactory
{
	private List<EventType> usedUniqueEvents;

	public EventFactory()
	{
		this.usedUniqueEvents = new ArrayList<>();
	}

	public IEvent createRandomEvent(int heroLevel)
	{
		EventType type = selectEventType();
		return createEvent(type, heroLevel);
	}

	private EventType selectEventType()
	{
		List<EventType> availableTypes = new ArrayList<>();
		double totalWeight = 0.0;

		for (EventType type : EventType.values())
		{
			if (type.isUnique() && usedUniqueEvents.contains(type))
				continue;

			availableTypes.add(type);
			totalWeight += type.getSpawnWeight();
		}

		double roll = Math.random() * totalWeight;
		double cumulative = 0.0;

		for (EventType type : availableTypes)
		{
			cumulative += type.getSpawnWeight();
			if (roll < cumulative)
			{
				if (type.isUnique())
					usedUniqueEvents.add(type);
				return type;
			}
		}

		return EventType.TREASURE_CHEST;
	}

	private IEvent createEvent(EventType type, int heroLevel)
	{
		switch (type)
		{
			case ALLY_ENCOUNTER:
				return new AllyEncounterEvent(heroLevel);
			case TREASURE_CHEST:
				return new TreasureChestEvent(heroLevel);
			case SHRINE:
				return new ShrineEvent(heroLevel);
			case TRAP:
				return new TrapEvent(heroLevel);
			case AMBUSH:
				return new AmbushEvent(heroLevel);
			default:
				return new TreasureChestEvent(heroLevel);
		}
	}

	public void resetUniqueEvents()
	{
		usedUniqueEvents.clear();
	}
}

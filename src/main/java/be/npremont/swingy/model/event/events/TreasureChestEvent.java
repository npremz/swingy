package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.ItemType;

import java.util.ArrayList;
import java.util.List;

public class TreasureChestEvent implements IEvent
{
	private final int heroLevel;
	private final Item item;

	public TreasureChestEvent(int heroLevel)
	{
		this.heroLevel = heroLevel;
		
		ItemType[] types = ItemType.values();
		ItemType randomType = types[(int)(Math.random() * types.length)];
		this.item = Item.generateRandom(randomType);
	}

	@Override
	public String getTitle()
	{
		return "Mysterious Chest";
	}

	@Override
	public String getDescription()
	{
		return "You discover an old wooden chest partially hidden behind some rocks. " +
			"It appears to be unlocked, but you can't see what's inside from here. " +
			"The chest emanates a faint magical glow...";
	}

	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Open the chest"));
		choices.add(new EventChoice(2, "Leave it alone"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		switch (choiceId)
		{
			case 1:
				return openChest();
			case 2:
				return ignoreChest();
			default:
				return ignoreChest();
		}
	}

	@Override
	public boolean isUnique()
	{
		return false;
	}

	private EventOutcome openChest()
	{
		EventOutcome outcome = new EventOutcome(
			true,
			"You carefully open the chest and find a valuable item inside!\n\n" +
			"Found: " + item.toString()
		);
		
		outcome.addEffect(EffectType.ITEM_GAINED, item);
		
		return outcome;
	}

	private EventOutcome ignoreChest()
	{
		return new EventOutcome(
			true,
			"You decide not to take any risks and continue on your journey. " +
			"The chest remains closed, its secrets hidden forever."
		);
	}
}

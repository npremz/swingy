package be.npremont.swingy.model.event;

import be.npremont.swingy.model.game.GameMap;
import be.npremont.swingy.view.IView;
import be.npremont.swingy.model.entity.Ally;
import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.event.enums.EffectType;
import be.npremont.swingy.model.event.events.ShrineEvent;

import java.util.ArrayList;
import java.util.List;

public class EventManager
{
	private List<EventSpawn> events;
	private EventFactory factory;

	public EventManager()
	{
		this.events = new ArrayList<>();
		this.factory = new EventFactory();
	}

	public void generateEvents(GameMap gameMap, int heroLevel)
	{
		int size = gameMap.getSize();
		int centerX = gameMap.getCenterX();
		int centerY = gameMap.getCenterY();

		int guaranteedEvents = 1;
		int eventsPlaced = 0;

		boolean merchantPlaced = false;
		while (!merchantPlaced)
		{
			int x = (int)(Math.random() * size);
			int y = (int)(Math.random() * size);

			if (x == centerX && y == centerY)
				continue;

			if (gameMap.hasEnemyAt(x, y) || gameMap.hasHealingSpotAt(x, y))
				continue;

			IEvent merchant = factory.createMerchantEvent(heroLevel);
			events.add(new EventSpawn(x, y, merchant));
			merchantPlaced = true;
		}

		while (eventsPlaced < guaranteedEvents)
		{
			int x = (int)(Math.random() * size);
			int y = (int)(Math.random() * size);

			if (x == centerX && y == centerY)
				continue;

			if (gameMap.hasEnemyAt(x, y))
				continue;

			if (gameMap.hasHealingSpotAt(x, y))
				continue;

			if (hasEventAt(x, y))
				continue;

			IEvent event = factory.createRandomEvent(heroLevel);
			events.add(new EventSpawn(x, y, event));
			eventsPlaced++;
		}

		for (int y = 0; y < size; y++)
		{
			for (int x = 0; x < size; x++)
			{
				if (x == centerX && y == centerY)
					continue;

				if (gameMap.hasEnemyAt(x, y))
					continue;

				if (gameMap.hasHealingSpotAt(x, y))
					continue;

				if (hasEventAt(x, y))
					continue;

				if (Math.random() < 0.05)
				{
					IEvent event = factory.createRandomEvent(heroLevel);
					events.add(new EventSpawn(x, y, event));
				}
			}
		}
	}

	public boolean hasEventAt(int x, int y)
	{
		for (EventSpawn spawn : events)
			if (spawn.getX() == x && spawn.getY() == y && !spawn.isTriggered())
				return true;
		return false;
	}

	public EventSpawn getEventAt(int x, int y)
	{
		for (EventSpawn spawn : events)
			if (spawn.getX() == x && spawn.getY() == y)
				return spawn;
		return null;
	}

	public void applyOutcome(EventOutcome outcome, Hero hero, IView view)
	{
		for (EffectType type : outcome.getEffects().keySet())
		{
			Object value = outcome.getEffect(type);

			switch (type)
			{
				case HP_CHANGE:
					int hpChange = (Integer)value;
					if (hpChange > 0)
						hero.getStats().heal(hpChange);
					else
						hero.getStats().takeDamage(-hpChange);
					break;

				case XP_CHANGE:
					int xpChange = (Integer)value;
					hero.getStats().addXp(xpChange);
					break;

				case ALLY_GAINED:
					Ally ally = (Ally)value;
					hero.setAlly(ally);
					break;

				case STAT_BUFF:
					ShrineEvent.StatBoost boost = (ShrineEvent.StatBoost)value;
					applyStatBoost(hero, boost);
					break;

				case ITEM_GAINED:
					Item item = (Item)value;
					handleItemGained(hero, item, view);
					break;

				default:
					break;
			}
		}
	}

	private void applyStatBoost(Hero hero, ShrineEvent.StatBoost boost)
	{
		int value = boost.getValue();
		
		switch (boost.getStatName())
		{
			case "attack":
				hero.getStats().addPermanentAttack(value);
				break;
			case "defense":
				hero.getStats().addPermanentDefense(value);
				break;
			case "hp":
				hero.getStats().addPermanentMaxHp(value);
				break;
		}
	}


	private void handleItemGained(Hero hero, Item item, be.npremont.swingy.view.IView view)
	{
		Item currentItem = null;
		
		switch (item.getType())
		{
			case WEAPON:
				currentItem = hero.getStats().getWeapon();
				break;
			case ARMOR:
				currentItem = hero.getStats().getArmor();
				break;
			case HELM:
				currentItem = hero.getStats().getHelm();
				break;
		}

		boolean equip = view.chooseEquipItem(item, currentItem);

		if (equip)
		{
			hero.getStats().equipItem(item);
			view.displayMessage("\nYou equipped " + item.toString());
		}
		else
		{
			view.displayMessage("\nYou left the item behind.");
		}
	}

	public void resetEvents()
	{
		events.clear();
		factory.resetUniqueEvents();
	}

	// Getters
	public int getEventCount()
	{
		return events.size();
	}
}

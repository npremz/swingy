package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;

import java.util.ArrayList;
import java.util.List;

public class TrapEvent implements IEvent
{
	private final int heroLevel;

	public TrapEvent(int heroLevel)
	{
		this.heroLevel = heroLevel;
	}

	@Override
	public String getTitle()
	{
		return "Suspicious Path";
	}

	@Override
	public String getDescription()
	{
		return "The ground ahead looks disturbed. You notice faint scratch marks and " +
			"what might be a pressure plate hidden beneath some leaves. " +
			"This could be a trap... or just your imagination.";
	}

	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Carefully disarm the trap (High risk, high reward)"));
		choices.add(new EventChoice(2, "Walk around it (Safe but slow)"));
		choices.add(new EventChoice(3, "Rush through quickly (Medium risk)"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		switch (choiceId)
		{
			case 1:
				return disarmTrap(context);
			case 2:
				return avoidTrap();
			case 3:
				return rushThrough(context);
			default:
				return avoidTrap();
		}
	}

	@Override
	public boolean isUnique()
	{
		return false;
	}

	private EventOutcome disarmTrap(GameContext context)
	{
		double roll = Math.random();

		if (roll < 0.60)
		{
			int xpReward = 200 + (heroLevel * 50);

			EventOutcome outcome = new EventOutcome(
				true,
				"With steady hands, you carefully dismantle the trap mechanism. " +
				"Inside, you find a small cache of valuables left by the trap's creator!"
			);

			outcome.addEffect(EffectType.XP_CHANGE, xpReward);

			return outcome;
		}
		else
		{
			int damage = 15 + (heroLevel * 5);

			EventOutcome outcome = new EventOutcome(
				false,
				"Your hand slips! The trap springs to life, and poisoned darts shoot out from hidden holes. " +
				"You manage to dodge most of them, but some find their mark."
			);

			outcome.addEffect(EffectType.HP_CHANGE, -damage);

			return outcome;
		}
	}

	private EventOutcome avoidTrap()
	{
		return new EventOutcome(
			true,
			"You carefully walk around the suspicious area, giving it a wide berth. " +
			"Better safe than sorry. You continue your journey unharmed."
		);
	}

	private EventOutcome rushThrough(GameContext context)
	{
		double roll = Math.random();

		if (roll < 0.50)
		{
			return new EventOutcome(
				true,
				"You sprint across the area with lightning speed! " +
				"If there was a trap, it didn't have time to activate. You're safe!"
			);
		}
		else
		{
			int damage = 10 + (heroLevel * 3);

			EventOutcome outcome = new EventOutcome(
				false,
				"As you run, you hear a click beneath your foot. " +
				"A net springs up and entangles you briefly. " +
				"You manage to cut yourself free, but not without some scrapes and bruises."
			);

			outcome.addEffect(EffectType.HP_CHANGE, -damage);

			return outcome;
		}
	}
}

package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;

import java.util.ArrayList;
import java.util.List;

public class ShrineEvent implements IEvent
{
	private final int heroLevel;
	private final int xpCost;

	public ShrineEvent(int heroLevel)
	{
		this.heroLevel = heroLevel;
		this.xpCost = heroLevel * 300;
	}

	@Override
	public String getTitle()
	{
		return "Ancient Shrine";
	}

	@Override
	public String getDescription()
	{
		return "You discover an ancient shrine emanating divine energy. " +
			"Three statues stand before you, each representing a different aspect of power. " +
			"The shrine whispers: 'Sacrifice your experience, and I shall grant you permanent strength.'\n\n" +
			"Cost: " + xpCost + " XP";
	}

	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Pray for Strength (+10% ATK permanently)"));
		choices.add(new EventChoice(2, "Pray for Resilience (+10% DEF permanently)"));
		choices.add(new EventChoice(3, "Pray for Vitality (+15% Max HP permanently)"));
		choices.add(new EventChoice(4, "Leave the shrine"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		if (choiceId == 4)
			return leaveShrine();

		if (!context.canAffordXp(xpCost))
		{
			return new EventOutcome(
				false,
				"You don't have enough experience to make an offering. " +
				"The shrine remains silent. (Need " + xpCost + " XP)"
			);
		}

		switch (choiceId)
		{
			case 1:
				return prayForStrength(context);
			case 2:
				return prayForResilience(context);
			case 3:
				return prayForVitality(context);
			default:
				return leaveShrine();
		}
	}

	@Override
	public boolean isUnique()
	{
		return true;
	}

	private EventOutcome prayForStrength(GameContext context)
	{
		int currentAttack = context.getHero().getStats().getAttack();
		int bonus = (int)Math.round(currentAttack * 0.10);

		EventOutcome outcome = new EventOutcome(
			true,
			"The statue of the warrior glows with crimson light! " +
			"You feel raw power coursing through your veins.\n\n" +
			"Your attack permanently increases by " + bonus + "!"
		);

		outcome.addEffect(EffectType.XP_CHANGE, -xpCost);
		outcome.addEffect(EffectType.STAT_BUFF, new StatBoost("attack", bonus, true));

		return outcome;
	}

	private EventOutcome prayForResilience(GameContext context)
	{
		int currentDefense = context.getHero().getStats().getDefense();
		int bonus = (int)Math.round(currentDefense * 0.10);

		EventOutcome outcome = new EventOutcome(
			true,
			"The statue of the guardian glows with azure light! " +
			"Your skin hardens like steel.\n\n" +
			"Your defense permanently increases by " + bonus + "!"
		);

		outcome.addEffect(EffectType.XP_CHANGE, -xpCost);
		outcome.addEffect(EffectType.STAT_BUFF, new StatBoost("defense", bonus, true));

		return outcome;
	}

	private EventOutcome prayForVitality(GameContext context)
	{
		int currentMaxHp = context.getHero().getStats().getMaxHp();
		int bonus = (int)Math.round(currentMaxHp * 0.15);

		EventOutcome outcome = new EventOutcome(
			true,
			"The statue of life glows with emerald light! " +
			"You feel your body strengthening from within.\n\n" +
			"Your maximum HP permanently increases by " + bonus + "!"
		);

		outcome.addEffect(EffectType.XP_CHANGE, -xpCost);
		outcome.addEffect(EffectType.STAT_BUFF, new StatBoost("hp", bonus, true));

		return outcome;
	}

	private EventOutcome leaveShrine()
	{
		return new EventOutcome(
			true,
			"You decide not to make an offering. " +
			"The shrine's energy fades as you walk away."
		);
	}

	public static class StatBoost
	{
		private final String statName;
		private final int value;
		private final boolean permanent;

		public StatBoost(String statName, int value, boolean permanent)
		{
			this.statName = statName;
			this.value = value;
			this.permanent = permanent;
		}

		public String getStatName()
		{
			return statName;
		}

		public int getValue()
		{
			return value;
		}

		public boolean isPermanent()
		{
			return permanent;
		}
	}
}

package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;

import java.util.ArrayList;
import java.util.List;

public class AmbushEvent implements IEvent
{
	private final int heroLevel;

	public AmbushEvent(int heroLevel)
	{
		this.heroLevel = heroLevel;
	}

	@Override
	public String getTitle()
	{
		return "AMBUSH!";
	}

	@Override
	public String getDescription()
	{
		return "Suddenly, bandits emerge from the shadows! " +
			"You're surrounded by three armed thugs. " +
			"Their leader steps forward with a wicked grin:\n\n" +
			"'Your gold or your life, traveler!'";
	}

	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Fight them all! (High risk, high reward)"));
		choices.add(new EventChoice(2, "Try to intimidate them (Medium risk)"));
		choices.add(new EventChoice(3, "Surrender some HP to escape (Safe but costly)"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		switch (choiceId)
		{
			case 1:
				return fightBandits(context);
			case 2:
				return intimidateBandits(context);
			case 3:
				return surrender(context);
			default:
				return surrender(context);
		}
	}

	@Override
	public boolean isUnique()
	{
		return false;
	}

	private EventOutcome fightBandits(GameContext context)
	{
		double roll = Math.random();

		int heroHp = context.getHero().getStats().getCurrentHp();
		int heroAtk = context.getHero().getStats().getTotalAttack();
		
		double successChance = 0.40 + (heroAtk / 200.0);
		successChance = Math.min(0.80, successChance);

		if (roll < successChance)
		{
			int damage = 20 + (heroLevel * 3);
			int xpReward = 400 + (heroLevel * 100);

			EventOutcome outcome = new EventOutcome(
				true,
				"You fight with fury! After a brutal battle, the bandits lie defeated at your feet. " +
				"You search their bodies and find valuables.\n\n" +
				"You took some damage in the fight, but you emerged victorious!"
			);

			outcome.addEffect(EffectType.HP_CHANGE, -damage);
			outcome.addEffect(EffectType.XP_CHANGE, xpReward);

			return outcome;
		}
		else
		{
			int damage = 35 + (heroLevel * 5);

			EventOutcome outcome = new EventOutcome(
				false,
				"You fight valiantly, but there are too many of them! " +
				"They overwhelm you with numbers. " +
				"You manage to escape, but you're badly wounded."
			);

			outcome.addEffect(EffectType.HP_CHANGE, -damage);

			return outcome;
		}
	}

	private EventOutcome intimidateBandits(GameContext context)
	{
		double roll = Math.random();

		int heroAtk = context.getHero().getStats().getTotalAttack();
		double successChance = 0.30 + (heroAtk / 300.0);
		successChance = Math.min(0.70, successChance);

		if (roll < successChance)
		{
			int xpReward = 150 + (heroLevel * 30);

			EventOutcome outcome = new EventOutcome(
				true,
				"You draw your weapon and stare them down with cold determination. " +
				"'I've killed things far worse than you,' you growl.\n\n" +
				"The bandits exchange nervous glances. Their leader hesitates, then signals a retreat. " +
				"'Not worth it, lads. Let's find easier prey.'\n\n" +
				"They disappear into the forest. Your reputation grows."
			);

			outcome.addEffect(EffectType.XP_CHANGE, xpReward);

			return outcome;
		}
		else
		{
			// Échec
			int damage = 25 + (heroLevel * 4);

			EventOutcome outcome = new EventOutcome(
				false,
				"The bandits laugh at your threats. " +
				"'Big words for someone outnumbered three to one!'\n\n" +
				"They attack! You manage to fight them off and escape, but you're injured."
			);

			outcome.addEffect(EffectType.HP_CHANGE, -damage);

			return outcome;
		}
	}

	private EventOutcome surrender(GameContext context)
	{
		int damage = 15 + (heroLevel * 2);

		EventOutcome outcome = new EventOutcome(
			true,
			"You raise your hands in surrender. " +
			"The bandits rough you up a bit and take what they can, but let you go.\n\n" +
			"'Smart choice, traveler. Live to fight another day.'\n\n" +
			"You escape with your life, though your pride and body are bruised."
		);

		outcome.addEffect(EffectType.HP_CHANGE, -damage);

		return outcome;
	}
}

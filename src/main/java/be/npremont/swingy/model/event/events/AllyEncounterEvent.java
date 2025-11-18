package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.entity.Ally;
import be.npremont.swingy.model.enums.AllyType;
import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;

import java.util.ArrayList;
import java.util.List;

public class AllyEncounterEvent implements IEvent
{
	private final Ally ally;

	public AllyEncounterEvent(int heroLevel)
	{
		// Choisir un type d'allié aléatoire
		AllyType[] types = AllyType.values();
		AllyType randomType = types[(int)(Math.random() * types.length)];
		
		this.ally = randomType.createAlly(heroLevel);
	}

	@Override
	public String getTitle()
	{
		return "Wounded Traveler";
	}

	@Override
	public String getDescription()
	{
		return "You encounter " + ally.getName() + " sitting by the roadside, wounded.\n" +
			"\n" +
			"'" + getDialogue() + "'\n" +
			"\n" +
			ally.getDescription() + "\n" +
			"\n" +
			"Stats: HP=" + ally.getMaxHp() + " | ATK=" + ally.getAttack() + " | DEF=" + ally.getDefense();
	}


	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Accept their offer (Ally joins for 3 combats)"));
		choices.add(new EventChoice(2, "Decline politely"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		switch (choiceId)
		{
			case 1:
				return acceptAlly();
			case 2:
				return declineAlly();
			default:
				return declineAlly();
		}
	}

	@Override
	public boolean isUnique()
	{
		return false;
	}

	private String getDialogue()
	{
		switch (ally.getType())
		{
			case WANDERING_WARRIOR:
				return "I was ambushed by bandits... If you help me, I'll fight alongside you for a while.";
			case SCOUT_ARCHER:
				return "My leg is injured, but my bow arm is fine. Let me repay your kindness with my arrows.";
			case TRAVELING_MAGE:
				return "I've exhausted my mana in a recent battle. I can still aid you with my knowledge.";
			case SNEAKY_THIEF:
				return "Those guards almost caught me... Help me out and I'll watch your back.";
			default:
				return "Will you help me?";
		}
	}

	private EventOutcome acceptAlly()
	{
		EventOutcome outcome = new EventOutcome(
			true,
			ally.getName() + " stands up and prepares their equipment.\n" +
			"\n" +
			"'Let's go! I'll fight by your side for the next 3 battles.'"
		);

		outcome.addEffect(EffectType.ALLY_GAINED, ally);

		return outcome;
	}

	private EventOutcome declineAlly()
	{
		return new EventOutcome(
			true,
			ally.getName() + " nods respectfully.\n" +
			"\n" +
			"'I understand. Safe travels, friend.'"
		);
	}

}

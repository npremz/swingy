package be.npremont.swingy.model.event.events;

import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.enums.EffectType;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.ItemType;
import be.npremont.swingy.model.enums.ItemRarity;

import java.util.ArrayList;
import java.util.List;

public class MysteryMerchantEvent implements IEvent
{
	private final int heroLevel;
	private final Item jackpotItem;

	public MysteryMerchantEvent(int heroLevel)
	{
		this.heroLevel = heroLevel;
		this.jackpotItem = generateJackpotItem();
	}

	private Item generateJackpotItem()
	{
		ItemRarity rarity = ItemRarity.LEGENDARY;
		
		ItemType[] types = ItemType.values();
		ItemType randomType = types[(int)(Math.random() * types.length)];
		
		int flatBonus = rarity.generateFlatBonus() + 5;
		double percentBonus = rarity.generatePercentBonus() + 2.0;
		String name = generatePremiumName(randomType, rarity);
		
		return new Item(randomType, rarity, name, flatBonus, percentBonus);
	}

	private String generatePremiumName(ItemType type, ItemRarity rarity)
	{
		String[] epicPrefixes = new String[]{"Mystic", "Enchanted", "Blessed", "Royal", "Ancient"};
		String[] legendaryPrefixes = new String[]{"Legendary", "Mythical", "Divine", "Celestial", "Eternal"};
		
		String[] prefixes = rarity == ItemRarity.LEGENDARY ? legendaryPrefixes : epicPrefixes;
		String prefix = prefixes[(int)(Math.random() * prefixes.length)];
		
		String[] bases;
		switch (type)
		{
			case WEAPON:
				bases = new String[]{"Excalibur", "Dragonslayer", "Nightbane", "Stormbreaker", "Soulreaver"};
				break;
			case ARMOR:
				bases = new String[]{"Plate of Dragons", "Guardian's Mail", "Unbreakable Vest", "Cosmic Cuirass"};
				break;
			case HELM:
				bases = new String[]{"Crown of Kings", "Dragon's Hood", "Celestial Circlet", "Abyss Helm"};
				break;
			default:
				bases = new String[]{"Item"};
		}
		
		String base = bases[(int)(Math.random() * bases.length)];
		return prefix + " " + base;
	}

	@Override
	public String getTitle()
	{
		return "MYSTERY MERCHANT";
	}

	@Override
	public String getDescription()
	{
		return "A cloaked figure emerges from the shadows, eyes gleaming with an otherworldly light.\n\n" +
			"'Greetings, traveler... I have a ONCE-IN-A-LIFETIME proposition for you.'\n\n" +
			"The merchant reveals a shimmering item:\n\n" +
			jackpotItem.toString() + "\n\n" +
			"'But everything has a price...'\n\n" +
			"STAKES OF THIS GAMBLE:\n" +
			"→ YOUR EQUIPPED ITEMS (all of them)\n" +
			"→ 30% OF YOUR CURRENT XP\n\n" +
			"POTENTIAL REWARDS:\n" +
			"60% chance: NOTHING (you lose everything)\n" +
			"15% chance: Rare item + 1000 XP (minus stake)\n" +
			"15% chance: Epic item + 2500 XP (minus stake)\n" +
			"10% chance: LEGENDARY JACKPOT - " + jackpotItem.getName() + " + 10000 XP (Pure Bonus!)\n\n" +
			"'So, what do you say?'";
	}

	@Override
	public List<EventChoice> getEventChoices()
	{
		List<EventChoice> choices = new ArrayList<>();
		choices.add(new EventChoice(1, "Accept the gamble (RISKY)"));
		choices.add(new EventChoice(2, "Walk away (Safe)"));
		return choices;
	}

	@Override
	public EventOutcome trigger(GameContext context, int choiceId)
	{
		switch (choiceId)
		{
			case 1:
				return acceptGamble(context);
			case 2:
				return walkAway();
			default:
				return walkAway();
		}
	}

	@Override
	public boolean isUnique()
	{
		return false;
	}

	private EventOutcome acceptGamble(GameContext context)
	{
		double roll = Math.random() * 100.0;
		
		int xpLost = (int)(context.getHero().getStats().getXp() * 0.30);


		if (roll < 10.0)
		{
			return jackpotWin(context, xpLost);
		}
		else if (roll < 25.0)
		{
			return epicWin(context, xpLost);
		}
		else if (roll < 40.0)
		{
			return rareWin(context, xpLost);
		}
		else
		{
			return totalLoss(context, xpLost);
		}
	}

	private EventOutcome jackpotWin(GameContext context, int xpLost)
	{
		EventOutcome outcome = new EventOutcome(
			true,
			"The merchant's grin widens unnaturally...\n\n" +
			"'CONGRATULATIONS! Against all odds, FATE SMILES UPON YOU!'\n\n" +
			"The merchant hands you the most magnificent item you've ever laid eyes on.\n\n" +
			"" + jackpotItem.getName() + "\n\n" +
			"Golden light emanates from the object. You feel the weight of destiny itself.\n\n" +
			"'You've won the ETERNAL JACKPOT! Take your prize and go... before luck turns against you.'\n\n" +
			"The merchant vanishes in a cloud of smoke, leaving only the legendary item in your hands.\n\n" +
			"LEGENDARY ITEM ACQUIRED\n" +
			"+10000 XP GAINED"
		);
		
		outcome.addEffect(EffectType.ITEM_GAINED, jackpotItem);
		outcome.addEffect(EffectType.XP_CHANGE, 10000);
		
		return outcome;
	}

	private EventOutcome epicWin(GameContext context, int xpLost)
	{
		Item epicItem = generateItem(ItemRarity.EPIC);
		
		EventOutcome outcome = new EventOutcome(
			true,
			"The merchant nods slowly...\n\n" +
			"'Well, well... Lady Luck favors you today, traveler.'\n\n" +
			"The merchant hands you an exquisite item.\n\n" +
			"" + epicItem.toString() + "\n\n" +
			"'A fine prize indeed. Perhaps you should leave now, before your luck changes...'\n\n" +
			"You secured a valuable item, but not the ultimate prize."
		);
		
		outcome.addEffect(EffectType.ITEM_GAINED, epicItem);
		outcome.addEffect(EffectType.XP_CHANGE, 2500 - xpLost);
		
		return outcome;
	}

	private EventOutcome rareWin(GameContext context, int xpLost)
	{
		Item rareItem = generateItem(ItemRarity.RARE);
		
		EventOutcome outcome = new EventOutcome(
			true,
			"The merchant tilts their head...\n\n" +
			"'Hmm. Your luck holds, barely.'\n\n" +
			"You receive a rare item.\n\n" +
			"" + rareItem.toString() + "\n\n" +
			"The merchant seems disappointed.\n" +
			"'A modest victory. You may keep your life... and this trinket.'"
		);
		
		outcome.addEffect(EffectType.ITEM_GAINED, rareItem);
		outcome.addEffect(EffectType.XP_CHANGE, 1000 - xpLost);
		
		return outcome;
	}

	private EventOutcome totalLoss(GameContext context, int xpLost)
	{
		EventOutcome outcome = new EventOutcome(
			false,
			"The merchant's laughter echoes through your mind...\n\n" +
			"'How PATHETIC! Did you really think you could beat destiny?'\n\n" +
			"The merchant waves their hand. Your equipped items vanish in a blinding light.\n\n" +
			"'Your stakes are FORFEIT. Pleasure doing business with you.'\n\n" +
			"The merchant fades away, leaving you stripped of your equipment.\n\n" +
			"ALL EQUIPPED ITEMS LOST\n" +
			"30% OF XP LOST\n\n" +
			"You stand in silence, your wealth and power completely drained.\n" +
			"The words echo in your mind: 'The house always wins...'"
		);
		
		if (context.getHero().getStats().hasWeapon())
		{
			context.getHero().getStats().equipWeapon(null);
		}
		if (context.getHero().getStats().hasArmor())
		{
			context.getHero().getStats().equipArmor(null);
		}
		if (context.getHero().getStats().hasHelm())
		{
			context.getHero().getStats().equipHelm(null);
		}
		
		outcome.addEffect(EffectType.XP_CHANGE, -xpLost);
		
		return outcome;
	}

	private EventOutcome walkAway()
	{
		return new EventOutcome(
			true,
			"You take a step back, your instincts screaming danger.\n\n" +
			"'A wise choice,' the merchant whispers, almost disappointed.\n\n" +
			"'Perhaps our paths will cross again... when your resolve weakens.'\n\n" +
			"The figure dissolves into shadow, and you continue your journey.\n" +
			"You managed to resist temptation... this time."
		);
	}

	private Item generateItem(ItemRarity rarity)
	{
		ItemType[] types = ItemType.values();
		ItemType randomType = types[(int)(Math.random() * types.length)];
		
		int flatBonus = rarity.generateFlatBonus();
		double percentBonus = rarity.generatePercentBonus();
		String name = generateName(randomType, rarity);
		
		return new Item(randomType, rarity, name, flatBonus, percentBonus);
	}

	private String generateName(ItemType type, ItemRarity rarity)
	{
		String[] prefixes;
		if (rarity == ItemRarity.EPIC)
			prefixes = new String[]{"Masterwork", "Exquisite", "Royal", "Ancient"};
		else
			prefixes = new String[]{"Superior", "Enchanted", "Blessed", "Reinforced"};
		
		String[] bases;
		switch (type)
		{
			case WEAPON:
				bases = new String[]{"Sword", "Blade", "Axe", "Mace", "Dagger"};
				break;
			case ARMOR:
				bases = new String[]{"Plate", "Mail", "Vest", "Cuirass", "Breastplate"};
				break;
			case HELM:
				bases = new String[]{"Helmet", "Crown", "Hood", "Cap", "Circlet"};
				break;
			default:
				bases = new String[]{"Item"};
		}
		
		String prefix = prefixes[(int)(Math.random() * prefixes.length)];
		String base = bases[(int)(Math.random() * bases.length)];
		return prefix + " " + base;
	}
}

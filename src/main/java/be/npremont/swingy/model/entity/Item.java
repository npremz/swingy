package be.npremont.swingy.model.entity;

import be.npremont.swingy.model.enums.ItemRarity;
import be.npremont.swingy.model.enums.ItemType;

public class Item
{
	private final ItemType type;
	private final ItemRarity rarity;
	private final String name;
	private final int flat_bonus;
	private final double percent_bonus;

	public Item(ItemType type, ItemRarity rarity, String name, int flat_bonus, double percent_bonus)
	{
		this.type = type;
		this.rarity = rarity;
		this.name = name;
		this.flat_bonus = flat_bonus;
		this.percent_bonus = percent_bonus;
	}

	// Factory method 
	public static Item generateRandom(ItemType type)
	{
		ItemRarity rarity = rollRarity();
		int flat_bonus = rarity.generateFlatBonus();
		double percent_bonus = rarity.generatePercentBonus();
		String name = generateName(type, rarity);
		
		return new Item(type, rarity, name, flat_bonus, percent_bonus);
	}

	private static ItemRarity rollRarity()
	{
		double roll = Math.random() * 100.0;
		double cumulative = 0.0;

		for (ItemRarity rarity : ItemRarity.values())
		{
			cumulative += rarity.getDropChance();
			if (roll < cumulative)
			{
				return rarity;
			}
		}

		return ItemRarity.COMMON;
	}

	private static String generateName(ItemType type, ItemRarity rarity)
	{
		String[] prefixes = getPrefixes(rarity);
		String[] bases = getBases(type);
		
		String prefix = prefixes[(int)(Math.random() * prefixes.length)];
		String base = bases[(int)(Math.random() * bases.length)];
		
		return prefix + " " + base;
	}

	private static String[] getPrefixes(ItemRarity rarity)
	{
		switch (rarity)
		{
			case COMMON:
				return new String[]{"Old", "Rusty", "Worn", "Crude"};
			case UNCOMMON:
				return new String[]{"Sturdy", "Fine", "Quality", "Solid"};
			case RARE:
				return new String[]{"Superior", "Enchanted", "Blessed", "Reinforced"};
			case EPIC:
				return new String[]{"Masterwork", "Exquisite", "Royal", "Ancient"};
			case LEGENDARY:
				return new String[]{"Legendary", "Mythical", "Divine", "Celestial"};
			default:
				return new String[]{"Strange"};
		}
	}

	private static String[] getBases(ItemType type)
	{
		switch (type)
		{
			case WEAPON:
				return new String[]{"Sword", "Blade", "Axe", "Mace", "Dagger"};
			case ARMOR:
				return new String[]{"Plate", "Mail", "Vest", "Cuirass", "Breastplate"};
			case HELM:
				return new String[]{"Helmet", "Crown", "Hood", "Cap", "Circlet"};
			default:
				return new String[]{"Item"};
		}
	}

	public int calculateTotalBonus(int baseStat)
	{
		return (int)Math.round((baseStat + flat_bonus) * (1 + percent_bonus / 100.0)) - baseStat;
	}

	// Getters
	public ItemType getType()
	{
		return type;
	}

	public ItemRarity getRarity()
	{
		return rarity;
	}

	public String getName()
	{
		return name;
	}

	public int getFlatBonus()
	{
		return flat_bonus;
	}

	public double getPercentBonus()
	{
		return percent_bonus;
	}

	@Override
	public String toString()
	{
		return "[" + rarity.getDisplayName().toUpperCase() + "] " + name + 
			" (+" + flat_bonus + " + " + String.format("%.1f", percent_bonus) + "% " + 
			type.getStatName() + ")";
	}
}

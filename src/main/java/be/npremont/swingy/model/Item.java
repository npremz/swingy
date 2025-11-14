package be.npremont.swingy.model;

public class Item
{
	private final ItemType type;
	private final ItemRarity rarity;
	private final String name;
	private final int bonus;

	public Item(ItemType type, ItemRarity rarity, String name, int bonus)
	{
		this.type = type;
		this.rarity = rarity;
		this.name = name;
		this.bonus = bonus;
	}

	// Factory method 
	public static Item generateRandom(ItemType type)
	{
		ItemRarity rarity = rollRarity();
		int bonus = rarity.generateBonus();
		String name = generateName(type, rarity);
		
		return new Item(type, rarity, name, bonus);
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

	public int getBonus()
	{
		return bonus;
	}

	@Override
	public String toString()
	{
		return "[" + rarity.getDisplayName().toUpperCase() + "] " + name + " (+" + bonus + " " + type.getStatName() + ")";
	}
}

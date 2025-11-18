package be.npremont.swingy.model.enums;

import be.npremont.swingy.model.entity.Ally;

public enum AllyType
{
	WANDERING_WARRIOR(
		"Gareth the Wanderer",
		"A battle-hardened warrior. Absorbs 20% of damage dealt to you.",
		80,
		12,
		8
	),
	SCOUT_ARCHER(
		"Lyra the Swift",
		"A skilled archer. 25% chance to deal critical hits (x2 damage).",
		60,
		15,
		5
	),
	TRAVELING_MAGE(
		"Eldrin the Wise",
		"A mysterious mage. Heals you for 5% max HP after each combat.",
		50,
		10,
		4
	),
	SNEAKY_THIEF(
		"Shadow the Rogue",
		"A cunning thief. Increases your flee chance by 15%.",
		70,
		13,
		6
	);

	private final String defaultName;
	private final String description;
	private final int baseHp;
	private final int baseAttack;
	private final int baseDefense;

	AllyType(String defaultName, String description, int baseHp, int baseAttack, int baseDefense)
	{
		this.defaultName = defaultName;
		this.description = description;
		this.baseHp = baseHp;
		this.baseAttack = baseAttack;
		this.baseDefense = baseDefense;
	}

	public Ally createAlly(int heroLevel)
	{
		int scaledHp = baseHp + (heroLevel * 5);
		int scaledAttack = baseAttack + (heroLevel * 2);
		int scaledDefense = baseDefense + (heroLevel * 1);

		return new Ally(this, defaultName, scaledHp, scaledAttack, scaledDefense);
	}

	// Getters
	public String getDefaultName()
	{
		return defaultName;
	}

	public String getDescription()
	{
		return description;
	}

	public int getBaseHp()
	{
		return baseHp;
	}

	public int getBaseAttack()
	{
		return baseAttack;
	}

	public int getBaseDefense()
	{
		return baseDefense;
	}
}

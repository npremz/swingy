package be.npremont.swingy.model;

public enum EnemyType
{
	GOBLIN(
		"Goblin",
		30,
		8,
		3,
		340
	),
	ORC(
		"Orc",
		50,
		12,
		7,
		500
	),
	DRAGON(
		"Dragon",
		80,
		18,
		12,
		1500
	);

	private final String display_name;
	private final int base_hp;
	private final int base_attack;
	private final int base_defense;
	private final int xp_reward;

	EnemyType(String display_name, int base_hp, int base_attack, int base_defense, int xp_reward)
	{
		this.display_name = display_name;
		this.base_hp = base_hp;
		this.base_attack = base_attack;
		this.base_defense = base_defense;
		this.xp_reward = xp_reward;
	}

	// Getters
	public String getDisplayName()
	{
		return display_name;
	}

	public int getBaseHp()
	{
		return base_hp;
	}

	public int getBaseAttack()
	{
		return base_attack;
	}

	public int getBaseDefense()
	{
		return base_defense;
	}

	public int getXpReward()
	{
		return xp_reward;
	}

	// Getters with relative scaling
	public int getScaledHp(int heroLevel)
	{
		return (int)(base_hp * (1 + (heroLevel - 1) * 0.25));
	}

	public int getScaledAttack(int heroLevel)
	{
		return (int)(base_attack * (1 + (heroLevel - 1) * 0.25));
	}

	public int getScaledDefense(int heroLevel)
	{
		return (int)(base_defense * (1 + (heroLevel - 1) * 0.25));
	}
}

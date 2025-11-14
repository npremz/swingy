package be.npremont.swingy.model;

public enum EnemyType
{
	GOBLIN(
		"Goblin",
		30,
		8,
		3,
		50
	),
	ORC(
		"Orc",
		50,
		12,
		7,
		100
	),
	DRAGON(
		"Dragon",
		80,
		18,
		12,
		200
	);

	private final String displayName;
	private final int baseHp;
	private final int baseAttack;
	private final int baseDefense;
	private final int xpReward;

	EnemyType(String displayName, int baseHp, int baseAttack, int baseDefense, int xpReward)
	{
		this.displayName = displayName;
		this.baseHp = baseHp;
		this.baseAttack = baseAttack;
		this.baseDefense = baseDefense;
		this.xpReward = xpReward;
	}

	// Getters
	public String getDisplayName()
	{
		return displayName;
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

	public int getXpReward()
	{
		return xpReward;
	}

	// Getters with relative scaling
	public int getScaledHp(int heroLevel)
	{
		return (int)(baseHp * (1 + (heroLevel - 1) * 0.25));
	}

	public int getScaledAttack(int heroLevel)
	{
		return (int)(baseAttack * (1 + (heroLevel - 1) * 0.25));
	}

	public int getScaledDefense(int heroLevel)
	{
		return (int)(baseDefense * (1 + (heroLevel - 1) * 0.25));
	}
}

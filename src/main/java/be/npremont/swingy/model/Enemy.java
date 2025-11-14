package be.npremont.swingy.model;

public class Enemy
{
	private final EnemyType type;
	private int currentHp;
	private final int maxHp;
	private final int attack;
	private final int defense;
	private final int xpReward;

	public Enemy(EnemyType type, int heroLevel)
	{
		this.type = type;
		this.maxHp = type.getScaledHp(heroLevel);
		this.currentHp = this.maxHp;
		this.attack = type.getScaledAttack(heroLevel);
		this.defense = type.getScaledDefense(heroLevel);
		this.xpReward = type.getXpReward();
	}

	public void takeDamage(int damage)
	{
		this.currentHp = Math.max(0, this.currentHp - damage);
	}

	public boolean isAlive()
	{
		return currentHp > 0;
	}

	// Getters
	public EnemyType getType()
	{
		return type;
	}

	public int getCurrentHp()
	{
		return currentHp;
	}

	public int getMaxHp()
	{
		return maxHp;
	}

	public int getAttack()
	{
		return attack;
	}

	public int getDefense()
	{
		return defense;
	}

	public int getXpReward()
	{
		return xpReward;
	}
}

package be.npremont.swingy.model;

public class BaseStats
{
	private final int hp;
	private final int attack;
	private final int defense;

	public BaseStats(int hp, int attack, int defense)
	{
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
	}

	// Getters
	public int getHp()
	{
		return hp;
	}

	public int getAttack()
	{
		return attack;
	}

	public int getDefense()
	{
		return defense;
	}
}

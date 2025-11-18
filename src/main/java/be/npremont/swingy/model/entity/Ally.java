package be.npremont.swingy.model.entity;

import be.npremont.swingy.model.enums.AllyType;

public class Ally
{
	private final AllyType type;
	private final String name;
	private int currentHp;
	private final int maxHp;
	private final int attack;
	private final int defense;
	private int remainingCombats;

	public Ally(AllyType type, String name, int maxHp, int attack, int defense)
	{
		this.type = type;
		this.name = name;
		this.maxHp = maxHp;
		this.currentHp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.remainingCombats = 3;
	}

	public void takeDamage(int damage)
	{
		this.currentHp = Math.max(0, this.currentHp - damage);
	}

	public boolean isAlive()
	{
		return currentHp > 0;
	}

	public void decrementCombats()
	{
		if (remainingCombats > 0)
			remainingCombats--;
	}

	public String getDescription()
	{
		return type.getDescription();
	}

	// Getters
	public AllyType getType()
	{
		return type;
	}

	public String getName()
	{
		return name;
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

	public int getRemainingCombats()
	{
		return remainingCombats;
	}
}

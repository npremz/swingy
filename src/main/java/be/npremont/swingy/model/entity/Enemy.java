package be.npremont.swingy.model.entity;

import be.npremont.swingy.model.enums.EnemyType;

public class Enemy
{
	private final EnemyType type;
	private int current_hp;
	private final int max_hp;
	private final int attack;
	private final int defense;
	private final int xp_reward;

	public Enemy(EnemyType type, int heroLevel)
	{
		this.type = type;
		this.max_hp = type.getScaledHp(heroLevel);
		this.current_hp = this.max_hp;
		this.attack = type.getScaledAttack(heroLevel);
		this.defense = type.getScaledDefense(heroLevel);
		this.xp_reward = type.getXpReward();
	}

	public void takeDamage(int damage)
	{
		this.current_hp = Math.max(0, this.current_hp - damage);
	}

	public boolean isAlive()
	{
		return current_hp > 0;
	}

	// Getters
	public EnemyType getType()
	{
		return type;
	}

	public int getCurrentHp()
	{
		return current_hp;
	}

	public int getMaxHp()
	{
		return max_hp;
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
		return xp_reward;
	}
}

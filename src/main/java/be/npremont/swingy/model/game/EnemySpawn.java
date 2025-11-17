package be.npremont.swingy.model.game;

import be.npremont.swingy.model.entity.Enemy;

public class EnemySpawn
{
	private final int x;
	private final int y;
	private final Enemy enemy;

	public EnemySpawn(int x, int y, Enemy enemy)
	{
		this.x = x;
		this.y = y;
		this.enemy = enemy;
	}

	// Getters
	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public Enemy getEnemy()
	{
		return enemy;
	}
}

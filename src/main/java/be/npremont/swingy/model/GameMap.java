package be.npremont.swingy.model;

import java.util.ArrayList;
import java.util.List;

public class GameMap
{
	private int size;
	private List<EnemySpawn> enemies;
	private int centerX;
	private int centerY;

	public GameMap(int size, int heroLevel)
	{
		this.size = size;
		this.centerX = size / 2;
		this.centerY = size / 2;
		this.enemies = new ArrayList<>();
		generateEnemies(heroLevel);
	}

	private void generateEnemies(int heroLevel)
	{
		int totalCells = size * size;
		int targetEnemies = (int)(totalCells * 0.25); // 25% Density
		int enemiesPlaced = 0;

		while (enemiesPlaced < targetEnemies)
		{
			int x = (int)(Math.random() * size);
			int y = (int)(Math.random() * size);

			if (x == centerX && y == centerY)
				continue;

			if (hasEnemyAt(x, y))
				continue;

			double distance = calculateDistance(x, y, centerX, centerY);
			double maxDistance = calculateDistance(0, 0, centerX, centerY);
			double normalizedDistance = distance / maxDistance; // 0.0 à 1.0

			EnemyType type = selectEnemyType(normalizedDistance);
			Enemy enemy = new Enemy(type, heroLevel);
			enemies.add(new EnemySpawn(x, y, enemy));
			enemiesPlaced++;
		}
	}

	private EnemyType selectEnemyType(double normalizedDistance)
	{
		double roll = Math.random() * 100.0;

		if (normalizedDistance < 0.33)
		{
			if (roll < 70.0)
				return EnemyType.GOBLIN;
			else if (roll < 95.0)
				return EnemyType.ORC;
			else
				return EnemyType.DRAGON;
		}
		else if (normalizedDistance < 0.66)
		{
			if (roll < 40.0)
				return EnemyType.GOBLIN;
			else if (roll < 85.0)
				return EnemyType.ORC;
			else
				return EnemyType.DRAGON;
		}
		else
		{
			if (roll < 15.0)
				return EnemyType.GOBLIN;
			else if (roll < 60.0)
				return EnemyType.ORC;
			else
				return EnemyType.DRAGON;
		}
	}

	private double calculateDistance(int x1, int y1, int x2, int y2)
	{
		int dx = x2 - x1;
		int dy = y2 - y1;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public boolean isOutOfBounds(int x, int y)
	{
		return x < 0 || x >= size || y < 0 || y >= size;
	}

	public boolean hasEnemyAt(int x, int y)
	{
		for (EnemySpawn spawn : enemies)
			if (spawn.getX() == x && spawn.getY() == y)
				return true;
		return false;
	}

	public Enemy getEnemyAt(int x, int y)
	{
		for (EnemySpawn spawn : enemies)
			if (spawn.getX() == x && spawn.getY() == y)
				return spawn.getEnemy();
		return null;
	}

	public void removeEnemyAt(int x, int y)
	{
		enemies.removeIf(spawn -> spawn.getX() == x && spawn.getY() == y);
	}

	public double getDistanceFromCenter(int x, int y)
	{
		return calculateDistance(x, y, centerX, centerY);
	}

	public int getDistanceToEdge(int x, int y)
	{
		int distanceToTop = y;
		int distanceToBottom = size - 1 - y;
		int distanceToLeft = x;
		int distanceToRight = size - 1 - x;

		return Math.min(
			Math.min(distanceToTop, distanceToBottom),
			Math.min(distanceToLeft, distanceToRight)
		);
	}

	// Getters
	public int getSize()
	{
		return size;
	}

	public int getCenterX()
	{
		return centerX;
	}

	public int getCenterY()
	{
		return centerY;
	}

	public int getEnemyCount()
	{
		return enemies.size();
	}
}

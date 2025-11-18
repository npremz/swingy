package be.npremont.swingy.model.event;

import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.game.GameMap;

public class GameContext
{
	private final Hero hero;
	private final GameMap gameMap;
	private final int currentLevel;

	public GameContext(Hero hero, GameMap gameMap, int currentLevel)
	{
		this.hero = hero;
		this.gameMap = gameMap;
		this.currentLevel = currentLevel;
	}

	// Getters
	public Hero getHero()
	{
		return hero;
	}

	public GameMap getGameMap()
	{
		return gameMap;
	}

	public int getCurrentLevel()
	{
		return currentLevel;
	}

	// Helper methods
	public boolean canAffordXp(int amount)
	{
		return hero.getStats().getXp() >= amount;
	}

	public boolean hasFullHealth()
	{
		return hero.getStats().getCurrentHp() >= hero.getStats().getMaxHpWithItems();
	}
}

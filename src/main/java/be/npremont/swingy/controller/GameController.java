package be.npremont.swingy.controller;

import be.npremont.swingy.model.GameMap;
import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.HeroClass;
import be.npremont.swingy.model.Enemy;
import be.npremont.swingy.model.Item;
import be.npremont.swingy.model.Combat;
import be.npremont.swingy.model.Direction;
import be.npremont.swingy.view.IView;

public class GameController
{
	private Hero hero;
	private GameMap game_map;
	private IView view;

	public GameController(IView view)
	{
		this.view = view;
	}

	public void start()
	{
		String name = view.getUserInput("Enter hero name: ");

		HeroClass heroClass = view.chooseHeroClass();
		view.displayMessage("\nYou chose " + heroClass.getDisplayName() + "!");

		int heroLevel = 1;
		int map_size = calculateMapSize(heroLevel);

		game_map = new GameMap(map_size, heroLevel);

		int center = map_size / 2;
		hero = new Hero(name, heroClass, center, center);


		view.displayHeroStats(hero);

		view.displayMessage("\nGame started! Reach the edge of the map to win!");
		view.displayMessage("Enemies: " + game_map.getEnemyCount() + " scattered across the map.");
		view.displayMessage("You cannot see them... be careful!");

		gameLoop();
	}

	private int calculateMapSize(int level)
	{
		return (level - 1) * 5 + 10 - (level % 2);
	}

	private void gameLoop()
	{
		boolean running = true;

		while (running)
		{
			double distanceFromCenter = game_map.getDistanceFromCenter(hero.getX(), hero.getY());
			int distanceToEdge = game_map.getDistanceToEdge(hero.getX(), hero.getY());
			
			if (view instanceof be.npremont.swingy.view.ConsoleView)
				((be.npremont.swingy.view.ConsoleView) view).displayMap(hero, game_map);
			
			view.displayGameStatus(hero, game_map.getSize(), distanceFromCenter, distanceToEdge);

			Direction dir = view.getDirection(hero);

			if (dir == null)
			{
				view.displayMessage("Terminating the session.");
				running = false;
				continue;
			}

			int new_x = hero.getX() + dir.getDx();
			int new_y = hero.getY() + dir.getDy();

			if (game_map.isOutOfBounds(new_x, new_y))
			{
				view.displayMessage("\n🏆 LEVEL COMPLETE! 🏆");
				view.displayMessage("You reached the edge of the map!");
				
				if (!nextLevel())
					running = false;
				continue;
			}

			hero.move(dir.getDx(), dir.getDy());

			if (game_map.hasEnemyAt(hero.getX(), hero.getY()))
			{
				Enemy enemy = game_map.getEnemyAt(hero.getX(), hero.getY());
				boolean fight = view.chooseFightOrRun(hero, enemy);

				if (!fight)
				{
					if (attemptRun())
					{
						view.displayMessage("\nYou successfully escaped!");
						view.displayMessage("You return to your previous position.");
						
						hero.move(-dir.getDx(), -dir.getDy());
					}
					else
					{
						view.displayMessage("\nYou failed to run away!");
						view.displayMessage("You must fight!");
						
						if (!handleCombat(enemy))
							running = false;
					}
				}
				else
					if (!handleCombat(enemy))
						running = false;
			}
		}
	}

	private boolean attemptRun()
	{
		return Math.random() < 0.5;
	}

	private boolean handleCombat(Enemy enemy)
	{
		Combat combat = new Combat(hero, enemy);
		Combat.CombatResult result = combat.fight();
		view.displayCombat(combat.getCombatLog());

		if (result.isVictory())
		{
			view.displayVictory(result.getXpGained());
			game_map.removeEnemyAt(hero.getX(), hero.getY());
			int oldLevel = hero.getStats().getLevel();
			hero.getStats().addXp(result.getXpGained());

			if (hero.getStats().getLevel() > oldLevel)
			{
				view.displayLevelUp(hero.getStats().getLevel());
				view.displayHeroStats(hero);
			}

			if (result.hasLoot())
				handleLoot(result.getLoot());

			return true;
		}
		else
		{
			view.displayDefeat();
			return false;
		}
	}

	private void handleLoot(Item item)
	{
		Item currentItem = null;
		
		switch (item.getType())
		{
			case WEAPON:
				currentItem = hero.getStats().getWeapon();
				break;
			case ARMOR:
				currentItem = hero.getStats().getArmor();
				break;
			case HELM:
				currentItem = hero.getStats().getHelm();
				break;
		}

		boolean equip = view.chooseEquipItem(item, currentItem);

		if (equip)
		{
			hero.getStats().equipItem(item);
			view.displayMessage("\nYou equipped " + item.toString());
		}
		else
		{
			view.displayMessage("\nYou left the item behind.");
		}
	}

	private boolean nextLevel()
	{
		view.displayMessage("\n=== Prepare for the next level ===");
		
		int newLevel = hero.getStats().getLevel();
		int new_map_size = calculateMapSize(newLevel);

		view.displayMessage("New map size: " + new_map_size + "x" + new_map_size);
		
		String input = view.getUserInput("Continue? (Y/N): ").toUpperCase();

		if (input.equals("Y"))
		{
			game_map = new GameMap(new_map_size, newLevel);

			int center = new_map_size / 2;
			hero.move(center - hero.getX(), center - hero.getY());
			hero.clearVisitedPositions();

			view.displayMessage("\nNew level started!");
			view.displayMessage("Enemies: " + game_map.getEnemyCount());
			
			return true;
		}
		else
		{
			view.displayMessage("\nThanks for playing!");
			return false;
		}
	}
}

package be.npremont.swingy.controller;

import be.npremont.swingy.model.entity.Enemy;
import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.entity.Ally;
import be.npremont.swingy.model.enums.Direction;
import be.npremont.swingy.model.enums.HeroClass;
import be.npremont.swingy.model.enums.AllyType;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.EventSpawn;
import be.npremont.swingy.model.event.GameContext;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.game.Combat;
import be.npremont.swingy.model.game.GameMap;
import be.npremont.swingy.model.game.HealingSpot;
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
			view.clearScreen();

			double distanceFromCenter = game_map.getDistanceFromCenter(hero.getX(), hero.getY());
			int distanceToEdge = game_map.getDistanceToEdge(hero.getX(), hero.getY());
			
			if (view instanceof be.npremont.swingy.view.console.ConsoleView)
				((be.npremont.swingy.view.console.ConsoleView) view).displayMap(hero, game_map);
			
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
				
				healOnLevelComplete();

				if (!nextLevel())
					running = false;
				continue;
			}

			hero.move(dir.getDx(), dir.getDy());

			if (game_map.getEventManager().hasEventAt(hero.getX(), hero.getY()))
			{
				handleEvent(hero.getX(), hero.getY());
			}
			else if (game_map.hasHealingSpotAt(hero.getX(), hero.getY()))
			{
				handleHealingSpot(hero.getX(), hero.getY());
			}
			else if (game_map.hasEnemyAt(hero.getX(), hero.getY()))
			{
				view.clearScreen();

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

	private void handleEvent(int x, int y)
	{
		EventSpawn spawn = game_map.getEventManager().getEventAt(x, y);

		if (spawn == null || spawn.isTriggered())
			return;

		view.clearScreen();
		IEvent event = spawn.getEvent();
		GameContext context = new GameContext(hero, game_map, hero.getStats().getLevel());
		view.displayEvent(event);
		int choiceId = view.getEventChoice(event.getEventChoices());
		EventOutcome outcome = event.trigger(context, choiceId);
		game_map.getEventManager().applyOutcome(outcome, hero, view);
		view.displayEventOutcome(outcome);

		spawn.markAsTriggered();
		view.waitForInput("Press Enter to continue...");
	}

	private void handleHealingSpot(int x, int y)
	{
		HealingSpot spot = game_map.getHealingSpotAt(x, y);
		
		if (spot == null || spot.isUsed())
			return;

		view.clearScreen();
		
		int oldHp = hero.getStats().getCurrentHp();
		int maxHp = hero.getStats().getMaxHpWithItems();
		
		if (oldHp >= maxHp)
		{
			view.displayHealingSpot(false, 0, 0);
			view.waitForInput("Press any key to continue...");
			return;
		}

		hero.getStats().healPercentage(0.60);
		spot.use();
		
		int newHp = hero.getStats().getCurrentHp();
		int healedAmount = newHp - oldHp;
		
		view.displayHealingSpot(true, healedAmount, maxHp);
		view.waitForInput("Press any key to continue...");
	}

	private void healOnLevelComplete()
	{
		int oldHp = hero.getStats().getCurrentHp();
		int maxHp = hero.getStats().getMaxHpWithItems();
		
		hero.getStats().healPercentage(0.30);
		
		int newHp = hero.getStats().getCurrentHp();
		int healedAmount = newHp - oldHp;
		
		if (healedAmount > 0)
		{
			view.displayMessage("\nYou rest and recover your strength...");
			view.displayMessage("Healed " + healedAmount + " HP! (" + newHp + "/" + maxHp + ")");
		}
		else
		{
			view.displayMessage("\nYou are already at full health!");
		}
	}


	private boolean attemptRun()
	{
		double baseChance = 0.50;
		
		if (hero.hasAlly() && hero.getAlly().getType() == be.npremont.swingy.model.enums.AllyType.SNEAKY_THIEF)
		{
			baseChance = 0.65;
			view.displayMessage("\n" + hero.getAlly().getName() + " creates a distraction!");
		}
		
		return Math.random() < baseChance;
	}


	private boolean handleCombat(Enemy enemy)
	{
		view.clearScreen();

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

			view.waitForInput("Press any key...");

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

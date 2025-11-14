package be.npremont.swingy.view;

import java.util.Scanner;
import java.util.List;

import be.npremont.swingy.model.Direction;
import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.HeroClass;
import be.npremont.swingy.model.HeroStats;
import be.npremont.swingy.model.Enemy;
import be.npremont.swingy.model.Item;
import be.npremont.swingy.model.ItemType;

public class ConsoleView implements IView 
{
	private Scanner scanner;

	public ConsoleView()
	{
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void displayMessage(String msg)
	{
		System.out.println(msg);
	}

	@Override
	public void displayGameStatus(Hero hero, int mapSize, double distanceFromCenter, int distanceToEdge)
	{
		System.out.println("\n=== Your Status ===");
		System.out.println("Position: (" + hero.getX() + ", " + hero.getY() + ")");
		System.out.println("Map size: " + mapSize + "x" + mapSize);
		System.out.println("Distance from center: " + String.format("%.1f", distanceFromCenter));
		System.out.println("Distance to edge: " + distanceToEdge);
		System.out.println();
		displayHeroStatsCompact(hero);
	}

	private void displayHeroStatsCompact(Hero hero)
	{
		HeroStats stats = hero.getStats();
		System.out.println("HP: " + stats.getCurrentHp() + "/" + stats.getMaxHpWithItems() + 
			" | Level: " + stats.getLevel() + 
			" | XP: " + stats.getXp() + "/" + stats.getXpToNextLevel());
	}

	@Override
	public void displayHeroStats(Hero hero)
	{
		HeroStats stats = hero.getStats();
		System.out.println("\n=== " + hero.getName() + " (" + stats.getHeroClass().getDisplayName() + ") ===");
		System.out.println("Level: " + stats.getLevel());
		System.out.println("XP: " + stats.getXp() + " / " + stats.getXpToNextLevel());
		System.out.println("HP: " + stats.getCurrentHp() + " / " + stats.getMaxHpWithItems() + 
			" (Base: " + stats.getMaxHp() + 
			(stats.hasHelm() ? " + " + stats.getHelm().getBonus() + " from helm" : "") + ")");
		System.out.println("Attack: " + stats.getTotalAttack() + 
			" (Base: " + stats.getAttack() + 
			(stats.hasWeapon() ? " + " + stats.getWeapon().getBonus() + " from weapon" : "") + ")");
		System.out.println("Defense: " + stats.getTotalDefense() + 
			" (Base: " + stats.getDefense() + 
			(stats.hasArmor() ? " + " + stats.getArmor().getBonus() + " from armor" : "") + ")");
		
		System.out.println("\n=== Equipment ===");
		System.out.println("Weapon: " + (stats.hasWeapon() ? stats.getWeapon().toString() : "None"));
		System.out.println("Armor: " + (stats.hasArmor() ? stats.getArmor().toString() : "None"));
		System.out.println("Helm: " + (stats.hasHelm() ? stats.getHelm().toString() : "None"));
	}

	@Override
	public void displayCombat(List<String> combatLog)
	{
		System.out.println("\n⚔️  COMBAT ⚔️");
		System.out.println();
		for (String line : combatLog)
		{
			System.out.println(line);
		}
	}

	@Override
	public void displayVictory(int xpGained)
	{
		System.out.println("\n🎉 VICTORY! 🎉");
		System.out.println("You gained " + xpGained + " XP!");
	}

	@Override
	public void displayDefeat()
	{
		System.out.println("\n💀 DEFEAT 💀");
		System.out.println("You have been slain...");
		System.out.println("GAME OVER");
	}

	@Override
	public void displayLevelUp(int newLevel)
	{
		System.out.println("\n⭐ LEVEL UP! ⭐");
		System.out.println("You are now level " + newLevel + "!");
	}

	@Override
	public String getUserInput(String prompt)
	{
		System.out.print(prompt);
		return scanner.nextLine();
	}

	@Override
	public Direction getDirection(Hero hero)
	{
		System.out.println("\nN=North, S=South, E=East, W=West, X(debug)=addXp, I=Info, Q=Quit");
		String input = getUserInput("> ").toUpperCase();

		switch (input)
		{
			case "N":
				return Direction.NORTH;
			case "S":
				return Direction.SOUTH;
			case "E":
				return Direction.EAST;
			case "W":
				return Direction.WEST;
			case "I":
				displayHeroStats(hero);
				return getDirection(hero);
			case "X":
				hero.getStats().addXp(800);
				displayMessage("Added 800xp.");
				return getDirection(hero);
			case "Q":
				return null;
			default:
				displayMessage("Invalid input");
				return getDirection(hero);
		}
	}

	@Override
	public HeroClass chooseHeroClass()
	{
		System.out.println("\n=== Choose Your Class ===");
		System.out.println("1. WARRIOR");
		System.out.println("   High HP and Defense");
		System.out.println("   Base Stats: HP=150, Attack=12, Defense=15");
		System.out.println("   Growth: HP+15%, Attack+8%, Defense+12%");
		System.out.println();
		System.out.println("2. ARCHER");
		System.out.println("   Balanced Stats");
		System.out.println("   Base Stats: HP=100, Attack=15, Defense=10");
		System.out.println("   Growth: HP+10%, Attack+10%, Defense+10%");
		System.out.println();
		System.out.println("3. ASSASSIN");
		System.out.println("   High Attack, Low Defense");
		System.out.println("   Base Stats: HP=80, Attack=20, Defense=8");
		System.out.println("   Growth: HP+5%, Attack+15%, Defense+5%");
		System.out.println();

		String input = getUserInput("Enter your choice (1-3): ");

		switch (input)
		{
			case "1":
				return HeroClass.WARRIOR;
			case "2":
				return HeroClass.ARCHER;
			case "3":
				return HeroClass.ASSASSIN;
			default:
				displayMessage("Invalid choice. Please try again.");
				return chooseHeroClass();
		}
	}

	@Override
	public boolean chooseFightOrRun(Hero hero, Enemy enemy)
	{
		System.out.println("\n⚠️  ENEMY ENCOUNTER! ⚠️");
		System.out.println();
		System.out.println("A wild " + enemy.getType().getDisplayName() + " appears!");
		System.out.println(enemy.getType().getDisplayName() + " - HP: " + enemy.getMaxHp() + 
			" | Attack: " + enemy.getAttack() + 
			" | Defense: " + enemy.getDefense());
		System.out.println();
		System.out.println("Your stats:");
		System.out.println("HP: " + hero.getStats().getCurrentHp() + "/" + hero.getStats().getMaxHpWithItems() + 
			" | Attack: " + hero.getStats().getTotalAttack() + 
			" | Defense: " + hero.getStats().getTotalDefense());
		System.out.println();
		System.out.println("1. FIGHT");
		System.out.println("2. RUN (50% chance)");

		String input = getUserInput("> ");

		switch (input)
		{
			case "1":
				return true;
			case "2":
				return false;
			default:
				displayMessage("Invalid choice. Please choose 1 or 2.");
				return chooseFightOrRun(hero, enemy);
		}
	}

	@Override
	public boolean chooseEquipItem(Item newItem, Item currentItem)
	{
		System.out.println("\n💎 LOOT FOUND! 💎");
		System.out.println();
		System.out.println("You found: " + newItem.toString());
		System.out.println();
		
		if (currentItem != null)
			System.out.println("Current " + newItem.getType().getDisplayName().toLowerCase() + ": " + currentItem.toString());
		else
			System.out.println("You don't have a " + newItem.getType().getDisplayName().toLowerCase() + " equipped.");
		
		System.out.println();
		System.out.println("1. EQUIP new item");
		System.out.println("2. KEEP current " + (currentItem != null ? "item" : "slot empty"));

		String input = getUserInput("> ");

		switch (input)
		{
			case "1":
				return true;
			case "2":
				return false;
			default:
				displayMessage("Invalid choice. Please choose 1 or 2.");
				return chooseEquipItem(newItem, currentItem);
		}
	}
}

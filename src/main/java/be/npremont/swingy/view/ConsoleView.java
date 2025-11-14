package be.npremont.swingy.view;

import java.util.Scanner;

import be.npremont.swingy.model.Direction;
import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.HeroClass;
import be.npremont.swingy.model.HeroStats;

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
	public void displayMap(Hero hero, int mapSize)
	{
		System.out.println("\n=== Map ===");
		System.out.println("Your position: (" + hero.getX() + ", " + hero.getY() + ")");
		System.out.println("Map size: " + mapSize);
	}

	@Override
	public void displayHeroStats(Hero hero)
	{
		HeroStats stats = hero.getStats();
		System.out.println("\n=== " + hero.getName() + " (" + stats.getHeroClass().getDisplayName() + ") ===");
		System.out.println("Level: " + stats.getLevel());
		System.out.println("XP: " + stats.getXp() + " / " + stats.getXpToNextLevel());
		System.out.println("HP: " + stats.getCurrentHp() + " / " + stats.getMaxHp());
		System.out.println("Attack: " + stats.getAttack());
		System.out.println("Defense: " + stats.getDefense());
	}

	@Override
	public String getUserInput(String prompt)
	{
		System.out.println(prompt);
		return scanner.nextLine();
	}

	@Override
	public Direction getDirection(Hero hero)
	{
		System.out.println("N=North, S=South, E=East, W=West, I=Info, X(debug)=addXp, Q=Quit");
		String input = getUserInput("> ").toUpperCase();

		switch (input) {
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
}

package be.npremont.swingy.view;

import java.util.Scanner;

import be.npremont.swingy.model.Direction;
import be.npremont.swingy.model.Hero;

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
	public String getUserInput(String prompt)
	{
		System.out.println(prompt);
		return scanner.nextLine();
	}

	@Override
	public Direction getDirection()
	{
		System.out.println("N=North, S=South, E=East, W=West, Q=Quit");
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
			case "Q":
				return null;
			default:
				displayMessage("Invalid input");
				return getDirection();
		}
	}
}

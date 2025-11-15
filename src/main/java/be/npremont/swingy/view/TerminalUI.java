package be.npremont.swingy.view;

import java.util.Set;
import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.GameMap;

public class TerminalUI
{
	// ANSI Color codes
	public static final String RESET = "\u001B[0m";
	public static final String BLACK = "\u001B[30m";
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String PURPLE = "\u001B[35m";
	public static final String CYAN = "\u001B[36m";
	public static final String WHITE = "\u001B[37m";
	
	// Bright colors
	public static final String BRIGHT_RED = "\u001B[91m";
	public static final String BRIGHT_GREEN = "\u001B[92m";
	public static final String BRIGHT_YELLOW = "\u001B[93m";
	public static final String BRIGHT_BLUE = "\u001B[94m";
	public static final String BRIGHT_PURPLE = "\u001B[95m";
	public static final String BRIGHT_CYAN = "\u001B[96m";
	
	// Styles
	public static final String BOLD = "\u001B[1m";
	public static final String DIM = "\u001B[2m";

	public static String colorize(String text, String color)
	{
		return color + text + RESET;
	}

	public static String drawProgressBar(int current, int max, int width)
	{
		if (max <= 0)
			return "[" + "?".repeat(width) + "]";
		
		int filled = (int)((double)current / max * width);
		filled = Math.min(filled, width);
		
		String bar = "█".repeat(filled) + "░".repeat(width - filled);
		
		String color = getHealthColor(current, max);
		return "[" + colorize(bar, color) + "]";
	}

	public static String drawXpBar(int current, int max, int width)
	{
		if (max <= 0)
			return "[" + "?".repeat(width) + "]";
		
		int filled = (int)((double)current / max * width);
		filled = Math.min(filled, width);
		
		String bar = "█".repeat(filled) + "░".repeat(width - filled);
		return "[" + colorize(bar, CYAN) + "]";
	}

	private static String getHealthColor(int current, int max)
	{
		double percentage = (double)current / max;
		
		if (percentage <= 0.3)
			return BRIGHT_RED;
		else if (percentage <= 0.6)
			return YELLOW;
		else
			return GREEN;
	}

	public static String getRarityColor(String rarity)
	{
		switch (rarity.toUpperCase())
		{
			case "COMMON":
				return WHITE;
			case "UNCOMMON":
				return GREEN;
			case "RARE":
				return BLUE;
			case "EPIC":
				return PURPLE;
			case "LEGENDARY":
				return BRIGHT_YELLOW;
			default:
				return RESET;
		}
	}

	public static void drawMap(Hero hero, GameMap gameMap, Set<String> visitedPositions)
	{
		int mapSize = gameMap.getSize();
		int heroX = hero.getX();
		int heroY = hero.getY();
		
		int viewRadius = 7;
		int startX = Math.max(0, heroX - viewRadius);
		int startY = Math.max(0, heroY - viewRadius);
		int endX = Math.min(mapSize - 1, heroX + viewRadius);
		int endY = Math.min(mapSize - 1, heroY + viewRadius);

		System.out.println("\n╔" + "═".repeat((endX - startX + 1) * 2 + 1) + "╗");
		
		for (int y = startY; y <= endY; y++)
		{
			System.out.print("║ ");
			for (int x = startX; x <= endX; x++)
			{
				if (x == heroX && y == heroY)
					System.out.print(colorize("@", BRIGHT_GREEN) + " ");
				else if (visitedPositions.contains(x + "," + y))
					System.out.print(colorize("·", DIM + WHITE) + " ");
				else if (x == 0 || x == mapSize - 1 || y == 0 || y == mapSize - 1)
					System.out.print(colorize("#", YELLOW) + " ");
				else
					System.out.print("  ");
			}
			System.out.println("║");
		}
		
		System.out.println("╚" + "═".repeat((endX - startX + 1) * 2 + 1) + "╝");
	}

	public static void drawItemComparison(String itemType, String statName, String currentName, 
										  int currentBonus, String currentRarity, String newName, 
										  int newBonus, String newRarity)
	{
		System.out.println("\n┌─────────────────┬──────────────────────┬──────────────────────┐");
		System.out.println("│                 │       Current        │         New          │");
		System.out.println("├─────────────────┼──────────────────────┼──────────────────────┤");
		
		String itemRow = String.format("│ %-15s │ %-20s │ %-20s │", 
			itemType, 
			currentName != null ? currentName : "None",
			newName);
		System.out.println(itemRow);
		
		String bonusComparison = "";
		if (currentBonus > 0)
		{
			int diff = newBonus - currentBonus;
			String diffStr = diff > 0 ? colorize("(+" + diff + ")", GREEN) : colorize("(" + diff + ")", RED);
			bonusComparison = String.format("│ %s Bonus %s│ %20s │ %20s │ %s", 
				colorize(statName, getStatColor(statName)),
				" ".repeat(Math.max(0, 7 - statName.length())),
				"+" + currentBonus,
				"+" + newBonus,
				diffStr);
		}
		else
		{
			bonusComparison = String.format("│ %s Bonus %s│ %20s │ %20s │ %s", 
				colorize(statName, getStatColor(statName)),
				" ".repeat(Math.max(0, 7 - statName.length())),
				"-",
				"+" + newBonus,
				colorize("(+" + newBonus + ")", GREEN));
		}
		System.out.println(bonusComparison);
		
		String rarityRow = String.format("│ Rarity          │ %20s │ %20s │",
			currentRarity != null ? currentRarity : "-",
			newRarity);
		System.out.println(rarityRow);
		
		System.out.println("└─────────────────┴──────────────────────┴──────────────────────┘");
	}

	private static String getStatColor(String statName)
	{
		switch (statName)
		{
			case "Attack":
				return RED;
			case "Defense":
				return BLUE;
			case "HP":
				return GREEN;
			default:
				return WHITE;
		}
	}

	public static void drawCombatHeader()
	{
		System.out.println("\n" + colorize("╔════════════════════════════════════╗", RED));
		System.out.println(colorize("║", RED) + "         ⚔️  COMBAT  ⚔️          " + colorize("║", RED));
		System.out.println(colorize("╚════════════════════════════════════╝", RED));
	}

	public static void drawVictoryBanner()
	{
		System.out.println("\n" + colorize("╔════════════════════════════════════╗", GREEN));
		System.out.println(colorize("║", GREEN) + "        🎉 VICTORY! 🎉          " + colorize("║", GREEN));
		System.out.println(colorize("╚════════════════════════════════════╝", GREEN));
	}

	public static void drawDefeatBanner()
	{
		System.out.println("\n" + colorize("╔════════════════════════════════════╗", BRIGHT_RED));
		System.out.println(colorize("║", BRIGHT_RED) + "         💀 DEFEAT 💀           " + colorize("║", BRIGHT_RED));
		System.out.println(colorize("╚════════════════════════════════════╝", BRIGHT_RED));
	}

	public static void drawLevelUpBanner(int level)
	{
		System.out.println("\n" + colorize("╔════════════════════════════════════╗", BRIGHT_YELLOW));
		System.out.println(colorize("║", BRIGHT_YELLOW) + "        ⭐ LEVEL UP! ⭐         " + colorize("║", BRIGHT_YELLOW));
		System.out.println(colorize("║", BRIGHT_YELLOW) + "      You are now level " + level + "!      " + colorize("║", BRIGHT_YELLOW));
		System.out.println(colorize("╚════════════════════════════════════╝", BRIGHT_YELLOW));
	}

	public static void pauseAnimation(int milliseconds)
	{
		try
		{
			Thread.sleep(milliseconds);
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	public static void clearScreen()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
}

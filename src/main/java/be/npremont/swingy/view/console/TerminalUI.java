package be.npremont.swingy.view.console;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.game.GameMap;

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

	public static final int TERMINAL_WIDTH = 80;

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

	private static int getVisibleLength(String text)
	{
		String withoutAnsi = text.replaceAll("\u001B\\[[0-9;]*m", "");
		return withoutAnsi.length();
	}

	public static String createLine(String character, int width)
	{
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < width; i++)
			line.append(character);
		return line.toString();
	}

	public static String createBorderedLine(String leftBorder, String content, String rightBorder, int width)
	{
		int contentWidth = width - leftBorder.length() - rightBorder.length();
		
		if (contentWidth <= 0)
			return leftBorder + rightBorder;
		
		StringBuilder line = new StringBuilder();
		for (int i = 0; i < contentWidth; i++)
			line.append(content);
		
		return leftBorder + line.toString() + rightBorder;
	}

	public static String createBoxedText(String text, int width)
	{
		int contentWidth = width - 4;
		
		if (contentWidth <= 0)
			return "| |";
		
		String centered = centerText(text, contentWidth);
		return "| " + centered + " |";
	}

	public static String createSectionHeader(String title, String color, int width)
	{
		int titleLength = getVisibleLength(title);
		int padding = (width - titleLength - 6) / 2;
		
		if (padding < 1)
		{
			return createBoxedText(title, width);
		}
		
		StringBuilder left = new StringBuilder();
		StringBuilder right = new StringBuilder();
		
		for (int i = 0; i < padding; i++)
		{
			left.append("=");
			right.append("=");
		}
		
		if ((width - titleLength - 4) % 2 != 0)
		{
			right.append("=");
		}
		
		return colorize("| " + left.toString() + " " + title + " " + right.toString() + " |", color);
	}

	public static String createLabelValueLine(String label, String value, int width)
	{
		int contentWidth = width - 4; 
		
		if (contentWidth <= 0)
		{
			return "| |";
		}
		
		int labelLength = getVisibleLength(label);
		int valueLength = getVisibleLength(value);
		
		if (labelLength + valueLength + 1 > contentWidth)
		{
			int maxLabelLength = contentWidth - valueLength - 4;
			if (maxLabelLength > 0)
			{
				label = label.substring(0, Math.min(label.length(), maxLabelLength)) + "...";
				labelLength = maxLabelLength + 3;
			}
		}
		
		int spacing = contentWidth - labelLength - valueLength;
		StringBuilder spaces = new StringBuilder();
		for (int i = 0; i < spacing; i++)
		{
			spaces.append(" ");
		}
		
		return "| " + label + spaces.toString() + value + " |";
	}

	public static String createLabeledProgressBar(String label, int current, int max, int barWidth, String color)
	{
		int width = TERMINAL_WIDTH;
		int contentWidth = width - 4;
		
		String valueText = current + "/" + max;
		int valueLength = valueText.length();
		int labelLength = getVisibleLength(label);
		
		int availableSpace = contentWidth - labelLength - valueLength - 2; 
		int actualBarWidth = Math.min(barWidth, Math.max(5, availableSpace));
		
		String bar = drawProgressBarCustom(current, max, actualBarWidth, color);
		
		StringBuilder spaces = new StringBuilder();
		int spacing = contentWidth - labelLength - getVisibleLength(bar) - valueLength - 2;
		for (int i = 0; i < Math.max(0, spacing); i++)
		{
			spaces.append(" ");
		}
		
		return "| " + label + " " + bar + spaces.toString() + " " + valueText + " |";
	}

	private static String drawProgressBarCustom(int current, int max, int width, String color)
	{
		if (max <= 0)
		{
			StringBuilder bar = new StringBuilder("[");
			for (int i = 0; i < width; i++)
			{
				bar.append("?");
			}
			bar.append("]");
			return bar.toString();
		}
		
		int filled = (int)((double)current / max * width);
		filled = Math.min(filled, width);
		
		StringBuilder bar = new StringBuilder("[");
		bar.append(colorize("█".repeat(filled), color));
		bar.append("░".repeat(width - filled));
		bar.append("]");
		
		return bar.toString();
	}

	public static void printTwoColumnTable(String[] labels, String[] values, String color)
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println(colorize(createBorderedLine("+", "-", "+", width), color));
		
		for (int i = 0; i < labels.length && i < values.length; i++)
		{
			System.out.println(createLabelValueLine(labels[i], values[i], width));
		}
		
		System.out.println(colorize(createBorderedLine("+", "-", "+", width), color));
	}

	public static void printBoxedMessage(String message, String color)
	{
		int width = TERMINAL_WIDTH;
		int contentWidth = width - 4;
		
		System.out.println(colorize(createBorderedLine("+", "-", "+", width), color));
		
		java.util.List<String> lines = wrapText(message, contentWidth);
		for (String line : lines)
		{
			System.out.println(createBoxedText(line, width));
		}
		
		System.out.println(colorize(createBorderedLine("+", "-", "+", width), color));
	}

	public static String centerText(String text, int width)
	{
		int textLength = getVisibleLength(text);
		
		if (textLength >= width)
			return text.substring(0, Math.min(text.length(), width));
		
		int leftPadding = (width - textLength) / 2;
		int rightPadding = width - textLength - leftPadding;
		
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < leftPadding; i++)
			result.append(" ");
		result.append(text);
		for (int i = 0; i < rightPadding; i++)
			result.append(" ");
		
		return result.toString();
	}

	public static List<String> wrapText(String text, int maxWidth)
	{
		List<String> lines = new ArrayList<>();
		
		if (text == null || text.isEmpty())
		{
			return lines;
		}
		
		String[] words = text.split(" ");
		StringBuilder currentLine = new StringBuilder();
		
		for (String word : words)
		{
			int wordLength = getVisibleLength(word);
			int currentLength = getVisibleLength(currentLine.toString());
			
			if (currentLength == 0)
			{
				currentLine.append(word);
			}
			else if (currentLength + 1 + wordLength <= maxWidth)
			{
				currentLine.append(" ").append(word);
			}
			else
			{
				lines.add(currentLine.toString());
				currentLine = new StringBuilder(word);
			}
		}
		
		if (currentLine.length() > 0)
		{
			lines.add(currentLine.toString());
		}
		
		return lines;
	}

	public static void printBoxedTextWrapped(String text, String color)
	{
		int width = TERMINAL_WIDTH;
		int contentWidth = width - 4;
		
		System.out.println(createBorderedLine("+", "-", "+", width));
		
		java.util.List<String> lines = wrapText(text, contentWidth);
		for (String line : lines)
		{
			System.out.println(createBoxedText(colorize(line, color), width));
		}
		
		System.out.println(createBorderedLine("+", "-", "+", width));
	}

	public static void drawMap(Hero hero, GameMap gameMap, Set<String> visitedPositions)
	{
		int width = TERMINAL_WIDTH;
		int mapSize = gameMap.getSize();
		int heroX = hero.getX();
		int heroY = hero.getY();
		
		int maxViewWidth = (width - 4) / 2;
		int viewRadius = Math.min(7, maxViewWidth / 2);
		
		int startX = Math.max(0, heroX - viewRadius);
		int startY = Math.max(0, heroY - viewRadius);
		int endX = Math.min(mapSize - 1, heroX + viewRadius);
		int endY = Math.min(mapSize - 1, heroY + viewRadius);

		int mapWidth = (endX - startX + 1) * 2 + 2;
		
		System.out.println();
		System.out.println(createBorderedLine("+", "-", "+", mapWidth));
		
		for (int y = startY; y <= endY; y++)
		{
			StringBuilder line = new StringBuilder("| ");
			for (int x = startX; x <= endX; x++)
			{
				if (x == heroX && y == heroY)
				{
					line.append(colorize("@", BRIGHT_GREEN)).append(" ");
				}
				else if (visitedPositions.contains(x + "," + y))
				{
					line.append(colorize("·", DIM + WHITE)).append(" ");
				}
				else if (x == 0 || x == mapSize - 1 || y == 0 || y == mapSize - 1)
				{
					line.append(colorize("#", YELLOW)).append(" ");
				}
				else
				{
					line.append("  ");
				}
			}
			line.append("|");
			System.out.println(line.toString());
		}
		
		System.out.println(createBorderedLine("+", "-", "+", mapWidth));
	}

	public static void drawItemComparison(String itemType, String statName, String currentName, 
										int currentBonus, String currentRarity, String newName, 
										int newBonus, String newRarity)
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println(createBorderedLine("+", "-", "+", width));
		System.out.println(createSectionHeader("COMPARISON", CYAN, width));
		System.out.println(createBorderedLine("+", "-", "+", width));
		
		// Item type
		String itemLine = itemType + ": " + 
			(currentName != null ? currentName : colorize("None", DIM)) + 
			" → " + newName;
		System.out.println(createBoxedText(itemLine, width));
		
		// Bonus comparison
		String currentBonusStr = currentBonus > 0 ? "+" + currentBonus : colorize("None", DIM);
		String diff = "";
		
		if (currentBonus > 0)
		{
			int diffValue = newBonus - currentBonus;
			if (diffValue > 0)
			{
				diff = " " + colorize("(+" + diffValue + ")", GREEN);
			}
			else if (diffValue < 0)
			{
				diff = " " + colorize("(" + diffValue + ")", RED);
			}
			else
			{
				diff = " " + colorize("(=)", YELLOW);
			}
		}
		else
		{
			diff = " " + colorize("(+" + newBonus + ")", GREEN);
		}
		
		String bonusLine = statName + " Bonus: " + currentBonusStr + " → +" + newBonus + diff;
		System.out.println(createBoxedText(bonusLine, width));
		
		// Rarity comparison
		String rarityLine = "Rarity: " + 
			(currentRarity != null ? currentRarity : colorize("None", DIM)) + 
			" → " + newRarity;
		System.out.println(createBoxedText(rarityLine, width));
		
		System.out.println(createBorderedLine("+", "-", "+", width));
	}
 
	public static void drawCombatHeader()
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), RED));
		System.out.println(colorize(createBoxedText("COMBAT", width), RED));
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), RED));
	}

	public static void drawVictoryBanner()
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), GREEN));
		System.out.println(colorize(createBoxedText("VICTORY!", width), GREEN));
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), GREEN));
	}

	public static void drawDefeatBanner()
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), BRIGHT_RED));
		System.out.println(colorize(createBoxedText("DEFEAT", width), BRIGHT_RED));
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), BRIGHT_RED));
	}

	public static void drawLevelUpBanner(int level)
	{
		int width = TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(createBoxedText("LEVEL UP!", width), BRIGHT_YELLOW));
		System.out.println(colorize(createBoxedText("You are now level " + level + "!", width), BRIGHT_YELLOW));
		System.out.println(colorize(createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
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
		try
		{
			final String os = System.getProperty("os.name");
			
			if (os.contains("Windows"))
			{
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			}
			else
			{
				System.out.print("\033[H\033[2J");
				System.out.flush();
				
				System.out.print("\033\143");
				System.out.flush();
			}
		}
		catch (Exception e)
		{
			for (int i = 0; i < 50; i++)
			{
				System.out.println();
			}
		}
	}
}

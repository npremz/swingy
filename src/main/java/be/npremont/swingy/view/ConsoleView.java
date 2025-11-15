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
import be.npremont.swingy.model.GameMap;

import static be.npremont.swingy.view.TerminalUI.*;

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
		int width = TerminalUI.getTerminalWidth();
		int contentWidth = width - 4;
		
		java.util.List<String> lines = TerminalUI.wrapText(msg, contentWidth);
		for (String line : lines)
		{
			System.out.println(line);
		}
	}

	@Override
	public void displayGameStatus(Hero hero, int mapSize, double distanceFromCenter, int distanceToEdge)
	{
		int width = TerminalUI.getTerminalWidth();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("STATUS", CYAN, width), CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), CYAN));
		
		// Position
		String posValue = colorize("(" + hero.getX() + ", " + hero.getY() + ")", BRIGHT_CYAN);
		System.out.println(TerminalUI.createLabelValueLine("Position:", posValue, width));
		
		// Map size
		String mapValue = colorize(mapSize + "x" + mapSize, WHITE);
		System.out.println(TerminalUI.createLabelValueLine("Map size:", mapValue, width));
		
		// Distance to edge
		String distColor = distanceToEdge <= 3 ? BRIGHT_GREEN : YELLOW;
		String distValue = colorize("" + distanceToEdge, distColor);
		System.out.println(TerminalUI.createLabelValueLine("Distance to edge:", distValue, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		// Stats compactes
		HeroStats stats = hero.getStats();
		
		// HP bar
		String hpColor = getHealthColorForStats(stats.getCurrentHp(), stats.getMaxHpWithItems());
		System.out.println(TerminalUI.createLabeledProgressBar("HP:", stats.getCurrentHp(), stats.getMaxHpWithItems(), 15, hpColor));
		
		// XP bar
		System.out.println(TerminalUI.createLabeledProgressBar("XP:", stats.getXp(), stats.getXpToNextLevel(), 15, CYAN));
		
		// Stats en ligne
		String statsLine = "LVL: " + colorize("" + stats.getLevel(), BRIGHT_YELLOW) + 
			" | ATK: " + colorize("" + stats.getTotalAttack(), RED) + 
			" | DEF: " + colorize("" + stats.getTotalDefense(), BLUE);
		System.out.println(TerminalUI.createBoxedText(statsLine, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), CYAN));
	}

	private String getHealthColorForStats(int current, int max)
	{
		double percentage = (double)current / max;
		
		if (percentage <= 0.3)
			return BRIGHT_RED;
		else if (percentage <= 0.6)
			return YELLOW;
		else
			return GREEN;
	}

	@Override
	public void displayHeroStats(Hero hero)
	{
		int width = TerminalUI.getTerminalWidth();
		HeroStats stats = hero.getStats();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("HERO", BRIGHT_CYAN, width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
		
		// Nom et classe
		String nameClass = colorize(hero.getName(), BOLD + BRIGHT_YELLOW) + " (" + stats.getHeroClass().getDisplayName() + ")";
		System.out.println(TerminalUI.createBoxedText(nameClass, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		
		// Level
		System.out.println(TerminalUI.createLabelValueLine("Level:", colorize("" + stats.getLevel(), BRIGHT_YELLOW), width));
		
		// XP bar
		System.out.println(TerminalUI.createLabeledProgressBar("XP:", stats.getXp(), stats.getXpToNextLevel(), 20, CYAN));
		
		// HP bar avec détails
		String hpColor = getHealthColorForStats(stats.getCurrentHp(), stats.getMaxHpWithItems());
		System.out.println(TerminalUI.createLabeledProgressBar("HP:", stats.getCurrentHp(), stats.getMaxHpWithItems(), 20, hpColor));
		
		String hpDetails = "(Base: " + stats.getMaxHp();
		if (stats.hasHelm())
		{
			hpDetails += " + " + colorize("" + stats.getHelm().getBonus(), GREEN) + " helm";
		}
		hpDetails += ")";
		System.out.println(TerminalUI.createBoxedText(hpDetails, width));
		
		// Attack
		String atkValue = colorize("" + stats.getTotalAttack(), RED) + " (Base: " + stats.getAttack();
		if (stats.hasWeapon())
		{
			atkValue += " + " + colorize("" + stats.getWeapon().getBonus(), GREEN) + " weapon";
		}
		atkValue += ")";
		System.out.println(TerminalUI.createLabelValueLine("ATK:", atkValue, width));
		
		// Defense
		String defValue = colorize("" + stats.getTotalDefense(), BLUE) + " (Base: " + stats.getDefense();
		if (stats.hasArmor())
		{
			defValue += " + " + colorize("" + stats.getArmor().getBonus(), GREEN) + " armor";
		}
		defValue += ")";
		System.out.println(TerminalUI.createLabelValueLine("DEF:", defValue, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("EQUIPMENT", CYAN, width), CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		
		// Equipment
		displayEquipmentItemResponsive("Weapon:", stats.getWeapon(), width);
		displayEquipmentItemResponsive("Armor:", stats.getArmor(), width);
		displayEquipmentItemResponsive("Helm:", stats.getHelm(), width);
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
	}

	private void displayEquipmentItemResponsive(String slot, Item item, int width)
	{
		if (item != null)
		{
			String rarityColor = getRarityColor(item.getRarity().name());
			String itemInfo = colorize(item.getName(), rarityColor) + " (+" + item.getBonus() + " " + colorize(item.getRarity().name(), rarityColor) + ")";
			System.out.println(TerminalUI.createLabelValueLine(slot, itemInfo, width));
		}
		else
		{
			System.out.println(TerminalUI.createLabelValueLine(slot, colorize("None", DIM), width));
		}
	}

	@Override
	public void displayCombat(List<String> combatLog)
	{
		int width = TerminalUI.getTerminalWidth();
		
		drawCombatHeader();
		System.out.println();
		
		for (String line : combatLog)
		{
			// Wrappe chaque ligne de combat
			int contentWidth = width - 4;
			java.util.List<String> wrappedLines = TerminalUI.wrapText(line, contentWidth);
			
			for (String wrappedLine : wrappedLines)
			{
				System.out.println(TerminalUI.createBoxedText(wrappedLine, width));
			}
			
			pauseAnimation(400);
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), RED));
	}

	@Override
	public void displayVictory(int xpGained)
	{
		int width = TerminalUI.getTerminalWidth();
		
		drawVictoryBanner();
		
		String message = "You gained " + colorize("+" + xpGained + " XP", BRIGHT_CYAN) + "!";
		System.out.println(TerminalUI.createBoxedText(message, width));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), GREEN));
	}

	@Override
	public void displayDefeat()
	{
		int width = TerminalUI.getTerminalWidth();
		
		drawDefeatBanner();
		
		System.out.println(TerminalUI.createBoxedText("You have been slain...", width));
		System.out.println(TerminalUI.createBoxedText(colorize("GAME OVER", BRIGHT_RED), width));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_RED));
	}

	@Override
	public void displayLevelUp(int newLevel)
	{
		drawLevelUpBanner(newLevel);
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
		int width = TerminalUI.getTerminalWidth();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		String controls = colorize("N", BOLD) + "=North, " + colorize("S", BOLD) + "=South, " + 
			colorize("E", BOLD) + "=East, " + colorize("W", BOLD) + "=West, " + 
			colorize("I", BOLD) + "=Info, " + colorize("Q", BOLD) + "=Quit";
		
		// Wrappe les contrôles si nécessaire
		int contentWidth = width - 4;
		java.util.List<String> wrappedControls = TerminalUI.wrapText(controls, contentWidth);
		
		for (String line : wrappedControls)
		{
			System.out.println(TerminalUI.createBoxedText(line, width));
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		String input = getUserInput(colorize("> ", BRIGHT_GREEN)).toUpperCase();

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
				displayMessage(colorize("Added 800xp.", YELLOW));
				return getDirection(hero);
			case "Q":
				return null;
			default:
				displayMessage(colorize("Invalid input", RED));
				return getDirection(hero);
		}
	}

	@Override
	public HeroClass chooseHeroClass()
	{
		int width = TerminalUI.getTerminalWidth();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createSectionHeader("Choose Your Class", BRIGHT_YELLOW, width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		
		// Warrior
		System.out.println(TerminalUI.createBoxedText(colorize("1. WARRIOR", BOLD + RED), width));
		System.out.println(TerminalUI.createBoxedText("   High HP and Defense", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=150, ATK=12, DEF=15", width));
		System.out.println(TerminalUI.createBoxedText("   Growth: HP+15%, ATK+8%, DEF+12%", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		// Archer
		System.out.println(TerminalUI.createBoxedText(colorize("2. ARCHER", BOLD + GREEN), width));
		System.out.println(TerminalUI.createBoxedText("   Balanced Stats", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=100, ATK=15, DEF=10", width));
		System.out.println(TerminalUI.createBoxedText("   Growth: HP+10%, ATK+10%, DEF+10%", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		// Assassin
		System.out.println(TerminalUI.createBoxedText(colorize("3. ASSASSIN", BOLD + PURPLE), width));
		System.out.println(TerminalUI.createBoxedText("   High Attack, Low Defense", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=80, ATK=20, DEF=8", width));
		System.out.println(TerminalUI.createBoxedText("   Growth: HP+5%, ATK+15%, DEF+5%", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));

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
				displayMessage(colorize("Invalid choice. Please try again.", RED));
				return chooseHeroClass();
		}
	}

	@Override
	public boolean chooseFightOrRun(Hero hero, Enemy enemy)
	{
		int width = TerminalUI.getTerminalWidth();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_RED));
		System.out.println(colorize(TerminalUI.createSectionHeader("ENEMY ENCOUNTER!", BRIGHT_RED, width), BRIGHT_RED));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_RED));
		
		String encounterMsg = "A wild " + colorize(enemy.getType().getDisplayName(), BOLD + RED) + " appears!";
		System.out.println(TerminalUI.createBoxedText(encounterMsg, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		System.out.println(colorize(TerminalUI.createSectionHeader("Enemy Stats", RED, width), RED));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		
		System.out.println(TerminalUI.createLabelValueLine("HP:", colorize("" + enemy.getMaxHp(), RED), width));
		System.out.println(TerminalUI.createLabelValueLine("ATK:", colorize("" + enemy.getAttack(), RED), width));
		System.out.println(TerminalUI.createLabelValueLine("DEF:", colorize("" + enemy.getDefense(), BLUE), width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		System.out.println(colorize(TerminalUI.createSectionHeader("Your Stats", GREEN, width), GREEN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		
		HeroStats stats = hero.getStats();
		String hpColor = getHealthColorForStats(stats.getCurrentHp(), stats.getMaxHpWithItems());
		System.out.println(TerminalUI.createLabeledProgressBar("HP:", stats.getCurrentHp(), stats.getMaxHpWithItems(), 15, hpColor));
		System.out.println(TerminalUI.createLabelValueLine("ATK:", colorize("" + stats.getTotalAttack(), RED), width));
		System.out.println(TerminalUI.createLabelValueLine("DEF:", colorize("" + stats.getTotalDefense(), BLUE), width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		
		System.out.println(TerminalUI.createBoxedText(colorize("1.", BOLD) + " FIGHT", width));
		System.out.println(TerminalUI.createBoxedText(colorize("2.", BOLD) + " RUN (50% chance)", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_RED));

		String input = getUserInput(colorize("> ", BRIGHT_GREEN));

		switch (input)
		{
			case "1":
				return true;
			case "2":
				return false;
			default:
				displayMessage(colorize("Invalid choice. Please choose 1 or 2.", RED));
				return chooseFightOrRun(hero, enemy);
		}
	}

	@Override
	public boolean chooseEquipItem(Item newItem, Item currentItem)
	{
		int width = TerminalUI.getTerminalWidth();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createSectionHeader("LOOT FOUND!", BRIGHT_YELLOW, width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		
		String rarityColor = getRarityColor(newItem.getRarity().name());
		String statColor = getStatColorForItem(newItem.getType());
		
		String foundMsg = "You found: " + colorize(newItem.getName(), BOLD + rarityColor);
		System.out.println(TerminalUI.createBoxedText(foundMsg, width));
		
		String bonusMsg = "+" + colorize("" + newItem.getBonus() + " " + newItem.getType().getStatName(), statColor) + 
			" (" + colorize(newItem.getRarity().name(), rarityColor) + ")";
		System.out.println(TerminalUI.createBoxedText(bonusMsg, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		drawItemComparison(
			newItem.getType().getDisplayName(),
			newItem.getType().getStatName(),
			currentItem != null ? currentItem.getName() : null,
			currentItem != null ? currentItem.getBonus() : 0,
			currentItem != null ? currentItem.getRarity().name() : null,
			newItem.getName(),
			newItem.getBonus(),
			newItem.getRarity().name()
		);
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		System.out.println(TerminalUI.createBoxedText(colorize("1.", BOLD) + " EQUIP new item", width));
		System.out.println(TerminalUI.createBoxedText(colorize("2.", BOLD) + " KEEP current " + (currentItem != null ? "item" : "slot empty"), width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));

		String input = getUserInput(colorize("> ", BRIGHT_GREEN));

		switch (input)
		{
			case "1":
				return true;
			case "2":
				return false;
			default:
				displayMessage(colorize("Invalid choice. Please choose 1 or 2.", RED));
				return chooseEquipItem(newItem, currentItem);
		}
	}

	private String getStatColorForItem(ItemType type)
	{
		switch (type)
		{
			case WEAPON:
				return RED;
			case ARMOR:
				return BLUE;
			case HELM:
				return GREEN;
			default:
				return WHITE;
		}
	}

	public void displayMap(Hero hero, GameMap gameMap)
	{
		drawMap(hero, gameMap, hero.getVisitedPositions());
	}
}

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
		System.out.println(msg);
	}

	@Override
	public void displayGameStatus(Hero hero, int mapSize, double distanceFromCenter, int distanceToEdge)
	{
		System.out.println("\n" + colorize("╔══════════════ STATUS ══════════════╗", CYAN));
		System.out.println("Position: " + colorize("(" + hero.getX() + ", " + hero.getY() + ")", BRIGHT_CYAN));
		System.out.println("Map size: " + colorize(mapSize + "x" + mapSize, WHITE));
		System.out.println("Distance to edge: " + colorize("" + distanceToEdge, distanceToEdge <= 3 ? BRIGHT_GREEN : YELLOW));
		System.out.println();
		displayHeroStatsCompact(hero);
		System.out.println(colorize("╚════════════════════════════════════╝", CYAN));
	}

	private void displayHeroStatsCompact(Hero hero)
	{
		HeroStats stats = hero.getStats();
		
		String hpBar = drawProgressBar(stats.getCurrentHp(), stats.getMaxHpWithItems(), 10);
		String xpBar = drawXpBar(stats.getXp(), stats.getXpToNextLevel(), 10);
		
		System.out.println("HP:  " + hpBar + " " + stats.getCurrentHp() + "/" + stats.getMaxHpWithItems());
		System.out.println("XP:  " + xpBar + " " + stats.getXp() + "/" + stats.getXpToNextLevel());
		System.out.println("LVL: " + colorize("" + stats.getLevel(), BRIGHT_YELLOW) + 
			" | ATK: " + colorize("" + stats.getTotalAttack(), RED) + 
			" | DEF: " + colorize("" + stats.getTotalDefense(), BLUE));
	}

	@Override
	public void displayHeroStats(Hero hero)
	{
		HeroStats stats = hero.getStats();
		System.out.println("\n" + colorize("╔══════════════ HERO ════════════════╗", BRIGHT_CYAN));
		System.out.println(colorize(hero.getName(), BOLD + BRIGHT_YELLOW) + " (" + stats.getHeroClass().getDisplayName() + ")");
		System.out.println();
		
		String hpBar = drawProgressBar(stats.getCurrentHp(), stats.getMaxHpWithItems(), 20);
		String xpBar = drawXpBar(stats.getXp(), stats.getXpToNextLevel(), 20);
		
		System.out.println("Level: " + colorize("" + stats.getLevel(), BRIGHT_YELLOW));
		System.out.println("XP:    " + xpBar + " " + stats.getXp() + " / " + stats.getXpToNextLevel());
		System.out.println("HP:    " + hpBar + " " + stats.getCurrentHp() + " / " + stats.getMaxHpWithItems() + 
			" (Base: " + stats.getMaxHp() + 
			(stats.hasHelm() ? " + " + colorize("" + stats.getHelm().getBonus(), GREEN) + " helm" : "") + ")");
		System.out.println("ATK:   " + colorize("" + stats.getTotalAttack(), RED) + 
			" (Base: " + stats.getAttack() + 
			(stats.hasWeapon() ? " + " + colorize("" + stats.getWeapon().getBonus(), GREEN) + " weapon" : "") + ")");
		System.out.println("DEF:   " + colorize("" + stats.getTotalDefense(), BLUE) + 
			" (Base: " + stats.getDefense() + 
			(stats.hasArmor() ? " + " + colorize("" + stats.getArmor().getBonus(), GREEN) + " armor" : "") + ")");
		
		System.out.println("\n" + colorize("--- Equipment ---", CYAN));
		displayEquipmentItem("Weapon", stats.getWeapon());
		displayEquipmentItem("Armor", stats.getArmor());
		displayEquipmentItem("Helm", stats.getHelm());
		System.out.println(colorize("╚════════════════════════════════════╝", BRIGHT_CYAN));
	}

	private void displayEquipmentItem(String slot, Item item)
	{
		if (item != null)
		{
			String rarityColor = getRarityColor(item.getRarity().name());
			System.out.println(slot + ": " + colorize(item.getName(), rarityColor) + 
				" (+" + item.getBonus() + " " + colorize(item.getRarity().name(), rarityColor) + ")");
		}
		else
		{
			System.out.println(slot + ": " + colorize("None", DIM));
		}
	}

	@Override
	public void displayCombat(List<String> combatLog)
	{
		drawCombatHeader();
		System.out.println();
		for (String line : combatLog)
		{
			System.out.println(line);
			pauseAnimation(400);
		}
	}

	@Override
	public void displayVictory(int xpGained)
	{
		drawVictoryBanner();
		System.out.println("You gained " + colorize("+" + xpGained + " XP", BRIGHT_CYAN) + "!");
	}

	@Override
	public void displayDefeat()
	{
		drawDefeatBanner();
		System.out.println("You have been slain...");
		System.out.println(colorize("GAME OVER", BRIGHT_RED));
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
		System.out.println("\n" + colorize("N", BOLD) + "=North, " + colorize("S", BOLD) + "=South, " + 
			colorize("E", BOLD) + "=East, " + colorize("W", BOLD) + "=West, " + 
			colorize("I", BOLD) + "=Info, " + colorize("Q", BOLD) + "=Quit");
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
		System.out.println("\n" + colorize("╔══════ Choose Your Class ══════╗", BRIGHT_YELLOW));
		System.out.println(colorize("1. WARRIOR", BOLD + RED));
		System.out.println("   High HP and Defense");
		System.out.println("   Base: HP=150, ATK=12, DEF=15");
		System.out.println("   Growth: HP+15%, ATK+8%, DEF+12%");
		System.out.println();
		System.out.println(colorize("2. ARCHER", BOLD + GREEN));
		System.out.println("   Balanced Stats");
		System.out.println("   Base: HP=100, ATK=15, DEF=10");
		System.out.println("   Growth: HP+10%, ATK+10%, DEF+10%");
		System.out.println();
		System.out.println(colorize("3. ASSASSIN", BOLD + PURPLE));
		System.out.println("   High Attack, Low Defense");
		System.out.println("   Base: HP=80, ATK=20, DEF=8");
		System.out.println("   Growth: HP+5%, ATK+15%, DEF+5%");
		System.out.println(colorize("╚═══════════════════════════════╝", BRIGHT_YELLOW));

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
		System.out.println("\n" + colorize("╔═══════ ENEMY ENCOUNTER! ═══════╗", BRIGHT_RED));
		System.out.println();
		System.out.println("A wild " + colorize(enemy.getType().getDisplayName(), BOLD + RED) + " appears!");
		System.out.println(enemy.getType().getDisplayName() + " Stats:");
		System.out.println("  HP: " + colorize("" + enemy.getMaxHp(), RED) + 
			" | ATK: " + colorize("" + enemy.getAttack(), RED) + 
			" | DEF: " + colorize("" + enemy.getDefense(), BLUE));
		System.out.println();
		System.out.println(colorize("Your Stats:", GREEN));
		
		HeroStats stats = hero.getStats();
		String hpBar = drawProgressBar(stats.getCurrentHp(), stats.getMaxHpWithItems(), 10);
		System.out.println("  HP: " + hpBar + " " + stats.getCurrentHp() + "/" + stats.getMaxHpWithItems());
		System.out.println("  ATK: " + colorize("" + stats.getTotalAttack(), RED) + 
			" | DEF: " + colorize("" + stats.getTotalDefense(), BLUE));
		System.out.println();
		System.out.println(colorize("1.", BOLD) + " FIGHT");
		System.out.println(colorize("2.", BOLD) + " RUN (50% chance)");
		System.out.println(colorize("╚════════════════════════════════╝", BRIGHT_RED));

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
		System.out.println("\n" + colorize("╔═══════ LOOT FOUND! ═══════╗", BRIGHT_YELLOW));
		System.out.println();
		
		String rarityColor = getRarityColor(newItem.getRarity().name());
		String statColor = getStatColorForItem(newItem.getType());
		
		System.out.println("You found: " + colorize(newItem.getName(), BOLD + rarityColor));
		System.out.println("  +" + colorize("" + newItem.getBonus() + " " + newItem.getType().getStatName(), statColor) + 
			" (" + colorize(newItem.getRarity().name(), rarityColor) + ")");
		System.out.println();
		
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
		
		System.out.println();
		System.out.println(colorize("1.", BOLD) + " EQUIP new item");
		System.out.println(colorize("2.", BOLD) + " KEEP current " + (currentItem != null ? "item" : "slot empty"));
		System.out.println(colorize("╚═══════════════════════════╝", BRIGHT_YELLOW));

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

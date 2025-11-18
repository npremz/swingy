package be.npremont.swingy.view.console;

import java.util.Scanner;

import static be.npremont.swingy.view.console.TerminalUI.*;

import java.util.List;

import be.npremont.swingy.model.entity.Ally;
import be.npremont.swingy.model.entity.Enemy;
import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.Direction;
import be.npremont.swingy.model.enums.HeroClass;
import be.npremont.swingy.model.enums.ItemType;
import be.npremont.swingy.model.event.EventChoice;
import be.npremont.swingy.model.event.EventOutcome;
import be.npremont.swingy.model.event.IEvent;
import be.npremont.swingy.model.event.events.ShrineEvent;
import be.npremont.swingy.model.game.GameMap;
import be.npremont.swingy.model.stats.HeroStats;
import be.npremont.swingy.view.IView;

public class ConsoleView implements IView 
{
	private Scanner scanner;

	public ConsoleView()
	{
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void clearScreen()
	{
		TerminalUI.clearScreen();
	}

	@Override
	public void waitForInput(String prompt)
	{
		System.out.println(colorize(prompt, BRIGHT_CYAN));
		scanner.nextLine();
	}

	@Override
	public void displayMessage(String msg)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		int contentWidth = width - 4;
		
		List<String> lines = TerminalUI.wrapText(msg, contentWidth);
		for (String line : lines)
		{
			System.out.println(line);
		}
	}

	@Override
	public void displayGameStatus(Hero hero, int mapSize, double distanceFromCenter, int distanceToEdge)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("STATUS", CYAN, width), CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), CYAN));
		
		String posValue = colorize("(" + hero.getX() + ", " + hero.getY() + ")", BRIGHT_CYAN);
		System.out.println(TerminalUI.createLabelValueLine("Position:", posValue, width));
		
		String mapValue = colorize(mapSize + "x" + mapSize, WHITE);
		System.out.println(TerminalUI.createLabelValueLine("Map size:", mapValue, width));
		
		String distColor = distanceToEdge <= 3 ? BRIGHT_GREEN : YELLOW;
		String distValue = colorize("" + distanceToEdge, distColor);
		System.out.println(TerminalUI.createLabelValueLine("Distance to edge:", distValue, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		HeroStats stats = hero.getStats();
		
		String hpColor = getHealthColorForStats(stats.getCurrentHp(), stats.getMaxHpWithItems());
		System.out.println(TerminalUI.createLabeledProgressBar("HP:", stats.getCurrentHp(), stats.getMaxHpWithItems(), 15, hpColor));
		
		System.out.println(TerminalUI.createLabeledProgressBar("XP:", stats.getXp(), stats.getXpToNextLevel(), 15, CYAN));
		
		String statsLine = "LVL: " + colorize("" + stats.getLevel(), BRIGHT_YELLOW) + 
			" | ATK: " + colorize("" + stats.getTotalAttack(), RED) + 
			" | DEF: " + colorize("" + stats.getTotalDefense(), BLUE);
		System.out.println(TerminalUI.createBoxedText(statsLine, width));
		
		if (hero.hasAlly())
		{
			Ally ally = hero.getAlly();
			
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
			System.out.println(colorize(TerminalUI.createSectionHeader("ALLY", GREEN, width), GREEN));
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
			
			String allyName = colorize(ally.getName(), BOLD + BRIGHT_GREEN) + " (" + ally.getType().name().replace("_", " ") + ")";
			System.out.println(TerminalUI.createBoxedText(allyName, width));
			
			String allyHpColor = getHealthColorForStats(ally.getCurrentHp(), ally.getMaxHp());
			System.out.println(TerminalUI.createLabeledProgressBar("HP:", ally.getCurrentHp(), ally.getMaxHp(), 15, allyHpColor));
			
			String allyStats = "ATK: " + colorize("" + ally.getAttack(), RED) + 
				" | DEF: " + colorize("" + ally.getDefense(), BLUE) + 
				" | Combats left: " + colorize("" + ally.getRemainingCombats(), BRIGHT_YELLOW);
			System.out.println(TerminalUI.createBoxedText(allyStats, width));
			
			int contentWidth = width - 4;
			List<String> descLines = TerminalUI.wrapText(ally.getDescription(), contentWidth);
			for (String line : descLines)
			{
				System.out.println(TerminalUI.createBoxedText(colorize(line, DIM), width));
			}
		}
		
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
		int width = TerminalUI.TERMINAL_WIDTH;
		HeroStats stats = hero.getStats();
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("HERO", BRIGHT_CYAN, width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
		
		String nameClass = colorize(hero.getName(), BOLD + BRIGHT_YELLOW) + " (" + stats.getHeroClass().getDisplayName() + ")";
		System.out.println(TerminalUI.createBoxedText(nameClass, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		System.out.println(TerminalUI.createLabelValueLine("Level:", colorize("" + stats.getLevel(), BRIGHT_YELLOW), width));
		System.out.println(TerminalUI.createLabeledProgressBar("XP:", stats.getXp(), stats.getXpToNextLevel(), 20, CYAN));
		String hpColor = getHealthColorForStats(stats.getCurrentHp(), stats.getMaxHpWithItems());
		System.out.println(TerminalUI.createLabeledProgressBar("HP:", stats.getCurrentHp(), stats.getMaxHpWithItems(), 20, hpColor));
		
		String hpDetails = "(Base: " + stats.getMaxHp();
		if (stats.hasHelm())
		{
			hpDetails += " + " + colorize("" + stats.getHelm().getFlatBonus(), GREEN) + " helm";
		}
		hpDetails += ")";
		System.out.println(TerminalUI.createBoxedText(hpDetails, width));
		
		String atkValue = colorize("" + stats.getTotalAttack(), RED) + " (Base: " + stats.getAttack();
		if (stats.hasWeapon())
		{
			atkValue += " + " + colorize("" + stats.getWeapon().getFlatBonus(), GREEN) + " weapon";
		}
		atkValue += ")";
		System.out.println(TerminalUI.createLabelValueLine("ATK:", atkValue, width));
		
		String defValue = colorize("" + stats.getTotalDefense(), BLUE) + " (Base: " + stats.getDefense();
		if (stats.hasArmor())
		{
			defValue += " + " + colorize("" + stats.getArmor().getFlatBonus(), GREEN) + " armor";
		}
		defValue += ")";
		System.out.println(TerminalUI.createLabelValueLine("DEF:", defValue, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		System.out.println(colorize(TerminalUI.createSectionHeader("EQUIPMENT", CYAN, width), CYAN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_CYAN));
		
		displayEquipmentItem("Weapon:", stats.getWeapon(), width);
		displayEquipmentItem("Armor:", stats.getArmor(), width);
		displayEquipmentItem("Helm:", stats.getHelm(), width);
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_CYAN));
	}

	private void displayEquipmentItem(String slot, Item item, int width)
	{
		if (item != null)
		{
			String rarityColor = getRarityColor(item.getRarity().name());
			String itemInfo = colorize(item.getName(), rarityColor) + 
				" (+" + item.getFlatBonus() + " + " + 
				String.format("%.1f", item.getPercentBonus()) + "% " + 
				colorize(item.getRarity().name(), rarityColor) + ")";
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
		int width = TerminalUI.TERMINAL_WIDTH;
		
		drawCombatHeader();
		System.out.println();
		
		for (String line : combatLog)
		{
			int contentWidth = width - 4;
			List<String> wrappedLines = TerminalUI.wrapText(line, contentWidth);
			
			for (String wrappedLine : wrappedLines)
			{
				System.out.println(TerminalUI.createBoxedText(wrappedLine, width));
			}
			
			pauseAnimation(150);
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), RED));
	}

	@Override
	public void displayVictory(int xpGained)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
		drawVictoryBanner();
		
		String message = "You gained " + colorize("+" + xpGained + " XP", BRIGHT_CYAN) + "!";
		System.out.println(TerminalUI.createBoxedText(message, width));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), GREEN));
	}

	@Override
	public void displayDefeat()
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
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
	public void displayHealingSpot(boolean healed, int healAmount, int maxHp)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_GREEN));
		System.out.println(colorize(TerminalUI.createSectionHeader("HEALING SPOT", BRIGHT_GREEN, width), BRIGHT_GREEN));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_GREEN));
		
		if (healed)
		{
			System.out.println(TerminalUI.createBoxedText("You found a healing fountain!", width));
			System.out.println(TerminalUI.createBoxedText("", width));
			System.out.println(TerminalUI.createBoxedText("The magical waters restore your vitality.", width));
			System.out.println(TerminalUI.createBoxedText("", width));
			
			String healMsg = "Recovered " + colorize("+" + healAmount + " HP", BRIGHT_GREEN) + 
				" (60% of max HP)";
			System.out.println(TerminalUI.createBoxedText(healMsg, width));
			
			System.out.println(TerminalUI.createBoxedText("", width));
			System.out.println(TerminalUI.createBoxedText("The fountain's magic fades away...", width));
		}
		else
		{
			System.out.println(TerminalUI.createBoxedText("You found a healing fountain...", width));
			System.out.println(TerminalUI.createBoxedText("", width));
			System.out.println(TerminalUI.createBoxedText(colorize("But you are already at full health!", YELLOW), width));
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_GREEN));
	}

	@Override
	public void displayEvent(IEvent event)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createSectionHeader("RANDOM EVENT", BRIGHT_YELLOW, width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		
		System.out.println(TerminalUI.createBoxedText(colorize(event.getTitle(), BOLD + BRIGHT_CYAN), width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		int contentWidth = width - 4;
		String[] paragraphs = event.getDescription().split("\n");
		
		for (String paragraph : paragraphs)
		{
			if (paragraph.trim().isEmpty())
			{
				System.out.println(TerminalUI.createBoxedText("", width));
			}
			else
			{
				List<String> wrappedLines = TerminalUI.wrapText(paragraph.trim(), contentWidth);
				for (String line : wrappedLines)
				{
					System.out.println(TerminalUI.createBoxedText(line, width));
				}
			}
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		List<EventChoice> choices = event.getEventChoices();
		for (EventChoice choice : choices)
		{
			String choiceText = colorize(choice.getId() + ".", BOLD) + " " + choice.getText();
			System.out.println(TerminalUI.createBoxedText(choiceText, width));
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
	}

	@Override
	public int getEventChoice(List<EventChoice> choices)
	{
		while (true)
		{
			String input = getUserInput(colorize("> ", BRIGHT_GREEN));
			
			try
			{
				int choice = Integer.parseInt(input);
				for (EventChoice c : choices)
				{
					if (c.getId() == choice)
						return choice;
				}
				
				displayMessage(colorize("Invalid choice. Please choose a valid option.", RED));
			}
			catch (NumberFormatException e)
			{
				displayMessage(colorize("Please enter a number.", RED));
			}
		}
	}

	@Override
	public void displayEventOutcome(EventOutcome outcome)
	{
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		
		if (outcome.isSuccess())
		{
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), GREEN));
			System.out.println(colorize(TerminalUI.createSectionHeader("OUTCOME", GREEN, width), GREEN));
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), GREEN));
		}
		else
		{
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), RED));
			System.out.println(colorize(TerminalUI.createSectionHeader("OUTCOME", RED, width), RED));
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), RED));
		}
		
		int contentWidth = width - 4;
		String[] paragraphs = outcome.getMessage().split("\n");
		
		for (String paragraph : paragraphs)
		{
			if (paragraph.trim().isEmpty())
			{
				System.out.println(TerminalUI.createBoxedText("", width));
			}
			else
			{
				java.util.List<String> wrappedLines = TerminalUI.wrapText(paragraph.trim(), contentWidth);
				for (String line : wrappedLines)
				{
					System.out.println(TerminalUI.createBoxedText(line, width));
				}
			}
		}
		
		if (!outcome.getEffects().isEmpty())
		{
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), outcome.isSuccess() ? GREEN : RED));
			System.out.println(TerminalUI.createBoxedText(colorize("Effects:", BOLD), width));
			
			for (var entry : outcome.getEffects().entrySet())
			{
				String effectText = formatEffect(entry.getKey(), entry.getValue());
				System.out.println(TerminalUI.createBoxedText(effectText, width));
			}
		}
		
		if (outcome.isSuccess())
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), GREEN));
		else
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), RED));
	}


	private String formatEffect(be.npremont.swingy.model.event.enums.EffectType type, Object value)
	{
		switch (type)
		{
			case HP_CHANGE:
				int hpChange = (Integer)value;
				if (hpChange > 0)
					return colorize("+ " + hpChange + " HP", GREEN);
				else
					return colorize("- " + (-hpChange) + " HP", RED);
			
			case XP_CHANGE:
				int xpChange = (Integer)value;
				if (xpChange > 0)
					return colorize("+ " + xpChange + " XP", CYAN);
				else
					return colorize("- " + (-xpChange) + " XP", RED);
			
			case ITEM_GAINED:
				return colorize("+ Item obtained", BRIGHT_YELLOW);
			
			case ALLY_GAINED:
				return colorize("+ Ally joined your party", BRIGHT_GREEN);
			
			case STAT_BUFF:
				ShrineEvent.StatBoost boost = (ShrineEvent.StatBoost)value;
				return colorize("+ " + boost.getValue() + " " + boost.getStatName().toUpperCase() + " (PERMANENT)", BRIGHT_GREEN);

			
			case STAT_DEBUFF:
				return colorize("- Temporary stat reduction", PURPLE);
			
			default:
				return "• Effect applied";
		}
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
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		String controls = colorize("W", BOLD) + "=North, " + colorize("S", BOLD) + "=South, " + 
			colorize("D", BOLD) + "=East, " + colorize("A", BOLD) + "=West, " + 
			colorize("I", BOLD) + "=Info, " + colorize("Q", BOLD) + "=Quit";
		
		int contentWidth = width - 4;
		List<String> wrappedControls = TerminalUI.wrapText(controls, contentWidth);
		
		for (String line : wrappedControls)
		{
			System.out.println(TerminalUI.createBoxedText(line, width));
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), CYAN));
		
		String input = getUserInput(colorize("> ", BRIGHT_GREEN)).toUpperCase();

		switch (input)
		{
			case "W":
				return Direction.NORTH;
			case "S":
				return Direction.SOUTH;
			case "D":
				return Direction.EAST;
			case "A":
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
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createSectionHeader("Choose Your Class", BRIGHT_YELLOW, width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		
		// WARRIOR
		System.out.println(TerminalUI.createBoxedText(colorize("1. WARRIOR", BOLD + RED), width));
		System.out.println(TerminalUI.createBoxedText("   High HP and Defense", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=120, ATK=10, DEF=12", width));
		System.out.println(TerminalUI.createBoxedText("   Growth: HP+12%, ATK+8%, DEF+12%", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		// ARCHER
		System.out.println(TerminalUI.createBoxedText(colorize("2. ARCHER", BOLD + GREEN), width));
		System.out.println(TerminalUI.createBoxedText("   Balanced Stats", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=100, ATK=12, DEF=8", width));
		System.out.println(TerminalUI.createBoxedText("   Growth: HP+10%, ATK+12%, DEF+8%", width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		// ASSASSIN
		System.out.println(TerminalUI.createBoxedText(colorize("3. ASSASSIN", BOLD + PURPLE), width));
		System.out.println(TerminalUI.createBoxedText("   High Attack, Low Defense", width));
		System.out.println(TerminalUI.createBoxedText("   Base: HP=80, ATK=15, DEF=6", width));
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
		int width = TerminalUI.TERMINAL_WIDTH;
		
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
		
		if (hero.hasAlly())
		{
			Ally ally = hero.getAlly();
			
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
			System.out.println(colorize(TerminalUI.createSectionHeader("Your Ally", GREEN, width), GREEN));
			System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
			
			String allyName = colorize(ally.getName(), BOLD + BRIGHT_GREEN);
			System.out.println(TerminalUI.createLabelValueLine("Name:", allyName, width));
			
			String allyHpColor = getHealthColorForStats(ally.getCurrentHp(), ally.getMaxHp());
			System.out.println(TerminalUI.createLabeledProgressBar("HP:", ally.getCurrentHp(), ally.getMaxHp(), 15, allyHpColor));
			System.out.println(TerminalUI.createLabelValueLine("ATK:", colorize("" + ally.getAttack(), RED), width));
		}
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_RED));
		
		System.out.println(TerminalUI.createBoxedText(colorize("1.", BOLD) + " FIGHT", width));
		
		String runText = colorize("2.", BOLD) + " RUN (50% chance";
		if (hero.hasAlly() && hero.getAlly().getType() == be.npremont.swingy.model.enums.AllyType.SNEAKY_THIEF)
		{
			runText += " → 65% with " + hero.getAlly().getName();
		}
		runText += ")";
		System.out.println(TerminalUI.createBoxedText(runText, width));
		
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
		int width = TerminalUI.TERMINAL_WIDTH;
		
		System.out.println();
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createSectionHeader("LOOT FOUND!", BRIGHT_YELLOW, width), BRIGHT_YELLOW));
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "=", "+", width), BRIGHT_YELLOW));
		
		String rarityColor = getRarityColor(newItem.getRarity().name());
		String statColor = getStatColorForItem(newItem.getType());
		
		String foundMsg = "You found: " + colorize(newItem.getName(), BOLD + rarityColor);
		System.out.println(TerminalUI.createBoxedText(foundMsg, width));
		
		String bonusMsg = "+" + newItem.getFlatBonus() + " + " + 
			String.format("%.1f", newItem.getPercentBonus()) + "% " +
			colorize(newItem.getType().getStatName(), statColor) + 
			" (" + colorize(newItem.getRarity().name(), rarityColor) + ")";
		System.out.println(TerminalUI.createBoxedText(bonusMsg, width));
		
		System.out.println(colorize(TerminalUI.createBorderedLine("+", "-", "+", width), BRIGHT_YELLOW));
		
		System.out.println(colorize(TerminalUI.createSectionHeader("COMPARISON", CYAN, width), CYAN));
		
		if (currentItem != null)
		{
			String currentInfo = currentItem.getName() + " (+" + currentItem.getFlatBonus() + 
				" + " + String.format("%.1f", currentItem.getPercentBonus()) + "%)";
			System.out.println(TerminalUI.createBoxedText("Current: " + currentInfo, width));
		}
		else
		{
			System.out.println(TerminalUI.createBoxedText("Current: " + colorize("None", DIM), width));
		}
		
		String newInfo = newItem.getName() + " (+" + newItem.getFlatBonus() + 
			" + " + String.format("%.1f", newItem.getPercentBonus()) + "%)";
		System.out.println(TerminalUI.createBoxedText("New: " + newInfo, width));
		
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

package be.npremont.swingy.view;

import be.npremont.swingy.model.entity.Enemy;
import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.Direction;
import be.npremont.swingy.model.enums.HeroClass;
import be.npremont.swingy.model.game.Combat;

import java.util.List;

public interface IView
{
	void displayMessage(String msg);
	void displayGameStatus(Hero hero, int mapSize, double distanceFromCenter, int distanceToEdge);
	void displayHeroStats(Hero hero);
	void displayCombat(List<String> combatLog);
	void displayVictory(int xpGained);
	void displayDefeat();
	void displayLevelUp(int newLevel);
	void displayHealingSpot(boolean healed, int healAmount, int maxHp);
	String getUserInput(String prompt);
	Direction getDirection(Hero hero);
	HeroClass chooseHeroClass();
	boolean chooseFightOrRun(Hero hero, Enemy enemy);
	boolean chooseEquipItem(Item newItem, Item currentItem);
	
	void clearScreen();
	void waitForInput(String prompt);
}

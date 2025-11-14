package be.npremont.swingy.view;

import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.Direction;
import be.npremont.swingy.model.HeroClass;
import be.npremont.swingy.model.Enemy;
import be.npremont.swingy.model.Item;
import be.npremont.swingy.model.Combat;
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
	String getUserInput(String prompt);
	Direction getDirection(Hero hero);
	HeroClass chooseHeroClass();
	boolean chooseFightOrRun(Hero hero, Enemy enemy);
	boolean chooseEquipItem(Item newItem, Item currentItem);
}

package be.npremont.swingy.view;

import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.Direction;
import be.npremont.swingy.model.HeroClass;

public interface IView
{
	void		displayMessage(String msg);
	void		displayMap(Hero hero, int map_size);
	void		displayHeroStats(Hero hero);
	String		getUserInput(String prompt);
	Direction	getDirection(Hero hero);
	HeroClass	chooseHeroClass();
}

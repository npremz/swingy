package be.npremont.swingy.view;

import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.Direction;

public interface IView
{
	void		displayMessage(String msg);
	void		displayMap(Hero hero, int map_size);
	String		getUserInput(String prompt);
	Direction	getDirection();
}

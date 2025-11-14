package be.npremont.swingy.controller;

import be.npremont.swingy.model.GameMap;
import be.npremont.swingy.model.Hero;
import be.npremont.swingy.model.HeroClass;
import be.npremont.swingy.view.IView;
import be.npremont.swingy.model.Direction;

public class GameController {
	private Hero hero;
	private GameMap game_map;
	private IView view;

	public GameController(IView view)
	{
		this.view = view;
	}

	public void start()
	{
		String name = view.getUserInput("Enter hero name: ");

		HeroClass hero_class = view.chooseHeroClass();
		view.displayMessage("You choose " + hero_class.getDisplayName() + "!");

		int map_size = 10;
		game_map = new GameMap(map_size);

		int center = map_size / 2;
		hero = new Hero(name, hero_class, center, center);

		view.displayHeroStats(hero);

		view.displayMessage("Game started, get on the edges of the map to win!");
		gameLoop();
	}

	private void gameLoop()
	{
		boolean running = true;

		while (running)
		{
			view.displayMap(hero, 10);
			Direction dir = view.getDirection(hero);

			if (dir == null)
			{
				view.displayMessage("Terminating the session.");
				running = false;
				continue;
			}

			int new_x = hero.getX() + dir.getDx();
			int new_y = hero.getY() + dir.getDy();

			if (game_map.isOutOfBounds(new_x, new_y))
			{
				view.displayMessage("Well done! You reached the end of this level.");
				running = false;
				continue;
			}

			hero.move(dir.getDx(), dir.getDy());
		}

	}
}

package be.npremont.swingy;

import be.npremont.swingy.controller.GameController;
import be.npremont.swingy.view.ConsoleView;

public class App 
{
	public static void main(String[] args)
	{
		if (args.length != 1)
		{
			System.out.println("Usage: java -jar swingy.jar [console|gui]");
			return;
		}
		
		String mode = args[0];
		
		if (mode.equals("console"))
		{
			System.out.println("Starting in console mode...");
			ConsoleView view = new ConsoleView();
			GameController game_controller = new GameController(view);
			game_controller.start();
		}
		else if (mode.equals("gui"))
		{
			System.out.println("Starting in GUI mode...");
			// TODO: lancer le jeu en mode GUI
		}
		else
		{
			System.out.println("Invalid mode. Use 'console' or 'gui'");
		}
	}
}

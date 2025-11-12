package be.npremont.swingy;

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
			// TODO: lancer le jeu en mode console
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

package be.npremont.swingy.model.game;

public class HealingSpot
{
	private final int x;
	private final int y;
	private boolean used;

	public HealingSpot(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.used = false;
	}

	public void use()
	{
		this.used = true;
	}

	// Getters
	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}

	public boolean isUsed()
	{
		return used;
	}
}

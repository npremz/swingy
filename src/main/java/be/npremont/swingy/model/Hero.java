package be.npremont.swingy.model;

public class Hero
{
	private String 	name;
	private int		x;
	private int		y;

	public Hero(String name, int startX, int startY)
	{
		this.name = name;
		this.x = startX;
		this.y = startY;
	}

	public void move(int dx, int dy)
	{
		this.x += dx;
		this.y += dy;
	}

	// Getters
	public String getName()
	{
		return name;
	}

	public int getX()
	{
		return x;
	}

	public int getY()
	{
		return y;
	}
}

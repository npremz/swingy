package be.npremont.swingy.model;

public class Hero
{
	private String 		name;
	private int			x;
	private int			y;
	private HeroStats	stats;

	public Hero(String name, HeroClass hero_class, int startX, int startY)
	{
		this.name = name;
		this.x = startX;
		this.y = startY;
		this.stats = new HeroStats(hero_class);
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

	public HeroStats getStats()
	{
		return stats;
	}
}

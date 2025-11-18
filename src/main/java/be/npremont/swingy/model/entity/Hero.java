package be.npremont.swingy.model.entity;

import java.util.HashSet;
import java.util.Set;

import be.npremont.swingy.model.enums.HeroClass;
import be.npremont.swingy.model.stats.HeroStats;

public class Hero
{
	private String 		name;
	private int			x;
	private int			y;
	private HeroStats	stats;
	private Set<String>	visitedPositions;
	private Ally ally;

	public Hero(String name, HeroClass hero_class, int startX, int startY)
	{
		this.name = name;
		this.x = startX;
		this.y = startY;
		this.stats = new HeroStats(hero_class);
		this.visitedPositions = new HashSet<>();
		this.visitedPositions.add(startX + "," + startY);
	}

	public void move(int dx, int dy)
	{
		this.x += dx;
		this.y += dy;
		this.visitedPositions.add(this.x + "," + this.y);
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

	public Set<String> getVisitedPositions()
	{
		return visitedPositions;
	}

	public Ally getAlly()
	{
		return ally;
	}

	public void setAlly(Ally ally)
	{
		this.ally = ally;
	}

	public boolean hasAlly()
	{
		return ally != null && ally.isAlive();
	}

	public void removeAlly()
	{
		this.ally = null;
	}

	public void clearVisitedPositions()
	{
		visitedPositions.clear();
		visitedPositions.add(x + "," + y);
	}
}

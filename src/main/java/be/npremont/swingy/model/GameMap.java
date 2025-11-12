package be.npremont.swingy.model;

public class GameMap
{
	private int size;

	public GameMap(int size)
	{
		this.size = size;
	}

	public boolean isOutOfBounds(int x, int y)
	{
		return x < 0 || x >= size || y < 0 || y >= size;
	}
	
	// Getters
	public int getSize()
	{
		return size;
	}
}

package be.npremont.swingy.model.enums;

public enum Direction
{
	NORTH(0, -1),
	SOUTH(0, 1),
	EAST(1, 0),
	WEST(-1, 0);

	private int dx;
	private int dy;

	Direction(int dx, int dy)
	{
		this.dx = dx;
		this.dy = dy;
	}

	// Getters
	public int getDx()
	{
		return this.dx;
	}

	public int getDy()
	{
		return this.dy;
	}
}

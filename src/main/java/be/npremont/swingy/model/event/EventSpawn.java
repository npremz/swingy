package be.npremont.swingy.model.event;

public class EventSpawn
{
	private final int x;
	private final int y;
	private final IEvent event;
	private boolean triggered;

	public EventSpawn(int x, int y, IEvent event)
	{
		this.x = x;
		this.y = y;
		this.event = event;
		this.triggered = false;
	}

	public void markAsTriggered()
	{
		this.triggered = true;
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

	public IEvent getEvent()
	{
		return event;
	}

	public boolean isTriggered()
	{
		return triggered;
	}
}

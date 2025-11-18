package be.npremont.swingy.model.event;

public class EventChoice
{
	private final int id;
	private final String text;

	public EventChoice(int id, String text)
	{
		this.id = id;
		this.text = text;
	}

	// Getters
	public int getId()
	{
		return id;
	}

	public String getText()
	{
		return text;
	}
}

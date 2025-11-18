package be.npremont.swingy.model.event;

import java.util.List;

public interface IEvent
{
	String getTitle();
	String getDescription();
	List<EventChoice> getEventChoices();
	EventOutcome trigger(GameContext context, int choiceId);
	boolean isUnique();
}

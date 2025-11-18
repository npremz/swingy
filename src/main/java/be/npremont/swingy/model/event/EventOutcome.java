package be.npremont.swingy.model.event;

import be.npremont.swingy.model.event.enums.EffectType;
import java.util.HashMap;
import java.util.Map;

public class EventOutcome
{
	private final boolean success;
	private final String message;
	private final Map<EffectType, Object> effects;

	public EventOutcome(boolean success, String message)
	{
		this.success = success;
		this.message = message;
		this.effects = new HashMap<>();
	}

	public void addEffect(EffectType type, Object value)
	{
		effects.put(type, value);
	}

	// Getters
	public boolean isSuccess()
	{
		return success;
	}

	public String getMessage()
	{
		return message;
	}

	public Map<EffectType, Object> getEffects()
	{
		return effects;
	}

	public boolean hasEffect(EffectType type)
	{
		return effects.containsKey(type);
	}

	public Object getEffect(EffectType type)
	{
		return effects.get(type);
	}
}

package be.npremont.swingy.model;

public enum ItemType
{
	WEAPON("Weapon", "Attack"),
	ARMOR("Armor", "Defense"),
	HELM("Helm", "HP");

	private final String displayName;
	private final String statName;

	ItemType(String displayName, String statName)
	{
		this.displayName = displayName;
		this.statName = statName;
	}

	// Getters
	public String getDisplayName()
	{
		return displayName;
	}

	public String getStatName()
	{
		return statName;
	}
}

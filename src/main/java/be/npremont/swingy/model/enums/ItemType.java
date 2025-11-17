package be.npremont.swingy.model.enums;

public enum ItemType
{
	WEAPON("Weapon", "Attack"),
	ARMOR("Armor", "Defense"),
	HELM("Helm", "HP");

	private final String display_name;
	private final String stat_name;

	ItemType(String display_name, String stat_name)
	{
		this.display_name = display_name;
		this.stat_name = stat_name;
	}

	// Getters
	public String getDisplayName()
	{
		return display_name;
	}

	public String getStatName()
	{
		return stat_name;
	}
}

package be.npremont.swingy.model;

public class HeroStats
{
	private int max_hp;
	private int current_hp;
	private int attack;
	private int defense;
	private int xp;
	private int level;
	private HeroClass hero_class;

	private Item weapon;
	private Item armor;
	private Item helm;

	public HeroStats(HeroClass hero_class)
	{
		this.hero_class = hero_class;
		this.level = 1;
		this.xp = 0;

		BaseStats base_stats = hero_class.getBaseStats();
		this.max_hp = base_stats.getHp();
		this.current_hp = this.max_hp;
		this.attack = base_stats.getAttack();
		this.defense = base_stats.getDefense();

		this.weapon = null;
		this.armor = null;
		this.helm = null;
	}

	public void addXp(int amount)
	{
		this.xp += amount;

		while (this.xp >= getXpToNextLevel())
			levelUp();
	}

	private void levelUp()
	{
		double hp_ratio = (double) current_hp / max_hp;
		GrowthRates growth = hero_class.getGrowthRates();

		this.max_hp = (int) Math.round(max_hp * growth.getHpMultiplier());
		this.attack = (int) Math.round(attack * growth.getAttackMultiplier());
		this.defense = (int) Math.round(defense * growth.getDefenseMultiplier());

		this.current_hp = (int) Math.round(hp_ratio * max_hp);

		this.level++;
	}

	public int getXpToNextLevel()
	{
		return level * 1000 + (int) Math.pow(level - 1, 2) * 450;
	}

	public void takeDamage(int damage)
	{
		this.current_hp = Math.max(0, this.current_hp - damage);
	}

	public void heal(int amount)
	{
		this.current_hp = Math.min(getMaxHpWithItems(), this.current_hp + amount);
	}

	public void equipWeapon(Item item)
	{
		if (item.getType() != ItemType.WEAPON)
			throw new IllegalArgumentException("Can only equip weapons in weapon slot");
		this.weapon = item;
	}

	public void equipArmor(Item item)
	{
		if (item.getType() != ItemType.ARMOR)
			throw new IllegalArgumentException("Can only equip armor in armor slot");
		this.armor = item;
	}

	public void equipHelm(Item item)
	{
		if (item.getType() != ItemType.HELM)
			throw new IllegalArgumentException("Can only equip helms in helm slot");

		int currentHpWithoutHelm = this.current_hp;
		int oldMaxHp = getMaxHpWithItems();

		this.helm = item;

		int newMaxHp = getMaxHpWithItems();
		if (oldMaxHp > 0)
		{
			double ratio = (double) currentHpWithoutHelm / oldMaxHp;
			this.current_hp = (int) Math.round(newMaxHp * ratio);
		}
	}

	public void equipItem(Item item)
	{
		switch (item.getType())
		{
			case WEAPON:
				equipWeapon(item);
				break;
			case ARMOR:
				equipArmor(item);
				break;
			case HELM:
				equipHelm(item);
				break;
		}
	}

	public int getTotalAttack()
	{
		int total = attack;
		if (weapon != null)
			total += weapon.getBonus();
		return total;
	}

	public int getTotalDefense()
	{
		int total = defense;
		if (armor != null)
			total += armor.getBonus();
		return total;
	}

	public int getMaxHpWithItems()
	{
		int total = max_hp;
		if (helm != null)
			total += helm.getBonus();
		return total;
	}

	// Getters 
	public int getMaxHp()
	{
		return max_hp;
	}

	public int getCurrentHp()
	{
		return current_hp;
	}

	public int getAttack()
	{
		return attack;
	}

	public int getDefense()
	{
		return defense;
	}

	public int getXp()
	{
		return xp;
	}

	public int getLevel()
	{
		return level;
	}

	public HeroClass getHeroClass()
	{
		return hero_class;
	}

	public Item getWeapon()
	{
		return weapon;
	}

	public Item getArmor()
	{
		return armor;
	}

	public Item getHelm()
	{
		return helm;
	}

	public boolean hasWeapon()
	{
		return weapon != null;
	}

	public boolean hasArmor()
	{
		return armor != null;
	}

	public boolean hasHelm()
	{
		return helm != null;
	}

	// Setter pour currentHp (utile pour les combats)
	public void setCurrentHp(int hp)
	{
		this.current_hp = Math.max(0, Math.min(hp, getMaxHpWithItems()));
	}
}

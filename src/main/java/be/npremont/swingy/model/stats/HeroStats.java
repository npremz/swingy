package be.npremont.swingy.model.stats;

import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.HeroClass;
import be.npremont.swingy.model.enums.ItemType;

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

	public void healPercentage(double percentage)
	{
		int maxHp = getMaxHpWithItems();
		int healAmount = (int)Math.round(maxHp * percentage);
		this.current_hp = Math.min(maxHp, this.current_hp + healAmount);
	}

	public void equipWeapon(Item item)
	{
		if (item != null && item.getType() != ItemType.WEAPON)
			throw new IllegalArgumentException("Can only equip weapons in weapon slot");
		this.weapon = item;
	}

	public void equipArmor(Item item)
	{
		if (item != null && item.getType() != ItemType.ARMOR)
			throw new IllegalArgumentException("Can only equip armor in armor slot");
		this.armor = item;
	}

	public void equipHelm(Item item)
	{
		if (item != null && item.getType() != ItemType.HELM)
			throw new IllegalArgumentException("Can only equip helms in helm slot");

		int oldMaxHp = getMaxHpWithItems();

		this.helm = item;

		if (item != null)
		{
			int newMaxHp = getMaxHpWithItems();
			if (oldMaxHp > 0)
			{
				double ratio = (double)current_hp / oldMaxHp;
				this.current_hp = (int)Math.round(newMaxHp * ratio);
			}
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
		{
			int bonus = weapon.calculateTotalBonus(attack);
			total += bonus;
		}
		return total;
	}

	public int getTotalDefense()
	{
		int total = defense;
		if (armor != null)
		{
			int bonus = armor.calculateTotalBonus(defense);
			total += bonus;
		}
		return total;
	}

	public int getMaxHpWithItems()
	{
		int total = max_hp;
		if (helm != null)
		{
			int bonus = helm.calculateTotalBonus(max_hp);
			total += bonus;
		}
		return total;
	}

	public double getCritChance()
	{
		double base_crit = 5.0;
		
		switch (hero_class)
		{
			case WARRIOR:
				base_crit = 5.0;
				break;
			case ARCHER:
				base_crit = 15.0;
				break;
			case ASSASSIN:
				base_crit = 10.0;
				break;
		}
		
		return base_crit;
	}

	public double getCritMultiplier()
	{
		switch (hero_class)
		{
			case WARRIOR:
				return 1.5;
			case ARCHER:
				return 1.75;
			case ASSASSIN:
				return 2.0;
			default:
				return 1.5;
		}
	}

	public double getDamageReduction()
	{
		switch (hero_class)
		{
			case WARRIOR:
				return 0.05;
			case ARCHER:
				return 0.0;
			case ASSASSIN:
				return 0.0;
			default:
				return 0.0;
		}
	}

	public void addPermanentAttack(int bonus)
	{
		this.attack += bonus;
	}

	public void addPermanentDefense(int bonus)
	{
		this.defense += bonus;
	}

	public void addPermanentMaxHp(int bonus)
	{
		this.max_hp += bonus;
		double ratio = (double)current_hp / (max_hp - bonus);
		this.current_hp = (int)Math.round(ratio * max_hp);
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

	public void unequipWeapon()
	{
		this.weapon = null;
	}

	public void unequipArmor()
	{
		this.armor = null;
	}

	public void unequipHelm()
	{
		this.helm = null;
	}

	public void setCurrentHp(int hp)
	{
		this.current_hp = Math.max(0, Math.min(hp, getMaxHpWithItems()));
	}
}

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

	// Setters
	public void setCurrentHp(int hp)
	{
		this.current_hp = Math.max(0, Math.min(hp, max_hp));
	}
}

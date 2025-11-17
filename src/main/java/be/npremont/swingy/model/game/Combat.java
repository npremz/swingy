package be.npremont.swingy.model.game;

import java.util.ArrayList;
import java.util.List;

import be.npremont.swingy.model.entity.Enemy;
import be.npremont.swingy.model.entity.Hero;
import be.npremont.swingy.model.entity.Item;
import be.npremont.swingy.model.enums.ItemType;

public class Combat
{
	private Hero hero;
	private Enemy enemy;
	private List<String> combat_log;

	public Combat(Hero hero, Enemy enemy)
	{
		this.hero = hero;
		this.enemy = enemy;
		this.combat_log = new ArrayList<>();
	}

	public CombatResult fight()
	{
		int round = 1;

		while (hero.getStats().getCurrentHp() > 0 && enemy.isAlive())
		{
			combat_log.add("[Round " + round + "]");

			int heroDamage = calculateDamage(
				hero.getStats().getTotalAttack(), 
				enemy.getDefense(),
				hero.getStats().getCritChance(),
				hero.getStats().getCritMultiplier(),
				true
			);
			enemy.takeDamage(heroDamage);
			combat_log.add("You deal " + heroDamage + " damage! (Enemy: " + enemy.getCurrentHp() + "/" + enemy.getMaxHp() + " HP)");

			if (!enemy.isAlive())
			{
				combat_log.add(enemy.getType().getDisplayName() + " is defeated!");
				break;
			}

			int enemyDamage = calculateDamage(
				enemy.getAttack(), 
				hero.getStats().getTotalDefense(),
				5.0,
				1.5,
				false
			);
			
			enemyDamage = (int)(enemyDamage * (1 - hero.getStats().getDamageReduction()));
			
			hero.getStats().takeDamage(enemyDamage);
			combat_log.add(enemy.getType().getDisplayName() + " deals " + enemyDamage + " damage! (You: " + hero.getStats().getCurrentHp() + "/" + hero.getStats().getMaxHpWithItems() + " HP)");

			if (hero.getStats().getCurrentHp() <= 0)
			{
				combat_log.add("You have been defeated...");
				break;
			}

			combat_log.add("");
			round++;
		}

		if (hero.getStats().getCurrentHp() > 0)
			return new CombatResult(true, enemy.getXpReward(), rollLoot());
		else
			return new CombatResult(false, 0, null);
	}

	private int calculateDamage(int attack, int defense, double critChance, double critMultiplier, boolean canCrit)
	{
		double defenseMultiplier = 1.0 - (defense / (defense + 100.0));
		
		double baseDamage = attack * defenseMultiplier;
		
		double variance = 0.85 + (Math.random() * 0.30);
		double finalDamage = baseDamage * variance;
		
		if (canCrit && Math.random() * 100.0 < critChance)
		{
			finalDamage *= critMultiplier;
			combat_log.add("CRITICAL HIT!");
		}
		
		return Math.max(1, (int)Math.round(finalDamage));
	}


	private Item rollLoot()
	{
		if (Math.random() < 0.40)
		{
			return null;
		}

		ItemType[] types = ItemType.values();
		ItemType randomType = types[(int)(Math.random() * types.length)];

		return Item.generateRandom(randomType);
	}

	// Getters
	public List<String> getCombatLog()
	{
		return combat_log;
	}

	public static class CombatResult
	{
		private final boolean victory;
		private final int xpGained;
		private final Item loot;

		public CombatResult(boolean victory, int xpGained, Item loot)
		{
			this.victory = victory;
			this.xpGained = xpGained;
			this.loot = loot;
		}

		// Getters
		public boolean isVictory()
		{
			return victory;
		}

		public int getXpGained()
		{
			return xpGained;
		}

		public Item getLoot()
		{
			return loot;
		}

		public boolean hasLoot()
		{
			return loot != null;
		}
	}
}

/*
 * This file is part of the L2J Mobius project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jmobius.gameserver.model.drops.strategy;

import com.l2jmobius.Config;
import com.l2jmobius.gameserver.datatables.ItemTable;
import com.l2jmobius.gameserver.model.actor.L2Character;
import com.l2jmobius.gameserver.model.drops.GeneralDropItem;
import com.l2jmobius.gameserver.model.itemcontainer.Inventory;

/**
 * @author Battlecruiser
 */
public interface IChanceMultiplierStrategy
{
	IChanceMultiplierStrategy DROP = DEFAULT_STRATEGY(Config.RATE_DEATH_DROP_CHANCE_MULTIPLIER);
	IChanceMultiplierStrategy SPOIL = DEFAULT_STRATEGY(Config.RATE_CORPSE_DROP_CHANCE_MULTIPLIER);
	IChanceMultiplierStrategy STATIC = (item, victim) -> 1;
	
	IChanceMultiplierStrategy QUEST = (item, victim) ->
	{
		double championmult;
		if ((item.getItemId() == Inventory.ADENA_ID) || (item.getItemId() == Inventory.ANCIENT_ADENA_ID))
		{
			championmult = Config.L2JMOD_CHAMPION_ADENAS_REWARDS_CHANCE;
		}
		else
		{
			championmult = Config.L2JMOD_CHAMPION_REWARDS_CHANCE;
		}
		
		return (Config.L2JMOD_CHAMPION_ENABLE && (victim != null) && victim.isChampion()) ? (Config.RATE_QUEST_DROP * championmult) : Config.RATE_QUEST_DROP;
	};
	
	static IChanceMultiplierStrategy DEFAULT_STRATEGY(double defaultMultiplier)
	{
		return (item, victim) ->
		{
			float multiplier = 1;
			if (victim.isChampion())
			{
				multiplier *= item.getItemId() != Inventory.ADENA_ID ? Config.L2JMOD_CHAMPION_REWARDS_CHANCE : Config.L2JMOD_CHAMPION_ADENAS_REWARDS_CHANCE;
			}
			final Float dropChanceMultiplier = Config.RATE_DROP_CHANCE_MULTIPLIER.get(item.getItemId());
			if (dropChanceMultiplier != null)
			{
				multiplier *= dropChanceMultiplier;
			}
			else if (ItemTable.getInstance().getTemplate(item.getItemId()).hasExImmediateEffect())
			{
				multiplier *= Config.RATE_HERB_DROP_CHANCE_MULTIPLIER;
			}
			else if (victim.isRaid())
			{
				multiplier *= Config.RATE_RAID_DROP_CHANCE_MULTIPLIER;
			}
			else
			{
				multiplier *= defaultMultiplier;
			}
			return multiplier;
		};
	}
	
	double getChanceMultiplier(GeneralDropItem item, L2Character victim);
}

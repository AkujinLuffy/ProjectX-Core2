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
package org.l2jmobius.gameserver.network.serverpackets;

import java.util.Set;

import org.l2jmobius.gameserver.data.xml.EnchantSkillGroupsData;
import org.l2jmobius.gameserver.network.ServerPackets;
import org.l2jmobius.gameserver.util.SkillEnchantConverter;

public class ExEnchantSkillInfo extends ServerPacket
{
	private final Set<Integer> _routes;
	private final int _skillId;
	private final int _skillLevel;
	private final int _skillSubLevel;
	private final int _currentSubLevel;
	
	public ExEnchantSkillInfo(int skillId, int skillLevel, int skillSubLevel, int currentSubLevel)
	{
		_skillId = skillId;
		_skillLevel = skillLevel;
		_skillSubLevel = skillSubLevel;
		_currentSubLevel = currentSubLevel;
		_routes = EnchantSkillGroupsData.getInstance().getRouteForSkill(_skillId, _skillLevel);
	}
	
	@Override
	public void write()
	{
		ServerPackets.EX_ENCHANT_SKILL_INFO.writeId(this);
		writeInt(_skillId);
		writeInt(_skillSubLevel > 1000 ? SkillEnchantConverter.levelToErtheia(_skillSubLevel) : _skillLevel);
		writeInt((_skillSubLevel % 1000) != EnchantSkillGroupsData.MAX_ENCHANT_LEVEL);
		writeInt(_skillSubLevel > 1000);
		writeInt(_routes.size());
		_routes.forEach(route ->
		{
			final int subLevel = (_currentSubLevel > 0 ? (route + (_currentSubLevel % 1000)) - 1 : route);
			writeInt(subLevel > 1000 ? SkillEnchantConverter.levelToErtheia(subLevel) : subLevel);
		});
	}
}

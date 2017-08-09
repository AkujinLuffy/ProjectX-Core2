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
package quests.Q00470_DivinityProtector;

import com.l2jmobius.gameserver.enums.QuestType;
import com.l2jmobius.gameserver.model.actor.L2Npc;
import com.l2jmobius.gameserver.model.actor.instance.L2PcInstance;
import com.l2jmobius.gameserver.model.quest.Quest;
import com.l2jmobius.gameserver.model.quest.QuestState;
import com.l2jmobius.gameserver.model.quest.State;

/**
 * Divinity Protector (470)
 * @author St3eT
 */
public final class Q00470_DivinityProtector extends Quest
{
	// NPCs
	private static final int ADVENTURER = 32327;
	private static final int AGRIPEL = 31348;
	private static final int[] MONSTERS =
	{
		21520, // Eye of Splendor
		21521, // Claws of Splendor
		21523, // Flash of Splendor
		21524, // Blade of Splendor
		21526, // Wisdom of Splendor
		21542, // Pilgrim's Disciple
		21543, // Page of Pilgrim
		21527, // Fury of Splendor
		21529, // Soul of Splendor
		21541, // Pilgrim of Splendor
		21530, // Victory of Splendor
		21532, // Shout of Splendor
		21533, // Alliance of Splendor
		21535, // Signet of Splendor
		21536, // Crown of Splendor
		21545, // Judge of Fire
		21546, // Judge of Light
		21537, // Fang of Splendor
		21539, // Wailing of Splendor
		21544, // Judge of Splendor
	};
	// Items
	private static final int ASH = 19489; // Remnant Ash
	// Misc
	private static final int MIN_LEVEL = 60;
	private static final int MAX_LEVEL = 64;
	
	public Q00470_DivinityProtector()
	{
		super(470);
		addStartNpc(ADVENTURER);
		addTalkId(ADVENTURER, AGRIPEL);
		addKillId(MONSTERS);
		registerQuestItems(ASH);
		addCondLevel(MIN_LEVEL, MAX_LEVEL, "");
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState st = getQuestState(player, false);
		
		if (st == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "32327-02.htm":
			case "32327-03.htm":
			{
				htmltext = event;
				break;
			}
			case "32327-04.htm":
			{
				st.startQuest();
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		String htmltext = getNoQuestMsg(player);
		final QuestState st = getQuestState(player, true);
		
		switch (st.getState())
		{
			case State.CREATED:
			{
				if (npc.getId() == ADVENTURER)
				{
					htmltext = "32327-01.htm";
				}
				break;
			}
			case State.STARTED:
			{
				if (st.isCond(1))
				{
					htmltext = npc.getId() == ADVENTURER ? "32327-05.html" : "31348-01.html";
				}
				else if (st.isCond(2))
				{
					if (npc.getId() == ADVENTURER)
					{
						htmltext = "32327-06.html";
					}
					else if (npc.getId() == AGRIPEL)
					{
						st.exitQuest(QuestType.DAILY, true);
						giveAdena(player, 194_000, true);
						if (player.getLevel() >= MIN_LEVEL)
						{
							addExpAndSp(player, 1_879_400, 451);
						}
						htmltext = "31348-02.html";
					}
				}
				break;
			}
			case State.COMPLETED:
			{
				if ((npc.getId() == ADVENTURER) && st.isNowAvailable())
				{
					st.setState(State.CREATED);
					htmltext = "32327-01.htm";
				}
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState st = getQuestState(killer, false);
		
		if ((st != null) && st.isCond(1))
		{
			if (giveItemRandomly(killer, ASH, 1, 20, 0.20, true))
			{
				st.setCond(2);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}
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
package com.l2jmobius.tools.dbinstaller;

import java.sql.Connection;

/**
 * @author mrTJO
 */
public interface DBOutputInterface
{
	void setProgressIndeterminate(boolean value);
	
	void setProgressMaximum(int maxValue);
	
	void setProgressValue(int value);
	
	void setFrameVisible(boolean value);
	
	void appendToProgressArea(String text);
	
	Connection getConnection();
	
	int requestConfirm(String title, String message, int type);
	
	void showMessage(String title, String message, int type);
}

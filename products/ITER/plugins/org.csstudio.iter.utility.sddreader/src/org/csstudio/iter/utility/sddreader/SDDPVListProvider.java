/*******************************************************************************
 * Copyright (c) 2010-2013 ITER Organization.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.csstudio.iter.utility.sddreader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import org.csstudio.autocomplete.AutoCompleteHelper;
import org.csstudio.autocomplete.AutoCompleteResult;
import org.csstudio.autocomplete.IAutoCompleteProvider;
import org.csstudio.platform.utility.rdb.RDBUtil;

public class SDDPVListProvider implements IAutoCompleteProvider {

	public static final String NAME = "SDD Database";

	private RDBUtil rdb;

	private final String pv_count = "SELECT count(*) FROM functionalvariables WHERE name like ?";
	private final String pv_get = "SELECT name FROM functionalvariables WHERE name like ? order by name";

	private PreparedStatement statement_get = null;
	private PreparedStatement statement_count = null;

	public SDDPVListProvider() {
		try {
			rdb = RDBUtil.connect(Preferences.getRDB_Url(),
					Preferences.getRDB_User(), Preferences.getRDB_Password(),
					true);
			Activator.getLogger().log(
					Level.INFO,
					"SDDPVListProvider connected to DB: "
							+ Preferences.getRDB_Url());
		} catch (Exception e) {
			Activator.getLogger().log(Level.SEVERE, e.getMessage());
		}
	}

	@Override
	public AutoCompleteResult listResult(final String type, final String name,
			final int limit) {
		AutoCompleteResult result = new AutoCompleteResult();

		try {
			String sqlPattern = AutoCompleteHelper.convertToSQL(name);
			statement_count = rdb.getConnection().prepareStatement(pv_count);
			statement_count.setString(1, sqlPattern);

			final ResultSet result_count = statement_count.executeQuery();
			if (result_count.next())
				result.setCount(result_count.getInt(1));

			statement_get = rdb.getConnection().prepareStatement(pv_get);
			statement_get.setString(1, sqlPattern);
			statement_get.setMaxRows(limit);

			final ResultSet result_get = statement_get.executeQuery();
			while (result_get.next()) {
				result.add(result_get.getString(1));
			}
		} catch (Exception e) {
			if ("!ERROR: canceling statement due to user request".equals(e
					.getMessage())) {
				Activator.getLogger().log(Level.WARNING, e.getMessage());
			}
		} finally {
			try {
				if (statement_count != null && !statement_count.isClosed())
					statement_count.close();
				if (statement_get != null && !statement_get.isClosed())
					statement_get.close();
			} catch (SQLException e) {
				Activator.getLogger().log(Level.WARNING, e.getMessage());
			}
		}
		return result;
	}

	@Override
	public void cancel() {
		try {
			if (statement_count != null && !statement_count.isClosed())
				statement_count.cancel();
			if (statement_get != null && !statement_get.isClosed())
				statement_get.cancel();
		} catch (Exception e) {
			Activator.getLogger().log(Level.WARNING, e.getMessage());
		} finally {
			try {
				if (statement_count != null && !statement_count.isClosed())
					statement_count.close();
				if (statement_get != null && !statement_get.isClosed())
					statement_get.close();
			} catch (SQLException e) {
				Activator.getLogger().log(Level.WARNING, e.getMessage());
			}
		}
	}

}

/*
 * SonarQube
 * Copyright (C) 2009-2024 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.platform.db.migration.version.v105;

import java.sql.SQLException;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.db.MigrationDbTester;
import org.sonar.server.platform.db.migration.sql.DbPrimaryKeyConstraintFinder;
import org.sonar.server.platform.db.migration.sql.DropPrimaryKeySqlGenerator;
import org.sonar.server.platform.db.migration.step.DdlChange;

import static org.sonar.server.platform.db.migration.version.v105.DropPrimaryKeyConstraintInRulesDefaultImpactsTable.COLUMN_NAME;
import static org.sonar.server.platform.db.migration.version.v105.DropPrimaryKeyConstraintInRulesDefaultImpactsTable.CONSTRAINT_NAME;
import static org.sonar.server.platform.db.migration.version.v105.DropPrimaryKeyConstraintInRulesDefaultImpactsTable.TABLE_NAME;

public class DropPrimaryKeyConstraintInRulesDefaultImpactsTableIT {

  @Rule
  public final MigrationDbTester db = MigrationDbTester.createForMigrationStep(DropPrimaryKeyConstraintInRulesDefaultImpactsTable.class);

  private final DbPrimaryKeyConstraintFinder dbPrimaryKeyConstraintFinder = new DbPrimaryKeyConstraintFinder(db.database());
  private final DdlChange underTest = new DropPrimaryKeyConstraintInRulesDefaultImpactsTable(db.database(),
    new DropPrimaryKeySqlGenerator(db.database(), dbPrimaryKeyConstraintFinder), dbPrimaryKeyConstraintFinder);

  @Test
  public void execute_shouldRemoveExistingPrimaryKey() throws SQLException {
    db.assertPrimaryKey(TABLE_NAME, CONSTRAINT_NAME, COLUMN_NAME);
    underTest.execute();
    db.assertNoPrimaryKey(TABLE_NAME);
  }

  @Test
  public void execute_when_reentrant_shouldRemoveExistingPrimaryKey() throws SQLException {
    db.assertPrimaryKey(TABLE_NAME, CONSTRAINT_NAME, COLUMN_NAME);
    underTest.execute();
    underTest.execute();
    db.assertNoPrimaryKey(TABLE_NAME);
  }
}
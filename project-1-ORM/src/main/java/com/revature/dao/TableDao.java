package com.revature.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import com.revature.Relation;
import com.revature.util.ColumnField;
//import com.revature.inspection.ClassInspector;
import com.revature.util.ConnectionUtil;
import com.revature.util.ForeignKeyField;
import com.revature.util.GenericField;
import com.revature.util.MetaModel;
import com.revature.util.PrimaryKeyField;

public class TableDao implements Dao {
	// private static Logger logBot = Logger.getLogger(TableDao.class);


	private static String managment = ConnectionUtil.getManagement();
	private static String schema = ConnectionUtil.getSchema();
	// private static Connection conn = ConnectionUtil.getConnection();

	/**
	 * This returns the string to set the column field If the Column name wasn't
	 * set, it returns the field name
	 *
	 * @author Caleb Kirschbaum
	 */
	private static String getSQLName(ColumnField myField) {
		String fieldName = myField.getColumnName();
		if (myField.getColumnName().equals("")) {

			fieldName = myField.getName();
		}
		return fieldName;
	}

	/**
	 * This returns the string of the SQL type that would be set for the column
	 *
	 * @author Caleb Kirschbaum
	 */
	private static String getSQLType(ColumnField myField) {
		String modifications = "";
		Class<?> type = myField.getType();

		if ((type == int.class) || (type == Integer.class)) {
			modifications += " int";
		} // Test against Integer
		else if ((type == byte.class) || (type == short.class)) {
			modifications += " smallint";
		} else if (type == long.class) {
			modifications += " bigint";
		} else if ((type == float.class) || (type == double.class) || (type == Double.class)) {
			modifications += " numeric(" + myField.getPercision() + ", " + myField.getScale() + ")";
		} else if (type == boolean.class) {
			modifications += " boolean";
		} else if ((type == char.class) || (type == Character.class)) {
			modifications += " char(1)";
		} else if (type == String.class) {
			modifications += " varchar(" + myField.getLength() + ")";
		} else if ((type == java.util.Date.class) || (type == java.sql.Date.class)
				|| (type == java.time.LocalDate.class)) {
			modifications += " date";
		} else {
			System.out.println(myField.getName());
			throw new IllegalArgumentException("Invalid data type for a Column key");
		}
		return modifications;
	}

	/**
	 * This returns the string of the SQL type that would be set for the column
	 *
	 * @author Caleb Kirschbaum
	 */
	private static String getSQLType(PrimaryKeyField myField) {
		String modifications = "";

		if (myField.getType() == int.class) {
			modifications += " int";
		} // Test against Integer
		else if ((myField.getType() == byte.class) || (myField.getType() == short.class)) {
			modifications += " smallint";
		} else if (myField.getType() == long.class) {
			modifications += " bigint";
		} else if (myField.getType() == boolean.class) {
			modifications += " boolean";
		} else if (myField.getType() == char.class) {
			modifications += " char(1)";
		} else if ((myField.getType() == java.util.Date.class) || (myField.getType() == java.sql.Date.class)
				|| (myField.getType() == java.time.LocalDate.class)) {
			modifications += " date";
		} else {
			throw new IllegalArgumentException("Invalid data type for a Primary key");
		}
		return modifications;
	}


	/**
	 * This returns the string of the SQL modification that would be set for the
	 * column
	 *
	 * <pre>
	 *
	 * List<ColumnField> cf = allFieldsInTable.getColumns();
	 * for (ColumnField t : cd) {
	 * 	String modification = getSQLModification(cf);
	 * }
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	private static String getSQLModification(ColumnField myField) {
		String modifications = "";
		if (myField.isSerial()) {
			modifications += " serial";
		}
		if (!myField.isNullable()) {
			modifications += " not null";
		}
		if (myField.isUnique()) {
			modifications += " unique";
		}
		if (!myField.getDefaultValue().equals("")) {
			modifications += " default " + myField.getDefaultValue();
		}
		/*
		 * if(myField.getCheckValue()!="" ) {
		 * modifications+=" check "+myField.getCheckValue(); }
		 */

		return modifications;

	}



	/**
	 * <P>
	 * This creates the table in the database Input is a metaModel
	 * <P>
	 * <pre>
	 * TableDao td = new TableDao();
	 * td.insert(MetaModel.of(clazz));
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void insert(MetaModel<?> allFieldsInTable)
			throws SQLException, ClassNotFoundException {
		String name = allFieldsInTable.getTableName();
		if (name.equals("")) {
			name = allFieldsInTable.getSimpleClassName();
		}
		//		System.out.println("\n\n\n\n\nCreating " + name);

		String sql = "";
		/*
		 * validate: validate the schema, makes no changes to the database. update:
		 * update the schema. create: creates the schema, destroying previous data.
		 *
		 */

		switch (TableDao.managment.toLowerCase()) {
		case ("validate"):
			throw new IllegalArgumentException("Can't insert tables on validate control. Change control to insert.");
		case ("create"):
			sql += "create table if not exists";
		break;
		case ("update"):
			alter(allFieldsInTable);
		return;
		default:
			throw new IllegalArgumentException("Not a valid managment style. Change to validate, create, or update");

		}

		name = "\"" + name + "\"";
		sql += " " + TableDao.schema + "." + name + "(";

		PrimaryKeyField PField = allFieldsInTable.getPrimaryKey();

		// for(PrimaryKeyField PFields : allFieldsInTable.getPrimaryKey()) {

		// }//Use for composite keys
		String PfieldName = "\"" + PField.getColumnName() + "\"";// Correct the naming convention
		if (PField.getColumnName().equals("")) {

			PfieldName = "\"" + PField.getName() + "\"";// Correct the naming convention
		}
		sql += PfieldName;


		Class<?> type = PField.getType();
		if (PField.isSerial()) {
			sql += " serial";
		} else if (type == int.class) {
			sql += " int";
		} // Test against Integer
		else if ((type == byte.class) || (type == short.class)) {
			sql += " smallint";
		} else if (type == long.class) {
			sql += " bigint";
		} else {
			throw new IllegalArgumentException("Invalid data type for a primary key");
		}

		sql += " primary key,";
		String modifications = "";
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			modifications += " \"" + TableDao.getSQLName(myField) + "\" ";
			if (!myField.isSerial()) {
				modifications += " " + TableDao.getSQLType(myField) + " ";
			}
			modifications += TableDao.getSQLModification(myField) + ",";

		}
		sql += modifications;
		sql = sql.substring(0, sql.length() - 1);
		sql += ");";

		TableDao.conn.setAutoCommit(false);
		String createSchemaSql = "CREATE SCHEMA IF NOT EXISTS " + TableDao.schema;
		PreparedStatement createStatment = TableDao.conn.prepareStatement(createSchemaSql);
		createStatment.execute();
		createStatment.close();
		if (TableDao.managment.toLowerCase().equals("create")) {

			drop(allFieldsInTable);


		}
		// System.out.println(sql);
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		// System.out.println("\n"+myStatment.toString());//Uncomment if throwing errors
		// to see what is being queried
		myStatment.execute();
		myStatment.close();
		TableDao.conn.commit();
		TableDao.conn.setAutoCommit(true);
		addForeignKeys(allFieldsInTable);
	}


	/**
	 * This alters a table that already exists to match the current class
	 *
	 * <pre>
	 *
	 *
	 *
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void alter(MetaModel<?> allFieldsInTable)
			throws SQLException, ClassNotFoundException {
		String name = allFieldsInTable.getTableName();
		if (name.equals("")) {
			name = allFieldsInTable.getSimpleClassName();
		}

		// Delete all nonexisting
		String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema = '" + TableDao.schema
				+ "' AND table_name  = '" + name + "';";
		// System.out.println(sql);
		String fieldName;
		TableDao.conn.setAutoCommit(false);

		PrimaryKeyField PField = allFieldsInTable.getPrimaryKey();
		fieldName = PField.getColumnName();
		if (PField.getColumnName().equals("")) {
			fieldName = PField.getName();
		}
		List<String> newColumns = new ArrayList<>();
		PField = allFieldsInTable.getPrimaryKey();
		String PfieldName = PField.getColumnName();// Correct the naming convention
		if (PField.getColumnName().equals("")) {
			PfieldName = PField.getName();// Correct the naming convention
		}


		newColumns.add(PfieldName);
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			// System.out.println(myField.getName());
			newColumns.add(TableDao.getSQLName(myField));
		}

		List<String> currentColumns = new ArrayList<>();

		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		ResultSet rs = myStatment.executeQuery();
		while (rs.next()) {
			String columnName = rs.getString("column_name");
			currentColumns.add(columnName);
		}
		myStatment.close();
		if (currentColumns.size() == 0) {
			String createTable = "create table if not exists \"" + TableDao.schema + "\".\"" + name + "\"()";
			PreparedStatement createStatment = TableDao.conn.prepareStatement(createTable);
			// System.out.println(createStatment.toString());
			createStatment.execute();
			createStatment.close();
		}

		// System.out.println("The current columns " + currentColumns);
		// System.out.println(newColumns);

		List<String> badColumns = new ArrayList<>(currentColumns);
		badColumns.removeAll(newColumns);
		// System.out.println("Bad columns" + badColumns);

		for (String columnName : badColumns) {
			sql = "ALTER TABLE \"" + TableDao.schema + "\".\"" + name + "\" DROP COLUMN if exists \"" + columnName
					+ "\" cascade;";
			myStatment = TableDao.conn.prepareStatement(sql);
			myStatment.execute();
			myStatment.close();
		}

		fieldName = PfieldName;
		sql = "ALTER TABLE \"" + TableDao.schema + "\".\"" + name + "\"";
		//		System.out.println("Current Columns: " + currentColumns);
		//		System.out.println("" + fieldName);
		//		System.out.println("Current cointains: "+currentColumns.contains(fieldName));
		if (!currentColumns.contains(fieldName)) {
			sql += " add";
			sql += " column \"" + fieldName + "\"";

			Class<?> type = PField.getType();
			if (PField.isSerial()) {
				sql += " serial";
			} else if (type == int.class) {
				sql += " int";
			} else if ((type == byte.class) || (type == short.class)) {
				sql += " smallint";
			} else if (type == long.class) {
				sql += " bigint";
			} else {
				TableDao.conn.rollback();
				throw new IllegalArgumentException("Invalid data type for a primary key");
			}
			sql += " primary key;";
			myStatment = TableDao.conn.prepareStatement(sql);
			//			System.out.println(sql);
			myStatment.execute();
			myStatment.close();
			// System.out.println(myStatment.toString());
		} else {
			String alterSqlType = sql + " alter" + " column \"" + fieldName + "\"" + " type "
					+ TableDao.getSQLType(PField) + " using \"" + fieldName + "\"::" + TableDao.getSQLType(PField);
			String alterSqlNull = sql + " alter column \"" + fieldName + "\"";
			String alterSqlUnique;
			String SQLDropSerial = "";
			alterSqlNull += " set not null";
			alterSqlUnique = sql;
			alterSqlUnique += " add constraint \"" + fieldName + "_unique\" unique (\"" + fieldName + "\");";

			if (PField.isSerial()) {
				String CreateSequence = "CREATE SEQUENCE if not exists \"" + TableDao.schema + "\".seq_" + fieldName
						+ " owned by \"" + TableDao.schema + "\".\"" + name + "\".\"" + fieldName + "\"";
				String SelectSequence = "SELECT setval('\"" + TableDao.schema + "\".seq_" + fieldName
						+ "', coalesce(max(\"" + fieldName + "\"), 0) + 1, false) from " + "\"" + TableDao.schema
						+ "\".\"" + name + "\";";
				String AlterSequence = "ALTER TABLE " + "\"" + TableDao.schema + "\".\"" + name + "\" alter column \""
						+ fieldName + "\" set default nextval('" + TableDao.schema + ".seq_" + fieldName + "');";
				myStatment = TableDao.conn.prepareStatement(CreateSequence);
				// System.out.println(myStatment.toString());
				myStatment.execute();
				myStatment.close();
				myStatment = TableDao.conn.prepareStatement(SelectSequence);
				// System.out.println(myStatment.toString());
				myStatment.execute();
				myStatment.close();
				myStatment = TableDao.conn.prepareStatement(AlterSequence);
				// System.out.println(myStatment.toString());
				myStatment.execute();
				myStatment.close();
			} else {
				SQLDropSerial = "ALTER TABLE \"" + TableDao.schema + "\".\"" + name + "\"" + " alter column \""
						+ fieldName + "\" drop default;";
			}
			myStatment = TableDao.conn.prepareStatement(alterSqlType);
			myStatment.execute();
			myStatment.close();
			myStatment = TableDao.conn.prepareStatement(alterSqlNull);
			myStatment.execute();
			myStatment.close();
			Savepoint TrySqlUnique = TableDao.conn.setSavepoint("TrySqlUnique");
			try {
				myStatment = TableDao.conn.prepareStatement(alterSqlUnique);
				myStatment.execute();
				myStatment.close();
			} catch (SQLException e) {
				TableDao.conn.rollback(TrySqlUnique);
			} finally {
				TableDao.conn.releaseSavepoint(TrySqlUnique);
			}
			myStatment = TableDao.conn.prepareStatement(SQLDropSerial);
			myStatment.execute();
			myStatment.close();
			int x = 0;
		}





		List<String> columnsToAdd = new ArrayList<>(newColumns);
		columnsToAdd.removeAll(currentColumns);

		// System.out.println("Columns to add" + columnsToAdd);

		// System.out.println("-----------------");
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			fieldName = TableDao.getSQLName(myField);
			sql = "ALTER TABLE \"" + TableDao.schema + "\".\"" + name + "\"";
			if (columnsToAdd.contains(fieldName)) {
				sql += " add";
				sql += " \"" + TableDao.getSQLName(myField) + "\" ";
				if (!myField.isSerial()) {
					sql += " " + TableDao.getSQLType(myField) + " ";
				}
				sql += TableDao.getSQLModification(myField);
				// System.out.println(sql);
				myStatment = TableDao.conn.prepareStatement(sql);
			} else {
				String alterSqlType = sql + " alter" + " column \"" + fieldName + "\"" + " type "
						+ TableDao.getSQLType(myField) + " using \"" + fieldName + "\"::"
						+ TableDao.getSQLType(myField);
				// " type " + TableDao.getSQLType(myField) + " using \"" + fieldName + "\"::" +
				// TableDao.getSQLType(myField);
				String alterSqlNull = sql + " alter column \"" + fieldName + "\"";
				String alterSqlUnique = sql;
				String alterSqlDefault = sql + " alter column \"" + fieldName + "\"";
				String SQLDropSerial = "";
				if (myField.isNullable()) {
					alterSqlNull += " drop not null";
				} else {
					alterSqlNull += " set not null";
				}
				if (myField.isUnique()) {

					/*
					 * alterSqlUnique+=" Drop CONSTRAINT if exists \""+fieldName+"_unique\";";
					 * myStatment= conn.prepareStatement(alterSqlUnique);
					 * System.out.println(myStatment.toString()); boolean constraintExisted =
					 * myStatment.execute(); System.out.println("Existed: "+constraintExisted);
					 */

					alterSqlUnique = sql;
					alterSqlUnique += " add constraint \"" + fieldName + "_unique\" unique (\"" + fieldName + "\");";
				} else {
					alterSqlUnique += " drop CONSTRAINT if exists \"" + fieldName + "_unique\";";
				}
				if (myField.isSerial()) {
					String CreateSequence = "CREATE SEQUENCE if not exists " + TableDao.schema + ".seq_" + fieldName
							+ " owned by " + TableDao.schema + "." + name + ".\"" + fieldName + "\"";
					String SelectSequence = "SELECT setval('" + TableDao.schema + ".seq_" + fieldName
							+ "', coalesce(max(\"" + fieldName + "\"), 0) + 1, false) from " + "" + TableDao.schema
							+ "." + name + ";";
					String AlterSequence = "ALTER TABLE " + "" + TableDao.schema + "." + name + " alter column \""
							+ fieldName + "\" set default nextval('" + TableDao.schema + ".seq_" + fieldName + "');";
					myStatment = TableDao.conn.prepareStatement(CreateSequence);
					// System.out.println(myStatment.toString());
					myStatment.execute();
					myStatment.close();
					myStatment = TableDao.conn.prepareStatement(SelectSequence);
					// System.out.println(myStatment.toString());
					myStatment.execute();
					myStatment.close();
					myStatment = TableDao.conn.prepareStatement(AlterSequence);
					// System.out.println(myStatment.toString());
					myStatment.execute();
					myStatment.close();
				} else {
					SQLDropSerial = "ALTER TABLE \"" + TableDao.schema + "\".\"" + name + "\"" + " alter column \""
							+ fieldName + "\" drop default;";
				}
				if (myField.getDefaultValue().equals("")) {
					alterSqlDefault += " drop default";
				} else {
					alterSqlDefault += " set default \'" + myField.getDefaultValue() + "\'";
				}

				// System.out.println(alterSqlNull);
				// System.out.println(alterSqlUnique);
				// System.out.println(alterSqlDefault);
				// System.out.println(SQLDropSerial);

				myStatment = TableDao.conn.prepareStatement(alterSqlType);
				myStatment.execute();
				myStatment.close();
				myStatment = TableDao.conn.prepareStatement(alterSqlNull);
				myStatment.execute();
				myStatment.close();
				Savepoint TrySqlUnique = TableDao.conn.setSavepoint("TrySqlUnique");
				try {
					myStatment = TableDao.conn.prepareStatement(alterSqlUnique);
					myStatment.execute();
					myStatment.close();
				} catch (SQLException e) {
					TableDao.conn.rollback(TrySqlUnique);
				} finally {
					TableDao.conn.releaseSavepoint(TrySqlUnique);
				}
				myStatment = TableDao.conn.prepareStatement(SQLDropSerial);
				myStatment.execute();
				myStatment.close();
				myStatment = TableDao.conn.prepareStatement(alterSqlDefault);
			}
			// myStatment.execute();
			myStatment.execute();
			myStatment.close();
		}
		TableDao.conn.commit();
		TableDao.conn.setAutoCommit(true);
		addForeignKeys(allFieldsInTable);

	}

	/**
	 * This truncates a table
	 *
	 * <pre>
	 *
	 *
	 *
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void truncate(MetaModel<?> table) throws SQLException {
		String name = table.getTableName();
		if (name.equals("")) {
			name = table.getSimpleClassName();
		}
		String sql = "truncate table \"" + TableDao.schema + "\".\"" + name + "\" cascade";
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		conn.setAutoCommit(false);
		Savepoint tryTruncating = conn.setSavepoint("tryTruncating");
		//		System.out.println(sql);
		try {
			myStatment.execute();
		} catch (SQLException e) {
			conn.rollback(tryTruncating);
		} finally {
			conn.releaseSavepoint(tryTruncating);
			myStatment.close();
			conn.setAutoCommit(true);
		}
	}

	/**
	 * This drops a table
	 *
	 * <pre>
	 *
	 *
	 *
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void drop(MetaModel<?> table) throws SQLException {
		String name = table.getTableName();
		if (name.equals("")) {
			name = table.getSimpleClassName();
		}
		try {
			for (ForeignKeyField fk : table.getForeignKeys()) {
				if (fk.getRelation() == Relation.ManyToMany) {
					String dropJoinTable = "drop table if exists \"" + TableDao.schema + "\".\"" + name + "_"
							+ fk.getMappedByTable() + "\" cascade";
					String dropOtherJoinTable = "drop table if exists \"" + TableDao.schema + "\".\""
							+ fk.getMappedByTable() + "_" + name + "\" cascade";
					PreparedStatement dropStatment = TableDao.conn.prepareStatement(dropJoinTable);
					PreparedStatement dropOtherStatment = TableDao.conn.prepareStatement(dropOtherJoinTable);
					dropStatment.execute();
					dropOtherStatment.execute();
					dropStatment.close();
					dropOtherStatment.close();
				}
			}
		} catch (Exception e) {
		}

		String sql = "drop table if exists \"" + TableDao.schema + "\".\"" + name + "\" cascade";
		//		System.out.println(sql);
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		myStatment.execute();
		myStatment.close();
	}

	/**
	 * This renames a table that was named using oldString to the the new name
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void renameTable(MetaModel<?> table, String oldName) throws SQLException {
		String name = table.getTableName();
		if (name.equals("")) {
			name = table.getSimpleClassName();
		}
		String sql = "Alter table \"" + TableDao.schema + "\".\"" + oldName + "\" rename to \"" + name + "\";";
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		myStatment.execute();
		myStatment.close();
	}

	/**
	 * This renames a Column that was named using oldString to the the newName
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void renameColumn(MetaModel<?> table, String oldName, String newName) throws SQLException {
		boolean exists = false;
		String name = table.getTableName();
		if (name.equals("")) {
			name = table.getSimpleClassName();
		}
		for (GenericField gf : table.getAllFields()) {
			if (gf.getColumnName().equals(newName) || gf.getName().equals(newName)) {
				exists = true;
			}
		}
		if (!exists) {
			throw new RuntimeException("The new name must be already in the table.");
		}
		String sql = "Alter table \"" + TableDao.schema + "\".\"" + name + "\" rename column \"" + TableDao.schema
				+ "\".\"" + oldName + "\" to \"" + newName + "\";";
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		myStatment.execute();
		myStatment.close();

	}

	/**
	 * This adds Foreign keys to the table
	 *
	 * <pre>
	 * </pre>
	 *
	 * @author Caleb Kirschbaum
	 */
	public void addForeignKeys(MetaModel<?> table) throws SQLException, ClassNotFoundException {
		String name = table.getTableName();
		if (name.equals("")) {
			name = table.getSimpleClassName();
		}
		List<ForeignKeyField> fks;
		try {
			fks = table.getForeignKeys();
		} catch (RuntimeException e) {
			return;
		}

		List<String> keys = new ArrayList<>();

		// List<String> currentColumns = new ArrayList<>();

		String sql = "SELECT column_name FROM information_schema.columns WHERE table_schema = '" + TableDao.schema
				+ "' AND table_name  = '" + name + "';";
		PreparedStatement myStatment = TableDao.conn.prepareStatement(sql);
		ResultSet rs = myStatment.executeQuery();
		while (rs.next()) {
			String columnName = rs.getString("column_name");
			keys.add(columnName);
		}
		myStatment.close();
		// System.out.println(keys.toString());
		for (ForeignKeyField key : fks) {
			String keyName = key.getColumnName();
			if (keyName.equals("")) {
				keyName = key.getName();
			}
			if (keys.contains(keyName)) {
				// System.out.println("Skipping: " + keyName);
				continue;
			} else {
				keys.add(keyName);
				// System.out.println("Adding " + keyName);
				// System.out.println("New list: " + keys.toString());
			}
			if (key.getRelation() == Relation.ManyToMany) {
				// Create a join table with values keyName and getMappedByColumn

				// Create a join table with values keyName and getMappedByColumn

				// create table if not exists project_zero.user_accounts_jt (
				String createJoinTable = "create table \"" + TableDao.schema + "\".\"" + name + "_"
						+ key.getMappedByTable() + "\"( ";
				String otherJoinTable = "create table \"" + TableDao.schema + "\".\"" + key.getMappedByTable() + "_"
						+ name + "\"( ";
				// account_owner integer not null references project_zero.users(id) on delete
				// cascade,
				Class<?> instance;
				try {
					// System.out.println("com.revature." + key.getMappedByTable());
					instance = Class.forName("com.revature." + key.getMappedByTable());
				} catch (ClassNotFoundException e) {
					throw new ClassNotFoundException("The table defined in the foreign key does not exist");
				}
				PrimaryKeyField otherKey = MetaModel.of(instance).getPrimaryKey();
				PrimaryKeyField thisPKey = table.getPrimaryKey();
				String thisPKeyName = thisPKey.getColumnName();
				if (thisPKeyName.equals("")) {
					thisPKeyName = thisPKey.getName();
				}
				String otherPKeyName = otherKey.getColumnName();
				if (otherPKeyName.equals("")) {
					otherPKeyName = otherKey.getName();
				}
				createJoinTable += "\"" + keyName + "\" " + TableDao.getSQLType(otherKey) + " not null references \""
						+ TableDao.schema + "\".\"" + name + "\"(\"" + thisPKeyName + "\") on delete cascade,";
				otherJoinTable += "\"" + keyName + "\" " + TableDao.getSQLType(otherKey) + " not null references \""
						+ TableDao.schema + "\".\"" + name + "\"(\"" + thisPKeyName + "\") on delete cascade,";

				// account integer not null references project_zero.accounts(id) on delete
				// cascade);
				createJoinTable += "\"" + key.getMappedByColumn() + "\" " + TableDao.getSQLType(table.getPrimaryKey())
				+ " not null references \"" + TableDao.schema + "\".\"" + key.getMappedByTable() + "\"(\""
				+ otherPKeyName + "\") on delete cascade);";

				otherJoinTable += "\"" + key.getMappedByColumn() + "\" " + TableDao.getSQLType(table.getPrimaryKey())
				+ " not null references \"" + TableDao.schema + "\".\"" + key.getMappedByTable() + "\"(\""
				+ otherPKeyName + "\") on delete cascade);";
				TableDao.conn.setAutoCommit(false);
				Savepoint joinSave = TableDao.conn.setSavepoint("JoinSave");

				try {
					// System.out.println(createJoinTable);
					PreparedStatement joinStatment = TableDao.conn.prepareStatement(createJoinTable);
					// System.out.println(joinStatment.toString());
					joinStatment.execute();
					// System.out.println(otherJoinTable);
					PreparedStatement OtherjoinStatment = TableDao.conn.prepareStatement(otherJoinTable);
					// System.out.println(OtherjoinStatment.toString());
					OtherjoinStatment.execute();

					TableDao.conn.rollback(joinSave);
					joinStatment = TableDao.conn.prepareStatement(createJoinTable);
					// System.out.println(joinStatment.toString());
					joinStatment.execute();
					joinStatment.close();
					OtherjoinStatment.close();
				} catch (SQLException e) {
					// System.out.println("Undoing the tables");
					// System.out.println(e.getMessage());
					TableDao.conn.rollback(joinSave);
				} finally {

					TableDao.conn.releaseSavepoint(joinSave);
					TableDao.conn.commit();
					TableDao.conn.setAutoCommit(true);
				}

				keys.add(key.getMappedByColumn());
			} else if (key.getRelation() == Relation.ManyToOne) {

				//				System.out.println("\n\n\n\n\n\n");

				Class<?> instance;
				try {
					// System.out.println("com.revature." + key.getMappedByTable());
					instance = Class.forName("com.revature." + key.getMappedByTable());
				} catch (ClassNotFoundException e) {
					throw new ClassNotFoundException("The table defined in the foreign key does not exist");
				}
				insert(MetaModel.of(instance));
				PrimaryKeyField otherKey = MetaModel.of(instance).getPrimaryKey();
				String otherPKeyName = otherKey.getColumnName();
				if (otherPKeyName.equals("")) {
					otherPKeyName = otherKey.getName();
				}

				String ForeignAddSQL = "alter table \"" + TableDao.schema + "\".\"" + name + "\" add column \""
						+ keyName + "\" " + getSQLType(otherKey) + " not null";
				String ForeignSQL = "alter table \"" + TableDao.schema + "\".\"" + name + "\" add constraint \"fk_"
						+ keyName + "\" foreign key (\"" + keyName + "\") references \"" + TableDao.schema + "\".\""
						+ key.getMappedByTable() + "\" (\"" + key.getMappedByColumn() + "\")";
				PreparedStatement foreignAddStatment = TableDao.conn.prepareStatement(ForeignAddSQL);
				PreparedStatement foreignStatment = TableDao.conn.prepareStatement(ForeignSQL);
				//				System.out.println(ForeignAddSQL);
				//				System.out.println(ForeignSQL);
				foreignAddStatment.execute();
				foreignStatment.execute();
				foreignAddStatment.close();
				foreignStatment.close();
			}

		}

	}

}

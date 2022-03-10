package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.person;
import com.revature.util.ColumnField;
//import com.revature.inspection.ClassInspector;
import com.revature.util.ConnectionUtil;
import com.revature.util.MetaModel;
import com.revature.util.PrimaryKeyField;


public class TableDao {
	private static Logger logBot=Logger.getLogger(TableDao.class);
	private static String managment= ConnectionUtil.getManagement();
	private static String schema= ConnectionUtil.getSchema();
	private static int String =0;
	private static Connection conn = ConnectionUtil.getConnection();

	public static String getSQLName(ColumnField myField) {
		String fieldName=myField.getColumnName();
		if(myField.getColumnName().equals("")) {
			
			fieldName=myField.getName();
		}
		return fieldName;
	}
	
	public static String getSQLType(ColumnField myField) {
		String modifications="";
		
		if(myField.getType()==int.class) {
			modifications+=" int";
		}//Test against Integer
		else if(myField.getType()==byte.class||myField.getType()==short.class) {				
			modifications+=" smallint";
		}
		else if(myField.getType()==long.class) {				
			modifications+=" bigint";
		}
		else if(myField.getType()==float.class || myField.getType()==double.class) {				
			modifications+=" numeric("+myField.getPercision()+", "+myField.getScale()+")";
		}
		else if(myField.getType()==boolean.class) {
			modifications+=" boolean";
		}
		else if(myField.getType()==char.class) {
			modifications+=" char(1)";
		}
		else if(myField.getType()==String.class ) {
			modifications+=" varchar("+myField.getLength()+")";
		}
		else if(myField.getType()==java.util.Date.class || myField.getType()==java.sql.Date.class ||myField.getType()==java.time.LocalDate.class) {
			modifications+=" date";
		}	
		return modifications;
	}
	
	public static String getSQLModification(ColumnField myField) {
		String modifications="";			
		if(myField.isSerial() ) {
			modifications+=" serial";
		}
		if(!myField.isNullable()) {
			modifications+=" not null";
		}
		if(myField.isUnique()) {
			modifications+=" unique";
		}		
		if(!myField.getDefaultValue().equals("")) {
			modifications+=" default "+myField.getDefaultValue();
		}
		/*if(myField.getCheckValue()!="" ) {
			modifications+=" check "+myField.getCheckValue();
		}*/
		
		return modifications;
		
	}
	
	
	public static void insert(String name, MetaModel<?> allFieldsInTable) throws IllegalAccessException, SQLException{
		
		
			
		String sql ="";
		/*	validate: validate the schema, makes no changes to the database.
			update: update the schema.
			create: creates the schema, destroying previous data.
			
		 * */
		if(managment.toLowerCase().equals("validate")) {
			throw new IllegalAccessException("Can't insert tables on validate control. Change control to insert.");
		}
		else if(managment.toLowerCase().equals("create")) {
			sql += "create table if not exists";
		}
		else if(managment.toLowerCase().equals("update")) {
			update(name, allFieldsInTable);
			return;
		}
		else {
			throw new IllegalArgumentException("Not a valid managment style. Change to validate, create, or update");
		}
		name="\""+name+"\"";
		sql += " "+schema+"."+name+"(";
		

		
		PrimaryKeyField PField = allFieldsInTable.getPrimaryKey();
		
		
		//for(PrimaryKeyField PFields : allFieldsInTable.getPrimaryKey()) {
			
		//}//Use for composite keys
		String PfieldName="\""+PField.getColumnName()+"\"";//Correct the naming convention
		if(PField.getColumnName().equals("")) {
			
			PfieldName=PField.getName();//Correct the naming convention
		}
		sql +=PfieldName;
		
		if(PField.isSerial()) {
			sql+=" serial";
		}
		else if(PField.getType()==int.class) {
			sql+=" int";
		}//Test against Integer
		else if(PField.getType()==byte.class||PField.getType()==short.class) {				
			sql+=" smallint";
		}
		else if(PField.getType()==long.class) {				
			sql+=" bigint";
		}
		else {
			throw new IllegalArgumentException("Invalid data type for a primary key");
		}
		
		
		sql+=" primary key,";		
		String fieldName;
		String modifications="";
		for (ColumnField myField : allFieldsInTable.getColumns()) {	
			modifications+=" \""+getSQLName(myField)+"\" ";
			if(!myField.isSerial()) {
				modifications+=" "+getSQLType(myField)+" ";
			}
			modifications+=getSQLModification(myField)+",";	
			
		}
		sql+=modifications;
		sql= sql.substring(0, sql.length() - 1);
		sql +=");";
		
			
		String createSchemaSql="CREATE SCHEMA IF NOT EXISTS "+schema;
		PreparedStatement createStatment= conn.prepareStatement(createSchemaSql);			
		createStatment.execute();
		
		if(managment.toLowerCase().equals("create")) {
			String dropsql = "drop table if exists "+schema+"."+name+" cascade";
			PreparedStatement dropStatment= conn.prepareStatement(dropsql);
			System.out.println("\n"+dropStatment.toString());//Uncomment if throwing errors to see what is being queried
			dropStatment.execute();
		}
		PreparedStatement myStatment= conn.prepareStatement(sql);			
		System.out.println("\n"+myStatment.toString());//Uncomment if throwing errors to see what is being queried
		myStatment.execute();
	}

	
	public static void update(String name, MetaModel<?> allFieldsInTable) throws IllegalAccessException, SQLException{
		//Delete all nonexisting 
		String sql="SELECT column_name FROM information_schema.columns WHERE table_schema = '"+schema+
				"' AND table_name  = '"+name+"';";
		System.out.println(sql);
		String fieldName="";
		
		
		
		PrimaryKeyField PField = allFieldsInTable.getPrimaryKey();
		fieldName=PField.getColumnName();
		if(PField.getColumnName().equals("")) {
			fieldName=PField.getName();
		}		
		List<String> newColumns=new ArrayList();
		PField = allFieldsInTable.getPrimaryKey();
		String PfieldName="\""+PField.getColumnName()+"\"";//Correct the naming convention
		if(PField.getColumnName().equals("")) {			
			PfieldName=PField.getName();//Correct the naming convention
		}		
		
		newColumns.add(PfieldName);
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			//System.out.println(myField.getName());
			newColumns.add(getSQLName(myField));
		}
				
		List<String> currentColumns= new ArrayList();
					
		PreparedStatement myStatment= conn.prepareStatement(sql);	
		ResultSet rs= myStatment.executeQuery();
		while(rs.next()) {
			String columnName=rs.getString("column_name");
			currentColumns.add(columnName);
		}			
			
		System.out.println(currentColumns);
		System.out.println(newColumns);
		
		List<String> badColumns= new ArrayList(currentColumns);		
		badColumns.removeAll(newColumns);	
		System.out.println("Bad columns"+badColumns);
		
		
		
		fieldName=PfieldName;
		sql="ALTER TABLE \""+schema+"\".\""+name+"\"";
		if (!currentColumns.contains(fieldName)) {
			sql+=" add";
		}
		else {
			sql+=" alter";
		}
		sql += " column \""+fieldName+"\"";		
			
		if(PField.isSerial()) {
			sql+=" serial";
		}
		else if(PField.getType()==int.class) {
			sql+=" int";
		}
		else if(PField.getType()==byte.class||PField.getType()==short.class) {				
			sql+=" smallint";
		}
		else if(PField.getType()==long.class) {				
			sql+=" bigint";
		}
		else {
			throw new IllegalArgumentException("Invalid data type for a primary key");
		}		
		sql+=" primary key;";	
		myStatment= conn.prepareStatement(sql);
		System.out.println(myStatment.toString());
		//myStatment.execute();
		
		
		
		
		for(String columnName : badColumns) {
			sql="ALTER TABLE \""+schema+"\".\""+name+"\" DROP COLUMN if exists \""+columnName+"\" cascade;";
			myStatment= conn.prepareStatement(sql);
			myStatment.execute();			
		}
		
		List<String> columnsToAdd= new ArrayList(newColumns);
		columnsToAdd.removeAll(currentColumns);		
		
		
		System.out.println("Columns to add"+columnsToAdd);
		
		System.out.println("-----------------");
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			fieldName=getSQLName(myField);
			sql="ALTER TABLE \""+schema+"\".\""+name+"\"";
			if (columnsToAdd.contains(fieldName)) {
				sql+=" add";
				sql+=" \""+getSQLName(myField)+"\" ";
				if(!myField.isSerial()) {
					sql+=" "+getSQLType(myField)+" ";
				}
				sql+=getSQLModification(myField);	
				System.out.println(sql);
				myStatment= conn.prepareStatement(sql);
				myStatment.execute();
			}
			else {
				String alterSqlType=sql+" alter";
				alterSqlType += " column \""+fieldName+"\"";
				alterSqlType += " type "+getSQLType(myField)+" using \""+fieldName+"\"::"+getSQLType(myField);
				String alterSqlNull=sql+" alter column \""+fieldName+"\"";
				String alterSqlUnique=sql;
				String alterSqlDefault=sql+" alter column \""+fieldName+"\"";
				String alterSqlSerial=sql+" alter column \""+fieldName+"\"";
				
				if(myField.isNullable()) {
					alterSqlNull+=" drop not null";
				}
				else {
					alterSqlNull+=" set not null";
				}
				if(myField.isUnique()) {
					alterSqlUnique+=" Drop CONSTRAINT if exists \""+fieldName+"_unique\";";	 
					myStatment= conn.prepareStatement(alterSqlUnique);
					System.out.println(myStatment.toString());
					myStatment.execute();
					alterSqlUnique=sql;
					alterSqlUnique+=" ADD CONSTRAINT \""+fieldName+"_unique\" UNIQUE (\""+fieldName+"\");";					
				}
				else {
					alterSqlUnique+=" drop CONSTRAINT if exists \""+fieldName+"_unique\";";	 					
				}
				if(myField.isSerial() ) {					
					String CreateSequence="CREATE SEQUENCE if not exists "+schema+".seq_"+fieldName+" owned by "+schema+"."+name+".\""+fieldName+"\"";
					String SelectSequence="SELECT setval('"+schema+".seq_"+fieldName+"', coalesce(max(\""+fieldName+"\"), 0) + 1, false) from "+""+schema+"."+name+";";
					String AlterSequence ="ALTER TABLE "+""+schema+"."+name+" alter column \""+fieldName+"\" set default nextval('"+schema+".seq_"+fieldName+"');";
					
					myStatment= conn.prepareStatement(CreateSequence);
					System.out.println(myStatment.toString());
					myStatment.execute();
					myStatment= conn.prepareStatement(SelectSequence);
					System.out.println(myStatment.toString());
					myStatment.execute();
					myStatment= conn.prepareStatement(AlterSequence);
					System.out.println(myStatment.toString());
					myStatment.execute();
					
				}
				if(myField.getDefaultValue().equals("")) {
					alterSqlDefault+=" drop default";
				}
				else {
					alterSqlDefault+=" set default \'"+myField.getDefaultValue()+"\'";
				}
				System.out.println(alterSqlNull);
				System.out.println(alterSqlUnique);
				System.out.println(alterSqlDefault);
				
				myStatment= conn.prepareStatement(alterSqlNull);
				myStatment.execute();
				myStatment= conn.prepareStatement(alterSqlUnique);
				myStatment.execute();
				myStatment= conn.prepareStatement(alterSqlDefault);
				myStatment.execute();
				
			}
			
			
				
		}
		

		
		
		
		
		
		
		
	}
	
	
	public static void main(String[] args) throws IllegalAccessException, SQLException {
		
		MetaModel bob = MetaModel.of(person.class);
		insert(bob.getSimpleClassName(), bob);
		
		
		
	}
	

}





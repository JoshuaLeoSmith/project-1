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

	public static void insert(String name, MetaModel<?> allFieldsInTable) throws IllegalAccessException{
		
		
			
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
			//System.out.printf(" int type");
		}//Test against Integer
		else if(PField.getType()==byte.class||PField.getType()==short.class) {				
			sql+=" smallint";
			//System.out.printf(" smallint type");
		}
		else if(PField.getType()==long.class) {				
			sql+=" bigint";
			//System.out.printf(" bigint type");
		}
		/*else if(PField.getType()==float.class || PField.getType()==double.class) {				
			sql+=" numeric("+PField.getPercision()+", "+PField.getScale()+")";
			//System.out.printf(" float type");
		}*/ //Is also allowed, although weird
		else if(PField.getType()==boolean.class) {
			sql+=" boolean";
			//System.out.printf(" boolean type");
		}//I Don't Like this, but it is infact possible, so it's being implemented. 
		else if(PField.getType()==char.class) {
			sql+=" char(1)";
			//System.out.printf(" char type");
		}
		/*else if(PField.getType()==String.class ) {
			//System.out.printf(" text type");
			sql+=" varchar("+PField.getLength()+")";
		}*/ //Also allowed, but actually makes sense
		else {
			throw new IllegalArgumentException("Invalid data type for a primary key");
		}
		
		sql+=" primary key,";
		
				
				
				
		
		String fieldName;
		String modifications="";
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			//System.out.printf("\n"+i+" - "+myField.toString());
			//i++;
			fieldName="\""+myField.getColumnName()+"\"";
			if(myField.getColumnName().equals("")) {
				
				fieldName=myField.getName();
			}
						
			modifications=fieldName;
			
			if(myField.getType()==int.class) {
				modifications+=" int";
				//System.out.printf(" int type");
			}//Test against Integer
			else if(myField.getType()==byte.class||myField.getType()==short.class) {				
				modifications+=" smallint";
				//System.out.printf(" smallint type");
			}
			else if(myField.getType()==long.class) {				
				modifications+=" bigint";
				//System.out.printf(" bigint type");
			}
			else if(myField.getType()==float.class || myField.getType()==double.class) {				
				modifications+=" numeric("+myField.getPercision()+", "+myField.getScale()+")";
				//System.out.printf(" float type");
			}
			else if(myField.getType()==boolean.class) {
				modifications+=" boolean";
				//System.out.printf(" boolean type");
			}
			else if(myField.getType()==char.class) {
				modifications+=" char(1)";
				//System.out.printf(" char type");
			}
			else if(myField.getType()==String.class ) {
				//System.out.printf(" text type");
				modifications+=" varchar("+myField.getLength()+")";
			}
			else if(myField.getType()==java.util.Date.class || myField.getType()==java.sql.Date.class ||myField.getType()==java.time.LocalDate.class) {
				modifications+=" date";
				//System.out.printf(" date type");
			}						
			if(!myField.isNullable()) {
				modifications+=" not null";
			}
			if(myField.isUnique()) {
				modifications+=" unique";
			}
			if(myField.isSerial() ) {
				modifications+=" serial";
			}
			if(!myField.getDefaultValue().equals("")) {
				//System.out.println(myField.getDefaultValue());
				modifications+=" default "+myField.getDefaultValue();
			}
			/*if(myField.getCheckValue()!="" ) {
				modifications+=" check "+myField.getCheckValue();
			}*/
			
			modifications+=",";
			
			sql+=modifications;
		}
		sql= sql.substring(0, sql.length() - 1);
		sql +=");";
		
		try {
			if(managment.toLowerCase().equals("create")) {
				String dropsql = "drop table if exists "+schema+"."+name+" cascade";
				PreparedStatement dropStatment= conn.prepareStatement(dropsql);
				System.out.println("\n"+dropStatment.toString());//Uncomment if throwing errors to see what is being queried
				
				dropStatment.execute();
			}
			
			
			
			PreparedStatement myStatment= conn.prepareStatement(sql);
			
			System.out.println("\n"+myStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			//System.out.println("\n"+createStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			
			//createStatment.execute();
			myStatment.execute();
			
		} catch (SQLException e) {
			
			
			
			
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}
		
		
		
		
	}

	
	public static void update(String name, MetaModel<?> allFieldsInTable) throws IllegalAccessException{
		//Delete all nonexisting 
		String sql="SELECT column_name FROM information_schema.columns WHERE table_schema = '"+schema+
				"' AND table_name   = '"+name+"'";
		String fieldName="";
		//List<String> Columns=new ArrayList(); 
		
		PrimaryKeyField PField = allFieldsInTable.getPrimaryKey();
		fieldName=PField.getColumnName();
		//System.out.println(fieldName);
		if(PField.getColumnName().equals("")) {
			fieldName=PField.getName().toLowerCase();
		}
		//System.out.println(fieldName);
		sql+=" and column_name != '"+fieldName+"'";
		
		
		int i=0;
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			
			//System.out.printf("\n"+i+" - "+myField.toString());
			i++;
			fieldName=myField.getColumnName();//Correct the naming convention
			//System.out.println(fieldName);
			if(myField.getColumnName().equals("")) {
				fieldName=myField.getName().toLowerCase();//Correct the naming convention
			}
			//System.out.println(fieldName);
			sql+=" and column_name != '"+fieldName+"'";
			//Columns.add(fieldName);
		}
		sql+=";";
		//System.out.println(sql);
		List<String> badColumns= new ArrayList();
		try {
			
			PreparedStatement myStatment= conn.prepareStatement(sql);
			
			System.out.println("\n"+myStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			
			 ResultSet rs= myStatment.executeQuery();
			 
			 while(rs.next()) {
				 String columnName=rs.getString("column_name");
			 }
			
		} catch (SQLException e) {
			
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}
		
		System.out.println(badColumns);
		
		
		for(String columnName : badColumns) {
			sql="ALTER TABLE "+name+" DROP COLUMN "+columnName+";";
		}
		
		
		
		
		
	}
	
	
	public static void main(String[] args) throws IllegalAccessException {
		
		MetaModel bob = MetaModel.of(person.class);
		insert(bob.getClassName(), bob);
		
		
		
	}
	

}





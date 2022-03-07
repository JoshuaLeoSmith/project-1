package com.revature.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
//import ;
//import java.;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.person;
import com.revature.annotations.*;
import com.revature.util.ColumnField;
//import com.revature.inspection.ClassInspector;
import com.revature.util.ConnectionUtil;
import com.revature.util.MetaModel;


public class TableDao {
	private static Logger logBot=Logger.getLogger(TableDao.class);
	private static String managment= ConnectionUtil.getManagement();
	private static String schema= ConnectionUtil.getSchema();
	private static int String =0;
	private static Connection conn = ConnectionUtil.getConnection();

	public static void insert(String name, MetaModel<?> allFieldsInTable) throws IllegalAccessException{
		name="\""+name+"\"";
		
		if(managment.toLowerCase().equals("validate")) {
			throw new IllegalAccessException("Can't insert tables on validate control. Change control to insert.");
		}	
		String sql ="";
		/*	validate: validate the schema, makes no changes to the database.
			update: update the schema.
			create: creates the schema, destroying previous data.
			
		 * */
		if(managment.toLowerCase().equals("create")) {
			sql += "create table if not exists";
		}
		if(managment.toLowerCase().equals("update")) {
			sql += " if not exists";
		}
		sql += " "+schema+"."+name+"(";
		
		String fieldName;
		String modifications="";
		//System.out.println(allFieldsInTable.size());
		//System.out.println(sql);
		int i=0;
		for (ColumnField myField : allFieldsInTable.getColumns()) {
			//System.out.printf("\n"+i+" - "+myField.toString());
			i++;
			fieldName=myField.getName();//Correct the naming convention
			modifications=fieldName;
			
			if(myField.getType()==int.class || myField.getType()==byte.class||myField.getType()==short.class||myField.getType()==long.class) {
				modifications+=" int";
				//System.out.printf(" int type");
			}//Test against Integer
			else if(myField.getType()==float.class || myField.getType()==double.class) {
				modifications+=" numeric";
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
				modifications+=" text";
			}
			else if(myField.getType()==java.util.Date.class || myField.getType()==java.sql.Date.class ||myField.getType()==java.time.LocalDate.class) {
				modifications+=" date";
				//System.out.printf(" date type");
			}
			//else {
			//	continue;
			//}
			
			
			if(!myField.isNullable()) {
				modifications+=" not null";
			}
			if(myField.isUnique()) {
				modifications+=" unique";
			}
			/*if(myField. ) {
				modifications+=" default "+defaultValue;
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
			
			
			String createSchemaSql="CREATE SCHEMA IF NOT EXISTS "+schema;
			//String createSchemaSql="CREATE SCHEMA IF NOT EXISTS "+schema;
			PreparedStatement createStatment= conn.prepareStatement(createSchemaSql);
			
			
			PreparedStatement myStatment= conn.prepareStatement(sql);
			
			
			System.out.println("\n"+myStatment.toString());//Uncomment if throwing errors to see what is being queried
			//System.out.println("\n"+createStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			
			createStatment.execute();
			myStatment.execute();
			
		} catch (SQLException e) {
			
			
			
			
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}
		
		
		
		
	}

	
	public static void main(String[] args) throws IllegalAccessException {
		
		MetaModel bob = MetaModel.of(person.class);
		insert("Dead People", bob);
		
	}
	

}





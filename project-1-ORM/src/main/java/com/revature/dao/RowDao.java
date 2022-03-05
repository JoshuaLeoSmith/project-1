package com.revature.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import com.revature.annotations.Id;
import com.revature.util.ConnectionUtil;

public class RowDao {
	
	private static Logger logBot=Logger.getLogger(TableDao.class);
	private static String managment= ConnectionUtil.getManagement();
	private static String schema= ConnectionUtil.getSchema();
	private static int String =0;
	private static Connection conn = ConnectionUtil.getConnection();

	public void insert(String name, List<Field> allFieldsInTable) throws IllegalAccessException{
		
		if(managment.toLowerCase().equals("validate")) {
			throw new IllegalAccessException("Can't insert rows on validate control. Change control to insert.");
		}	
		String sql ="insert into "+schema+"."+name+"(";
		
		/*if(managment.toLowerCase().equals("update")) {
			sql += " if not exists";
		}*/
		sql += " ";
		
		String fieldName;
		String modifications="";
		for (Field myField : allFieldsInTable) {
			fieldName=myField.getName();//Correct the naming convention
			if(myField.getAnnotation(Id.class) != null) {
				modifications+=" serial primary key";
			}
			else if(myField.getType()==int.class || myField.getType()==byte.class||myField.getType()==short.class||myField.getType()==long.class) {
				modifications+=" int";
			}//Test against Integer
			else if(myField.getType()==float.class || myField.getType()==double.class) {
				modifications+=" numeric";
			}
			else if(myField.getType()==boolean.class) {
				modifications+=" boolean";
			}
			else if(myField.getType()==char.class) {
				modifications+=" char(1)";
			}
			else if(myField.getType()==String.class ) {
				modifications+=" text";
			}
			else if(myField.getType()==java.util.Date.class || myField.getType()==java.sql.Date.class ||myField.getType()==java.time.LocalDate.class) {
				modifications+=" date";
			}
			/*if(myField. ) {
				modifications+=" not null";
			}
			if(myField. ) {
				modifications+=" unique";
			}
			if(myField. ) {
				modifications+=" default "+defaultValue;
			}
			if(myField. ) {
				modifications+=" references "+refrenceValue +" on delete cascade";
			}
			*
			*
			*
			*/ //
			modifications+=",";
			
			sql+=modifications;
		}
		sql= sql.substring(0, sql.length() - 1);
		sql +=");";
		
		try {
			PreparedStatement myStatment= conn.prepareStatement(sql);
			
			
			System.out.println(myStatment.toString());//Uncomment if throwing errors to see what is being queried
			
			myStatment.execute();
			
		} catch (SQLException e) {
			logBot.error("Database Error, unable to run");
			e.printStackTrace();
		}
		
		
		
		
	}

	
	/*public static void main(String[] args) {
		int rounds=0;
		int name=0;
		List<Field> test=new ArrayList<>();
		test.add( rounds);
		
		insert("Weapons", test);
	}*/
	
	
	
}

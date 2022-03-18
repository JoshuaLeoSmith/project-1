package com.revature.dao;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import org.apache.log4j.Logger;

import com.revature.Relation;
import com.revature.UserAccounts;
import com.revature.annotations.Entity;
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;
import com.revature.util.Configuration;
import com.revature.util.ConnectionUtil;



public class DMLDao implements Dao {
	
	private static Logger logger = Logger.getLogger(DMLDao.class);

	public int insert(LinkedHashMap<String, Object> colNameToValue, String tableName, String pkName) {
		
		try{
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			
			
			String values = "(";
			String colNames = "(";
			
			
			for(String s : colNameToValue.keySet()) {
				
				Object value = colNameToValue.get(s);
				if(String.valueOf(value).equals("null") || String.valueOf(value).equals(" ")) {
					value = " ";
					
					
				}else if(colNameToValue.get(s).getClass()==Character.class) {
					if(String.valueOf(value).charAt(0) == '\0') {
						value = (char)32;
					}
				}
				colNames = colNames + "\"" + s + "\",";
				values = values + "'" + value + "',";
				
			}
			
			colNames = colNames.substring(0, colNames.length()-1) + ")";
			values = values.substring(0, values.length()-1) + ")";
			
			
			
			
			String sql = "INSERT INTO " + schema + "." + "\"" + tableName + "\" " + colNames + " VALUES " + values + " RETURNING " + "\"" + pkName + "\"";
			
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
		
			//System.out.println(sql);
			ResultSet rs;
			
			int id = -1;
			if((rs = stmt.executeQuery()) != null) {
				rs.next();
				
				id = rs.getInt(pkName);
			}
			

			return id;
		
		} catch(SQLException e) {
			e.printStackTrace();
			
			return -1;	
		} 
	}
	
	public int remove(String tableName, int id, String pkName) {
		
		
		try{
			String schema = "\""+ ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			String sql = "DELETE FROM " + schema + "." + tableName + " WHERE " + "\"" + pkName + "\"=" + id;
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			//stmt.setInt(1, id);
			
			stmt.execute();
			

			return id;
			
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return -1;
		}
	}
	
	
	public ArrayList<Integer> remove(String tableName, String where, String pkName) {
		
		
		try{
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			String sql = "DELETE FROM " + schema + "." + tableName + " WHERE " + where + " RETURNING " + "\"" + pkName + "\"";
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			
			ResultSet rs;
			
			ArrayList<Integer> removed = new ArrayList<Integer>();
			if((rs = stmt.executeQuery()) != null) {
				while(rs.next()) {
					
				
					removed.add(rs.getInt(pkName));
					
				}
			}
			

			return removed;
			
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<Object> find(Class<?> clazz, String where, String tableName, Relation r){
		
		ArrayList<Object> found = new ArrayList<Object>();
		try{
			
			
			
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			String sql = "SELECT * FROM " + schema + "." + tableName + " WHERE " + where;
			//System.out.println(sql);
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			ResultSet rs;
			
			if((rs = stmt.executeQuery()) != null) {
				while(rs.next()) {
					try {
						Constructor[] cons = clazz.getConstructors();
						int max = 0;
						int ind = 0;
						for(int i =0; i<cons.length; i++) {
							int paramNumbers = cons[i].getParameterCount();
							if(paramNumbers > max) {
								max = paramNumbers;
								ind = i;
							}
						}
						Constructor c = cons[ind];
						int parameterCount = c.getParameterCount();	
						Parameter[] parameters = c.getParameters();
						Object[] values = new Object[parameterCount];
						
						int count = 1;
						for(Parameter p : parameters) {
							String t = p.getParameterizedType().getTypeName();
							if(t.equals("int")) {
								values[count-1] = rs.getInt(count);
							}else if(t.equals("java.lang.String")) {
								values[count-1] = rs.getString(count);
							}else if(t.equals("double")) {
								values[count-1] = rs.getDouble(count);
							} else if(t.equals("byte")) {
								values[count-1] = rs.getByte(count);
							} else if(t.equals("short")) {
								values[count-1] = rs.getShort(count);
							}else if (t.equals("long")) {
								values[count-1] = rs.getLong(count);
							}else if (t.equals("boolean")) {
								values[count-1] = rs.getBoolean(count);
							}else if (t.equals("float")) {
								values[count-1] = rs.getFloat(count);
							}else if (t.equals("char")) {
								values[count-1] = rs.getString(count).charAt(0);
							} else if(t.equals("java.time.LocalDate")) {
								try{
									values[count-1] = rs.getDate(count).toLocalDate();
								}catch(NullPointerException e) {
									values[count-1] = null;
								}
								//dateToConvert.toInstant()
							   //   .atZone(ZoneId.systemDefault())
							   //   .toLocalDate();
							} else {
								values[count-1] = rs.getObject(count);
							} 
							
							count++;
						}
						
						Object ro = c.newInstance(values);
			
						found.add(ro);
				} catch (SecurityException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
					logger.error("Exception thrown...");
					e.printStackTrace();
					return found;
					}
				}
			}
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return found;
		}
		
		return found;
	}
	
	
	public Object findByPk(Class<?> clazz, int id, String tableName, String pkName, Relation r, String mappedByTable, String mappedByColumn) {
		
		try{
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			
			String sql = "SELECT * FROM " + schema + "." + tableName + " WHERE " + "\"" + pkName + "\" = " + id;
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			ResultSet rs;
			
			if((rs = stmt.executeQuery()) != null) {
				rs.next();
				try {
					Constructor[] cons = clazz.getConstructors();
					int max = 0;
					int ind = 0;
					for(int i =0; i<cons.length; i++) {
						int paramNumbers = cons[i].getParameterCount();
						if(paramNumbers > max) {
							max = paramNumbers;
							ind = i;
						}
					}
					Constructor c = cons[ind];
					int parameterCount = c.getParameterCount();	
					Parameter[] parameters = c.getParameters();
					Object[] values = new Object[parameterCount];
					int count = 1;
					for(Parameter p : parameters) {
						
						String t = p.getParameterizedType().getTypeName();
						
						
						if(r==Relation.OneToMany && t.contains("java.util.ArrayList")) {
							
							count++;
							continue;
							
							
						}
						if(t.equals("int")) {
							values[count-1] = rs.getInt(count);
						}else if(t.equals("class java.lang.String")) {
							values[count-1] = rs.getString(count);
						}else if(t.equals("double")) {
							values[count-1] = rs.getDouble(count);
						} else if(t.equals("byte")) {
							values[count-1] = rs.getByte(count);
						} else if(t.equals("short")) {
							values[count-1] = rs.getShort(count);
						}else if (t.equals("long")) {
							values[count-1] = rs.getLong(count);
						}else if (t.equals("boolean")) {
							values[count-1] = rs.getBoolean(count);
						}else if (t.equals("float")) {
							values[count-1] = rs.getFloat(count);
						}
						else if (t.equals("char")) {
							values[count-1] = rs.getString(count).charAt(0);
						}else if(t.equals("java.time.LocalDate")) {
							try{
								values[count-1] = rs.getDate(count).toLocalDate();
							}catch(NullPointerException e) {
								values[count-1] = null;
							}
						}else{
							values[count-1] = rs.getObject(count);
						}
						
						count++;
					}
					
					DMLDao tmp = new DMLDao();
					
					if(r ==Relation.OneToMany) {
						Class<?>act = null; 
						try {
							    act = Class.forName("com.revature." +mappedByTable);
							    
							 } catch (ClassNotFoundException e) {
							        e.printStackTrace();
							}
						ArrayList<Object> pleaseWork =tmp.find(act, "\"" + mappedByColumn + "\"=" + id, mappedByTable, Relation.OneToOne);
						values[count-2] = pleaseWork;
					}
					
					Object ro = c.newInstance(values);
					return ro;
				} catch (SecurityException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
					logger.error("Exception thrown...");
					e.printStackTrace();
					return null;
				}
			}
			
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	
	public ArrayList<Object> findBySimilarAttributes(Class<?> clazz, LinkedHashMap<String,Object> colNameToValue, String tableName, String pkName){
		ArrayList<Object> found = new ArrayList<Object>();
		try{
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			String setVals = "";
			
			for(String s : colNameToValue.keySet()) {
				
				setVals = setVals + "\"" + s + "\"='" + colNameToValue.get(s) + "' AND ";
			}
			
			try{
				setVals = setVals.substring(0, setVals.length()-5);
			} catch(StringIndexOutOfBoundsException e) {
				System.out.println("No results returned");
				return null;
			}
			
			String sql = "SELECT * FROM " + schema + "." + tableName + " WHERE " + setVals;
			//System.out.println(sql);
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
			ResultSet rs;
			
			if((rs = stmt.executeQuery()) != null) {
				while(rs.next()) {
					try {
						Constructor[] cons = clazz.getConstructors();
						int max = 0;
						int ind = 0;
						for(int i =0; i<cons.length; i++) {
							int paramNumbers = cons[i].getParameterCount();
							if(paramNumbers > max) {
								max = paramNumbers;
								ind = i;
							}
						}
						Constructor c = cons[ind];
						int parameterCount = c.getParameterCount();	
						Parameter[] parameters = c.getParameters();
						Object[] values = new Object[parameterCount];
						
						int count = 1;
						for(Parameter p : parameters) {
							String t = p.getParameterizedType().getTypeName();
							if(t.equals("int")) {
								values[count-1] = rs.getInt(count);
							}else if(t.equals("java.lang.String")) {
								values[count-1] = rs.getString(count);
							}else if(t.equals("double")) {
								values[count-1] = rs.getDouble(count);
							} else if(t.equals("byte")) {
								values[count-1] = rs.getByte(count);
							} else if(t.equals("short")) {
								values[count-1] = rs.getShort(count);
							}else if (t.equals("long")) {
								values[count-1] = rs.getLong(count);
							}else if (t.equals("boolean")) {
								values[count-1] = rs.getBoolean(count);
							}else if (t.equals("float")) {
								values[count-1] = rs.getFloat(count);
							}else if (t.equals("char")) {
								values[count-1] = rs.getString(count).charAt(0);
							} else if(t.equals("java.time.LocalDate")) {
								try{
									values[count-1] = rs.getDate(count).toLocalDate();
								}catch(NullPointerException e) {
									values[count-1] = null;
								}
								//dateToConvert.toInstant()
							   //   .atZone(ZoneId.systemDefault())
							   //   .toLocalDate();
							} else {
								values[count-1] = rs.getObject(count);
							} 
							
							count++;
						}
						
						Object ro = c.newInstance(values);
			
						found.add(ro);
				} catch (SecurityException | IllegalArgumentException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
					logger.error("Exception thrown...");
					e.printStackTrace();
					return null;
					}
				}
			}	
		} catch(SQLException e) {
			logger.error("SQLException thrown... cannot access the database...");
			e.printStackTrace();
			return null;
		}
		return found;
		
		
	}
	
	public int updateRow(LinkedHashMap<String, Object> colNameToValue, String tableName, String pkName, int id) {
		try{
			
			String schema = "\"" + ConnectionUtil.getSchema() + "\"";
			tableName = "\"" + tableName + "\"";
			pkName =  "\"" + pkName + "\"";
	
			String setVals = "";
			for(String s : colNameToValue.keySet()) {
				Object val = colNameToValue.get(s);
				if (String.valueOf(val).equals("null")) {
					val = " "; 
				} else if(val.getClass()==Character.class) {
					if((char)val == '\0') {
						val = (char)32;
					}
		
					
					
				}
				setVals = setVals + "\"" + s + "\"='" + val + "',";
				
			}
			
			setVals = setVals.substring(0, setVals.length()-1);
			
			
			String sql = "UPDATE " + schema + "." + tableName + " SET " + setVals + " WHERE " + pkName + "=" + id;
			
			//System.out.println();
			//System.out.println(sql);
			
			PreparedStatement stmt = this.conn.prepareStatement(sql);
			
		
			stmt.execute();
			
			

			return id;
		
		} catch(SQLException e) {
			e.printStackTrace();
			
			return -1;	
		}
	}
	
	
	
	
}

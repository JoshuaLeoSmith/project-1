package com.revature.service;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.revature.Relation;
import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToMany;
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;
import com.revature.dao.DMLDao;
import com.revature.dao.TableDao;
import com.revature.dao.TransactionDao;
import com.revature.util.MetaModel;



public class ServicesImpl implements IServices {

	private static final DMLDao td = new DMLDao();
	private static final TableDao tableDao = new TableDao();
	private static final TransactionDao transDao = new TransactionDao();

	@Override
	public int create(Class<?> clazz) {
		try {
			TableDao td = new TableDao();
			td.insert(MetaModel.of(clazz));
			return 1;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return -1;
		}
	}


	@Override
	public int insert(Object o) {


		Field[] fields = o.getClass().getDeclaredFields();
		LinkedHashMap<String, Object> colNameToValue = new LinkedHashMap<>();

		MetaModel m = MetaModel.of(o.getClass());


		for(Field f : fields) {
			f.setAccessible(true);
			try {
				
				if(f.getAnnotation(Exclude.class) != null || f.getAnnotation(JoinColumn.class)!= null) {
					if(f.getAnnotation(ManyToOne.class) != null) {
						
					}else {
						continue;
					}
					
					
				}
				
				
				if(f.getType()== LocalDate.class && f.get(o) == null){
					f.set(o, LocalDate.of(1900, 1, 1));
				}
				
				if (f.getAnnotation(Column.class) != null) {
					String keyVal = f.getAnnotation(Column.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), f.get(o));
					}else {
						colNameToValue.put(keyVal, f.get(o));
					}
				} else if (f.getAnnotation(Id.class) != null) {
					if((int)f.get(o) == 0) {
						continue;
					}
					String keyVal = f.getAnnotation(Id.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), f.get(o));
					}else {
						colNameToValue.put(keyVal, f.get(o));
					}
				} else {
					String keyVal = f.getName();
					colNameToValue.put(keyVal, f.get(o));
				}
				
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
		
		String tableName = o.getClass().getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = o.getClass().getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		
		String pkName = m.getPrimaryKey().getColumnName();
		
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}


		return td.insert(colNameToValue, tableName, pkName);
	}

	@Override
	public int removeByPk(Class <?> clazz, int id) {

		MetaModel m = MetaModel.of(clazz);

		String tableName = clazz.getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = clazz.getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		
		String pkName = m.getPrimaryKey().getColumnName();
		
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}

		return td.remove(tableName, id, pkName);
	}

	@Override
	public ArrayList<Integer> remove(Class<?> clazz, String where) {

		MetaModel m = MetaModel.of(clazz);

		String tableName = clazz.getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = clazz.getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		String pkName = m.getPrimaryKey().getColumnName();
		
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}

		return td.remove(tableName, where, pkName);
	}

	@Override
	public ArrayList<Object> find(Class<?> clazz, String where){
	
		
		String tableName = clazz.getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = clazz.getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		return td.find(clazz, where, tableName, Relation.ManyToMany);
	}
	
	@Override
	public Object findByPk(Class<?> clazz, int id) {
		
		Relation r = Relation.OneToOne;
		String mappedByTable = "null";
		String mappedByColumn = "null";
		for(Field f : clazz.getDeclaredFields()) {
			if(f.getAnnotation(JoinColumn.class) != null) {
				mappedByTable = f.getAnnotation(JoinColumn.class).mappedByTable();
				mappedByColumn = f.getAnnotation(JoinColumn.class).mappedByColumn();
				if (f.getAnnotation(ManyToOne.class)!= null){
				
				r = Relation.ManyToOne;
				
				} else if (f.getAnnotation(OneToMany.class) != null) {
					r=Relation.OneToMany;
				} else if (f.getAnnotation(ManyToMany.class)!= null) {
					r=Relation.ManyToMany;
				} 
			}
		}

		
		
		MetaModel m = MetaModel.of(clazz);
		
		String tableName = clazz.getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = clazz.getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }	
		}
		
		String pkName = m.getPrimaryKey().getColumnName();
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}
		

		return td.findByPk(clazz, id, tableName, pkName, r, mappedByTable, mappedByColumn);
	}

	@Override
	public ArrayList<Object> findBySimilarAttributes(Object o){
		MetaModel m = MetaModel.of(o.getClass());
		LinkedHashMap<String, Object> colNameToValue = new LinkedHashMap<String, Object>();
		int id = -1;
		for(Field f: o.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			try {
			
				
				if(f.getAnnotation(Exclude.class) != null || f.getAnnotation(JoinColumn.class) != null) {
					continue;
				}
				Class fieldType = null;
				try {
					fieldType = f.get(o).getClass();
				} catch(NullPointerException e) {
					continue;
				}
				Object fieldVal = f.get(o);

				
				if(f.get(o) == null) {
					continue;
				} else if(fieldType.equals(Integer.class) && ((int)fieldVal) == 0){
					continue;
				}else if(fieldType.equals(Short.class) && ((short)fieldVal) == 0){
					continue;
				}else if(fieldType.equals(Long.class) && ((long)fieldVal) == 0){
					continue;
				}else if(fieldType.equals(Byte.class) && ((byte)fieldVal) == 0){
					continue;
				}else if(fieldType.equals(Float.class) && ((float)fieldVal) == 0){
					continue;
				}else if(fieldType.equals(Double.class) && ((double)fieldVal) == 0.0){
					continue;
				}else if(fieldType.equals(Boolean.class)){
					continue;
				} else if(fieldType.equals(String.class) && fieldVal.equals(null)){
					continue;
				}else if(fieldType.equals(Integer.class) && fieldVal.equals(null)){
					continue;
				}else if(fieldType.equals(Double.class) && fieldVal.equals(null)){
					continue;
				}else if(fieldType.equals(LocalDate.class) && fieldVal.equals(null)){
					continue;
				}else if(fieldType.equals(Character.class) && fieldVal.equals('\0')){
					continue;
				}
				
				
				
				if (f.getAnnotation(Column.class) != null) {
					String keyVal = f.getAnnotation(Column.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), f.get(o));
					}else {
						colNameToValue.put(keyVal, f.get(o));
					}
				} else if (f.getAnnotation(Id.class) != null) {
					id = (int)f.get(o);
					if(id == 0) {
						continue;
					}
					String keyVal = f.getAnnotation(Id.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), f.get(o));
					}else {
						colNameToValue.put(keyVal, f.get(o));
					}
				} else {
					String keyVal = f.getName();
					colNameToValue.put(keyVal, f.get(o));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
		
		String tableName = o.getClass().getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = o.getClass().getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		
		String pkName = m.getPrimaryKey().getColumnName();
		
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}
		
		return td.findBySimilarAttributes(o.getClass(), colNameToValue, tableName, pkName);
	}
	
	@Override
	public int updateRow(Object o) {

		Field[] fields = o.getClass().getDeclaredFields();
		LinkedHashMap<String, Object> colNameToValue = new LinkedHashMap<>();
		int id=0;
		MetaModel m = MetaModel.of(o.getClass());


		for(Field f : fields) {
			f.setAccessible(true);
			try {
				
				if(f.getAnnotation(Exclude.class) != null || f.getAnnotation(JoinColumn.class) != null) {
					continue;
				}
				Object value = f.get(o);
			
				if (f.getAnnotation(Column.class) != null) {
					String keyVal = f.getAnnotation(Column.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), value);
					}else {
						colNameToValue.put(keyVal, value);
					}
				} else if (f.getAnnotation(Id.class) != null) {
					id = (int)value;
					if(id == 0) {
						continue;
					}
					String keyVal = f.getAnnotation(Id.class).columnName();
					if (keyVal.equals("")) {
						colNameToValue.put(f.getName(), value);
					}else {
						colNameToValue.put(keyVal,value);
					}
				} else {
					String keyVal = f.getName();
					colNameToValue.put(keyVal, value);
				}
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}
		String tableName = o.getClass().getAnnotation(Entity.class).tableName();
		if (tableName.equals("")){
			tableName = o.getClass().getName();
		    int firstChar;
		    firstChar = tableName.lastIndexOf ('.') + 1;
		    if ( firstChar > 0 ) {
		    	tableName = tableName.substring ( firstChar );
		      }
			
		}
		
		String pkName = m.getPrimaryKey().getColumnName();
		
		if(pkName.equals("")) {
			pkName = m.getPrimaryKey().getName();
		}

		return td.updateRow(colNameToValue, tableName, pkName, id);
		
	}


	@Override
	public void alter(Class<?> clazz) {

		MetaModel m = MetaModel.of(clazz);

		try {
			tableDao.alter(m);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void truncate(Class<?> clazz) {

		MetaModel m = MetaModel.of(clazz);

		try {
			tableDao.truncate(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void drop(Class<?> clazz) {
		MetaModel m = MetaModel.of(clazz);

		try {
			tableDao.drop(m);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	@Override
	public void renameTable(Class<?> clazz, String oldName) {

		MetaModel m = MetaModel.of(clazz);

		try {
			tableDao.renameTable(m, oldName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void renameColumn(Class<?> clazz, String oldName, String newName) {
		MetaModel m = MetaModel.of(clazz);

		try {
			tableDao.renameColumn(m, oldName, newName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
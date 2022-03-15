package com.revature.service;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.dao.DMLDao;
import com.revature.dao.TableDao;
import com.revature.util.MetaModel;


public class ServicesImpl implements IServices {

	private final DMLDao td = new DMLDao();
	private final TableDao tableDao = new TableDao();

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
	public int insert(Object o, boolean save) {

		// Object o has fields that will be added to the table
		// boolean save indicates whether the changes will be committed or not

		Field[] fields = o.getClass().getDeclaredFields();
		LinkedHashMap<String, Object> colNameToValue = new LinkedHashMap<>();

		MetaModel m = MetaModel.of(o.getClass());


		for(Field f : fields) {
			f.setAccessible(true);
			try {
				
				if(f.getAnnotation(Exclude.class) != null || f.getAnnotation(JoinColumn.class) != null) {
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


		// return the list of field values, the name of the table, and if you want to commit the changes.
		return td.insert(colNameToValue, tableName, pkName, true);
		// this should return the id of the new row created, or -1 if failed.
	}


	@Override
	public int removeByPk(Class <?> clazz, int id, boolean save) {

		// int id is the primary key of the column that will be removed
		// boolean save indicates whether the changes will be committed or not

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

		return td.remove(tableName, id, pkName, save);
		// return TableDao.removeFromTable(id)
		// this should return the id of the row deleted, or -1 if failed.

	}

	@Override
	public ArrayList<Integer> remove(Class<?> clazz, String where, boolean save) {

		// String where is the condition that will be used to determine what will
		// 		be removed. (ex. age > 4) .
		// boolean save indicates whether the changes will be committed or not

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

		return td.remove(tableName, where, pkName, save);
		// return TableDao.removeFromTable(where)
		// this should return an arraylist of ids which were deleted
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
		return td.find(clazz, where, tableName);


	}


	@Override
	public Object findByPk(Class<?> clazz, int id) {
		MetaModel m = MetaModel.of(clazz);
		String pkName = m.getPrimaryKey().getColumnName();

		return td.findByPk(clazz, id, pkName);
	}


	@Override
	public int updateRow(Object o) {

		Field[] fields = o.getClass().getDeclaredFields();
		LinkedHashMap<String, Object> colNameToValue = new LinkedHashMap<>();

		MetaModel m = MetaModel.of(o.getClass());


		for(Field f : fields) {
			f.setAccessible(true);
			try {
				if (f.getAnnotation(Column.class) != null) {
					colNameToValue.put(f.getName(), f.get(o));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			} finally {
				f.setAccessible(false);
			}
		}


		// return the list of field values, the name of the table, and if you want to commit the changes.
		return td.updateRow(colNameToValue, o.getClass().getAnnotation(Entity.class).tableName(), m.getPrimaryKey().getColumnName(), true);

	}




	@Override
	public int rollback() {
		// will rollback to last commit/savepoint/rollback
		// basically just call the SQL rollback command


		return -1;
		// return TableDao.roll()
		// this should return 1 if successful, -1 if unsuccessful

	}

	@Override
	public int commit() {
		// will commit saved changes
		// basically just the SQL commit command


		return -1;
		// return tableDao.commit()
		// this should return 1 if successful, -1 if unsuccessful
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







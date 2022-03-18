package com.revature.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.revature.Management;
import com.revature.Relation;
import com.revature.annotations.Column;
import com.revature.dao.TableDao;

/**
 * The configuration for storing all information pertaining to a user's database
 * to be used by the Api.
 * 
 * @author Nathaniel Blichfeldt
 *
 */
public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
	private List<MetaModel<Class<?>>> metaModels;
	private TableDao tbdao;

	private String url;
	private String schema;
	private String username;
	private String password;
	private Management type;

	/**
	 * Constructor for Configuration (default file location)
	 * 
	 * @throws FileNotFoundException	if the file is not found in the specified location
	 */
	public Configuration() throws FileNotFoundException {
		this("./src/main/resources/");
	}

	
	/**
	 * Constructor for Configuration (testing only)
	 * 
	 * @param tbdao	the TableDao 
	 * @throws FileNotFoundException	if the file is not found in the specified location
	 */
	public Configuration(TableDao tbdao) throws FileNotFoundException {
		this("./src/main/resources/");
		this.tbdao = tbdao;
	}

	/**
	 * Constructor for Configuration (custom file path)
	 * 
	 * @param pathname
	 * @throws FileNotFoundException	if the file is not found in the specified location
	 */
	public Configuration(String pathname) throws FileNotFoundException {
		super();
		File config = new File(pathname + "config.properties");
		Scanner scan = new Scanner(config);
		List<String> missing = new ArrayList<>(
				Arrays.asList("url", "schema", "username", "password", "schemaManagement"));
		metaModels = new ArrayList<>();

		while (scan.hasNext()) {
			String[] nextVar = scan.nextLine().split("=");

			switch (nextVar[0]) {
			case "url":
				url = nextVar[1];
				missing.removeIf(s -> s.equals("url"));
				break;
			case "schema":
				schema = nextVar[1];
				missing.removeIf(s -> s.equals("schema"));
				break;
			case "username":
				username = nextVar[1];
				missing.removeIf(s -> s.equals("username"));
				break;
			case "password":
				password = nextVar[1];
				missing.removeIf(s -> s.equals("password"));
				break;
			case "SchemaManagement":
				type = Management.valueOf(nextVar[1]);
				missing.removeIf(s -> s.equals("schemaManagement"));
				break;
			default:
				logger.warn(nextVar[0] + " is not supported");
				break;
			}
		}

		if (!missing.isEmpty()) {
			Collectors.joining();
			String msg = missing.stream().collect(Collectors.joining(", "));
			throw new IllegalStateException("The following fields are missing: " + msg);
		}
		
		tbdao = new TableDao();
	}

	/**
	 * Constructor for Configuration
	 * 
	 * @param url		the database end point
	 * @param schema	the database schema
	 * @param username	the database username
	 * @param password	the database password
	 * @param type		the Api management type
	 */
	public Configuration(String url, String schema, String username, String password, Management type) {
		super();
		List<String> missing = new ArrayList<>();
		metaModels = new ArrayList<>();

		if (url != null && !url.isEmpty()) {
			this.url = url;
		} else {
			missing.add("url");
		}
		if (schema != null && !schema.isEmpty()) {
			this.schema = schema;
		} else {
			missing.add("schema");
		}
		if (username != null && !username.isEmpty()) {
			this.username = username;
		} else {
			missing.add("username");
		}
		if (password != null && !password.isEmpty()) {
			this.password = password;
		} else {
			missing.add("password");
		}
		if (type != null) {
			this.type = type;
		} else {
			missing.add("schemaManagement");
		}

		if (!missing.isEmpty()) {
			Collectors.joining();
			String msg = missing.stream().collect(Collectors.joining(", "));
			throw new IllegalStateException("The following fields are missing: " + msg);
		}
		
		tbdao = new TableDao();
	}
	
	/**
	 * Gets the current list of table names that have been added to the configuration
	 * 
	 * @return	list of table names
	 */
	public List<String> getTables() {
		return metaModels.stream()
				.map(m -> m.getClassName())
				.collect(Collectors.toList());
	}
	
	/**
	 * add a single class to the configuration so it can be made into a table
	 * 
	 * @param clazz	class to be made into a table on the database
	 */
	public void addTable(Class<?> clazz) {
		MetaModel<Class<?>> meta = MetaModel.of(clazz);
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}
		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		
		metaModels.add(meta);
	}

	/**
	 * Adds a list of classes to the configuration to be made into tables
	 * 
	 * @param clazzes	classes to be made into tables on the database
	 */
	public void addTables(Collection<Class<?>> clazzes) {
		for (Class<?> clazz : clazzes)
			addTable(clazz);
	}

	/**
	 * Adds any number of classes to the configuration to be made into tables
	 * 
	 * @param clazzes	classes to be made into tables on the database
	 */
	public void addTables(Class<?>... clazzes) {
		for (Class<?> clazz : clazzes)
			addTable(clazz);
	}

	/**
	 * Checks if foreign key relations between tables are set up correctly across all user-defined classes.
	 * 
	 * @throws IllegalStateException	any time when the foreign key relationship is incorrect.
	 */
	@SuppressWarnings("incomplete-switch")
	public void validate() {
		for (MetaModel<?> meta : metaModels) {
			List<ForeignKeyField> fkeys;

			try {
				fkeys = meta.getForeignKeys();
			} catch (RuntimeException e) {
				logger.warn("No foreign keys found in " + meta.getClassName() + ". Skipping");
				continue;
			}

			for (ForeignKeyField column : fkeys) {
				MetaModel<Class<?>> meta2 = getModelByName(column.getMappedByTable());

				if (meta2 != null) {
					
					GenericField column2 = meta2.getFieldByName(column.getMappedByColumn());

					switch (column.getRelation()) {
					case OneToOne:
						if (!column.getType().equals(meta2.getTableClass()))
							throw new IllegalStateException(meta.getSimpleClassName() + "." + column.getName()
									+ " needs to be of type " + meta2.getTableClass() + ", found: " + column.getType());
						if (!column2.getType().equals(meta.getTableClass()))
							throw new IllegalStateException(meta2.getSimpleClassName() + "." + column2.getName()
									+ " needs to be of type " + meta.getTableClass() + ", found: " + column2.getType());

						if (column2.getRelation() != Relation.OneToOne) {
							throw new IllegalStateException("Miss-matching mappings, " + column.getName()
									+ " = OneToOne, " + column2.getName() + " = " + column2.getRelation().toString());
						}
						break;
					case ManyToOne:
						if (!column.getType().equals(meta2.getTableClass()))
							throw new IllegalStateException(
									meta.getSimpleClassName() + "." + column.getName() + " needs to be of type "
											+ meta2.getSimpleClassName() + ", found: " + column.getType());

						String column2Component = column2.getSubType();
						if (column2Component == null)
							throw new IllegalStateException(
									meta2.getSimpleClassName() + "." + column2.getName() + " must be a Collection");

						if (!column2Component.equals(meta.getSimpleClassName()))
							throw new IllegalStateException(meta2.getSimpleClassName() + "." + column2.getName()
									+ " needs to be of type java.util.Collection<" + meta.getSimpleClassName()
									+ ">, found: " + column2.getField().getGenericType());

						if (column2.getRelation() != Relation.OneToMany) {
							throw new IllegalStateException("Miss-matching mappings, " + column.getName()
									+ " = ManyToOne, " + column2.getName() + " = " + column2.getRelation().toString());
						}
						break;
					case ManyToMany:
						String columnComponent = column.getSubType();
						if (columnComponent == null)
							throw new IllegalStateException(
									meta.getSimpleClassName() + "." + column.getName() + " must be a Collection");

						if (!columnComponent.equals(meta2.getSimpleClassName()))
							throw new IllegalStateException(meta.getSimpleClassName() + "." + column.getName()
									+ " needs to be of type java.util.Collection<" + meta2.getSimpleClassName()
									+ ">, found: " + column.getField().getGenericType());

						column2Component = column2.getSubType();
						if (column2Component == null)
							throw new IllegalStateException(
									meta2.getSimpleClassName() + "." + column2.getName() + " must be a Collection");

						if (!column2Component.equals(meta.getSimpleClassName()))
							throw new IllegalStateException(meta2.getSimpleClassName() + "." + column2.getName()
									+ " needs to be of type java.util.Collection<" + meta.getSimpleClassName()
									+ ">, found: " + column2.getField().getGenericType());

						if (column2.getRelation() != Relation.ManyToMany) {
							throw new IllegalStateException("Miss-matching mappings, " + column.getName()
									+ " = ManyToOne, " + column2.getName() + " = " + column2.getRelation().toString());
						}
						break;
					}
				} else {
					throw new IllegalStateException("Unable to find class " + column.getMappedByTable());
				}
			}
		}
		
		try {
			buildTables();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * creates all of the tables for the database based on the user-defined classes, if needed.
	 * 
	 * @throws ClassNotFoundException persisted exception from the TableDao.insert() method
	 * @see	TableDao
	 */
	private void buildTables() throws ClassNotFoundException {
		metaModels.forEach(m -> {
			try {
				tbdao.insert(m, m.getTableClass().getPackage().getName());
			} catch (SQLException e) {
				throw new RuntimeException("Unable to create table " + m.getClassName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	/**
	 * 
	 */
	public void reset() {
		metaModels.clear();
	}

	/**
	 * Gets the full current end point (url + schema) for connecting to the database
	 * 
	 * @return
	 */
	public String getFullUrl() {
		StringBuilder str = new StringBuilder();

		str.append(url);
		str.append("?currentSchema=");
		str.append(schema);

		return str.toString();
	}

	/**
	 * Gets the current end point for connecting to the database
	 * 
	 * @return	the database end point
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Gets the current schema in which the api will be operating on in the database
	 * 
	 * @return	the database schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Gets the current username for logging into the database
	 * 
	 * @return	the database username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the current password for logging into the database
	 * 
	 * @return	the database password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the current management of the Api.
	 * 
	 * @return	the Management to be used by the Api
	 */
	public Management getType() {
		return type;
	}

	/**
	 * Finds a MedaModel with the provided class name.
	 * 
	 * @param name	the class name
	 * @return	a MetaModel with the given name if it exists, Otherwise null.
	 */
	public MetaModel<Class<?>> getModelByName(String name) {
		Optional<MetaModel<Class<?>>> meta = metaModels.stream()
				.filter(m -> {
					return m.getSimpleClassName().equals(name) || m.getTableName().equals(name);
				}).findFirst();

		return meta.isPresent() ? meta.get() : null;
	}

	/**
	 * Finds a MetaModel with the provided class.
	 * 
	 * @param clazz	the class
	 * @return	a MetaModel of the provide class if it exists, Otherwise null.
	 */
	public MetaModel<Class<?>> getModelByClass(Class<?> clazz) {
		Optional<MetaModel<Class<?>>> meta = metaModels.stream()
				.filter(m -> m.getClassName().equals(clazz.getName()))
				.findFirst();

		return meta.isPresent() ? meta.get() : null;
	}
}
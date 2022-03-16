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

public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
	private List<MetaModel<Class<?>>> metaModels;
	private TableDao tbdao;

	private String url;
	private String schema;
	private String username;
	private String password;
	private Management type;

	public Configuration() throws FileNotFoundException {
		this("./src/main/resources/");
	}

	// for testing only
	public Configuration(TableDao tbdao) throws FileNotFoundException {
		this("./src/main/resources/");
		this.tbdao = tbdao;
	}

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
	
	public List<String> getTables() {
		return metaModels.stream()
				.map(m -> m.getClassName())
				.collect(Collectors.toList());
	}
	
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

	public void addTables(Collection<Class<?>> clazzes) {
		for (Class<?> clazz : clazzes)
			addTable(clazz);
	}

	public void addTables(Class<?>... clazzes) {
		for (Class<?> clazz : clazzes)
			addTable(clazz);
	}

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
				MetaModel<Class<?>> temp = getModelByName(column.getMappedByTable());

				if (temp != null) {
					try {
						GenericField column2 = temp.getFieldByName(column.getMappedByColumn());

						if (!column.getType().equals(column2.getType())) {
							throw new IllegalStateException(
									"Miss-matching types, " + column.getName() + " = " + column.getType() + ", "
											+ column2.getName() + " = " + column2.getType());
						}
						
						switch (column.getRelation()) {
						case OneToOne:
							if (column2.getRelation() != Relation.OneToOne) {
								throw new IllegalStateException(
										"Miss-matching mappings, " + column.getName() + " = OneToOne, "
												+ column2.getName() + " = " + column2.getRelation().toString());
							}
							break;
						case ManyToOne:
							if (column2.getRelation() != Relation.OneToMany) {
								throw new IllegalStateException(
										"Miss-matching mappings, " + column.getName() + " = ManyToOne, "
												+ column2.getName() + " = " + column2.getRelation().toString());
							}
							break;
						case ManyToMany:
							if (column2.getRelation() != Relation.ManyToMany) {
								throw new IllegalStateException(
										"Miss-matching mappings, " + column.getName() + " = ManyToMany, "
												+ column2.getName() + " = " + column2.getRelation().toString());
							}
							break;
						case OneToMany:
						default:
							throw new IllegalStateException("Invalid relation " + column.getRelation() + " set to foreign key " + column.getName());
						}
					} catch (RuntimeException e) {
						throw new IllegalStateException("Unable to find field " + column.getMappedByColumn()
								+ " in class " + column.getMappedByTable());
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

	private void buildTables() throws ClassNotFoundException {
		metaModels.forEach(m -> {
			try {
				tbdao.insert(m);
			} catch (SQLException e) {
				throw new RuntimeException("Unable to create table " + m.getClassName());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
	}

	public void reset() {
		metaModels.clear();
	}

	public String getFullUrl() {
		StringBuilder str = new StringBuilder();

		str.append(url);
		str.append("?currentSchema=");
		str.append(schema);

		return str.toString();
	}

	public String getUrl() {
		return url;
	}

	public String getSchema() {
		return schema;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Management getType() {
		return type;
	}

	public MetaModel<Class<?>> getModelByName(String name) {
		Optional<MetaModel<Class<?>>> meta = metaModels.stream()
				.filter(m -> {
					return m.getSimpleClassName().equals(name) || m.getTableName().equals(name);
				}).findFirst();

		return meta.isPresent() ? meta.get() : null;
	}

	public MetaModel<Class<?>> getModelByClass(Class<?> clazz) {
		Optional<MetaModel<Class<?>>> meta = metaModels.stream()
				.filter(m -> m.getClass().equals(clazz))
				.findFirst();

		return meta.isPresent() ? meta.get() : null;
	}
}
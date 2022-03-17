import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.Management;
import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.annotations.ManyToMany;
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;
import com.revature.annotations.OneToOne;
import com.revature.dao.TableDao;
import com.revature.util.Configuration;
import com.revature.util.MetaModel;

public class ConfigurationTest {
	private Configuration defaultConfig;
	private TableDao mockDao;

	@Before
	public void setup() {
		mockDao = mock(TableDao.class);
		try {
			defaultConfig = new Configuration(mockDao);
		} catch (FileNotFoundException e) {
			throw new AssertionError("unable to open the default file");
		}
	}

	@After
	public void teardown() {
		defaultConfig = null;
		mockDao = null;
	}

	@Test
	public void testDefaultConstructor() {
		try {
			Configuration config = new Configuration();
		} catch (FileNotFoundException e) {
			throw new AssertionError("unable to open the default file");
		}
	}

	@Test(expected = IllegalStateException.class)
	public void testUserDefinedConfigNoParameters() {
		try {
			Configuration config = new Configuration("./src/test/resources/");
		} catch (FileNotFoundException e) {
			// Any reason this was Assertion Error?
			//			throw new AssertionError("unable to open the default file");
			throw new IllegalStateException("unable to open the default file");
		}
	}

	@Test(expected = FileNotFoundException.class)
	public void testUserDefinedConfigInvalid() throws FileNotFoundException {
		Configuration config = new Configuration("./src/main/");
	}

	@Test(expected = IllegalStateException.class)
	public void testUserNonFileMissingTwo() {
		Configuration config = new Configuration(
				"jdbc:postgresql://team-5-ent.cvtq9j4axrge.us-east-1.rds.amazonaws.com:5432/postgres", null, "postgres",
				"", Management.create);
	}

	@Test
	public void testUserNonFileValid() {
		Configuration config = new Configuration(
				"jdbc:postgresql://team-5-ent.cvtq9j4axrge.us-east-1.rds.amazonaws.com:5432/postgres", "test0",
				"postgres", "postgres", Management.create);
	}

	@Test
	public void testSetTablesEmpty() {
		List<Class<?>> list = new ArrayList<>();

		defaultConfig.reset();
		defaultConfig.addTables(list);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesOneTable() {
		List<Class<?>> list = new ArrayList<>();
		list.add(BasicTable.class);

		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTables(list);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesConnectedManyToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
		defaultConfig.addTable(BasicTableManyToOneForeign.class);
		defaultConfig.validate();
	}

	@Test(expected=IllegalStateException.class)
	public void testSetTablesDisconnectedManyToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToOnePrimary.class);
		defaultConfig.addTable(BasicTableManyToOneForeignInvalid.class);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesConnectedOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToOnePrimary.class);
		defaultConfig.addTable(BasicTableOneToOneForeign.class);
		defaultConfig.validate();
	}

	@Test(expected=IllegalStateException.class)
	public void testSetTablesDisconnectedOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
		defaultConfig.addTable(BasicTableOneToOneForeignInvalid.class);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesConnectedManyToManyTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableManyToManyPrimary.class);
		defaultConfig.addTable(BasicTableManyToManyForeign.class);
		defaultConfig.validate();
	}

	@Test(expected=IllegalStateException.class)
	public void testSetTablesDisconnectedManyToManyTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
		defaultConfig.addTable(BasicTableManyToManyForeignInvalid.class);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesConnectedRelationMismatchTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
		defaultConfig.addTable(BasicTableManyToOneForeign.class);
		defaultConfig.validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testSetTablesDisconnectedTable() {
		List<Class<?>> list = new ArrayList<>();
		list.add(BasicTableManyToOneForeign.class);

		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTables(list);
		defaultConfig.validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testSetTablesMismatchingTypes() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTables(BasicTableOneToManyPrimary.class, BasicTableForeignMismatch.class);
		defaultConfig.validate();
	}

	@Test
	public void testGetModelByNameInvalid() {
		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
		try {
			meta1.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getForeignKeys();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getColumns();
		} catch (RuntimeException e) {
		}

		defaultConfig.reset();
		defaultConfig.addTables();

		assertNotEquals(meta1, defaultConfig.getModelByName("test"));
	}

	@Test
	public void testGetModelByNameValid() {
		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
		try {
			meta1.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getForeignKeys();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getColumns();
		} catch (RuntimeException e) {
		}

		defaultConfig.reset();
		defaultConfig.addTables(BasicTable.class, BasicTableOneToManyPrimary.class, BasicTableManyToOneForeign.class);

		assertEquals(meta1, defaultConfig.getModelByName("basic"));
	}

	@Test
	public void testGetModelByClassInvalid() {
		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
		try {
			meta1.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getForeignKeys();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getColumns();
		} catch (RuntimeException e) {
		}

		defaultConfig.reset();
		defaultConfig.addTables();

		assertNotEquals(meta1, defaultConfig.getModelByClass(BasicTableForeignMismatch.class));
	}

	@Test
	public void testGetModelByClassValid() {
		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTableManyToOneForeign.class);
		try {
			meta1.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getForeignKeys();
		} catch (RuntimeException e) {
		}
		try {
			meta1.getColumns();
		} catch (RuntimeException e) {
		}

		defaultConfig.reset();
		defaultConfig.addTables(BasicTable.class, BasicTableOneToManyPrimary.class, BasicTableManyToOneForeign.class);

		assertEquals(meta1, defaultConfig.getModelByName("BasicTableManyToOneForeign"));
	}

	@Test
	public void testGetFullUrl() {
		assertEquals(
				"jdbc:postgresql://team-5-ent.cvtq9j4axrge.us-east-1.rds.amazonaws.com:5432/postgres?currentSchema=test0",
				defaultConfig.getFullUrl());
	}
}

@Entity(tableName = "basic")
class BasicTable {
	@Id
	private int id;

	@Column(unique = true, columnName = "student_name", length = 80)
	private String name;
}

@Entity
class BasicTableForeignMismatch {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToManyPrimary")
	@ManyToOne
	private long owner;

	private String name;
}

@Entity
class BasicTableOneToManyPrimary {
	@Id(serial = true)
	@OneToMany
	private int id;

	private String name;
}

@Entity
class BasicTableOneToOnePrimary {
	@Id(serial = true)
	@OneToOne
	private int id;

	private String name;
}

@Entity
class BasicTableManyToManyPrimary {
	@Id(serial = true)
	@ManyToMany
	private int id;

	private String name;
}

@Entity
class BasicTableManyToOneForeign {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToManyPrimary")
	@ManyToOne
	private int owner;

	private String name;
}

@Entity
class BasicTableOneToOneForeign {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToOnePrimary")
	@OneToOne
	private int owner;

	private String name;
}

@Entity
class BasicTableManyToManyForeign {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableManyToManyPrimary")
	@ManyToMany
	private int owner;

	private String name;
}

@Entity
class BasicTableManyToOneForeignInvalid {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToOnePrimary")
	@ManyToOne
	private int owner;

	private String name;
}

@Entity
class BasicTableOneToOneForeignInvalid {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToManyPrimary")
	@OneToOne
	private int owner;

	private String name;
}

@Entity
class BasicTableManyToManyForeignInvalid {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTableOneToOnePrimary")
	@ManyToMany
	private int owner;

	private String name;
}
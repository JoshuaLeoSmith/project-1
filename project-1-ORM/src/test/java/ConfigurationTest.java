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
import com.revature.annotations.ManyToOne;
import com.revature.annotations.OneToMany;
import com.revature.dao.TableDao;
import com.revature.util.Configuration;
import com.revature.util.MetaModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

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
			throw new AssertionError("unable to open the default file");
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
	public void testSetTablesConnectedTables() {
		List<Class<?>> list = new ArrayList<>();
		list.add(BasicTablePrimary.class);
		list.add(BasicTableForeign.class);

		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(BasicTablePrimary.class);
		defaultConfig.addTable(BasicTableForeign.class);
		defaultConfig.validate();
	}

	@Test(expected = IllegalStateException.class)
	public void testSetTablesDisconnectedTable() {
		List<Class<?>> list = new ArrayList<>();
		list.add(BasicTableForeign.class);

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
		defaultConfig.addTables(BasicTablePrimary.class, BasicTableForeignMismatch.class);
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
		defaultConfig.addTables(BasicTable.class, BasicTablePrimary.class, BasicTableForeign.class);

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
		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTableForeign.class);
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
		defaultConfig.addTables(BasicTable.class, BasicTablePrimary.class, BasicTableForeign.class);

		assertEquals(meta1, defaultConfig.getModelByName("BasicTableForeign"));
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
class BasicTablePrimary {
	@Id(serial = true)
	@OneToMany
	private int id;

	private String name;
}

@Entity
class BasicTableForeign {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTablePrimary")
	@ManyToOne
	private int owner;

	private String name;
}

@Entity
class BasicTableForeignMismatch {
	@Id(serial = true)
	private int id;

	@JoinColumn(mappedByColumn = "id", mappedByTable = "BasicTablePrimary")
	@ManyToOne
	private long owner;

	private String name;
}
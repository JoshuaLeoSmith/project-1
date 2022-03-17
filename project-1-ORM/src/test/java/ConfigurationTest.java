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
			// throw new AssertionError("unable to open the default file");
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

//	@Test
//	public void testSetTablesOneTable() {
//		List<Class<?>> list = new ArrayList<>();
//		list.add(BasicTable.class);
//
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables(list);
//		defaultConfig.validate();
//	}
//
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
		defaultConfig.addTable(PrimaryOneToMany.class);
		defaultConfig.addTable(ForeignManyToOne.class);
		defaultConfig.validate();
	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testSetTablesDisconnectedManyToOneTables() {
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTable(BasicTableOneToOnePrimary.class);
//		defaultConfig.addTable(BasicTableManyToOneForeignInvalid.class);
//		defaultConfig.validate();
//	}
//
	@Test
	public void testSetTablesOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class));
		} catch (ClassNotFoundException e) {
			throw new AssertionError(e.getMessage());
		} catch (SQLException e) {
			throw new AssertionError(e.getMessage());
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryOneToOne.class);
		defaultConfig.addTable(ForeignOneToOne.class);
		defaultConfig.validate();
	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testSetTablesDisconnectedOneToOneTables() {
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
//		defaultConfig.addTable(BasicTableOneToOneForeignInvalid.class);
//		defaultConfig.validate();
//	}
//
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
		defaultConfig.addTable(PrimaryManyToMany.class);
		defaultConfig.addTable(ForeignManyToMany.class);
		defaultConfig.validate();
	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testSetTablesDisconnectedManyToManyTables() {
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
//		defaultConfig.addTable(BasicTableManyToManyForeignInvalid.class);
//		defaultConfig.validate();
//	}
//
//	@Test
//	public void testSetTablesConnectedRelationMismatchTables() {
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTable(BasicTableOneToManyPrimary.class);
//		defaultConfig.addTable(BasicTableManyToOneForeign.class);
//		defaultConfig.validate();
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testSetTablesDisconnectedTable() {
//		List<Class<?>> list = new ArrayList<>();
//		list.add(BasicTableManyToOneForeign.class);
//
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables(list);
//		defaultConfig.validate();
//	}
//
//	@Test(expected = IllegalStateException.class)
//	public void testSetTablesMismatchingTypes() {
//		try {
//			doNothing().when(mockDao).insert(isA(MetaModel.class));
//		} catch (ClassNotFoundException e) {
//			throw new AssertionError("This should never happen");
//		} catch (SQLException e) {
//			throw new AssertionError("This should never happen");
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables(BasicTableOneToManyPrimary.class, BasicTableForeignMismatch.class);
//		defaultConfig.validate();
//	}
//
//	@Test
//	public void testGetModelByNameInvalid() {
//		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
//		try {
//			meta1.getPrimaryKey();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getForeignKeys();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getColumns();
//		} catch (RuntimeException e) {
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables();
//
//		assertNotEquals(meta1, defaultConfig.getModelByName("test"));
//	}
//
//	@Test
//	public void testGetModelByNameValid() {
//		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
//		try {
//			meta1.getPrimaryKey();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getForeignKeys();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getColumns();
//		} catch (RuntimeException e) {
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables(BasicTable.class, BasicTableOneToManyPrimary.class, BasicTableManyToOneForeign.class);
//
//		assertEquals(meta1, defaultConfig.getModelByName("basic"));
//	}
//
//	@Test
//	public void testGetModelByClassInvalid() {
//		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTable.class);
//		try {
//			meta1.getPrimaryKey();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getForeignKeys();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getColumns();
//		} catch (RuntimeException e) {
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables();
//
//		assertNotEquals(meta1, defaultConfig.getModelByClass(BasicTableForeignMismatch.class));
//	}
//
//	@Test
//	public void testGetModelByClassValid() {
//		MetaModel<Class<?>> meta1 = MetaModel.of(BasicTableManyToOneForeign.class);
//		try {
//			meta1.getPrimaryKey();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getForeignKeys();
//		} catch (RuntimeException e) {
//		}
//		try {
//			meta1.getColumns();
//		} catch (RuntimeException e) {
//		}
//
//		defaultConfig.reset();
//		defaultConfig.addTables(BasicTable.class, BasicTableOneToManyPrimary.class, BasicTableManyToOneForeign.class);
//
//		assertEquals(meta1, defaultConfig.getModelByName("BasicTableManyToOneForeign"));
//	}

	@Test
	public void testGetFullUrl() {
		assertEquals(
				defaultConfig.getUrl() + "?currentSchema="+ defaultConfig.getSchema(),
				defaultConfig.getFullUrl());
	}
}

@Entity
class PrimaryOneToOne {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	private ForeignOneToOne account;
}

@Entity
class PrimaryOneToMany {
	@Id(serial=true)
	private int id;
	
	@OneToMany
	private List<ForeignManyToOne> account;
}

@Entity
class PrimaryManyToMany {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	private List<ForeignManyToMany> account;
}















@Entity
class ForeignOneToOne {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	@JoinColumn(mappedByTable="PrimaryOneToOne",  mappedByColumn="account")
	private PrimaryOneToOne owner;
}

@Entity
class ForeignManyToOne {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	@JoinColumn(mappedByTable="PrimaryOneToMany",  mappedByColumn="account")
	private PrimaryOneToMany owner;
}

@Entity
class ForeignManyToMany {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	@JoinColumn(mappedByTable="PrimaryManyToMany",  mappedByColumn="account")
	private List<PrimaryManyToMany> owner;
}


@Entity
class ForeignOneToOneInvalid {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	private List<PrimaryOneToOne> owners;
}

@Entity
class ForeignManyToOneInvalid {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	private PrimaryOneToOne owner;
}
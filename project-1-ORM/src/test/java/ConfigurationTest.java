import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.ArgumentMatchers.anyString;

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
	public void testUserNonFileMissingAll() {
		Configuration config = new Configuration(null, null, null, null, null);
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
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
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
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
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

	@Test(expected = IllegalStateException.class)
	public void testSetTablesWrongTypeManyToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(ForeignManyToOneInvalid.class);
		defaultConfig.addTable(PrimaryOneToMany.class);
		defaultConfig.validate();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTablesWrongTypeManyToOneTables2() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryOneToManyInvalid.class);
		defaultConfig.addTable(ForeignManyToOneInvalid2.class);
		defaultConfig.validate();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTablesMismatchRelationsManyToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryOneToManyInvalid2.class);
		defaultConfig.addTable(ForeignManyToOneInvalid3.class);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
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

	@Test(expected = IllegalStateException.class)
	public void testSetTablesWrongTypeOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryOneToOne.class);
		defaultConfig.addTable(ForeignOneToOneInvalid.class);
		defaultConfig.validate();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTablesMismatchRelationsOneToOneTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryOneToOneInvalid.class);
		defaultConfig.addTable(ForeignOneToOneInvalid2.class);
		defaultConfig.validate();
	}

	@Test
	public void testSetTablesConnectedManyToManyTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
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

	@Test(expected = IllegalStateException.class)
	public void testSetTablesWrongTypeManyToManyTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryManyToMany.class);
		defaultConfig.addTable(ForeignManyToManyInvalid.class);
		defaultConfig.validate();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTablesWrongTypeManyToManyTables2() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryManyToManyInvalid.class);
		defaultConfig.addTable(ForeignManyToManyInvalid2.class);
		defaultConfig.validate();
	}
	
	@Test(expected = IllegalStateException.class)
	public void testSetTablesMismatchRelationsManyToManyTables() {
		try {
			doNothing().when(mockDao).insert(isA(MetaModel.class), anyString());
		} catch (ClassNotFoundException e) {
			throw new AssertionError("This should never happen");
		} catch (SQLException e) {
			throw new AssertionError("This should never happen");
		}

		defaultConfig.reset();
		defaultConfig.addTable(PrimaryManyToManyInvalid2.class);
		defaultConfig.addTable(ForeignManyToManyInvalid3.class);
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
		defaultConfig.addTables(BasicTable.class, PrimaryOneToMany.class, ForeignManyToMany.class);

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

		assertNotEquals(meta1, defaultConfig.getModelByClass(PrimaryOneToOne.class));
	}

	@Test
	public void testGetModelByClassValid() {
		MetaModel<Class<?>> meta1 = MetaModel.of(ForeignManyToOne.class);
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
		defaultConfig.addTables(BasicTable.class, PrimaryOneToMany.class, ForeignManyToOne.class);

		assertEquals(meta1, defaultConfig.getModelByClass(ForeignManyToOne.class));
	}

	@Test
	public void testGetFullUrl() {
		assertEquals(
				defaultConfig.getUrl() + "?currentSchema="+ defaultConfig.getSchema(),
				defaultConfig.getFullUrl());
	}
}

@Entity(tableName="basic")
class BasicTable {
	@Id(serial=true)
	private int id;
	
	private String name;
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
	private List<ForeignManyToOne> accounts;
}

@Entity
class PrimaryManyToMany {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	private List<ForeignManyToMany> accounts;
}




@Entity
class PrimaryOneToOneInvalid {
	@Id(serial=true)
	private int id;
	
	@OneToMany
	private ForeignOneToOneInvalid2 account;
}

@Entity
class PrimaryOneToManyInvalid {
	@Id(serial=true)
	private int id;
	
	@OneToMany
	private ForeignManyToOneInvalid2 account;
}

@Entity
class PrimaryOneToManyInvalid2 {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	private List<ForeignManyToOneInvalid3> accounts;
}

@Entity
class PrimaryManyToManyInvalid {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	private List<String> accounts;
}

@Entity
class PrimaryManyToManyInvalid2 {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	private List<ForeignManyToManyInvalid3> accounts;
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
	@JoinColumn(mappedByTable="PrimaryOneToMany",  mappedByColumn="accounts")
	private PrimaryOneToMany owner;
}

@Entity
class ForeignManyToMany {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	@JoinColumn(mappedByTable="PrimaryManyToMany",  mappedByColumn="accounts")
	private List<PrimaryManyToMany> owners;
}




@Entity
class ForeignOneToOneInvalid {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	@JoinColumn(mappedByTable="PrimaryOneToOne",  mappedByColumn="account")
	private List<PrimaryOneToOne> owners;
}

@Entity
class ForeignOneToOneInvalid2 {
	@Id(serial=true)
	private int id;
	
	@OneToOne
	@JoinColumn(mappedByTable="PrimaryOneToOneInvalid",  mappedByColumn="account")
	private PrimaryOneToOneInvalid owner;
}




@Entity
class ForeignManyToOneInvalid {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	@JoinColumn(mappedByTable="PrimaryOneToMany",  mappedByColumn="accounts")
	private List<PrimaryOneToMany> owners;
}

@Entity
class ForeignManyToOneInvalid2 {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	@JoinColumn(mappedByTable="PrimaryOneToManyInvalid",  mappedByColumn="account")
	private PrimaryOneToManyInvalid owner;
}

@Entity
class ForeignManyToOneInvalid3 {
	@Id(serial=true)
	private int id;
	
	@ManyToOne
	@JoinColumn(mappedByTable="PrimaryOneToManyInvalid2",  mappedByColumn="accounts")
	private PrimaryOneToManyInvalid2 owner;
}




@Entity
class ForeignManyToManyInvalid {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	@JoinColumn(mappedByTable="PrimaryManyToMany",  mappedByColumn="accounts")
	private PrimaryManyToMany owner;
}

@Entity
class ForeignManyToManyInvalid2 {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	@JoinColumn(mappedByTable="PrimaryManyToManyInvalid",  mappedByColumn="accounts")
	private List<PrimaryManyToManyInvalid> owners;
}

@Entity
class ForeignManyToManyInvalid3 {
	@Id(serial=true)
	private int id;
	
	@ManyToMany
	@JoinColumn(mappedByTable="PrimaryManyToManyInvalid2",  mappedByColumn="accounts")
	private List<PrimaryManyToManyInvalid2> owners;
}
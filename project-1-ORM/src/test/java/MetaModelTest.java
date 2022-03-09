import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.revature.annotations.Check;
import com.revature.annotations.Column;
import com.revature.annotations.Entity;
import com.revature.annotations.Exclude;
import com.revature.annotations.Id;
import com.revature.annotations.JoinColumn;
import com.revature.util.ColumnField;
import com.revature.util.ForeignKeyField;
import com.revature.util.GenericField;
import com.revature.util.MetaModel;
import com.revature.util.PrimaryKeyField;

public class MetaModelTest {

	// of
	@Test
	public void testMetaModelValid() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		assertNotNull(meta);
	}

	@Test(expected = IllegalStateException.class)
	public void testMetaModelInvalid() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyNotTable.class);
	}

	// getPrimaryKeys
	@Test
	public void testGetPrimaryKeyValid() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		PrimaryKeyField pkField;
		try {
			pkField = new PrimaryKeyField(DummyTable.class.getDeclaredField("id"));
			assertTrue(meta.getPrimaryKey().equals(pkField));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetPrimaryKeyValid");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetPrimaryKeyValid");
		}
	}

	@Test(expected = RuntimeException.class)
	public void testGetNonExistentPrimaryKey() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyNoPKTable.class);

		meta.getPrimaryKey();
	}

	// getColumns
	@Test
	public void testGetColumnsExclude() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			List<ColumnField> columns = Arrays.asList(new ColumnField(DummyTable.class.getDeclaredField("first_name")),
					new ColumnField(DummyTable.class.getDeclaredField("last_name")),
					new ColumnField(DummyTable.class.getDeclaredField("balance")));

			assertTrue(meta.getColumns().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsExclude");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsExclude");
		}
	}

	@Test(expected = RuntimeException.class)
	public void testGetColumnsKeysOnly() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyProjectTable.class);

		meta.getColumns().forEach(System.out::println);;
	}

	@Test
	public void testGetColumnsMix() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			List<ColumnField> columns = Arrays.asList(new ColumnField(DummyTable.class.getDeclaredField("first_name")),
					new ColumnField(DummyTable.class.getDeclaredField("last_name")),
					new ColumnField(DummyTable.class.getDeclaredField("balance")));

			assertTrue(meta.getColumns().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsMix");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsMix");
		}
	}

	// getForignKeys
	@Test(expected = RuntimeException.class)
	public void testGetForeignKeysNone() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		meta.getForeignKeys();
	}

	@Test
	public void testGetForeignKeysOne() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyProjectTable.class);

		try {
			List<ForeignKeyField> columns = Arrays
					.asList(new ForeignKeyField(DummyProjectTable.class.getDeclaredField("user_id")));

			assertTrue(meta.getForeignKeys().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetForeignKeysOne");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetForeignKeysOne");
		}
	}

	// getFieldByName
	@Test
	public void testGetFieldByNameValidColumnDefault() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new ColumnField(DummyTable.class.getDeclaredField("last_name"));

			assertEquals(meta.getFieldByName("last_name"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetFieldByNameValidColumnUserSet() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new ColumnField(DummyTable.class.getDeclaredField("first_name"));

			assertEquals(meta.getFieldByName("First_name"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetFieldByNameValidForeignKeyDefault() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyProjectTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new ForeignKeyField(DummyProjectTable.class.getDeclaredField("user_id"));

			assertEquals(meta.getFieldByName("user_id"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetFieldByNameValidForeignKeyUserSet() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyProjectTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new ForeignKeyField(DummyProjectTable.class.getDeclaredField("user_id"));

			assertEquals(meta.getFieldByName("user_Id"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetFieldByNameValidPrimaryKeyDefault() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new PrimaryKeyField(DummyTable.class.getDeclaredField("id"));

			assertEquals(meta.getFieldByName("id"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetFieldByNameValidPrimaryKeyUserSet() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			GenericField column = new PrimaryKeyField(DummyTable.class.getDeclaredField("id"));

			assertEquals(meta.getFieldByName("ID"), column);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test(expected = RuntimeException.class)
	public void testGetFieldByNameInvalid() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		meta.getFieldByName("test");
	}

	@Test(expected = RuntimeException.class)
	public void testGetFieldByNameExcluded() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		meta.getFieldByName("favorite_color");
	}

	// GetALLFields
	@Test
	public void testGetAllFieldsMissingForeignKeys() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			List<GenericField> columns = Arrays.asList(new PrimaryKeyField(DummyTable.class.getDeclaredField("id")),
					new ColumnField(DummyTable.class.getDeclaredField("first_name")),
					new ColumnField(DummyTable.class.getDeclaredField("last_name")),
					new ColumnField(DummyTable.class.getDeclaredField("balance")));

			assertTrue(meta.getAllFields().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetAllFieldsMissingColumns() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyProjectTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			List<GenericField> columns = Arrays.asList(
					new PrimaryKeyField(DummyProjectTable.class.getDeclaredField("id")),
					new ForeignKeyField(DummyProjectTable.class.getDeclaredField("user_id")));

			assertTrue(meta.getAllFields().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	@Test
	public void testGetAllFieldsMissingPrimaryKey() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyNoPKTable.class);

		try {
			meta.getColumns();
		} catch (RuntimeException e) {
		}
		try {
			meta.getPrimaryKey();
		} catch (RuntimeException e) {
		}
		try {
			meta.getForeignKeys();
		} catch (RuntimeException e) {
		}

		try {
			List<GenericField> columns = Arrays.asList(
					new ColumnField(DummyNoPKTable.class.getDeclaredField("username")),
					new ForeignKeyField(DummyNoPKTable.class.getDeclaredField("id")));

			assertTrue(meta.getAllFields().containsAll(columns));
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new AssertionError("Failed testGetColumnsSimple");
		}
	}

	// getSimpleClassName
	@Test
	public void testGetSimpleClassName() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		assertEquals(meta.getSimpleClassName(), "DummyTable");
	}

	// getClassName
	@Test
	public void testGetClassName() {
		MetaModel<Class<?>> meta = MetaModel.of(DummyTable.class);

		assertEquals(meta.getClassName(), "DummyTable");
	}
}

class DummyNotTable {
}

@Entity
class DummyPKOnlyTable {
	@Id
	private int id;
}

@Entity
class DummyNoPKTable {
	@JoinColumn
	private int id;

	@Column
	private String username;
}

@Entity(tableName = "dummy_table")
class DummyTable {

	@Id(columnName = "ID")
	private int id;

	@Column(columnName = "First_name")
	private String first_name;

	private String last_name;

	@Exclude
	private String favorite_color;

	@Check("? < 1000000000000")
	@Check("? > 0")
	private double balance;
}

@Entity
class DummyProjectTable {
	@Id
	private int id;

	@JoinColumn(columnName = "user_Id", mappedByColumn = "id", mappedByTable = "dummy_table")
	private int user_id;

}
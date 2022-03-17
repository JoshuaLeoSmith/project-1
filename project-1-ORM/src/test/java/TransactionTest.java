import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Savepoint;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.postgresql.jdbc.PSQLSavepoint;

import com.revature.dao.TransactionDao;
import com.revature.exceptions.SavePointNotFoundException;
import com.revature.exceptions.TransactionClosedException;
import com.revature.exceptions.TransactionException;
import com.revature.service.Transaction;

public class TransactionTest {
	private TransactionDao mockDao;

	@Before
	public void setup() {
		mockDao = mock(TransactionDao.class);
	}

	@After
	public void teardown() {
		mockDao = null;
	}

	// constructor exception
	@Test
	public void testCreateTransaction() {
		doNothing().when(mockDao).begin();

		Transaction trans = new Transaction(mockDao);
		assertFalse(trans.isClosed());
	}

	@Test(expected = TransactionException.class)
	public void testCreateTransactionFailed() {
		doThrow(new TransactionException()).when(mockDao).begin();

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
	}

	// getTransactionIsolation
	@Test
	public void testGetTransactionIsolation() {
		doNothing().when(mockDao).begin();
		when(mockDao.getTransactionIsolation()).thenReturn(Transaction.READ_COMMITED);

		Transaction trans = new Transaction(mockDao);
		assertEquals(trans.getTransactionIsolation(), Transaction.READ_COMMITED);
	}

	@Test(expected = TransactionException.class)
	public void testGetTransactionIsolationFailed() {
		doNothing().when(mockDao).begin();
		when(mockDao.getTransactionIsolation()).thenThrow(TransactionException.class);

		Transaction trans = new Transaction(mockDao);
		trans.getTransactionIsolation();
	}

	@Test(expected = TransactionClosedException.class)
	public void testGetTransactionIsolationClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).rollback();
		doNothing().when(mockDao).end();
		when(mockDao.getTransactionIsolation()).thenReturn(Transaction.READ_COMMITED);

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
		trans.getTransactionIsolation();
	}

	// setTransactionIsolation
	@Test
	public void testSetTransactionIsolation() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).setTransactionIsolation(anyInt());

		Transaction trans = new Transaction(mockDao);
		trans.setTransactionIsolation(Transaction.SERIALIZABLE);
	}

	@Test(expected = TransactionException.class)
	public void testSetTransactionIsolationFailed() {
		doNothing().when(mockDao).begin();
		doThrow(new TransactionException()).when(mockDao).setTransactionIsolation(anyInt());

		Transaction trans = new Transaction(mockDao);
		trans.setTransactionIsolation(Transaction.SERIALIZABLE);
	}

	@Test(expected = TransactionClosedException.class)
	public void testSetTransactionIsolationClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).rollback();
		doNothing().when(mockDao).end();
		doNothing().when(mockDao).setTransactionIsolation(anyInt());

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
		trans.setTransactionIsolation(Transaction.SERIALIZABLE);
	}

	// commit
	@Test
	public void testCommit() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.commit();
	}

	@Test(expected = TransactionException.class)
	public void testCommitFailed() {
		doNothing().when(mockDao).begin();
		doThrow(new TransactionException()).when(mockDao).commit();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.commit();
	}

	@Test(expected = TransactionClosedException.class)
	public void testCommitClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.commit();
		trans.commit();
	}

	// rollback
	@Test
	public void testRollback() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).rollback();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
	}

	@Test(expected = TransactionException.class)
	public void testRollbackFailed() {
		doNothing().when(mockDao).begin();
		doThrow(new TransactionException()).when(mockDao).rollback();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
	}

	@Test(expected = TransactionClosedException.class)
	public void testRollbackClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.rollback();
		trans.rollback();
	}

	// rollback(String)
	@Test(expected = SavePointNotFoundException.class)
	public void testRollbackToNonexistantSavepoint() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).rollback(isA(Savepoint.class));
		doNothing().when(mockDao).end();

		Transaction trans = new Transaction(mockDao);
		trans.rollback("test");
	}
	
	@Test
	public void testRollbackToExistingSavepoint() {
		doNothing().when(mockDao).begin();
		when(mockDao.setSavepoint(anyString())).thenReturn(new PSQLSavepoint("test"));
		doNothing().when(mockDao).rollback(isA(Savepoint.class));
		
		Transaction trans = new Transaction(mockDao);
		trans.createSavepoint("test");
		trans.rollback("test");
	}
	
	@Test(expected = TransactionClosedException.class)
	public void testRollbackToSavepointClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).rollback(isA(Savepoint.class));
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();
		
		Transaction trans = new Transaction(mockDao);
		trans.commit();
		trans.rollback("test");
	}
	
	// destroySavepoint
	@Test(expected = SavePointNotFoundException.class)
	public void testDestroyNonExistentSavepoint() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).releaseSavepoint(anyString(), isA(Savepoint.class));

		Transaction trans = new Transaction(mockDao);
		trans.destroySavepoint("test");
	}
	
	@Test
	public void testDestroySavepoint() {
		doNothing().when(mockDao).begin();
		when(mockDao.setSavepoint(anyString())).thenReturn(new PSQLSavepoint("test"));
		doNothing().when(mockDao).releaseSavepoint(anyString(), isA(Savepoint.class));
		
		Transaction trans = new Transaction(mockDao);
		trans.createSavepoint("test");
		trans.destroySavepoint("test");
	}
	
	@Test(expected = TransactionClosedException.class)
	public void testDestroySavepointClosed() {
		doNothing().when(mockDao).begin();
		doNothing().when(mockDao).releaseSavepoint(anyString(), isA(Savepoint.class));
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();
		
		Transaction trans = new Transaction(mockDao);
		trans.commit();
		trans.destroySavepoint("test");
	}
	
	@Test(expected = TransactionClosedException.class)
	public void testCreateSavepointClosed() {
		doNothing().when(mockDao).begin();
		when(mockDao.setSavepoint(anyString())).thenReturn(new PSQLSavepoint("test"));
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();
		
		Transaction trans = new Transaction(mockDao);
		trans.commit();
		trans.createSavepoint("test");
	}
	
	// getSavepoints
	@Test
	public void testGetSavepoints() {
		Set<String> test = new HashSet<>();
		
		doNothing().when(mockDao).begin();
		when(mockDao.setSavepoint(anyString())).thenReturn(new PSQLSavepoint("test"));
		
		Transaction trans = new Transaction(mockDao);
		trans.createSavepoint("test");
		test.add("test");
		trans.createSavepoint("sql");
		test.add("sql");
		trans.createSavepoint("yes");
		test.add("yes");
		
		assertTrue(trans.getSavepoints().containsAll(test));
	}
	
	@Test
	public void testGetSavepointsClosed() {
		doNothing().when(mockDao).begin();
		when(mockDao.setSavepoint(anyString())).thenReturn(new PSQLSavepoint("test"));
		doNothing().when(mockDao).commit();
		doNothing().when(mockDao).end();
		
		Transaction trans = new Transaction(mockDao);
		trans.commit();
		assertEquals(trans.getSavepoints(), new HashSet<String>());
	}
}

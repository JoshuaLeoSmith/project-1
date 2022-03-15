import java.io.FileNotFoundException;

import org.junit.Test;

import com.revature.Management;
import com.revature.util.Configuration;

public class ConfigurationTest {
	@Test
	public void testDefaultConstructor() {
		try {
			Configuration config = new Configuration();
		} catch (FileNotFoundException e) {
			throw new AssertionError("unable to open the default file");
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUserDefinedConfigNoParameters() {
		try {
			Configuration config = new Configuration("./src/test/resources/");
		} catch (FileNotFoundException e) {
			throw new AssertionError("unable to open the default file");
		}
	}
	
	@Test(expected=FileNotFoundException.class)
	public void testUserDefinedConfigInvalid() throws FileNotFoundException {
		Configuration config = new Configuration("./src/main/");
	}
	
	@Test(expected=IllegalStateException.class)
	public void testUserNonFileMissingTwo() {
		Configuration config = new Configuration("jdbc:postgresql://team-5-ent.cvtq9j4axrge.us-east-1.rds.amazonaws.com:5432/postgres", null, "postgres", "", Management.create);
	}
	
	@Test
	public void testUserNonFileValid() {
		Configuration config = new Configuration("jdbc:postgresql://team-5-ent.cvtq9j4axrge.us-east-1.rds.amazonaws.com:5432/postgres", "test0", "postgres", "postgres", Management.create);
	}
}
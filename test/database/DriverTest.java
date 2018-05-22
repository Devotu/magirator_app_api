package drivers;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

public class DriverTest {
    
    @Test 
    public void driverConnection() {
        Logger.debug("Testing db driver connection");

        Neo4jDriver db = new Neo4jDriver();

        int uniqueId = 0;

        try {
            uniqueId = db.getUniqueId(db);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(uniqueId > 0);
    }    
}
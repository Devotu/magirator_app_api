package drivers;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

public class UniqueIdTest {
    
    @Test 
    public void idGeneration() {
        Logger.debug("Testing db driver unique id function");

        Neo4jDriver db = Neo4jDriverFactory.getDriver();

        int uniqueId1 = 0;
        int uniqueId2 = 0;
        int uniqueId3 = 0;

        int uniqueId = 0;

        try {
            uniqueId1 = db.getUniqueId(db);
            uniqueId2 = db.getUniqueId(db);
            uniqueId3 = db.getUniqueId(db);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(uniqueId1 > 0);
        assertTrue(uniqueId2 > uniqueId1);
        assertTrue(uniqueId3 > uniqueId2);
    }    
}
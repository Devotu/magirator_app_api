package model;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import java.util.Date;

public class ExtensionTest {
    
    @Test 
    public void modelAttributes() {
        Logger.debug("Testing Model + extended class attributes");
    
        Long id = 1L;
        Date created = new Date();
        String name = "Name";
        String password = "Pwd";
    
        User user = new User();

        user.id = id;
        user.created = created;
        user.name = name;
        user.password = password;
    
        assertEquals((Long)id, (Long)user.id);
        assertEquals(created, user.created);
        assertEquals(ModelEditType.PERSISTENT, user.editType);
        assertEquals(name, user.name);
        assertEquals(password, user.password);
    }    
}
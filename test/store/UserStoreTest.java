package stores;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import model.*;
import transfers.*;
import drivers.*;

public class UserStoreTest {

    @Test
    public void user(){

        //Creating
        UserStore userStore = new UserStore();
        User inputUser = new User();
        String userName = "Anna";
        String userPassword = "Hemligt";

        inputUser.name = userName;
        inputUser.password = userPassword;

        Neo4jDriver db = new Neo4jDriver();

        Parcel created = userStore.create(null, inputUser, db);

        Status status = created.status;
        User createdUser = (User) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdUser != null);
        assertTrue(createdUser.id > 0);
        assertTrue(userName.equals(createdUser.name));


        //Reading
        Parcel createdParcel = userStore.read(createdUser.id, db);
        User readUser = (User) createdParcel.payload;

        if (readUser == null) {
            Logger.debug("User is null");
        }

        Logger.debug("User read: " + readUser.id + ", " + readUser.name);

        assertEquals(Status.OK, status);
        assertEquals((Long)createdUser.id, (Long)readUser.id);
        assertEquals(inputUser.name, readUser.name);
        assertEquals(inputUser.password, readUser.password);


        //Updating
        User updatingUser = inputUser;
        updatingUser.name = "Beata";
        updatingUser.password = "Jättehemligt";
        Parcel updatedParcel = userStore.update(updatingUser, db);

        boolean userUpdated = (Boolean) updatedParcel.payload;

        assertTrue(userUpdated);

        Parcel updatedReadParcel = userStore.read(updatingUser.id, db);
        User updatedUser = (User) updatedReadParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Long)readUser.id, (Long)updatedUser.id);
        assertEquals(updatingUser.name, updatedUser.name);
        assertEquals(updatingUser.password, updatedUser.password);


        //Deleting
        Parcel deletedParcel = userStore.delete(readUser.id, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }

}
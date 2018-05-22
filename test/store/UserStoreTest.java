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
        Long createdUserId = (Long) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdUserId > 0);


        //Reading
        Parcel createdParcel = userStore.read(createdUserId, db);
        User createdUser = (User) createdParcel.payload;

        if (createdUser == null) {
            Logger.debug("User is null");
        }

        Logger.debug("User read: " + createdUser.id + ", " + createdUser.name);

        assertEquals(Status.OK, status);
        assertEquals((Long)createdUserId, (Long)createdUser.id);
        assertEquals(inputUser.name, createdUser.name);
        assertEquals(inputUser.password, createdUser.password);


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
        assertEquals((Long)createdUserId, (Long)updatedUser.id);
        assertEquals(updatingUser.name, updatedUser.name);
        assertEquals(updatingUser.password, updatedUser.password);


        //Deleting
        Parcel deletedParcel = userStore.delete(createdUserId, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }

}
package stores;

import model.*;
import transfers.*;
import drivers.*;
import test.*;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

public class PlayerStoreTest {

    @Test
    public void player(){

        Neo4jDriver db = Neo4jDriverFactory.getDriver();

        //Fetching Anchor 
        UserStore userStore = new UserStore();
        Parcel anchorParcel = userStore.read(TestVariables.USER_2_ID, db);
        User anchorUser = (User) anchorParcel.payload;

        //Creating
        PlayerStore playerStore = new PlayerStore();
        Player inputPlayer = new Player();
        String playerName = "Catrine";

        inputPlayer.name = playerName;

        Parcel created = playerStore.create(anchorUser, inputPlayer, db);

        Status status = created.status;
        Long createdPlayerId = (Long) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdPlayerId > 0);


        //Reading
        Parcel createdParcel = playerStore.read(createdPlayerId, db);
        Player createdPlayer = (Player) createdParcel.payload;

        Logger.debug("Player read: " + createdPlayer.id + ", " + createdPlayer.name);

        assertEquals(Status.OK, status);
        assertEquals((Long)createdPlayerId, (Long)createdPlayer.id);
        assertEquals(inputPlayer.name, createdPlayer.name);


        //Updating
        Player updatingPlayer = inputPlayer;
        updatingPlayer.name = "Denise";
        Parcel updatedParcel = playerStore.update(updatingPlayer, db);

        boolean playerUpdated = (Boolean) updatedParcel.payload;

        assertTrue(playerUpdated);

        Parcel updatedReadParcel = playerStore.read(updatingPlayer.id, db);
        Player updatedPlayer = (Player) updatedReadParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Long)createdPlayerId, (Long)updatedPlayer.id);
        assertEquals(updatingPlayer.name, updatedPlayer.name);


        //Deleting
        Parcel deletedParcel = playerStore.delete(createdPlayerId, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }


    @Test
    public void listPlayers(){

        Neo4jDriver db = Neo4jDriverFactory.getDriver();

        PlayerStore playerStore = new PlayerStore();

        Parcel created = playerStore.list( db );

        Status status = created.status;
        List<?> players = (List<?>) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(players != null);
        assertTrue(players.size() > 0);
    }
}
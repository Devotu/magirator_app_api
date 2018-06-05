package stores;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import model.*;
import transfers.*;
import drivers.*;
import test.*;

import java.util.*;

public class GameStoreTest {

    @Test
    public void CRuD(){

        Neo4jDriver db = new Neo4jDriver();

        //Fetching Anchor 
        PlayerStore playerStore = new PlayerStore();
        Parcel anchorParcel = playerStore.read(TestVariables.PLAYER_1_ID, db);
        Player anchorPlayer = (Player) anchorParcel.payload;

        //Creating
        GameStore gameStore = new GameStore();
        Game inputGame = new Game();

        inputGame.assignNew(
            GameEnd.VICTORY
        );

        Parcel created = gameStore.create(anchorPlayer, inputGame, db);

        Status status = created.status;
        Game createdGame = (Game) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdGame.id > 0);
        assertTrue(inputGame.end == createdGame.end);


        //Reading
        Parcel readParcel = gameStore.read(createdGame.id, db);
        Game readGame = (Game) readParcel.payload;

        Logger.debug("Game read: " + readGame.id);

        assertEquals(Status.OK, status);
        assertEquals(createdGame.id, readGame.id);


        //Deleting
        Parcel deletedParcel = gameStore.delete(createdGame.id, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }
}
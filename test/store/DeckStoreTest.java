package stores;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import model.*;
import transfers.*;
import drivers.*;
import test.*;

import java.util.Date;

public class DeckStoreTest {

    @Test
    public void deck(){

        Neo4jDriver db = new Neo4jDriver(true);

        //Fetching Anchor 
        PlayerStore playerStore = new PlayerStore();
        Parcel anchorParcel = playerStore.read(TestVariables.PLAYER_1_ID, db);
        Player anchorPlayer = (Player) anchorParcel.payload;

        //Creating
        DeckStore deckStore = new DeckStore();
        Deck inputDeck = new Deck();

        inputDeck.assignAll(
            0, new Date(), "Apor", DeckFormat.STANDARD, "Created by DeckStoreTest.deck",
            true, false, true, false, true, false
        );

        Parcel created = deckStore.create(anchorPlayer, inputDeck, db);

        Status status = created.status;
        Long createdDeckId = (Long) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdDeckId > 0);


        //Reading
        Parcel createdParcel = deckStore.read(createdDeckId, db);
        Deck createdDeck = (Deck) createdParcel.payload;

        Logger.debug("Deck read: " + createdDeck.id + ", " + createdDeck.name);

        assertEquals(Status.OK, status);
        assertEquals((Long)createdDeckId, (Long)createdDeck.id);
        assertEquals(inputDeck.name, createdDeck.name);
        assertEquals(inputDeck.format, createdDeck.format);
        assertEquals(inputDeck.black, createdDeck.black);
        assertEquals(inputDeck.green, createdDeck.green);
        assertTrue(null != createdDeck.created);
        assertTrue(null != inputDeck.created);


        //Updating
        Deck updatingDeck = inputDeck;
        updatingDeck.name = "Bävrar";
        updatingDeck.format = DeckFormat.MODERN;
        updatingDeck.black = true;
        updatingDeck.green = true;

        Parcel updatedParcel = deckStore.update(updatingDeck, db);

        boolean deckUpdated = (Boolean) updatedParcel.payload;

        assertTrue(deckUpdated);

        Parcel updatedReadParcel = deckStore.read(updatingDeck.id, db);
        Deck updatedDeck = (Deck) updatedReadParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Long)createdDeckId, (Long)updatedDeck.id);
        assertEquals(updatingDeck.name, updatedDeck.name);
        assertEquals(updatingDeck.format, updatedDeck.format);
        assertEquals(updatingDeck.black, updatedDeck.black);
        assertEquals(updatingDeck.green, updatedDeck.green);

        //Deleting
        Parcel deletedParcel = deckStore.delete(createdDeckId, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }

}
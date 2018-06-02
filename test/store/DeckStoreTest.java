package stores;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import model.*;
import transfers.*;
import drivers.*;
import test.*;

import java.util.*;

public class DeckStoreTest {

    @Test
    public void CRUD(){

        Neo4jDriver db = new Neo4jDriver();

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
        Deck createdDeck = (Deck) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(createdDeck.id > 0);


        //Reading
        Parcel readParcel = deckStore.read(createdDeck.id, db);
        Deck readDeck = (Deck) readParcel.payload;

        Logger.debug("Deck read: " + readDeck.id + ", " + readDeck.name);

        assertEquals(Status.OK, status);
        assertEquals(createdDeck.id, readDeck.id);
        assertEquals(inputDeck.name, readDeck.name);
        assertEquals(inputDeck.format, readDeck.format);
        assertEquals(inputDeck.black, readDeck.black);
        assertEquals(inputDeck.green, readDeck.green);
        assertTrue(null != readDeck.created);
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
        assertEquals(createdDeck.id, updatedDeck.id);
        assertEquals(updatingDeck.name, updatedDeck.name);
        assertEquals(updatingDeck.format, updatedDeck.format);
        assertEquals(updatingDeck.black, updatedDeck.black);
        assertEquals(updatingDeck.green, updatedDeck.green);

        //Deleting
        Parcel deletedParcel = deckStore.delete(createdDeck.id, db);
        boolean deleted = (Boolean) deletedParcel.payload;

        assertEquals(Status.OK, status);
        assertEquals((Boolean)true, (Boolean)deleted);
    }

    @Test
    public void listDecks(){

        Neo4jDriver db = new Neo4jDriver();

        //Listing
        DeckStore deckStore = new DeckStore();

        Parcel created = deckStore.listByPlayer( TestVariables.PLAYER_1_ID, db );

        Status status = created.status;
        List<?> decks = (List<?>) created.payload;

        assertEquals(Status.OK, status);
        assertTrue(decks != null);
        assertTrue(decks.size() > 0);
    }

}
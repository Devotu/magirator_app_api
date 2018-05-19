package model;

import static org.junit.Assert.*;
import org.junit.Test;
import play.Logger;

import java.util.Date;

/**
 * Each model object class is tested to be consistent with the 
 * model extension specified in the design contract
 */
public class ObjectTest {
    
    @Test 
    public void objects() {
        Logger.debug("Testing Objects");

        User user = new User();
        assertEquals(ModelEditType.PERSISTENT, user.editType);
        assertEquals(0L, user.id);

        Player player = new Player();
        assertEquals(ModelEditType.PERSISTENT, player.editType);
        assertEquals(0L, player.id);

        Token token = new Token();
        assertEquals(ModelEditType.FINAL, token.editType);
        assertEquals(0L, token.id);

        Settings settings = new Settings();
        assertEquals(ModelEditType.PERSISTENT, settings.editType);
        assertEquals(0L, settings.id);

        Deck deck = new Deck();
        assertEquals(ModelEditType.PERSISTENT, deck.editType);
        assertEquals(0L, deck.id);

        Game game = new Game();
        assertEquals(ModelEditType.FINAL, game.editType);
        assertEquals(0L, game.id);  

        Result result = new Result();
        assertEquals(ModelEditType.COMPONENT, result.editType);
        assertEquals(0L, result.id);

        Life life = new Life();
        assertEquals(ModelEditType.COMPONENT, life.editType);
        assertEquals(0L, life.id); 

        Death death = new Death();
        assertEquals(ModelEditType.COMPONENT, death.editType);
        assertEquals(0L, death.id); 

        Tag tag = new Tag();
        assertEquals(ModelEditType.FINAL, tag.editType);
        assertEquals(0L, tag.id);

        Rating rating = new Rating();
        assertEquals(ModelEditType.FINAL, rating.editType);
        assertEquals(0L, rating.id);
    }    
}
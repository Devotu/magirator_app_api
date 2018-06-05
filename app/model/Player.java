package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Player extends Model {

    public ModelEditType editType = ModelEditType.PERSISTENT;

    public String name;
    public List<Long> deckIds = new ArrayList<>();

    public void assignNew ( String name ){
        this.name = name;
    }

    public void assignAll ( long id, Date created, String name, List deckIds ){
        this.id = id;
        this.created = created;
        this.name = name;
        this.deckIds = deckIds;
    }
}
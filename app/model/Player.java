package model;

import java.util.Date;

public class Player extends Model {

    public ModelEditType editType = ModelEditType.PERSISTENT;

    public String name;

    public void assignAll (long id, Date created, String name){
        this.id = id;
        this.created = created;
        this.name = name;
    }
}
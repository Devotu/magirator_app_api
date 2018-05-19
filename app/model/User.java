package model;

import java.util.Date;

public class User extends Model {

    public ModelEditType editType = ModelEditType.PERSISTENT;

    public String name;
    public String password;

    public void assignAll (long id, Date created, String name, String password){
        this.id = id;
        this.created = created;
        this.name = name;
        this.password = password;
    }
}
package model;

import java.util.Date;

public class Deck extends Model {

    public ModelEditType editType = ModelEditType.PERSISTENT;

    public String name;
    public DeckFormat format;
    public String theme;
    public boolean black;
    public boolean white;
    public boolean red;
    public boolean green;
    public boolean blue;
    public boolean colorless;

    public void assignAll (long id, Date created, String name, DeckFormat format, String theme, 
            boolean black, boolean white, boolean red, boolean green, boolean blue, boolean colorless){
        this.id = id;
        this.created = created;
        this.name = name;
        this.format = format;
        this.theme = theme;
        this.black = black;
        this.white = white;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.colorless = colorless;
    }
}
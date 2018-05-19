package model;

import java.util.List;

public class Result extends Model {

    public ModelEditType editType = ModelEditType.COMPONENT;

    public int place;
    public String comment;
    public List<Life> lifeLog;
    public Death death;
}
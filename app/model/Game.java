package model;

import java.util.List;

public class Game extends Model {

    public ModelEditType editType = ModelEditType.FINAL;

    public GameEnd end;
    public List<Result> results;
}
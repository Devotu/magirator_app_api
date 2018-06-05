package model;

import java.util.*;

public class Game extends Model {

    public ModelEditType editType = ModelEditType.FINAL;

    public GameEnd end;
    public List<Long> resultIds = new ArrayList<>();

    public void assignNew ( GameEnd end ) throws IllegalArgumentException {
        
        this.end = end;

        validateFields();
    }
    
    @SuppressWarnings("unchecked")
    public void assignAll ( long id, Date created, GameEnd end, List resultIds ) throws IllegalArgumentException {
        
        this.id = id;
        this.created = created;
        this.end = end;
        this.resultIds = resultIds;

        validateFields();
    }

    private void validateFields() throws IllegalArgumentException {
        //No (identified) Possible mismatches due to circumstances such as player input.
    }
}
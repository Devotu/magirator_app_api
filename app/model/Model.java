package model;

import java.util.Date;

/**
 * Base class that must be extended by all model package classes.
 */
public class Model {

    public long id;
    public Date created;
    public ModelEditType editType;
}
package stores;

import model.*;
import transfers.*;
import drivers.*;

public interface Store <A extends Model, M extends Model>{
    
    public Parcel create(A anchor, M model, Neo4jDriver db);
    public Parcel read(long id, Neo4jDriver db);
    public Parcel update(M model, Neo4jDriver db);
    public Parcel delete(long id, Neo4jDriver db);
}
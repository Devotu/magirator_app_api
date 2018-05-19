package transfers;

/**
 * Parcel is a package to be a standard response from all methods
 */
public class Parcel {

    public final Status status;
    public final String message;
    public final Object payload;

    public Parcel(Status status, String message, Object payload){
        this.status = status;
        this.message = message;
        this.payload = payload;
    }
}
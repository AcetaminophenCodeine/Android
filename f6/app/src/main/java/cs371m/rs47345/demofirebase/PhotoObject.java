package cs371m.rs47345.demofirebase;

/**
 * Created by witchel on 11/5/16.
 */

// How to define a Java object that Firebase can store
// https://www.firebase.com/docs/java-api/javadoc/com/firebase/client/Firebase.html#setValue-java.lang.Object-
public class PhotoObject {
    public String name;
    public String comment;
    public Long date;
    public String encodedBytes; // This is large, it should use a file

    public PhotoObject ()
    {

    }

    public String getName() { return name; }
    public String getComment() { return comment; }
    public Long getDate() { return date; }
    public String getEncodedBytes(){ return encodedBytes;}
}

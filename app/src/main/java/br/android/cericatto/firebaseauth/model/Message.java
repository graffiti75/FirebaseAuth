package br.android.cericatto.firebaseauth.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Message.java.
 *
 * @author Rodrigo Cericatto
 * @since Jan 28, 2017
 */
public class Message {

    //--------------------------------------------------
    // Attributes
    //--------------------------------------------------

    private String uid;
    private String user;
    private String text;

    //--------------------------------------------------
    // To String
    //--------------------------------------------------

    @Override
    public String toString() {
        return "Message{" +
            "uid='" + uid + '\'' +
            ", user='" + user + '\'' +
            ", text='" + text + '\'' +
            '}';
    }

    //--------------------------------------------------
    // To Map
    //--------------------------------------------------

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("text", text);
        result.put("user", user);

        return result;
    }

    //--------------------------------------------------
    // Getters and Setters
    //--------------------------------------------------

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
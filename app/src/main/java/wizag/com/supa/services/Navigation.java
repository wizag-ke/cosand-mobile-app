package wizag.com.supa.services;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Navigation {

    public String destination;
    public String currentPosition;

    public Navigation() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Navigation(String destination, String currentPosition) {
        this.destination = destination;
        this.currentPosition = currentPosition;
    }

}




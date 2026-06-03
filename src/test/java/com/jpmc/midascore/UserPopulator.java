package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import org.springframework.stereotype.Component;

@Component
public class UserPopulator {
    
    private final FileLoader fileLoader;
    private final DatabaseConduit databaseConduit;

    public UserPopulator(FileLoader fileLoader, DatabaseConduit databaseConduit) {
        this.fileLoader = fileLoader;
        this.databaseConduit = databaseConduit;
    }

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        
        if (userLines == null) {
            return;
        }

        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");
            if (userData.length >= 2) { 
                try {
                    UserRecord user = new UserRecord(userData[0], Float.parseFloat(userData[1]));
                    databaseConduit.save(user);
                } catch (NumberFormatException e) {
                    // Gracefully handle parsing failures for individual malformed lines
                }
            }
        }
    }
}

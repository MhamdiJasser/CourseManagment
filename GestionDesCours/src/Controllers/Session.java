package Controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Session 
{
    private static final String SESSION_FILE = "session.txt";

    public static void saveSession(int ncin, String role, String username)
    {
        String content = "NCIN=" + ncin + "\n" + "role=" + role + "\n" + "username=" + username;
        try 
        {
            Files.write(Path.of(SESSION_FILE), content.getBytes());
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public static Map<String, String> loadSession() 
    {
        File file = new File(SESSION_FILE);
        if (!file.exists()) return null;

        Map<String, String> sessionData = new HashMap<>();
        try 
        {
            List<String> lines = Files.readAllLines(Path.of(SESSION_FILE));
            for (String line : lines) 
            {
                String[] parts = line.split("=");
                if (parts.length == 2)
                {
                    sessionData.put(parts[0], parts[1]);
                }
            }
            return sessionData;
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
            return null;
        }
    }


    public static void clearSession() 
    {
        File file = new File(SESSION_FILE);
        if (file.exists()) file.delete();
    }
}

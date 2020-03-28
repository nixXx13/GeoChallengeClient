package Common;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;

class ConnectionUtils {

    private static Gson gson = new Gson();
    private final static Logger logger = Logger.getLogger(ConnectionUtils.class);

    static void sendObjectOutputStream(ObjectOutputStream os, String json) throws IOException {
        logger.trace(String.format("Sending '%s'" ,json));
        os.writeObject(json);

        PrintStream ps = new PrintStream(os);
        if (ps.checkError()){
            throw new IOException("Error sending client with objectStream " + os.toString());
        }
    }

    static GameData read(ObjectInputStream is){
        GameData gameData = null;
        try {
            String s = (String) is.readObject();
            logger.trace(String.format("Received '%s'",s));
            gameData = gson.fromJson(s,GameData.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed reading from server");
        }
        return gameData;
    }

}

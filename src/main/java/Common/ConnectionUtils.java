package Common;

import GeoChallengeClient.GeoChallengeCoreImpl;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

import java.io.*;

public class ConnectionUtils {

    private static Gson gson = new Gson();
    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    public static String readBufferReader(BufferedReader br) throws IOException {
        return br.readLine();
    }

    public static void sendObjectOutputStream(ObjectOutputStream os, String json) throws IOException {
        os.writeObject(json);

        PrintStream ps = new PrintStream(os);
        if (ps.checkError()){
            throw new IOException("Error sending client with objectStream " + os.toString());
        }
    }

    public static GameData read(ObjectInputStream is){
        GameData gameData = null;
        try {
            String s = (String) is.readObject();
            gameData = gson.fromJson(s,GameData.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Failed reading from server");
        }
        return gameData;
    }

    public static void sendString(OutputStreamWriter os,String s) throws IOException {
        os.write(s + "\n");
        os.flush();
    }

}

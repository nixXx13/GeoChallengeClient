package GeoChallengeClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Common.GameData;
import com.google.gson.Gson;
import org.apache.log4j.Logger;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    // refactor to clearer code
    // TODO - add UT

    private Socket socket;
    private OutputStreamWriter os;
    private ObjectInputStream is;
    private List<IResponseHandler> handlers;
    private boolean connected;
    private Gson gson;

    public GeoChallengeCoreImpl(Socket socket, OutputStreamWriter os, ObjectInputStream is){
        this.socket = socket;
        this.os = os;
        this.is = is;
        handlers = new ArrayList<IResponseHandler>();
        gson = new Gson();
        connected = true;
    }

    private GameData read(){
        GameData gameData = null;
        try {
            String s = (String) is.readObject();
            gameData = gson.fromJson(s,GameData.class);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Failed reading from server");
            terminateConnection();
        }
        return gameData;
    }

    @Override
    public void send(String s) {
        if(connected) {
            logger.debug(String.format("Sending '%s' to server",s));
            try {
                os.write(s + "\n");
                os.flush();
            } catch (IOException e) {
                logger.error("Failed sending to server");
                terminateConnection();
            }
        }
        //TODO - add not connected msg
    }

    private void updateHandlers(GameData gameData){
        if(handlers.size()>0) {
            logger.debug(String.format("updating handlers with response type '%s' and content '%s'",
                    gameData.getType(),gameData.getContent() ));
            for (IResponseHandler rh : handlers) {
                rh.handle(gameData);
            }
        }
        else {
            logger.info("no handlers registered");
        }
    }

    @Override
    public void registerHandler(IResponseHandler responseHandler) {
        if(responseHandler!=null) {
            handlers.add(responseHandler);
        }
    }

    @Override
    public void run() {
        GameData gameData = read();
        while ( gameData != null ){
            updateHandlers(gameData);
            if ( gameData.getType() == GameData.GameDataType.END){
                // use end msg before quitting
                send("end");
                terminateConnection();
                break;
            }
            gameData = read();
        }
        logger.info("game finished");
    }

    private void terminateConnection(){
        try {
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            logger.error("Failed closing connection");
        }
        connected = false;
    }
}

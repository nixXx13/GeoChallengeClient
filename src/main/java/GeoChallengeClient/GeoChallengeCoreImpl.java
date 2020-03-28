package GeoChallengeClient;
import java.util.ArrayList;
import java.util.List;
import Common.GameData;
import Common.INetworkConnector;
import org.apache.log4j.Logger;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    private INetworkConnector serverConnector;
    private List<IResponseHandler> handlers;
    private boolean connected;

    protected GeoChallengeCoreImpl(INetworkConnector serverConnector){
        this.serverConnector = serverConnector;

        handlers = new ArrayList<IResponseHandler>();
        connected = true;
    }

    @Override
    public void send(GameData gameData) {
        if(!connected) {
            logger.warn("Connection to server isn't active");
            return;
        }
        logger.debug(String.format("Sending '%s' to server",gameData.toString()));
        if (!serverConnector.send(gameData)){
            logger.error("Failed sending to server");
            terminateConnection();
        }
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
        if (!serverConnector.init()) {
            logger.error("Failed initializing connection");
            return;
        }
        // TODO - add acks as constants
        updateHandlers(new GameData(GameData.GameDataType.ACK,"Connected"));
        GameData gameData = serverConnector.read();
        while (gameData != null) {
            updateHandlers(gameData);
            if (gameData.getType() == GameData.GameDataType.END) {
                // use end msg before quitting
                send(new GameData(GameData.GameDataType.END,"end"));
                break;
            }
            gameData = serverConnector.read();
        }
        if (gameData == null){
            String errorMsg = "Failed reading from server";
            GameData errorGameData = new GameData(GameData.GameDataType.ERROR,errorMsg);
            updateHandlers(errorGameData);
        }
        terminateConnection();
        logger.info("game finished");
    }


    private void terminateConnection(){
        serverConnector.terminate();
        connected = false;
    }
}

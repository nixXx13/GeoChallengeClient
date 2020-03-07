package GeoChallengeClient;
import java.util.ArrayList;
import java.util.List;
import Common.Converter;
import Common.GameData;
import org.apache.log4j.Logger;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    // refactor to clearer code
    // TODO - add UT

    private IServerConnector serverConnector;
    private List<IResponseHandler> handlers;
    private boolean connected;

    protected GeoChallengeCoreImpl(IServerConnector serverConnector){
        this.serverConnector = serverConnector;

        handlers = new ArrayList<IResponseHandler>();
        connected = true;
    }

    @Override
    public void send(GameData gameData) {
        if(connected) {
            logger.debug(String.format("Sending '%s' to server",gameData.toString()));
            if (!serverConnector.send(Converter.toJson(gameData))){
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
        if (!serverConnector.init()) {
            logger.error("Failed initializing connection");
            return;
        }
        GameData gameData = serverConnector.read();
        while (gameData != null) {
            updateHandlers(gameData);
            if (gameData.getType() == GameData.GameDataType.END) {
                // use end msg before quitting
                send(new GameData(GameData.GameDataType.END,"end"));
                terminateConnection();
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

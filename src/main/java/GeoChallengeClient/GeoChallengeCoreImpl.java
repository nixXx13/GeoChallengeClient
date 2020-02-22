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
import Common.ConnectionUtils;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    // refactor to clearer code
    // TODO - add UT

    private String serverIp;
    private int port;
    private Socket socket;
    private OutputStreamWriter os;
    private ObjectInputStream is;
    private List<IResponseHandler> handlers;
    private boolean connected;
    private Gson gson;

    protected GeoChallengeCoreImpl(String serverIp, int port){
        this.serverIp = serverIp;
        this.port = port;
        handlers = new ArrayList<IResponseHandler>();
        gson = new Gson();
        connected = true;
    }

    @Override
    public void send(String s) {
        if(connected) {
            logger.debug(String.format("Sending '%s' to server",s));
            try {
                ConnectionUtils.sendString(os,s);
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
        if (initConnection()) {
            GameData gameData = ConnectionUtils.read(is);
            while (gameData != null) {
                updateHandlers(gameData);
                if (gameData.getType() == GameData.GameDataType.END) {
                    // use end msg before quitting
                    send("end");
                    terminateConnection();
                    break;
                }
                gameData = ConnectionUtils.read(is);
            }
            terminateConnection();
            logger.info("game finished");
        }
    }

    private boolean initConnection() {
        try {
            this.socket = new Socket(serverIp, port);
            logger.debug(String.format("Socket to server ip %s:%d is initialized",serverIp,port));
            this.os = new OutputStreamWriter(socket.getOutputStream());
            logger.debug("output stream initialized");
            this.is = new ObjectInputStream(socket.getInputStream());
            logger.debug("input stream initialized");
        } catch (IOException e) {
            logger.error("Failed initializing connection");
            e.printStackTrace();
            return false;
        }
        return true;
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

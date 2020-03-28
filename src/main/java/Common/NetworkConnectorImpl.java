package Common;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class NetworkConnectorImpl implements INetworkConnector {

    private final static Logger logger = Logger.getLogger(NetworkConnectorImpl.class);

    private String ip;
    private int port;

    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    public NetworkConnectorImpl(String ip, int port){
        this.ip = ip;
        this.port = port;
        this.socket = null;
    }

    public NetworkConnectorImpl(Socket socket){
        this.socket = socket;
    }

    public boolean init(){

        try {
            if(socket == null){
                this.socket = new Socket(ip, port);
                logger.debug(String.format("Socket to server ip %s:%d is initialized",ip,port));
            }
            this.os = new ObjectOutputStream(socket.getOutputStream());
            logger.trace("output stream initialized");
            this.is = new ObjectInputStream(socket.getInputStream());
            logger.trace("input stream initialized");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("failed initializing Server connector with ip-'%s:%d'",ip,port));
            return false;
        }
        return true;
    }

    public void terminate(){
        try {
            os.close();
            is.close();
            socket.close();
        } catch (IOException e) {
            logger.error("Failed closing connection");
        }
    }

    public GameData read(){
        return ConnectionUtils.read(is);
    }

    public boolean send(List<GameStage> gameStages) {
        String json = Converter.toJson(gameStages);
        return send(json);
    }

    public boolean send(GameData gameData){
        String json = Converter.toJson(gameData);
        return send(json);
    }

    private boolean send(String s) {
        try {
            ConnectionUtils.sendObjectOutputStream(os,s);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(String.format("failed sending to Server with ip-'%s:%d'",ip,port));
            return false;
        }
        return true;
    }

}

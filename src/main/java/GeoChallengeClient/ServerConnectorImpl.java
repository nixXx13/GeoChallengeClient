package GeoChallengeClient;

import Common.ConnectionUtils;
import Common.GameData;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnectorImpl implements IServerConnector{

    private final static Logger logger = Logger.getLogger(ServerConnectorImpl.class);

    private String ip;
    private int port;

    private Socket socket;
    private ObjectOutputStream os;
    private ObjectInputStream is;

    public ServerConnectorImpl(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    public boolean init(){
        try {
            this.socket = new Socket(ip, port);
            logger.debug(String.format("Socket to server ip %s:%d is initialized",ip,port));
            this.os = new ObjectOutputStream(socket.getOutputStream());
            logger.debug("output stream initialized");
            this.is = new ObjectInputStream(socket.getInputStream());
            logger.debug("input stream initialized");
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

    public boolean send(String s) {
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

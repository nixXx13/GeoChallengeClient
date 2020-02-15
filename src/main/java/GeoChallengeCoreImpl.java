import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private final static Logger logger = Logger.getLogger(GeoChallengeCoreImpl.class);

    // TODO - add socket to Impl so it can be closed
    // TODO - add UT

    private OutputStreamWriter os;
    private ObjectInputStream is;
    private List<IResponseHandler> handlers;
    private boolean connected;

    public GeoChallengeCoreImpl(OutputStreamWriter os, ObjectInputStream is){
        this.os = os;
        this.is = is;
        handlers = new ArrayList<IResponseHandler>();
        connected = true;
    }

    private String read(){
        String s = null;
        try {
            s = (String) is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            logger.error("Failed reading from server");
            terminateConnection();
        }
        return s;
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

    private void updateHandlers(String s){
        if(handlers.size()>0) {
            logger.debug("updating handlers with response " + s);
            for (IResponseHandler rh : handlers) {
                rh.handle(s);
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
        String s = read();
        while ( s != null ){
            updateHandlers(s);
            if ( s.contains("END")){
                send("end");
                terminateConnection();
                break;
            }
            s = read();
        }
        logger.info("game finished");
    }

    private void terminateConnection(){
        try {
            os.close();
            is.close();
        } catch (IOException e) {
            logger.error("Failed closing connection");
        }
        connected = false;
    }
}

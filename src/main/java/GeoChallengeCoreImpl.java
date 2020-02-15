import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GeoChallengeCoreImpl implements IGeoChallengeCore {

    private String serverIp;
    private Socket socket;
    private int port;
    private OutputStreamWriter os;
    private ObjectInputStream is;
    private List<IResponseHandler> handlers;
    private boolean connected = false;

    public GeoChallengeCoreImpl(OutputStreamWriter os, ObjectInputStream is){
        this.os = os;
        this.is = is;
        handlers = new ArrayList<>();
        connected = true;
    }

    private String read(){
        String s = null;
        try {
            s = (String) is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public void send(String s) {
        if(connected) {
            try {
                os.write(s + "\n");
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //TODO - add not connected msg
    }

    private void updateHandlers(String s){
        if(handlers.size()>0) {
            for (IResponseHandler rh : handlers) {
                rh.handle(s);
            }
        }
        else {
            System.out.println("INFO - no handlers registered");
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
                connected = false;
                break;
            }
            s = read();
        }
        System.out.println("EXITING");
    }
}

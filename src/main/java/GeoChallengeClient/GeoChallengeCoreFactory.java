package GeoChallengeClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GeoChallengeCoreFactory {

    public static IGeoChallengeCore getGeoChallengeCore(String serverIp, int port) throws IOException {

        Socket socket = new Socket(serverIp, port);
        OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream());
        ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
        return new GeoChallengeCoreImpl(socket, os,is);
    }
}

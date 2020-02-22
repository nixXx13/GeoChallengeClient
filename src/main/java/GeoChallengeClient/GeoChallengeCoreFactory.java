package GeoChallengeClient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class GeoChallengeCoreFactory {

    public static IGeoChallengeCore getGeoChallengeCore(String serverIp, int port){

        return new GeoChallengeCoreImpl(serverIp, port);
    }
}

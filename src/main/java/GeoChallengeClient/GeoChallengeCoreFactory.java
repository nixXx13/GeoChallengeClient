package GeoChallengeClient;

public class GeoChallengeCoreFactory {

    public static IGeoChallengeCore getGeoChallengeCore(String serverIp, int port){
        ServerConnectorImpl serverConnector = new ServerConnectorImpl(serverIp, port);
        return new GeoChallengeCoreImpl(serverConnector);
    }
}

package GeoChallengeClient;

import Common.NetworkConnectorImpl;

public class GeoChallengeCoreFactory {

    public static IGeoChallengeCore getGeoChallengeCore(String serverIp, int port){
        NetworkConnectorImpl serverConnector = new NetworkConnectorImpl(serverIp, port);
        return new GeoChallengeCoreImpl(serverConnector);
    }
}

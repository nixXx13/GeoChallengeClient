package GeoChallengeClient;

import Common.GameData;

public interface IServerConnector {

    boolean init();

    GameData read();

    boolean send(String s);

    void terminate();

}

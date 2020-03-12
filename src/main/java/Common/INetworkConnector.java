package Common;

import Common.GameData;
import java.util.List;

public interface INetworkConnector {

    boolean init();

    GameData read();

    boolean send(GameData gameData);

    boolean send(List<GameStage> gameStages);

    void terminate();

}

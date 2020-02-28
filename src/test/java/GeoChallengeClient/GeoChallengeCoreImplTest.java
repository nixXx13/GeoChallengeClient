package GeoChallengeClient;

import Common.GameData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoChallengeCoreImplTest {

    @Test
    void playerUpdate() throws InterruptedException {

        ServerConnectorImpl serverConnector = mock(ServerConnectorImpl.class);
        when(serverConnector.init()).thenReturn(true);
        when(serverConnector.send(anyString())).thenReturn(true);
        when(serverConnector.read()).thenReturn(new GameData(GameData.GameDataType.END,""));

        GeoChallengeCoreImpl geoChallengeCore =  new GeoChallengeCoreImpl(serverConnector);

        TestResponseHandler testResponseHandler = new TestResponseHandler();
        geoChallengeCore.registerHandler(testResponseHandler);

        Thread t = new Thread(geoChallengeCore);
        t.start();

        Thread.sleep(1000);

        List<GameData> gameData = testResponseHandler.getGameData();
        assertEquals(gameData.get(0).getType(), GameData.GameDataType.END);
    }


    class TestResponseHandler implements IResponseHandler{

        private ArrayList<GameData> gameDataList = new ArrayList<GameData>();

        public void handle(GameData gameData) {
            gameDataList.add(gameData);
        }

        public ArrayList<GameData> getGameData() {
            return gameDataList;
        }
    }

}
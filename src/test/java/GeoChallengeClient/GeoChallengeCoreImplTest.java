package GeoChallengeClient;

import Common.GameData;
import Common.INetworkConnector;
import Common.NetworkConnectorImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GeoChallengeCoreImplTest {

    @Test
    void serverConnectionErrorTest() {
        INetworkConnector nc = getNetworkConnectorMock();
        when(nc.read()).thenReturn(null);
        when(nc.send(any(GameData.class))).thenThrow(new NullPointerException("send shouldn't be called!"));
        GeoChallengeCoreImpl geoChallengeCore = new GeoChallengeCoreImpl(nc);

        TestResponseHandler testResponseHandler = new TestResponseHandler();
        geoChallengeCore.registerHandler(testResponseHandler);
        geoChallengeCore.run();
        geoChallengeCore.send(null);

        assertEquals(GameData.GameDataType.ERROR,testResponseHandler.getGameData().get(0).getType());
    }

//    @Test
//    void receivingEndTest() {
//        // TODO
//    }


    class TestResponseHandler implements IResponseHandler{

        private ArrayList<GameData> gameDataList = new ArrayList<GameData>();

        public void handle(GameData gameData) {
            gameDataList.add(gameData);
        }

        public ArrayList<GameData> getGameData() {
            return gameDataList;
        }
    }

    private static INetworkConnector getNetworkConnectorMock(){
        NetworkConnectorImpl nc = mock(NetworkConnectorImpl.class);
        when(nc.init()).thenReturn(true);
        when(nc.send(any(GameData.class))).thenReturn(true);
        return nc;
    }

}
import Common.GameData;
import GeoChallengeClient.GeoChallengeCoreFactory;
import GeoChallengeClient.IGeoChallengeCore;
import GeoChallengeClient.IResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String SERVER_IP = "localhost";
        int PORT = 4567;

        IGeoChallengeCore gc;
        gc = GeoChallengeCoreFactory.getGeoChallengeCore(SERVER_IP,PORT);
        gc.registerHandler(mockResponseHandler());
        Thread t = new Thread(gc);
        t.start();

        gc.send(new GameData(GameData.GameDataType.ACK,"Nir:newRoom:true:1:5:GEO"));
        Thread.sleep(1000);
        gc.send(answerGameData("1",1f));
        Thread.sleep(1000);
        gc.send(answerGameData("2",1f));
        Thread.sleep(1000);
        gc.send(answerGameData("3",1f));
        Thread.sleep(1000);
        gc.send(answerGameData("4",1f));
        Thread.sleep(1000);
        gc.send(answerGameData("5",1f));

    }

    public static GameData answerGameData(String answer, Float time){
        Map<String,String> map = new HashMap<String,String>();
        map.put("time",String.valueOf(time));
        map.put("answer",answer);
        return new GameData(GameData.GameDataType.DATA,map);
    }

    public static IResponseHandler mockResponseHandler(){
        return new IResponseHandler() {
            public void handle(GameData gameData) {
//                System.out.println("recieved " + gameData.toString());
//                 if data , gc.send
            }
        };
    }
}



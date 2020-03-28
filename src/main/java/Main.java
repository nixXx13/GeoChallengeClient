import Common.GameData;
import GeoChallengeClient.GeoChallengeCoreFactory;
import GeoChallengeClient.IGeoChallengeCore;
import GeoChallengeClient.IResponseHandler;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String SERVER_IP = "18.195.215.39";
//        String SERVER_IP = "localhost";
        int PORT = 4567;

        IGeoChallengeCore gc;
        gc = GeoChallengeCoreFactory.getGeoChallengeCore(SERVER_IP,PORT);
        gc.registerHandler(mockResponseHandler(gc));
        Thread t = new Thread(gc);
        t.start();

        t.join();
    }

    static IResponseHandler mockResponseHandler(final IGeoChallengeCore gc){
        return new IResponseHandler() {
            public void handle(GameData gameData) {
                 if(gameData.getType().equals(GameData.GameDataType.DATA)){
                     System.out.println("Received questions! game started");
                     try {
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
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     }
                 }
                 if(gameData.getType().equals(GameData.GameDataType.ACK)){
                 //        gc.send(new GameData(GameData.GameDataType.ACK,"Nir:newRoom:true:1:5:GEO"));
                        gc.send(new GameData(GameData.GameDataType.ACK,"Nir:TheRoom:true:2:5:GEO"));
//                        gc.send(new GameData(GameData.GameDataType.ACK,"Nir2:TheRoom:false:2:5:GEO"));
                 }
            }
        };
    }

    public static GameData answerGameData(String answer, Float time){
        Map<String,String> map = new HashMap<String,String>();
        map.put("time",String.valueOf(time));
        map.put("answer",answer);
        return new GameData(GameData.GameDataType.DATA,map);
    }
}



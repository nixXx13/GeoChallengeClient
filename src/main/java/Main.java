import Common.GameData;
import GeoChallengeClient.GeoChallengeCoreFactory;
import GeoChallengeClient.IGeoChallengeCore;
import GeoChallengeClient.IResponseHandler;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String SERVER_IP = "localhost";
        int PORT = 8888;

        IGeoChallengeCore gc;
        try {
            gc = GeoChallengeCoreFactory.getGeoChallengeCore(SERVER_IP,PORT);
            gc.registerHandler(mockResponseHandler());
            Thread t = new Thread(gc);
            t.start();

            Thread.sleep(1000);

            gc.send("1");
            Thread.sleep(1000);
            gc.send("2");
            Thread.sleep(1000);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static IResponseHandler mockResponseHandler(){
        return new IResponseHandler() {
            public void handle(GameData gameData) {
                //System.out.println("recieved " + s);
            }
        };
    }
}



import java.io.IOException;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String SERVER_IP = "localhost";
        int PORT = 8888;

        IGeoChallengeCore gc = null;
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
            @Override
            public void handle(String s) {
                System.out.println("recieved " + s);
            }
        };
    }
}



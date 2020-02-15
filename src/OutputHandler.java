import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class OutputHandler implements Runnable {

    private OutputStreamWriter os;

    public OutputHandler(OutputStreamWriter os){
        this.os = os;
    }

    @Override
    public void run() {
        int i =0;
        while(i<2) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Start answering");
            Scanner reader = new Scanner(System.in);
//            String answer = reader.next();
            String answer = "1"+"\n";
//            os.writeObject(answer + "\n");
            try {
                System.out.println("Sending answer " + answer);
                os.write(answer);
                os.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }
    }

}

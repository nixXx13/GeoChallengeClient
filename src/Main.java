import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        String SERVER_IP = "localhost";
        int PORT = 8888;

        Socket socket;
        OutputStreamWriter os;
        ObjectInputStream is;
        try {
            socket = new Socket(SERVER_IP, PORT);
            os = new OutputStreamWriter(socket.getOutputStream());
            is = new ObjectInputStream(socket.getInputStream());

            go(is, os);
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void go(ObjectInputStream is, OutputStreamWriter os) throws IOException {
        OutputHandler oh = new OutputHandler(os);
        Thread out = new Thread(oh);
        out.start();

        String s = read(is);
        while ( s != null ){
            System.out.println(s);
            if ( s.contains("END")){
                os.write("end" + "\n");
                os.flush();
                break;
            }
            s = read(is);
        }
        System.out.println("EXITING");
        is.close();
        os.close();

    }

    public static String read(ObjectInputStream is){
        String s = null;
        try {
            s = (String) is.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
}

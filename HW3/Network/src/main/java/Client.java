import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final int port = 5001;
    private final String address = "127.0.0.1";

    public void start() {
        Socket socket = null;
        DataOutputStream sendBuffer = null;
        DataInputStream receiveBuffer = null;
        Scanner scanner = new Scanner(System.in);

        try {
            socket = new Socket(address, port);
            sendBuffer = new DataOutputStream(socket.getOutputStream());
            receiveBuffer = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("unable to initialize socket!");
        }
        while (true) {
            String input = scanner.nextLine();
            try {
                sendBuffer.writeUTF(input);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error in sending input to buffer");
            }
            String outout = null;
            try {
                assert receiveBuffer != null;
                outout = receiveBuffer.readUTF();
                System.out.println(outout);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error in reading output data from buffer");
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }
}

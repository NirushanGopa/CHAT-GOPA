package CLIENT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * gestisce il client e inizializza i due stream send e read thread 
 * @author Nirushan
 */
public class Client {
    private final static int serverPort = 6789;
    private Socket s;
    private BufferedReader input;
    private BufferedReader inFromServer;
    private DataOutputStream outToServer;
    private SendThread sendThread;
    private ReadThread readThread;

    public void connect() {
        // Inizializzazione input
        input = new BufferedReader(new InputStreamReader(System.in));

        try {
            // Recupero ip localhost
            InetAddress ip = InetAddress.getByName("localhost");

            // Conessione al server
            s = new Socket(ip, serverPort);

            // Apertura canali
            inFromServer = new BufferedReader(new InputStreamReader(s.getInputStream()));
            outToServer = new DataOutputStream(s.getOutputStream());

            sendThread = new SendThread(outToServer, inFromServer, input, s, readThread);
            readThread = new ReadThread(inFromServer);
        } catch (Exception ex) {
            ex.toString();
        }
    }
}

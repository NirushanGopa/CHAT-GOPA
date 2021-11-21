package CLIENT;

import java.io.IOException;
import java.net.UnknownHostException;
//import progetto_chat.GUI_Client;

public class MainClient {

    public static void main(String args[]) throws UnknownHostException, IOException {
        Client client = new Client();
        client.connect();
        //GUI_Client bellaberi = new GUI_Client();
 
    }

}

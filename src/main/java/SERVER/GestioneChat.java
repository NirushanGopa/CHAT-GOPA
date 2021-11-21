package SERVER;

import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * classe che da un server per ogni client
 * @author Nirushan
 */
public class GestioneChat {
    private ArrayList<ServerThread> sockets = new ArrayList<>();
    private ArrayList<String> utenti = new ArrayList<>();
    private ServerSocket s;

    public void start() {
        
        //crea una porta, successivamente crea un server thread e lo attribuisce
        try {
            System.out.println("server in attesa");
            s = new ServerSocket(6789);

            for (int i = 0; i < 10; i++) {
                ServerThread t = new ServerThread(s.accept(), sockets, utenti);
                sockets.add(t);
                sockets.get(i).start();
            }
            s.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del messaggio!");
            System.exit(1);

        }
    }
}

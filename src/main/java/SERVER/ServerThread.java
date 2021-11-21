package SERVER;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    private Socket clientS;
    private BufferedReader inDalClient;
    private DataOutputStream outVersoClient;
    private String usernameClient;

    private ArrayList<ServerThread> sockets;
    private ArrayList<String> utenti;

    public ServerThread(Socket socket, ArrayList<ServerThread> sockets, ArrayList<String> utenti) {
        this.clientS = socket;
        this.sockets = sockets;
        this.utenti = utenti;
    }

    @Override
    public void run() {
        try {
            comunica();
        } catch (Exception e) {
            e.toString();
        }
    }
    
    /**
     * metodo che permette la comunicazione con il client, per prima cosa inizializza gli stream(in, out), 
     * poi richiede il nome al client con eventuali controlli, 
     * successivamnete controlla se il messaggio è di gruppo e di tipo privato
     * @throws Exception 
     */
    public void comunica() throws Exception {
        inDalClient = new BufferedReader(new InputStreamReader(clientS.getInputStream()));
        outVersoClient = new DataOutputStream(clientS.getOutputStream());
        outVersoClient.writeBytes("Inserisci username" + '\n');
        usernameClient = inDalClient.readLine();
        
        //LOGIN
        if (utenti.isEmpty())
            utenti.add(usernameClient);
        else {
            for (int i = 0; i < utenti.size(); i++) {
                while (usernameClient.equals(utenti.get(i))) {
                    outVersoClient.writeBytes("Nome utente non disponibile, inserirne uno nuovo" + '\n');
                    usernameClient = inDalClient.readLine();
                }
            }
            utenti.add(usernameClient);
        }

        System.out.println(usernameClient + " connesso");
        outVersoClient.writeBytes(usernameClient + " connesso" + '\n' + '\n');

        for (int i = 0; i < sockets.size(); i++) {
            sockets.get(i).outVersoClient.writeBytes("\nUtenti connessi:" + '\n');
            for (int j = 0; j < utenti.size(); j++) {
                sockets.get(i).outVersoClient.writeBytes("Utente:" + utenti.get(j) + '\n');
            }
            sockets.get(i).outVersoClient.writeBytes("\n");
        }
        
        //controllo disconnessione
        for (;;) {
            String mex = inDalClient.readLine();
            if (mex.equalsIgnoreCase("ESCI")) {
                System.out.println("\n" + usernameClient + ": utente disconnesso" + '\n');
                for (ServerThread s : sockets) {
                    if (s != this)
                        s.outVersoClient.writeBytes("Utente " + usernameClient + " disconnesso" + '\n');
                }
                utenti.remove(usernameClient);
                sockets.remove(this);
                
                //stampa della lista
                for (int i = 0; i < sockets.size(); i++) {
                    sockets.get(i).outVersoClient.writeBytes("Utenti connessi:" + '\n');
                    for (int j = 0; j < utenti.size(); j++) {
                        sockets.get(i).outVersoClient.writeBytes("Utente:" + utenti.get(j) + '\n');
                    }
                    sockets.get(i).outVersoClient.writeBytes("\n");
                }
                break;
            } else {
                
                //controllo messaggio di gruppo
                String[] mexSplit = mex.split("@");
                if (sockets.size() > 1) {
                    if (mexSplit[1].equalsIgnoreCase("Group")) {
                        for (ServerThread s : sockets) {
                            if (s != this)
                                s.outVersoClient.writeBytes(usernameClient + ": " + mexSplit[0] + ("(group)") + '\n');
                        }
                        
                    //controllo messaggio tipo privato
                    } else {
                        for (ServerThread s : sockets) {
                            if (s != this) {
                                if (s.usernameClient.equals(mexSplit[1])) {
                                    s.outVersoClient.writeBytes(usernameClient + ": " + mexSplit[0] + '\n');
                                    break;
                                }
                            }
                        }
                    }
                } else
                    outVersoClient.writeBytes("Nessuno è connesso" + '\n');
            }

        }

        outVersoClient.close();
        inDalClient.close();
        clientS.close();

    }
}

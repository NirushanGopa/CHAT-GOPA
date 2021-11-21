package CLIENT;

import java.io.BufferedReader;
/**
 * serve per la lettura dei messaggi provenienti dal server
 * @author Nirushan
 */
public class ReadThread extends Thread {
    private BufferedReader inFromServer;

    public ReadThread(BufferedReader inFromServer) {
        this.inFromServer = inFromServer;
        start();
    }

    @Override
    public void run() {
        try {
            for (;;) {
                String mex = inFromServer.readLine();
                if (mex != null)
                    System.out.println(mex);
            }
        } catch (Exception ex) {
            ex.toString();
            System.exit(1);
        }
    }

}
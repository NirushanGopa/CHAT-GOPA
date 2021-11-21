package CLIENT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class SendThread extends Thread {

    DataOutputStream outToServer;
    BufferedReader inFromServer;
    BufferedReader input;
    Socket s;
    ReadThread readThread;
    
    /**
     * invia il messaggio verso il server
     * @param outToServer
     * @param inFromServer
     * @param input
     * @param s
     * @param readThread 
     */
    public SendThread(DataOutputStream outToServer, BufferedReader inFromServer, BufferedReader input, Socket s,
            ReadThread readThread) {
        start();
        this.outToServer = outToServer;
        this.inFromServer = inFromServer;
        this.input = input;
        this.s = s;
        this.readThread = readThread;
    }

    @Override
    public void run() {
        try {
            for (;;) {
                String mex = input.readLine();
                if (mex.equalsIgnoreCase("ESCI")) {
                    outToServer.writeBytes(mex + '\n');
                    outToServer.writeBytes("Connessione in chiusura..." + '\n');//non fa non capisco motivo
                    s.close();

                    break;
                } else {
                    System.out.println("IO: " + mex);
                    outToServer.writeBytes(mex + '\n');
                }
            }
        } catch (Exception ex) {
            ex.toString();
            System.exit(1);
        }
    }

}
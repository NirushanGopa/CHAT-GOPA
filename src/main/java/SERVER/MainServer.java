package SERVER;

public class MainServer {
    public static void main(String[] args) {
        GestioneChat tcpServer = new GestioneChat();
        tcpServer.start();
    }

}

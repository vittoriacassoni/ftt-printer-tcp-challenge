package connections;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrinterServer extends Thread {
    private static ServerSocket server;
    private static int port = 5580;
    private boolean serverActive;
    private List<ClientManager> clients = Collections.synchronizedList(new ArrayList<ClientManager>());


    @Override
    public void run() {
        try {
            serverActive = true;
            server = new ServerSocket(port);

            while (serverActive) {
                Socket socket = server.accept();
                addClientTreatment(socket);
                Thread.sleep(10);
            }

            if (!server.isClosed()) {
                server.close();
            }
        } catch (InterruptedException | IOException ex) {
            if (ex.getMessage().equals("Socket closed")) {
                System.out.println("Conexão server fechada!!!");
            } else {
                Logger.getLogger(PrinterServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addClientTreatment(Socket socket) {
        ClientManager manager = new ClientManager();
        manager.setClientSocket(socket);
        clients.add(manager);
        Thread threadSocket = new Thread(manager);
        threadSocket.start();
    }

    public void close() throws IOException {
        clients.forEach((client) -> {
            try {
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(PrinterServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        serverActive = false;
        server.close();
    }

    public void list() {
        System.out.println("Lista de clients -------------------------------------------------------");
        for (ClientManager client : clients) {
            client.identify();
        }
    }
}

package connections;

import print.ManageQueuePrint;
import tos.MessageTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientManager implements Runnable {

    private Socket clientSocket;

    private ReceptionStateMachine machine = new ReceptionStateMachine();

    @Override
    public void run() {
        try {
            machine.initialize();
            System.out.println(clientSocket.hashCode() + ": Conexão cliente estabelecida");
            InputStream stream = clientSocket.getInputStream();
            try {
                int bytesLidos = 0;
                do {
                    byte[] dados = new byte[1024];
                    bytesLidos = stream.read(dados);
                    if (bytesLidos > 0) {
                        List<MessageTO> message = machine.prepareData(dados, bytesLidos);
                        prepareMessageReception(message);
                    }
                } while (bytesLidos != -1);
            } finally {
                if (stream != null) {
                    stream.close();
                }
                if (clientSocket.isConnected()) {
                    clientSocket.close();
                }
            }
        } catch (IOException ex) {
            if (ex.getMessage().equals("Socket closed")) {
                System.out.println(clientSocket.hashCode() + ": Conexão cliente closeda");
            } else {
                Logger.getLogger(ClientManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    
    public Socket getClientSocket() {
        return clientSocket;
    }
    
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void close() throws IOException {
        if (clientSocket.isConnected()) {
            clientSocket.close();
        }
    }

    void identify() {
        if ((clientSocket.isConnected()) && (!clientSocket.isClosed())) {
            System.out.println(clientSocket.hashCode() + ": Conexão cliente estabelecida.");
        } else {
            System.out.println(clientSocket.hashCode() + ": Conexão cliente closeda.");
        }
    }

   private void prepareMessageReception(List<MessageTO> mensagens) {
        for (MessageTO msg : mensagens) {
            switch (msg.getOpCode()) {
                case "I":
                    ManageQueuePrint.getInstance().addMessagePrint(msg.getMessage());
                    break;
                case "S":
                    System.out.println(clientSocket.hashCode() + ": Mensagem de status recebida: " + msg.getMessage());
                    break;
                default:
                    System.out.println(clientSocket.hashCode() + ": Mensagem desconhecida recebida: " + msg.getMessage());
                    break;
            }
        }
    }
}
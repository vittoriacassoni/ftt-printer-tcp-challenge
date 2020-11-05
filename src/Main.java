import connections.ClientManager;
import connections.PrinterServer;
import print.ManageQueuePrint;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        PrinterServer server = new PrinterServer();
        server.start();

        ManageQueuePrint.getInstance().activate();

        Scanner scan = new Scanner(System.in);
        try {
            boolean exit = false;
            do{
                System.out.println("Bem vindo(a) ao sistema de gerenciamento de impressoras! O que deseja fazer?");
                System.out.println("1 - Listar clients");
                System.out.println("2 - Sair");
                var opc = scan.next();
                switch (opc){
                    case "1":
                        server.list();
                        break;
                    case "2":
                        exit = true;
                        break;
                }
            }while (!exit);
        } finally {
            scan.close();
            server.close();
            ManageQueuePrint.getInstance().disable();
        }

    }
}

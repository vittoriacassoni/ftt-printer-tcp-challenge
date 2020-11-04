package print;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadManageQueuePrint extends Thread {

    private boolean status;

    @Override
    public void run() {
        setStatus(true);
        while (status) {
            try {
                String print = ManageQueuePrint.getInstance().removeMessagePrint();
                if (print != null) {
                   printMessage(print);
                }
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadManageQueuePrint.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setStatus(boolean value) {
        status = value;
    }

    private void printMessage(String msg) throws InterruptedException {
        System.out.println("Nova impressão da impressora: " + getName() + " - " + msg);
        Thread.sleep(1000);
    }
}

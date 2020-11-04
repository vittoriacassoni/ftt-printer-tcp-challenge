package print;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageQueuePrint {
    ConcurrentLinkedQueue<String> queueMessages;

    private static ManageQueuePrint instance;

    private ManageQueuePrint() {
        queueMessages = new ConcurrentLinkedQueue<>();
    }

    public static ManageQueuePrint getInstance() {
        if (instance == null) {
            instance = new ManageQueuePrint();
        }
        return instance;
    }

    List<ThreadManageQueuePrint> threads;

    public void addMessagePrint(String message) {
        queueMessages.add(message);
    }

    String removeMessagePrint() {
        return queueMessages.poll();
    }

    public void activate() {
        if (threads == null) {
            threads = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                ThreadManageQueuePrint thread = new ThreadManageQueuePrint();
                thread.setName("Thread " + (i + 1));
                thread.start();
                threads.add(thread);
            }
        }
    }

    public void disable() {
        if (threads != null) {
            for (ThreadManageQueuePrint thread : threads) {
                thread.setStatus(false);
                try {
                    thread.join(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ManageQueuePrint.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (thread.isAlive()) {
                    thread.interrupt();
                }
            }
        }
    }
}

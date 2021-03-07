import java.util.LinkedList;
import java.util.Queue;

public class TaskQueue {
    Queue<String> taskQueues = new LinkedList<>();

    public synchronized void add(String task) {
        taskQueues.add(task);
        this.notifyAll();
    }

    public synchronized String get() throws InterruptedException {
        while (taskQueues.isEmpty()) {
            this.wait();
        }
        return taskQueues.remove();
    }
}

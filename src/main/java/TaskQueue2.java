import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue2 {

    ReentrantLock reentrantLock = new ReentrantLock();
    Condition condition = reentrantLock.newCondition();
    Queue<String> taskQueues = new LinkedList<>();

    public void add(String task) {
        reentrantLock.lock();
        try {
            taskQueues.add(task);
            condition.signalAll();
        } finally {
            reentrantLock.unlock();
        }
    }

    public String get() throws InterruptedException {
        reentrantLock.lock();
        try {
            while (taskQueues.isEmpty()) {
                condition.await();
            }
            return taskQueues.remove();
        } finally {
            reentrantLock.unlock();
        }
    }
}

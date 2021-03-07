public class AddThread extends Thread {

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            synchronized (Counter.lock) {
                Counter.num += 1;
            }
        }
    }
}

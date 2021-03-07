import java.util.concurrent.RecursiveTask;

public class SumTask extends RecursiveTask<Long> {

    private final long[] array;
    private final int start;
    private final int end;

    public SumTask(long[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if (end - start < 500) {
            long sum = 0;
            //任务足够小，则直接计算结果
            for (int i = start; i < end; i++) {
                sum += array[i];
            }
            return sum;
        }
        int middle = (start + end) / 2;
        SumTask sumTask = new SumTask(array, start, middle);
        SumTask sumTask2 = new SumTask(array, middle, end);
        System.out.println(String.format("拆分前：%d,%d 拆分后：[%d,%d]和[%d,%d]", start, end, start, middle, middle, end));
        invokeAll(sumTask, sumTask2);
        long result = sumTask.join();
        long result2 = sumTask2.join();
        long sum = result + result2;
        System.out.println("sum:" + sum);
        return sum;
    }
}

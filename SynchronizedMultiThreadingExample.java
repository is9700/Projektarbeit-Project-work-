package synchr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class SynchronizedMultiThreadingExample {
    // Shared semaphore with one permit
    private static final Semaphore semaphore = new Semaphore(1);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<String>> futureResults = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Callable<String> task = new MyCallablee("Task " + i, i * 100);
            Future<String> future = executor.submit(() -> {
                try {
                    semaphore.acquire(); // Acquire the semaphore
                    return task.call();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return "";
                } finally {
                    semaphore.release(); // Release the semaphore in a finally block
                }
            });
            futureResults.add(future);
        }

        executor.shutdown();

        for (Future<String> future : futureResults) {
            try {
                String result = future.get();
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyCallablee implements Callable<String> {
    private final String taskName;
    private final int computationTime;

    MyCallablee(String taskName, int computationTime) {
        this.taskName = taskName;
        this.computationTime = computationTime;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Task " + taskName + " started by thread: " + Thread.currentThread().getName());
        Thread.sleep(computationTime);
        return "Task " + taskName + " completed by thread: " + Thread.currentThread().getName();
    }
}

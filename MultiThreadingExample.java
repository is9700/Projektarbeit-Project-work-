package asynchr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadingExample {
    public static void main(String[] args) {
        // Create a thread pool with a fixed number of threads (5 in this case)
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Create a list to hold the Future objects representing the results of the tasks
        List<Future<String>> futureResults = new ArrayList<>();

        // Submit tasks to the thread pool
        for (int i = 1; i <= 10; i++) {
            Callable<String> task = new MyCallable("Task " + i, i * 100); // Longer computation time
            Future<String> future = executor.submit(task);
            futureResults.add(future);
        }

        // Shut down the executor service to stop accepting new tasks
        executor.shutdown();

        // Retrieve and print the results of the tasks
        for (Future<String> future : futureResults) {
            try {
                String result = future.get(); // This call blocks until the result is available
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}

class MyCallable implements Callable<String> {
    private final String taskName;
    private final int computationTime; // Simulated computation time in milliseconds

    MyCallable(String taskName, int computationTime) {
        this.taskName = taskName;
        this.computationTime = computationTime;
    }

    @Override
    public String call() throws Exception {
        System.out.println("Task " + taskName + " started by thread: " + Thread.currentThread().getName());
        // Simulate longer computation time
        Thread.sleep(computationTime);
        return "Task " + taskName + " completed by thread: " + Thread.currentThread().getName();
    }
}

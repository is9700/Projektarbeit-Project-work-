package optimized;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizedThreading {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<CompletableFuture<MyCallable>> futures = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> new MyCallable("Task " + i, i * 100), executor))
                .collect(Collectors.toList());

        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .map(MyCallable::getResult)
                .collect(Collectors.toList());

        executor.shutdown();

        results.forEach(System.out::println);
    }
}

class MyCallable implements Callable<MyCallable> {
    private final String taskName;
    private final int computationTime;

    MyCallable(String taskName, int computationTime) {
        this.taskName = taskName;
        this.computationTime = computationTime;
    }

    @Override
    public MyCallable call() throws InterruptedException {
        System.out.println("Task " + taskName + " started by thread: " + Thread.currentThread().getName());

        TimeUnit.MILLISECONDS.sleep(computationTime);
        System.out.println("Task " + taskName + " completed by thread: " + Thread.currentThread().getName());
        return this;
    }

    public String getResult() {
        return "Task " + taskName + " completed by thread: " + Thread.currentThread().getName();
    }
}

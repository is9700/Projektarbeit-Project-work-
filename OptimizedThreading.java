import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizedThreading {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        List<CompletableFuture<String>> futures = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return new MyCallable("Task " + i, i * 100).call();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return "";
                    }
                }, executor))
                .collect(Collectors.toList());
        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        executor.shutdown();
        results.forEach(System.out::println);
    }
}

class MyCallable implements Callable<String> {
    private final String taskName;
    private final int computationTime;

    MyCallable(String taskName, int computationTime) {
        this.taskName = taskName;
        this.computationTime = computationTime;
    }

    @Override
    public String call() throws InterruptedException {
        System.out.println("Task " + taskName + " started by thread: " +
                Thread.currentThread().getName());
        Thread.sleep(computationTime);
        return "Task " + taskName + " completed by thread: " +
                Thread.currentThread().getName();
    }
}



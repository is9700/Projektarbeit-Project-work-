package optimized;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OptimizedThreading {

    public static void main(String[] args) {
//Threadpool erstellen
        ExecutorService executor = Executors.newFixedThreadPool(5);
    //fur jede zahl iùm bereich erzeugt ein CompletableFuture-Objekt(asynchron)
        List<CompletableFuture<MyCallable>> futures = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> CompletableFuture.supplyAsync(() -> new MyCallable("Task " + i, i * 100), executor))//mit Lambda
                .collect(Collectors.toList());//Sammelt die CompletableFuture-Objekte in eine Liste

        List<String> results = futures.stream()
                .map(CompletableFuture::join)
                .map(MyCallable::getResult)
                .collect(Collectors.toList());

        executor.shutdown();

        results.forEach(System.out::println);//ausgabe
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
    public MyCallable call() throws InterruptedException {//hier MyCallable Object als Ausgabe statt String
        System.out.println("Task " + taskName + " started by thread: " + Thread.currentThread().getName());

        TimeUnit.MILLISECONDS.sleep(computationTime);
        System.out.println("Task " + taskName + " completed by thread: " + Thread.currentThread().getName());
        return this;
    }

    public String getResult() {//ausgabe 
        return "Task " + taskName + " completed by thread: " + Thread.currentThread().getName();
    }
}

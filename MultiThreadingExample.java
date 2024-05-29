package asynchr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadingExample {
    public static void main(String[] args) {
        //5 Threads erstellen
        ExecutorService executor = Executors.newFixedThreadPool(5);

      //futurelist erstellen
        List<Future<String>> futureResults = new ArrayList<>();

//aufgaben zu threadpool 
        for (int i = 1; i <= 10; i++) {
            Callable<String> task = new MyCallable("Task " + i, i * 100); // Longer computation time
            Future<String> future = executor.submit(task);
            futureResults.add(future);
        }

      
        executor.shutdown();

       //Ergebnis ausgeben
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
// Aufgabe erstellen
class MyCallable implements Callable<String> {
    private final String taskName;
    private final int computationTime; 

    MyCallable(String taskName, int computationTime) {
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

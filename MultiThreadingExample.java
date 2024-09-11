import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadingExample {
    public static void main(String[] args) {
        // erstelle einen ExecutorService mit einem threadPool von 5 Threads
        ExecutorService executor = Executors.newFixedThreadPool(5);
        
        // Liste, um Future-Objekte zu speichern, die die Ergebnisse der Aufgaben enthalten
        List<Future<String>> futureResults = new ArrayList<>();

        // Schleife zur Erstellung und Einreichung von 10 Aufgaben
        for (int i = 1; i <= 10; i++) {
            // Erstelle eine neue Aufgabe vom Typ MyCallable
            Callable<String> task = new MyCallable("Task " + i, i * 100);
            
            // Übergebe die Aufgabe an den ExecutorService //erhalte ein Future-Objekt
            Future<String> future = executor.submit(task);
            
            // Füge das Future-Objekt der Liste ein
            futureResults.add(future);
        }

        // Schleife, um die abgeschlossenen Aufgaben zu verarbeiten
        while (!futureResults.isEmpty()) {
            for (Future<String> future : futureResults) {
                if (future.isDone()) {  // Überprüfe, ob die Aufgabe abgeschlossen ist
                    try {
                        // Erhalte das Ergebnis der abgeschlossenen Aufgabe
                        String result = future.get();
                        System.out.println(result);
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    
                    // Entferne das abgeschlossene Future objekt 
                    futureResults.remove(future);
                    
                    // Breche die Schleife ab, um ConcurrentModificationException zu vermeiden
                    break; 
                }
            }
        }

        // Fahre den ExecutorService herunter, nachdem alle Aufgaben abgeschlossen sind
        executor.shutdown();
    }
}

class MyCallable implements Callable<String> {
    private final String taskName;
    private final int computationTime;

    // Konstruktor 
    MyCallable(String taskName, int computationTime) {
        this.taskName = taskName;
        this.computationTime = computationTime;
    }

    @Override
    public String call() throws Exception {
        // Gibt aus, dass die Aufgabe gestartet wurde
        System.out.println("Task " + taskName + " gestartet von Thread: " +
                Thread.currentThread().getName());

        // Simuliere eine Berechnungszeit
        Thread.sleep(computationTime);

        // Gibt aus, dass die Aufgabe abgeschlossen wurde
        return "Task " + taskName + " abgeschlossen von Thread: " +
                Thread.currentThread().getName();
    }
}

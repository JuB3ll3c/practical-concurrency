package concurrency.exercise08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskExecutor {
    private final ExecutorService executor;
    private final List<String> results = new ArrayList<>();
    private final List<Throwable> exceptions = new ArrayList<>();

    public TaskExecutor(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void executeTask(Runnable task) {
        executor.execute(task);
    }

    public void executeTaskWithResult(String taskName, Task task) {
        executor.execute(() -> {
            try {
                String result = task.call();
                results.add(taskName + ": " + result);
            } catch (Exception e) {
                exceptions.add(e);
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    public List<String> getResults() {
        return new ArrayList<>(results);
    }

    public List<Throwable> getExceptions() {
        return new ArrayList<>(exceptions);
    }

    public int getCompletedTaskCount() {
        return results.size() + exceptions.size();
    }

    @FunctionalInterface
    public interface Task {
        String call() throws Exception;
    }
}

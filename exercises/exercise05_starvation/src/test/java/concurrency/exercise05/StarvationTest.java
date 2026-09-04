package concurrency.exercise05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StarvationTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testLowPriorityTasksShouldEventuallyExecute() throws InterruptedException {
        TaskScheduler scheduler = new TaskScheduler();
        
        Task highPriorityTask = new TaskScheduler.Task("High-1", 10);
        Task lowPriorityTask = new TaskScheduler.Task("Low-1", 1);
        
        scheduler.addTask(highPriorityTask);
        scheduler.addTask(lowPriorityTask);

        Thread executor = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                scheduler.executeNext();
            }
        });

        executor.start();
        executor.join();

        assertTrue(highPriorityTask.isExecuted(), "High priority task should be executed");
        assertTrue(lowPriorityTask.isExecuted(), "Low priority task should eventually be executed");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testAllTasksShouldExecuteRegardlessOfPriority() throws InterruptedException {
        TaskScheduler scheduler = new TaskScheduler();
        int numHighPriority = 100;
        int numLowPriority = 100;
        
        TaskScheduler.Task[] highPriorityTasks = new TaskScheduler.Task[numHighPriority];
        TaskScheduler.Task[] lowPriorityTasks = new TaskScheduler.Task[numLowPriority];
        
        for (int i = 0; i < numHighPriority; i++) {
            highPriorityTasks[i] = new TaskScheduler.Task("High-" + i, 10);
            scheduler.addTask(highPriorityTasks[i]);
        }
        
        for (int i = 0; i < numLowPriority; i++) {
            lowPriorityTasks[i] = new TaskScheduler.Task("Low-" + i, 1);
            scheduler.addTask(lowPriorityTasks[i]);
        }

        Thread executor = new Thread(() -> {
            for (int i = 0; i < numHighPriority + numLowPriority + 100; i++) {
                scheduler.executeNext();
            }
        });

        executor.start();
        executor.join();

        for (int i = 0; i < numHighPriority; i++) {
            assertTrue(highPriorityTasks[i].isExecuted(), 
                       "High priority task " + i + " should be executed");
        }
        
        for (int i = 0; i < numLowPriority; i++) {
            assertTrue(lowPriorityTasks[i].isExecuted(), 
                       "Low priority task " + i + " should be executed");
        }
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testContinuousHighPriorityTasksShouldNotStarveLowPriority() throws InterruptedException {
        TaskScheduler scheduler = new TaskScheduler();
        TaskScheduler.Task lowPriorityTask = new TaskScheduler.Task("Starved", 1);
        
        scheduler.addTask(lowPriorityTask);

        Thread highPriorityAdder = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                scheduler.addTask(new TaskScheduler.Task("High-" + i, 10));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread executor = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                scheduler.executeNext();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        highPriorityAdder.start();
        executor.start();

        highPriorityAdder.join();
        executor.join();

        assertTrue(lowPriorityTask.isExecuted(), 
                   "Low priority task should not be starved by continuous high priority tasks");
    }
}

package concurrency.exercise08;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadPoolTest {

    private TaskExecutor executor;

    @AfterEach
    public void cleanup() {
        if (executor != null) {
            executor.shutdown();
        }
    }

    @Test
    public void testAllTasksShouldComplete() throws InterruptedException {
        executor = new TaskExecutor(4);
        int numTasks = 100;

        for (int i = 0; i < numTasks; i++) {
            final int taskId = i;
            executor.executeTask(() -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
    }

    @Test
    public void testTasksWithResultsShouldBeCollected() throws InterruptedException {
        executor = new TaskExecutor(4);
        int numTasks = 50;

        for (int i = 0; i < numTasks; i++) {
            final int taskId = i;
            executor.executeTaskWithResult("Task-" + taskId, () -> {
                Thread.sleep(10);
                return "Result-" + taskId;
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(numTasks, executor.getResults().size());
        
        for (String result : executor.getResults()) {
            assertTrue(result.startsWith("Task-"), "Result should start with Task-");
            assertTrue(result.contains("Result-"), "Result should contain Result-");
        }
    }

    @Test
    public void testExceptionsShouldBeCaptured() throws InterruptedException {
        executor = new TaskExecutor(2);
        int numFailingTasks = 20;
        int numSuccessfulTasks = 10;

        for (int i = 0; i < numFailingTasks; i++) {
            final int taskId = i;
            executor.executeTaskWithResult("Failing-" + taskId, () -> {
                if (taskId % 2 == 0) {
                    throw new RuntimeException("Simulated failure");
                }
                return "Should not reach here";
            });
        }

        for (int i = 0; i < numSuccessfulTasks; i++) {
            final int taskId = i;
            executor.executeTaskWithResult("Success-" + taskId, () -> {
                return "Success-" + taskId;
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        assertEquals(numFailingTasks, executor.getExceptions().size());
        assertEquals(numSuccessfulTasks, executor.getResults().size());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testMixedTaskTypesShouldExecuteCorrectly() throws InterruptedException {
        executor = new TaskExecutor(3);
        int totalTasks = 100;
        int[] counters = new int[1];

        for (int i = 0; i < totalTasks; i++) {
            final int taskId = i;
            if (taskId % 3 == 0) {
                executor.executeTaskWithResult("Counter-" + taskId, () -> {
                    counters[0]++;
                    return "Counted";
                });
            } else if (taskId % 3 == 1) {
                executor.executeTaskWithResult("Fail-" + taskId, () -> {
                    throw new RuntimeException("Expected failure");
                });
            } else {
                executor.executeTask(() -> {
                    counters[0]++;
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        int expectedResults = totalTasks / 3 + (totalTasks % 3 >= 1 ? 1 : 0);
        int expectedExceptions = totalTasks / 3 + (totalTasks % 3 >= 2 ? 1 : 0);

        assertEquals(expectedResults, executor.getResults().size());
        assertEquals(expectedExceptions, executor.getExceptions().size());
    }
}

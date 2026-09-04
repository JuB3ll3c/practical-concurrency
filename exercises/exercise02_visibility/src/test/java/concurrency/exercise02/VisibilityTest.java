package concurrency.exercise02;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VisibilityTest {

    @Test
    public void testRateUpdateShouldBeVisibleToReaderThread() throws InterruptedException {
        InterestRate.updateRate(5.0);

        Thread readerThread = new Thread(() -> {
            double rate = 0.0;
            boolean updated = false;
            
            for (int i = 0; i < 100000; i++) {
                updated = InterestRate.isRateUpdated();
                if (updated) {
                    rate = InterestRate.getCurrentRate();
                    break;
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            
            assertTrue(updated, "Rate update should be visible");
            assertEquals(5.0, rate, 0.001, "Updated rate should be visible");
        });

        readerThread.start();
        readerThread.join(5000);

        assertTrue(readerThread.isAlive() == false, "Reader thread should have completed");
    }

    @Test
    public void testConcurrentUpdatesShouldBeVisible() throws InterruptedException {
        int numThreads = 10;
        Thread[] threads = new Thread[numThreads];
        double[] expectedRates = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};

        for (int i = 0; i < numThreads; i++) {
            final double rate = expectedRates[i];
            threads[i] = new Thread(() -> {
                InterestRate.updateRate(rate);
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertTrue(InterestRate.isRateUpdated(), "Rate should be updated");
        double finalRate = InterestRate.getCurrentRate();
        assertTrue(finalRate >= 1.0 && finalRate <= 10.0, 
                   "Final rate should be one of the updated values");
    }

    @Test
    public void testWriterReaderCommunication() throws InterruptedException {
        InterestRate.updateRate(0.0);

        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(100);
                InterestRate.updateRate(42.0);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread readerThread = new Thread(() -> {
            double observedRate = 0.0;
            for (int i = 0; i < 500; i++) {
                if (InterestRate.isRateUpdated()) {
                    observedRate = InterestRate.getCurrentRate();
                    if (observedRate == 42.0) {
                        break;
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            assertEquals(42.0, observedRate, 0.001, "Reader should see the updated rate");
        });

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join(2000);

        assertTrue(!readerThread.isAlive(), "Reader thread should have completed");
    }
}

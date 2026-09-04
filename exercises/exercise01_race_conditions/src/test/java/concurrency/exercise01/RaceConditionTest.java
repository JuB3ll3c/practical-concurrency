package concurrency.exercise01;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RaceConditionTest {

    @Test
    public void testConcurrentDepositsShouldNotLoseMoney() throws InterruptedException {
        Account account = new Account(0);
        int numberOfThreads = 100;
        int depositsPerThread = 1000;
        int expectedBalance = numberOfThreads * depositsPerThread;

        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < depositsPerThread; j++) {
                    account.deposit(1);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void testConcurrentWithdrawalsShouldNotExceedBalance() throws InterruptedException {
        int initialBalance = 100000;
        Account account = new Account(initialBalance);
        int numberOfThreads = 100;
        int withdrawalsPerThread = 1000;
        int totalWithdrawals = numberOfThreads * withdrawalsPerThread;
        int expectedBalance = initialBalance - totalWithdrawals;

        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < withdrawalsPerThread; j++) {
                    account.withdraw(1);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(expectedBalance, account.getBalance());
    }

    @Test
    public void testMixedOperationsShouldMaintainConsistency() throws InterruptedException {
        Account account = new Account(50000);
        int numberOfThreads = 50;
        int operationsPerThread = 2000;

        Thread[] threads = new Thread[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadIndex = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    if (threadIndex % 2 == 0) {
                        account.deposit(10);
                    } else {
                        account.withdraw(10);
                    }
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(50000, account.getBalance());
    }
}

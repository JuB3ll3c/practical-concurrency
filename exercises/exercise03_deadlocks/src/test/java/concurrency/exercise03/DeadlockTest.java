package concurrency.exercise03;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeadlockTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testTransferShouldNotDeadlock() throws InterruptedException {
        Account accountA = new Account("A", 1000);
        Account accountB = new Account("B", 1000);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                accountA.transfer(accountB, 10);
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                accountB.transfer(accountA, 10);
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertEquals(1000, accountA.getBalance());
        assertEquals(1000, accountB.getBalance());
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testMultipleTransfersShouldComplete() throws InterruptedException {
        Account account1 = new Account("1", 10000);
        Account account2 = new Account("2", 10000);
        Account account3 = new Account("3", 10000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                account1.transfer(account2, 1);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                account2.transfer(account3, 1);
            }
        });

        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                account3.transfer(account1, 1);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);

        assertEquals(10000, account1.getBalance());
        assertEquals(10000, account2.getBalance());
        assertEquals(10000, account3.getBalance());
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testCircularTransferShouldNotHang() throws InterruptedException {
        Account alice = new Account("Alice", 1000);
        Account bob = new Account("Bob", 1000);

        Thread aliceToBob = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                alice.transfer(bob, 1);
            }
        });

        Thread bobToAlice = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                bob.transfer(alice, 1);
            }
        });

        aliceToBob.start();
        bobToAlice.start();

        aliceToBob.join(10000);
        bobToAlice.join(10000);

        assertEquals(1000, alice.getBalance());
        assertEquals(1000, bob.getBalance());
    }
}

package concurrency.exercise06;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LivelockTest {

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testDinerShouldNotLivelock() throws InterruptedException {
        Diner diner = new Diner();
        
        Person alice = new Person("Alice", true, false, diner);
        Person bob = new Person("Bob", false, true, diner);

        Thread aliceThread = new Thread(alice::eat);
        Thread bobThread = new Thread(bob::eat);

        aliceThread.start();
        bobThread.start();

        aliceThread.join(5000);
        bobThread.join(5000);

        assertTrue(alice.isEating(), "Alice should eventually eat");
        assertTrue(bob.isEating(), "Bob should eventually eat");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testMultipleDinersShouldNotLivelock() throws InterruptedException {
        Diner diner = new Diner();
        
        Person person1 = new Person("Person1", true, false, diner);
        Person person2 = new Person("Person2", false, true, diner);
        Person person3 = new Person("Person3", true, false, diner);
        Person person4 = new Person("Person4", false, true, diner);

        Thread t1 = new Thread(person1::eat);
        Thread t2 = new Thread(person2::eat);
        Thread t3 = new Thread(person3::eat);
        Thread t4 = new Thread(person4::eat);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        t1.join(5000);
        t2.join(5000);
        t3.join(5000);
        t4.join(5000);

        assertTrue(person1.isEating(), "Person1 should eventually eat");
        assertTrue(person2.isEating(), "Person2 should eventually eat");
        assertTrue(person3.isEating(), "Person3 should eventually eat");
        assertTrue(person4.isEating(), "Person4 should eventually eat");
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testLivelockShouldResolveWithBackoff() throws InterruptedException {
        for (int attempt = 0; attempt < 5; attempt++) {
            Diner diner = new Diner();
            
            Person charlie = new Person("Charlie", true, false, diner);
            Person diana = new Person("Diana", false, true, diner);

            Thread charlieThread = new Thread(charlie::eat);
            Thread dianaThread = new Thread(diana::eat);

            charlieThread.start();
            dianaThread.start();

            charlieThread.join(2000);
            dianaThread.join(2000);

            if (charlieThread.isAlive() || dianaThread.isAlive()) {
                charlieThread.interrupt();
                dianaThread.interrupt();
            }

            assertTrue(charlie.isEating() && diana.isEating(), 
                       "Both should eventually eat on attempt " + attempt);
        }
    }
}

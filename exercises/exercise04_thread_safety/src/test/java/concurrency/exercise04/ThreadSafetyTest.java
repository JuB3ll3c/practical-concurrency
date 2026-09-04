package concurrency.exercise04;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadSafetyTest {

    @Test
    public void testConcurrentRegistrationShouldNotLoseUsers() throws InterruptedException {
        UserRegistry registry = new UserRegistry();
        int numThreads = 50;
        int usersPerThread = 100;
        int expectedCount = numThreads * usersPerThread;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < usersPerThread; j++) {
                    String username = "user_" + threadId + "_" + j;
                    registry.registerUser(username);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(expectedCount, registry.getUserCount());
    }

    @Test
    public void testConcurrentRegistrationShouldNotCreateDuplicates() throws InterruptedException {
        UserRegistry registry = new UserRegistry();
        int numThreads = 50;
        int registrationsPerThread = 100;
        String username = "duplicate_user";

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < registrationsPerThread; j++) {
                    registry.registerUser(username);
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        assertEquals(1, registry.getUserCount());
        assertTrue(registry.containsUser(username));
    }

    @Test
    public void testMixedOperationsShouldBeConsistent() throws InterruptedException {
        UserRegistry registry = new UserRegistry();
        int numThreads = 20;
        String[] usernames = new String[100];
        
        for (int i = 0; i < usernames.length; i++) {
            usernames[i] = "user_" + i;
        }

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            final int startIndex = i * 5;
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    int index = startIndex + j;
                    if (index < usernames.length) {
                        registry.registerUser(usernames[index]);
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        if (index % 3 == 0) {
                            registry.unregisterUser(usernames[index]);
                        }
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

        Set<String> registeredUsers = new HashSet<>(registry.getAllUsers());
        for (String username : usernames) {
            boolean shouldBeRegistered = true;
            for (int i = 0; i < numThreads; i++) {
                int startIndex = i * 5;
                for (int j = 0; j < 5; j++) {
                    int index = startIndex + j;
                    if (index == Integer.parseInt(username.split("_")[1])) {
                        if (index % 3 == 0) {
                            shouldBeRegistered = false;
                        }
                        break;
                    }
                }
            }
            if (shouldBeRegistered) {
                assertTrue(registeredUsers.contains(username), 
                           "User " + username + " should be registered");
            } else {
                assertTrue(!registeredUsers.contains(username), 
                           "User " + username + " should not be registered");
            }
        }
    }
}

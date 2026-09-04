package concurrency.exercise07;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MemoryConsistencyTest {

    @Test
    public void testMessageShouldBeVisibleToReader() throws InterruptedException {
        Message message = new Message();
        String expectedContent = "Hello, Concurrency!";

        Thread writerThread = new Thread(() -> {
            message.setMessage(expectedContent);
        });

        Thread readerThread = new Thread(() -> {
            while (!message.isReady()) {
                Thread.yield();
            }
            String content = message.getContent();
            assertNotNull(content, "Content should not be null");
            assertEquals(expectedContent, content, "Content should match expected value");
        });

        writerThread.start();
        readerThread.start();

        writerThread.join();
        readerThread.join(5000);
    }

    @Test
    public void testReorderingShouldNotBreakConsistency() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Message message = new Message();
            String expectedContent = "Test-" + i;

            Thread writerThread = new Thread(() -> {
                message.setMessage(expectedContent);
            });

            Thread readerThread = new Thread(() -> {
                while (!message.isReady()) {
                    Thread.yield();
                }
                String content = message.getContent();
                assertEquals(expectedContent, content, 
                           "Content should be visible after ready flag on iteration " + i);
            });

            writerThread.start();
            readerThread.start();

            writerThread.join();
            readerThread.join(1000);

            if (readerThread.isAlive()) {
                readerThread.interrupt();
                readerThread.join();
            }
        }
    }

    @Test
    public void testMultipleWritersSingleReader() throws InterruptedException {
        Message message = new Message();
        String[] expectedContents = new String[100];
        for (int i = 0; i < expectedContents.length; i++) {
            expectedContents[i] = "Message-" + i;
        }

        Thread[] writerThreads = new Thread[expectedContents.length];
        for (int i = 0; i < expectedContents.length; i++) {
            final String content = expectedContents[i];
            writerThreads[i] = new Thread(() -> {
                message.setMessage(content);
            });
        }

        Thread readerThread = new Thread(() -> {
            String lastContent = null;
            for (int i = 0; i < expectedContents.length * 2; i++) {
                if (message.isReady()) {
                    String content = message.getContent();
                    if (content != null) {
                        lastContent = content;
                    }
                }
                Thread.yield();
            }
            assertNotNull(lastContent, "At least one message should have been received");
        });

        readerThread.start();
        for (Thread writerThread : writerThreads) {
            writerThread.start();
        }

        readerThread.join(5000);
        for (Thread writerThread : writerThreads) {
            writerThread.join();
        }
    }
}

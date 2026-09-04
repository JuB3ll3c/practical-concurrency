package concurrency.exercise05;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskScheduler {
    private final Queue<Task> taskQueue = new PriorityQueue<>((t1, t2) -> {
        return Integer.compare(t2.priority, t1.priority);
    });
    
    private final Lock lock = new ReentrantLock();
    private volatile boolean running = true;

    public void addTask(Task task) {
        lock.lock();
        try {
            taskQueue.add(task);
        } finally {
            lock.unlock();
        }
    }

    public void executeNext() {
        lock.lock();
        try {
            if (!taskQueue.isEmpty()) {
                Task task = taskQueue.poll();
                task.execute();
            }
        } finally {
            lock.unlock();
        }
    }

    public void executeAll() {
        while (running) {
            executeNext();
        }
    }

    public void stop() {
        running = false;
    }

    public int getQueueSize() {
        lock.lock();
        try {
            return taskQueue.size();
        } finally {
            lock.unlock();
        }
    }

    public static class Task {
        private final String name;
        private final int priority;
        private volatile boolean executed = false;

        public Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        public void execute() {
            executed = true;
        }

        public boolean isExecuted() {
            return executed;
        }

        public String getName() {
            return name;
        }

        public int getPriority() {
            return priority;
        }
    }
}

# Practical Concurrency

> **A hands-on Java concurrency learning project** inspired by [Practical Reactor](https://github.com/loicmathieu/practical-reactor).
> **Objective**: Learn Java concurrency through failing tests that must be fixed.

---

## Project Overview

This repository contains **exercises** with **failing tests** that demonstrate common concurrency issues in Java (race conditions, visibility problems, deadlocks, etc.).
Each exercise includes:
- Failing tests (demonstrating the problem)
- Minimal description (topic context only)
- Buggy source code that needs to be fixed

The goal is to **understand the problem**, **fix the code**, and **make the tests pass** without any hints or solutions provided.

---

## Project Structure

```
practical-concurrency/
├── README.md                     # This file
├── pom.xml                       # Maven parent POM
├── .gitignore
│
├── exercises/                    # All exercises with failing tests
│   ├── exercise01_race_conditions/   # Race conditions in shared state
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise01/Account.java
│   │       └── test/java/concurrency/exercise01/RaceConditionTest.java
│   │
│   ├── exercise02_visibility/         # Java Memory Model visibility
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise02/InterestRate.java
│   │       └── test/java/concurrency/exercise02/VisibilityTest.java
│   │
│   ├── exercise03_deadlocks/          # Circular dependency between locks
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise03/Account.java
│   │       └── test/java/concurrency/exercise03/DeadlockTest.java
│   │
│   ├── exercise04_thread_safety/      # Non-thread-safe collections
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise04/UserRegistry.java
│   │       └── test/java/concurrency/exercise04/ThreadSafetyTest.java
│   │
│   ├── exercise05_starvation/         # Thread priority and fairness
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise05/TaskScheduler.java
│   │       └── test/java/concurrency/exercise05/StarvationTest.java
│   │
│   ├── exercise06_livelock/           # Threads trapped in non-progressive state
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise06/Diner.java
│   │       ├── main/java/concurrency/exercise06/Person.java
│   │       └── test/java/concurrency/exercise06/LivelockTest.java
│   │
│   ├── exercise07_memory_consistency/# Happens-before relationship
│   │   ├── README.md
│   │   ├── pom.xml
│   │   └── src/
│   │       ├── main/java/concurrency/exercise07/Message.java
│   │       └── test/java/concurrency/exercise07/MemoryConsistencyTest.java
│   │
│   └── exercise08_thread_pools/       # Task execution in thread pools
│       ├── README.md
│       ├── pom.xml
│       └── src/
│           ├── main/java/concurrency/exercise08/TaskExecutor.java
│           └── test/java/concurrency/exercise08/ThreadPoolTest.java
│
└── solutions/                    # (To be created by you!)
    └── (Your fixed implementations go here)
```

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.8+

### Build and Test

To build all exercises:
```bash
mvn clean compile
```

To run tests for a specific exercise:
```bash
cd exercises/exercise01_race_conditions
mvn test
```

To run all tests:
```bash
mvn test
```

---

## Exercises Summary

| Exercise | Topic | Key Concept |
|----------|-------|-------------|
| 01 | Race Conditions | Atomic operations, synchronized |
| 02 | Visibility | Java Memory Model, volatile |
| 03 | Deadlocks | Lock ordering, timeout |
| 04 | Thread Safety | Thread-safe collections, synchronization |
| 05 | Starvation | Fairness, lock policies |
| 06 | Livelock | Progress, backoff strategies |
| 07 | Memory Consistency | Happens-before, reordering |
| 08 | Thread Pools | Task execution, exception handling |

---

## Approach

For each exercise:
1. Read the README.md to understand the topic
2. Run the tests with `mvn test` - they should fail
3. Analyze the source code to identify the concurrency issue
4. Fix the code to make the tests pass
5. Verify your solution works consistently

**No solutions are provided** - the learning comes from figuring it out yourself!

---

## Contributing

Feel free to add more exercises by following the same pattern. Each exercise should:
- Have a clear topic description in README.md
- Contain buggy code that demonstrates a concurrency issue
- Include tests that fail due to the bug
- Be solvable without external hints

---

## License

MIT License - Feel free to use this for learning and teaching purposes.

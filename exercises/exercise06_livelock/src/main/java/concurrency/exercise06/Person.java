package concurrency.exercise06;

import java.util.Random;

public class Person {
    private final String name;
    private final boolean hasSalt;
    private final boolean hasPepper;
    private boolean eating = false;
    private final Diner diner;
    private final Random random = new Random();

    public Person(String name, boolean hasSalt, boolean hasPepper, Diner diner) {
        this.name = name;
        this.hasSalt = hasSalt;
        this.hasPepper = hasPepper;
        this.diner = diner;
    }

    public void eat() {
        while (!eating) {
            if (hasSalt && !hasPepper) {
                if (diner.isPepperAvailable()) {
                    System.out.println(name + " getting pepper");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (diner.takePepper()) {
                        eating = true;
                        System.out.println(name + " is eating");
                    } else {
                        System.out.println(name + " failed to get pepper, passing salt");
                        diner.setSaltAvailable(true);
                    }
                } else {
                    System.out.println(name + " passing salt");
                    diner.setSaltAvailable(true);
                }
            } else if (hasPepper && !hasSalt) {
                if (diner.isSaltAvailable()) {
                    System.out.println(name + " getting salt");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (diner.takeSalt()) {
                        eating = true;
                        System.out.println(name + " is eating");
                    } else {
                        System.out.println(name + " failed to get salt, passing pepper");
                        diner.setPepperAvailable(true);
                    }
                } else {
                    System.out.println(name + " passing pepper");
                    diner.setPepperAvailable(true);
                }
            } else {
                eating = true;
                System.out.println(name + " already has both, eating");
            }

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean isEating() {
        return eating;
    }

    public String getName() {
        return name;
    }
}

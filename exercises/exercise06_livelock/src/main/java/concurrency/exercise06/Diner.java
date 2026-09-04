package concurrency.exercise06;

public class Diner {
    private boolean saltAvailable = true;
    private boolean pepperAvailable = true;

    public synchronized boolean isSaltAvailable() {
        return saltAvailable;
    }

    public synchronized boolean isPepperAvailable() {
        return pepperAvailable;
    }

    public synchronized boolean takeSalt() {
        if (saltAvailable) {
            saltAvailable = false;
            return true;
        }
        return false;
    }

    public synchronized boolean takePepper() {
        if (pepperAvailable) {
            pepperAvailable = false;
            return true;
        }
        return false;
    }

    public synchronized void setSaltAvailable(boolean available) {
        saltAvailable = available;
    }

    public synchronized void setPepperAvailable(boolean available) {
        pepperAvailable = available;
    }
}

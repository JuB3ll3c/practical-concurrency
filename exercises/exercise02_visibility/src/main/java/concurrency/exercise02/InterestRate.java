package concurrency.exercise02;

public class InterestRate {
    private static boolean rateUpdated = false;
    private static double currentRate = 0.0;

    public static void updateRate(double newRate) {
        currentRate = newRate;
        rateUpdated = true;
    }

    public static double getCurrentRate() {
        if (rateUpdated) {
            return currentRate;
        }
        return 0.0;
    }

    public static boolean isRateUpdated() {
        return rateUpdated;
    }
}

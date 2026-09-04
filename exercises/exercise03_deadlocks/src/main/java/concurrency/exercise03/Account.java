package concurrency.exercise03;

public class Account {
    private final String id;
    private int balance;

    public Account(String id, int initialBalance) {
        this.id = id;
        this.balance = initialBalance;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public void transfer(Account toAccount, int amount) {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " locked " + this.id);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            synchronized (toAccount) {
                System.out.println(Thread.currentThread().getName() + " locked " + toAccount.id);
                this.withdraw(amount);
                toAccount.deposit(amount);
            }
        }
    }
}

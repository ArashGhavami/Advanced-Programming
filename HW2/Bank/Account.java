import java.util.ArrayList;

public class Account {
    static private ArrayList<Account> allAccounts = new ArrayList<>();
    private Bank bank;
    private int id;
    private int money;
    private int remainingDuration;
    private int interest;
    private Customer customer;

    public Account(Bank bank, Customer customer, int id, int money, int duration, int interest) {
        this.bank = bank;
        this.customer = customer;
        this.id = id;
        this.money = money;
        this.remainingDuration = duration;
        this.interest = interest;
    }

    private void passMonthEach() {
        this.remainingDuration--;
        if (this.remainingDuration == 0) {
            Customer customer = this.customer;
            customer.setMoneyInSafe((1 + (double) this.interest / 100) * this.money + customer.getMoneyInSafe());
            ArrayList<Account> newAccounts = customer.getAccounts();
            newAccounts.remove(this);
            customer.setAllAcounts(newAccounts);
        }

    }

    public static void passMonth() {
        for (Account account : allAccounts) {
            account.passMonthEach();
        }
    }

    public static void deleteAccount(Account account) {
        ArrayList<Account> newAccounts = new ArrayList<>();
        for (Account account2 : allAccounts) {
            if (!account2.equals(account)) {
                newAccounts.add(account2);
            }
        }
        allAccounts = newAccounts;
    }

    public int getId() {
        return this.id;
    }

    public double getAmountOfMoneyForLeaving() {
        if (this.remainingDuration == 0) {
            return (1 + this.interest / 100) * this.money;
        } else {
            return this.money;
        }
    }

    public Bank getBank() {
        return this.bank;
    }


    public static void addAcountToAllAccounts(Account account) {
        Account.allAccounts.add(account);
    }


}

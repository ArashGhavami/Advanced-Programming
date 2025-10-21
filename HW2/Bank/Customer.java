import java.util.ArrayList;

public class Customer {
    static private ArrayList<Customer> allCustomers = new ArrayList<>();
    private String name;
    private double moneyInSafe;
    private ArrayList<Account> allActiveAccounts = new ArrayList<>();
    private int totalNumberOfAccountsCreated;
    {
        totalNumberOfAccountsCreated = 0;
    }
    private int negativeScore;

    public Customer(String name, double moneyInSafe) {
        this.name = name;
        this.moneyInSafe = moneyInSafe;
    }

    static public Customer getCustomerByName(String name) {
        for (Customer customer : allCustomers) {
            if (customer.name.equals(name)) {
                return customer;
            }
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    public void createNewAccount(Bank bank, int money, int duration, int interest) {
        Account account = new Account(bank, this, this.totalNumberOfAccountsCreated, money, duration, interest);
        Account.addAcountToAllAccounts(account);
        this.allActiveAccounts.add(account);
    }

    public void leaveAccount(int accountId) {
        for (Account account : allActiveAccounts) {
            if (account.getId() == accountId) {
                this.moneyInSafe += account.getAmountOfMoneyForLeaving();
            }
        }
        ArrayList<Account> newAccountsList = new ArrayList<>();
        for (Account account : allActiveAccounts) {
            if (account.getId() != accountId) {
                newAccountsList.add(account);
            }
            else {
                account.deleteAccount(account);
            }
        }
        allActiveAccounts = newAccountsList;
    }

    public boolean canPayLoan(double amount) {
        if (this.moneyInSafe >= amount)
            return true;
        else
            return false;
    }

    public double getMoneyInSafe() {
        return this.moneyInSafe;
    }

    public void setMoneyInSafe(double moneyInSafe) {
        this.moneyInSafe = moneyInSafe;
    }


    public static void addCustomerToAllCustomers(Customer customer) {
        allCustomers.add(customer);
    }

    public static void showAllCustomers() {
        for (Customer customer : allCustomers) {
            System.out.println(customer.name + " " + customer.moneyInSafe);
        }
    }

    public int getTotalNumberOfAccount() {
        return this.totalNumberOfAccountsCreated;
    }

    public void setTotalNumberOfAccount() {
        this.totalNumberOfAccountsCreated++;
    }

    public boolean doesItHaveAccount(Customer customer, Bank bank) { // hasActiveAccountinBank
        for (Account account : this.allActiveAccounts) {
            if (account.getBank().equals(bank)) {
                return true;
            }
        }
        return false;
    }

    public int getNegativeScore() {
        return this.negativeScore;
    }

    public void setNegativePoints() { //addNegativeScore
        this.negativeScore++;
    }

    public ArrayList<Account> getAccounts(){
        return allActiveAccounts;
    }

    public void setAllAcounts(ArrayList<Account> allAcounts){
        this.allActiveAccounts = allAcounts;
    }

    public Bank getBankById(int n){
        for(Account account : allActiveAccounts){
            if(account.getId() == n){
                return account.getBank();
            }
        }
    return   null;
    }
}

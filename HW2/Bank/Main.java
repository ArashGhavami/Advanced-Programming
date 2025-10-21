import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Main {

    public static void main(String[] args) {

        String input = "";
        Scanner scanner = new Scanner(System.in);
        outerLoop:
        while (true) {
            input = scanner.nextLine();
            if (input.equals("Base dige, berid khonehatoon."))
                break;
            Matcher matcher;
            // creating new bank
            matcher = getMatcher(input, "Create bank \\s*(.*\\S)\\s*\\.\\s*");
            if (matcher.matches()) {
                String bankName = matcher.group(1);
                Bank bank = new Bank(bankName);
                Bank.addBankToAllBanks(bank);
                continue outerLoop;
            }
            // adding customer
            matcher = getMatcher(input,
                    "Add a customer with name \\s*(?<name>.*\\S)\\s* and (?<money>\\d+) unit initial money\\.\\s*");
            if (matcher.matches()) {
                String customerName = matcher.group("name");
                double moneyInSafe = Double.parseDouble(matcher.group("money"));
                Customer customer = new Customer(customerName, moneyInSafe);
                Customer.addCustomerToAllCustomers(customer);
                continue outerLoop;
            }
            //creating new account
            matcher = getMatcher(input,
                    "Create a (?<accountType>KOOTAH|BOLAN|VIZHE) account for \\s*(?<customerName>.*\\S)\\s* in \\s*(?<bankName>.*\\S)\\s*, with duration (?<time>\\d+) and initial deposit of (?<money>\\d+)\\.\\s*");
            if (matcher.matches()) {
                String accountType = matcher.group("accountType");
                String customerName = matcher.group("customerName");
                String bankName = matcher.group("bankName");
                int duration = Integer.parseInt(matcher.group("time"));
                int depositMoney = Integer.parseInt(matcher.group("money"));
                int interest = 0;
                Customer customer = Customer.getCustomerByName(customerName);
                interest = Bank.getAccountInterestFromName(accountType);
                boolean everyThingOK = true;
                boolean isThereBankWithName = Bank.isThereBankWithName(bankName);
                if (isThereBankWithName == false) {
                    System.out.println("In dige banke koodoom keshvarie?");
                    everyThingOK = false;
                }
                if (customer.getMoneyInSafe() < depositMoney) {
                    everyThingOK = false;
                    System.out.println("Boro baba pool nadari!");
                }
                if (everyThingOK == false)
                    continue outerLoop;
                else {
                    Bank bank = Bank.getBankWithName(bankName);
                    customer.setTotalNumberOfAccount();
                    customer.createNewAccount(bank, depositMoney, duration, interest);
                    double newMoneyInSafe = customer.getMoneyInSafe() - depositMoney;
                    customer.setMoneyInSafe(newMoneyInSafe);
                }
                continue outerLoop;
            }

            //Does A have account in bank B?
            matcher = getMatcher(input,
                    "Does \\s*(?<customer>.*\\S)\\s* have active account in \\s*(?<bank>.*\\S)\\s*\\?\\s*");
            if (matcher.matches()) {
                String customerName = matcher.group("customer");
                String bankName = matcher.group("bank");
                Customer customer = Customer.getCustomerByName(customerName);
                Bank bank = Bank.getBankWithName(bankName);
                boolean doesItHaveAccount = customer.doesItHaveAccount(customer, bank);
                if (doesItHaveAccount)
                    System.out.println("yes");
                else
                    System.out.println("no");
                continue outerLoop;
            }

            //A's bad points
            matcher = getMatcher(input, "Print \\s*(?<customer>.*\\S)\\s*'s NOMRE MANFI count\\.\\s*");
            if (matcher.matches()) {
                String customerName = matcher.group("customer");
                Customer customer = Customer.getCustomerByName(customerName);
                System.out.println(customer.getNegativeScore());
                continue outerLoop;
            }

            //A's money in safe
            matcher = getMatcher(input, "Print \\s*(?<customer>.*\\S)\\s*'s GAVSANDOOGH money\\.\\s*");
            if (matcher.matches()) {
                String customerName = matcher.group("customer");
                Customer customer = Customer.getCustomerByName(customerName);
                System.out.println((int) customer.getMoneyInSafe());
                continue outerLoop;
            }

            //giving money back
            matcher = getMatcher(input,
                    "Give \\s*(?<customer>.*\\S)\\s*'s money out of his account number (?<number>\\d+)\\.\\s*");
            if (matcher.matches()) {
                String customerName = matcher.group("customer");
                int accountNumber = Integer.parseInt(matcher.group("number"));
                Customer customer = Customer.getCustomerByName(customerName);
                if (customer == null) {
                    System.out.println("Chizi zadi?!");

                }
                else{
                    Bank bank = customer.getBankById(accountNumber);

                    if(!customer.doesItHaveAccount(customer, bank)){
                        System.out.println("Chizi zadi?!");
                        continue  outerLoop;
                    }
                    customer.leaveAccount(accountNumber);

                }

                continue outerLoop;
            }

            //getting loan
            matcher = getMatcher(input,
                    "Pay a (?<loanMoney>\\d+) unit loan with %(?<interestRate>\\d+) interest and (?<duration>6|12) payments from \\s*(?<bank>.*\\S)\\s* to \\s*(?<customer>.*\\S)\\s*\\.\\s*");
            if (matcher.matches()) {
                int amount = Integer.parseInt(matcher.group("loanMoney"));
                int interestRate = Integer.parseInt(matcher.group("interestRate"));
                int duration = Integer.parseInt(matcher.group("duration"));
                String bankName = matcher.group("bank");
                String customerName = matcher.group("customer");
                boolean isThereBankWithName = Bank.isThereBankWithName(bankName);
                boolean everyThingOK = true;
                if (isThereBankWithName == false) {
                    System.out.println("Gerefti maro nesfe shabi?");
                    everyThingOK = false;
                }
                Customer customer = Customer.getCustomerByName(customerName);
                if (customer.getNegativeScore() >= 5) {
                    System.out.println("To yeki kheyli vazet bade!");
                    everyThingOK = false;
                }
                if (everyThingOK == false)
                    continue outerLoop;
                else {
                    Loan loan = new Loan(customer, duration, interestRate, amount);
                    customer.setMoneyInSafe(customer.getMoneyInSafe() + (double) amount);
                    continue outerLoop;
                }
            }

            //passing time
            matcher = getMatcher(input, "Pass time by (?<time>\\d+) unit\\.\\s*");
            if (matcher.matches()) {
                int time = Integer.parseInt(matcher.group("time"));
                for (int i = 0; i < time; i++) {
                    passMonth();
                }
            }

        }
    }

    private static void passMonth() {
        Loan.passMonth();
        Account.passMonth();
    }

    static private Matcher getMatcher(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher;
    }



}
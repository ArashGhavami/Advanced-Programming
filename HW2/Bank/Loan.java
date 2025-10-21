import java.util.ArrayList;

public class Loan {
    private static ArrayList<Loan> allLoans = new ArrayList<>();
    private Customer customer;
    private int duration;
    private int remainingPayments;
    private int interest;
    private int amount;

    public Loan(Customer customer, int duration, int interest, int amount) {
        this.customer = customer;
        this.duration = duration;
        this.interest = interest;
        this.amount = amount;
        this.remainingPayments = duration;
        allLoans.add(this);
    }

    public static void passMonth() {
        for (Loan loan : allLoans) {
            loan.passMonthEach();
        }
    }

    private double getPaymentAmount(Loan loan) {
        return (double)loan.amount * (1 + (double)loan.interest / 100) / (double)loan.duration;
    }

    private void passMonthEach() {
        Customer customer = this.customer;
        double payment = getPaymentAmount(this);
        if (customer.canPayLoan(payment)) {
            customer.setMoneyInSafe(customer.getMoneyInSafe() - payment);
            this.remainingPayments--;
            if (this.remainingPayments == 0) {
                deleteLoan(this);
            }
        } else {
            customer.setNegativePoints();
        }
    }

    private static void deleteLoan(Loan loan) {
        ArrayList<Loan> newLoans = new ArrayList<>();
        for (Loan loan2 : allLoans) {
            if (!loan2.equals(loan)) {
                newLoans.add(loan2);
            }
        }
        allLoans = newLoans;
    }
}
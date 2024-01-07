/**
 * The Main class simulates the bank app.
 * The application creates and manages bank accounts, transaction processors, and a transaction generator.
 * It prints out the details of accounts and transaction processor threads after the simulation.
 *
 * @Daniel_Szuflicki
 */

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();

        /**
         * Create and add three Account instances to the bank with different starting balances.
         */
        try {
            Account account1 = new Account(12345, Money.of(CurrencyUnit.EUR, 10000));
            Account account2 = new Account(12346, Money.of(CurrencyUnit.EUR, 5000));
            Account account3 = new Account(12347, Money.of(CurrencyUnit.EUR, 15000));

            bank.addAccount(account1);
            bank.addAccount(account2);
            bank.addAccount(account3);
        } catch (NegativeBalanceException e) {
            System.err.println("Error creating account: " + e.getMessage());
        }

        /**
         * Declare and instantiate two TransactionProcessor threads and one
         * RandomTransactionGenerator thread.
         */
        TransactionProcessor tp1 = new TransactionProcessor("Thread-0", bank);
        TransactionProcessor tp2 = new TransactionProcessor("Thread-1", bank);
        RandomTransactionGenerator rtg = new RandomTransactionGenerator(bank, "RandomTransactionGenerator");

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(tp1);
        executorService.submit(tp2);
        executorService.submit(rtg);

        /**
         * Sleep for 10s
         */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rtg.interrupt();

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /**
         * Prints out the details of the accounts after the transactions have finished.
         */
        for (int accountNumber : bank.getAccountNumbers()) {
            Account account = bank.getAccount(accountNumber);
            System.out.println("Account " + account.getAccountNumber() + " has a balance of " + account.getBalance() + ".");
        }

        /**
         * Print out the details of TransactionProcessor threads.
         */
        System.out.println("Transaction generator terminated.");
        System.out.println(tp2.getName() + " finished processing " + tp2.getTotalTransactions() +
                " transactions, including " + tp2.getDepositCount() + " deposits, and " + tp2.getWithdrawalCount() + " withdrawals.");
        System.out.println(tp1.getName() + " finished processing " + tp1.getTotalTransactions() +
                " transactions, including " + tp1.getDepositCount() + " deposits, and " + tp1.getWithdrawalCount() + " withdrawals.");
    }
}

/**
 * The TransactionProcessor class represents a thread that processes
 * transactions in a bank account. It processes deposit and withdrawal transactions,
 * updates account balances and gives details on the transactions.
 *
 * @Daniel_Szuflicki
 */

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.RoundingMode;
import java.util.Random;

public class TransactionProcessor extends Thread {
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    private final String name;
    private final Bank bank;
    private int depositCount;
    private int withdrawalCount;
    private int totalTransactions;
    private final Random random;

    /**
     * Constructs a TransactionProcessor object.
     *
     * @param name The name of the transaction processor thread.
     * @param bank The Bank instance associated with the transaction processor.
     */
    public TransactionProcessor(String name, Bank bank) {
        this.name = name;
        this.bank = bank;
        this.depositCount = 0;
        this.withdrawalCount = 0;
        this.totalTransactions = 0;
        this.random = new Random();
    }

    /**
     * Runs the transaction processor thread.
     * Processes transactions from the bank's queue, updates account balances and tracks transaction details.
     * The thread runs for 10 seconds or until interrupted.
     */
    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            while (!Thread.currentThread().isInterrupted() && (System.currentTimeMillis() - startTime) < 10000) {
                Transaction transaction = bank.getNextTransaction();

                if (transaction == null || transaction.getAccountNumber() == 0) {
                    break;
                }

                processTransaction(transaction);

                Thread.sleep(random.nextInt(1000));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.printf("Thread %s was interrupted!\n", name);
        }
    }

    /**
     * Processes a transaction by updating the associated account's balance.
     * Updates the transaction details (total transactions, deposits, withdrawals).
     *
     * @param transaction The Transaction to be processed.
     */
    private void processTransaction(Transaction transaction) {
        totalTransactions++;
        int accountNumber = transaction.getAccountNumber();
        Account account = bank.getAccount(accountNumber);

        float transactionValue = transaction.getAmount();

        if (transactionValue < 0) {
            try {
                System.out.printf("%s is processing %s\n", name, transaction);
                account.makeWithdrawal(Money.of(CurrencyUnit.EUR, -transactionValue, ROUNDING_MODE));
                withdrawalCount++;
            } catch (InsufficientFundsException e) {
                System.err.printf("Account No. %d has insufficient funds!\n", accountNumber);
            }
        } else {
            System.out.printf("%s is processing %s\n", name, transaction);
            account.makeDeposit(Money.of(CurrencyUnit.EUR, transactionValue, ROUNDING_MODE));
            depositCount++;
        }
    }

    /**
     * Gets the total number of transactions.
     *
     * @return The total number of transactions processed.
     */
    public int getTotalTransactions() {
        return totalTransactions;
    }

    /**
     * Gets the total number of deposit transactions.
     *
     * @return The total number of deposit transactions processed.
     */
    public int getDepositCount() {
        return depositCount;
    }

    /**
     * Gets the total number of withdrawal transactions.
     *
     * @return The total number of withdrawal transactions processed.
     */
    public int getWithdrawalCount() {
        return withdrawalCount;
    }
}

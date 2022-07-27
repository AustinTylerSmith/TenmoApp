package model;

import java.math.BigDecimal;

public class Account {
    private int accountId;
    private int userId;
    private BigDecimal balance;
    private final BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(1000);

    public Account () {

    }

    public Account(int userId) {
        this.userId = userId;
        this.balance = DEFAULT_BALANCE;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getUserId() {
        return userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}

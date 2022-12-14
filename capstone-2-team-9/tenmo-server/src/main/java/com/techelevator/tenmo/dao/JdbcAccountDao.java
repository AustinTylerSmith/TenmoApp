package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.Exceptions.AccountNotFound;
import com.techelevator.tenmo.Exceptions.InsufficientBalance;
import com.techelevator.tenmo.Exceptions.InvalidAmount;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amount, int accountId) throws InsufficientBalance,
            AccountNotFound, InvalidAmount {
        if (findByAccountId(accountId).getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalance();
        } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmount();
        }
        String sql = "UPDATE account " +
                "SET balance = balance - ? " +
                "WHERE account_id = ? " +
                "RETURNING balance;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, amount, accountId);
    }

    @Override
    public BigDecimal deposit(BigDecimal amount, int accountId) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ? " +
                "RETURNING balance;";
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, amount, accountId);
    }

    @Override
    public void create(Account account) throws DataAccessException {
        String sql = "INSERT INTO account " +
                "(user_id,balance) " +
                "VALUES(?,?);";
        jdbcTemplate.update(sql, account.getUserId(), account.getBalance());
    }

    @Override
    public List<Account> listAccounts() {
        List<Account> accountList = new ArrayList<>();
        String sql = "SELECT account_id, user_id, balance FROM account;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            accountList.add(mapRowToAccount(rowSet));
        }
        return accountList;
    }

    @Override
    public Account findByAccountId(int id) throws AccountNotFound {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        throw new AccountNotFound();
    }

    @Override
    public Account findByUserId(int userId) throws AccountNotFound {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }
        throw new AccountNotFound();
    }

    @Override
    public int getUserIdAssociatedWithAccount(int accountId) {
        String sql = "SELECT tu.user_id FROM account AS ac JOIN tenmo_user AS tu ON ac.user_id = tu.user_id WHERE ac.account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if(rowSet.next()) {
            return rowSet.getInt("user_id");
        }
        return 0;
    }

    private Account mapRowToAccount (SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}

package services;

import model.Transaction;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class TransactionServices {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private  RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) { this.authToken = authToken;}

    public Transaction[] listAccountTransactions(int accountId)  {
        Transaction[] transactions = null;
        try {
            ResponseEntity<Transaction[]> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/transactions", HttpMethod.GET, makeAuthEntity(), Transaction[].class);
            transactions = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error listAccountTransactions method");
        }
        return transactions;
    }

    public Transaction[] listPendingAccountTransactions(int accountId) {
        Transaction[] transactions = null;
        try {
            ResponseEntity<Transaction[]> response =
                    restTemplate.exchange(API_BASE_URL + "accounts/" + accountId + "/pending_transactions", HttpMethod.GET, makeAuthEntity(), Transaction[].class);
            transactions = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error pendingTransactions method");
        }
        return transactions;
    }

    public Transaction getTransaction(int transactionId) {
        Transaction transaction = null;
        try {
            ResponseEntity<Transaction> response =
                    restTemplate.exchange(API_BASE_URL + "transactions/" + transactionId, HttpMethod.GET, makeAuthEntity(),Transaction.class);
            transaction = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error getTransaction method");
        }
        return transaction;
    }

    public Transaction addTransaction(Transaction newTransaction) {
        HttpEntity<Transaction> entity = makeTransactionEntity(newTransaction);
        Transaction transaction = null;
        try {
            transaction = restTemplate.postForObject(API_BASE_URL + "transactions", entity, Transaction.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return transaction;
    }

    public boolean approveTransaction(int transactionId) {
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "transactions/" + transactionId + "/accept", makeAuthEntity(), void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    public boolean rejectTransaction(int transactionId) {
        boolean success = false;
        try {
            restTemplate.put(API_BASE_URL + "transactions/" + transactionId + "/reject", makeAuthEntity(), void.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error rejectTransaction method");
        }
        return success;
    }




    private HttpEntity<Transaction> makeTransactionEntity(Transaction transaction) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transaction, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}

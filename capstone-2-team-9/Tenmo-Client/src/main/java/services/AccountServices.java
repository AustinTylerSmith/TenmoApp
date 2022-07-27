package services;

import model.Account;
import model.Transaction;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class AccountServices {
    public static final String API_BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Account[] listAccounts() {
        Account[] accounts = null;
        try {
            ResponseEntity<Account[]> response = restTemplate.exchange(API_BASE_URL + "accounts",
                    HttpMethod.GET, makeAuthEntity(), Account[].class);
            accounts = response.getBody();
        } catch (ResponseStatusException | RestClientResponseException e) {
            System.out.println("Error at listAccounts Method");
        }
        return accounts;
    }

    public Account listAccountByUserId(int userId) {
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "accounts/user/" + userId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (ResponseStatusException | RestClientResponseException e) {
            System.out.println("Error at listAccounts Method");
        }
        return account;
    }

    public Account listAccountByAccountId(int accountId) {
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + "accounts/" + accountId,
                    HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        } catch (ResponseStatusException | RestClientResponseException e) {
            System.out.println("Error at listAccountsByAccountID Method");
        }
        return account;
    }

    public int recieveUserIdForAccount(int accountId) {
        int userId = 0;
        try {
        Integer response = restTemplate.getForObject(API_BASE_URL + "accounts/" + accountId + "/userid", Integer.class);
        userId = response;
        } catch (ResponseStatusException | RestClientResponseException e) {
            System.out.println(e.getMessage());
        }
        return userId;
    }



    private HttpEntity<Account> makeAccountEntity(Account account) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(account, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}

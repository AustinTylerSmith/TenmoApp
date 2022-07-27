package services;

import model.Transaction;
import model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserServices {
    public static final String API_BASE_URL = "http://localhost:8080/users";
    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) { this.authToken = authToken;}

    public User[] listUsersWithFriendlyInfo() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response =
                    restTemplate.exchange(API_BASE_URL, HttpMethod.GET, makeAuthEntity(), User[].class);
            users = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println("Error listUserFriendlyInfo method");
        }
        return users;
    }

    public String getUserIdsAndName(String Username) {
        User[] users = listUsersWithFriendlyInfo();
        String name = null;
        String userId = null;
        for(int i = 0; i < users.length; i++) {
            name = users[i].getUsername();
            userId = String.valueOf(users[i].getId());
            if(name.compareToIgnoreCase(Username) != 0) {
                System.out.println(userId + "       " + name + "\n");
            }
        }
        return null;
    }

    public String getUserIdByName(String name) {
        User[] users = listUsersWithFriendlyInfo();
        String id = null;
        for (User user : users) {
            if(user.getUsername().equals(name)) {
                id = String.valueOf(user.getId());
                return id;
            }
        }
        return null;
    }

    public String getNameByUserId(int userId) {
        User[] users = listUsersWithFriendlyInfo();
        String name = null;
        for(int i = 0; i < users.length; i++) {
            if (users[i].getId() == userId) {
                name = users[i].getUsername();
                return name;
            }
        }
        return null;
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}

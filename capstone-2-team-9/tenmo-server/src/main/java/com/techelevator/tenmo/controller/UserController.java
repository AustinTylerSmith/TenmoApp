package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.Exceptions.AccountNotFound;
import com.techelevator.tenmo.Exceptions.InsufficientBalance;
import com.techelevator.tenmo.Exceptions.InvalidAmount;
import com.techelevator.tenmo.Exceptions.InvalidEntry;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransactionsDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transaction;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {
    private UserDao userDao;

    public UserController (UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> listUserFriendlyInfo() {
        return userDao.listUserFriendlyInfo();
    }

}

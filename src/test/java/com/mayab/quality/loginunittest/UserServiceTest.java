package com.mayab.quality.loginunittest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.loginunittest.dao.DAOUser;
import com.mayab.quality.loginunittest.dao.IDAOUser;
import com.mayab.quality.loginunittest.model.User;
import com.mayab.quality.loginunittest.service.UserService;

public class UserServiceTest {
    private static IDAOUser daoUserMock;
    private static UserService userService;
    private static User userMock;
    private static List<User> dataBase;
    private static User userAlreadyRegistered;

    @BeforeAll
    public static void setUp() {
        daoUserMock = mock(DAOUser.class);
        userMock = mock(User.class);
        userService = new UserService(daoUserMock);
        dataBase = new ArrayList<>();
        userAlreadyRegistered = new User("email1@email.com", "user1", "password1");
        userAlreadyRegistered.setId(1);
        dataBase.add(userAlreadyRegistered);

        when(daoUserMock.findUserByEmail(anyString())).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                for (User userIn : dataBase) {
                    if (userIn.getEmail().equals(invocation.getArguments()[0])) {
                        System.out.println("Tienen el mismo email");
                        return userIn;
                    }
                }
                return null;
            }
        });
        when(daoUserMock.registerUser(any(User.class))).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = (User) invocation.getArguments()[0];
                user.setId(dataBase.size() + 1);
                dataBase.add(user);
                return user;
            }
        });

    }

    @Test
    public void testLogIn() {
        when(userMock.getUsername()).thenReturn("user");
        when(userMock.getPassword()).thenReturn("password");

        when(daoUserMock.findUserByUsername(anyString())).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                return userMock.getUsername().equals(invocation.getArguments()[0]) ? userMock : null;
            }
        });

        boolean result = userService.logIn("user", "password");

        assertTrue(result);
    }

    // When the user is created
    @Test
    public void creatingUser() {

        User userCreated = userService.createUser("newuser@email.com", "newUser", "123456789");
        if (userCreated != null) {
            System.out.println("ID: " + userCreated.getId() + " Username: " + userCreated.getUsername());

        }
        assertNotNull(userCreated);

    }

    @Test
    public void whenPasswordLong() {
        User userCreated = userService.createUser("newuser@email.com", "newUser", "12345678901234567890");
        if (userCreated == null) {
            System.out.println("Se excedió de longitud de password");

        }
        assertNull(userCreated);

    }

    @Test
    public void whenPasswordShort() {
        User userCreated = userService.createUser("newuser@email.com", "newUser", "123456");
        if (userCreated == null) {
            System.out.println("Se quedó corto de longitud de password");

        }
        assertNull(userCreated);

    }

    @Test
    public void whenUserAlreadyExist() {
        // Need to use an email already in use
        User userCreated = userService.createUser("email1@email.com", "newUser", "12345678");
        if (userCreated == null) {
            System.out.println("Ya existe el usuario con ese email");

        }
        assertNull(userCreated);

    }

}

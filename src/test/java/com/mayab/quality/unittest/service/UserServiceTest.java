package com.mayab.quality.unittest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.mayab.quality.unittest.dao.IDAOUser;
import com.mayab.quality.unittest.model.User;

public class UserServiceTest {
    private static IDAOUser daoUserMock;
    private static UserService userService;
    // private static User userMock;
    private static List<User> dataBase;
    private static User userAlreadyRegistered;

    @BeforeAll
    public static void setUp() {
        daoUserMock = mock(IDAOUser.class);
        // userMock = mock(User.class);
        userService = new UserService(daoUserMock);
        dataBase = new ArrayList<>();
        userAlreadyRegistered = new User("user1", "password1", "email1@email.com");
        userAlreadyRegistered.setId(1);
        dataBase.add(userAlreadyRegistered);
    }

    // TESTS

    /*
     * Test the method loginUser
     * 
     * @Test
     * public void testLogIn() {
     * 
     * when(userMock.getUsername()).thenReturn("user");
     * when(userMock.getPassword()).thenReturn("password");
     * 
     * when(daoUserMock.findByUserName(anyString())).thenAnswer(new Answer<User>() {
     * public User answer(InvocationOnMock invocation) throws Throwable {
     * return userMock.getUsername().equals(invocation.getArguments()[0]) ? userMock
     * : null;
     * }
     * });
     * 
     * boolean result = userService.logIn("user", "password");
     * 
     * assertTrue(result);
     * }
     */

    /*
     * 
     * CREATE
     * 
     * 
     */

    // When the user is created (HAPPY PATH)
    @Test
    public void creatingUser() {
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
        User userCreated = userService.createUser("newuser@email.com", "newUser", "123456789");
        if (userCreated != null) {
            System.out.println("ID: " + userCreated.getId() + " Username: " + userCreated.getUsername());

        }
        assertNotNull(userCreated);

    }

    // When the user is created and the email is already registered (UNHAPPY PATH)
    @Test
    public void whenUserAlreadyExist() {
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

        when(daoUserMock.save(any(User.class))).thenAnswer(new Answer<Integer>() {
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                User user = (User) invocation.getArguments()[0];
                dataBase.add(user);
                return dataBase.size();
            }
        });

        // Need to use an email already in use
        User userCreated = userService.createUser("email1@email.com", "newUser", "12345678");
        if (userCreated == null) {
            System.out.println("Ya existe el usuario con ese email");

        }

        assertNull(userCreated);

    }

    /*
     * WHERE PASSWORD IS TOO SHORT OR TOO LONG
     * // When the user is created and the password is too long (UNHAPPY PATH)
     * 
     * @Test
     * public void whenPasswordLong() {
     * User userCreated = userService.createUser("newuser@email.com", "newUser",
     * "12345678901234567890");
     * if (userCreated == null) {
     * System.out.println("Se excedió de longitud de password");
     * 
     * }
     * assertNull(userCreated);
     * 
     * }
     * 
     * // When the user is created and the password is too short (UNHAPPY PATH)
     * 
     * @Test
     * public void whenPasswordShort() {
     * 
     * User userCreated = userService.createUser("newuser@email.com", "newUser",
     * "123456");
     * if (userCreated == null) {
     * System.out.println("Se quedó corto de longitud de password");
     * 
     * }
     * assertNull(userCreated);
     * 
     * }
     */

    /*
     * 
     * 
     * RETRIEVE / READ TEST
     * 
     * 
     */

    // When the user is found (HAPPY PATH)
    @Test
    public void findUserByEmail() {
        when(daoUserMock.findUserByEmail(anyString())).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {

                for (User userIn : dataBase) {
                    if (userIn.getEmail().equals(invocation.getArguments()[0])) {
                        return userIn;
                    }
                }

                return null;
            }
        });
        User user = userService.findUserByEmail("email1@email.com");
        assertNotNull(user);
    }

    // When the user is not found (UNHAPPY PATH)
    @Test
    public void findUserByEmailFail() {
        when(daoUserMock.findUserByEmail(anyString())).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {

                for (User userIn : dataBase) {
                    if (userIn.getEmail().equals(invocation.getArguments()[0])) {
                        return userIn;
                    }
                }

                return null;
            }
        });
        User user = userService.findUserByEmail("noemail@email.com");
        assertNull(user);
    }

    /*
     * USING ID
     * 
     * @Test
     * public void findUserById() {
     * when(daoUserMock.findById(anyInt())).thenAnswer(new Answer<User>() {
     * public User answer(InvocationOnMock invocation) throws Throwable {
     * Integer userId = (int) invocation.getArguments()[0];
     * for (User user : dataBase) {
     * 
     * if (user.getId() == userId) {
     * return user;
     * }
     * 
     * }
     * return null;
     * }
     * });
     * User user = userService.findUserById(1);
     * assertNotNull(user);
     * }
     */

    // All users found
    @Test
    public void findAll() {
        when(daoUserMock.findAll()).thenReturn(dataBase);
        List<User> users = userService.findAllUsers();

        assertEquals(users, dataBase);
    }

    /*
     * 
     * 
     * UPDATE TEST
     * 
     */

    @Test
    public void updateUser() {
        User user = dataBase.get(0);
        String newUsernamePassword = "updated";
        User update = new User(user.getUsername(), user.getPassword(), user.getEmail());
        update.setId(user.getId());

        update.setPassword(newUsernamePassword);

        when(daoUserMock.updateUser(any(User.class))).thenAnswer(new Answer<User>() {
            public User answer(InvocationOnMock invocation) throws Throwable {
                User user = (User) invocation.getArguments()[0];
                for (int i = 0; i < dataBase.size(); i++) {
                    User userX = dataBase.get(i);
                    if (user.getId() == userX.getId()) {
                        System.out.println("DATABASE BEFORE CHANGES:\n " + dataBase);
                        dataBase.set(i, user);
                        return user;
                    }
                }
                return null;
            }
        });
        User updatedUser = userService.updateUser(update);
        if (updatedUser != null) {
            System.out.println("DATABASE AFTER CHANGES:\n " + dataBase);
            assertEquals(updatedUser.getPassword(), newUsernamePassword);
        } else {
            assertNull(updatedUser);
        }
    }

    /*
     * 
     * 
     * DELETE TEST
     * 
     * 
     */
    @Test
    public void deleteUser() {
        int databaseLengthBefore = dataBase.size();
        when(daoUserMock.deleteById(anyInt())).thenAnswer(new Answer<Boolean>() {
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Integer userId = (int) invocation.getArguments()[0];
                for (User userIn : dataBase) {
                    if (userIn.getId() == userId) {
                        System.out.println("DATABASE BEFORE CHANGES:\n " + dataBase);
                        dataBase.remove(userIn);
                        return true;
                    }
                }
                return false;
            }
        });

        boolean result = userService.deleteUser(1);
        System.out.println("DATABASE AFTER CHANGES:\n " + dataBase);
        int databaseLengthAfter = dataBase.size();
        assertEquals(databaseLengthBefore - 1, databaseLengthAfter);
        assertTrue(result);
    }

}

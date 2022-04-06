
import model.Consumer;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state.UserState;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserState {
    @Test
    @DisplayName("Testing set current user sets and gets the user correctly")
    public void testSetCurrentUser(){
        UserState userState = new UserState();
        User testUser = new Consumer("euan","email@com","other",
                "password","email@email.com");
        userState.setCurrentUser(testUser);
        assertEquals(testUser ,userState.getCurrentUser());
    }
    @Test
//wont run as there is nothing to catch the null
    @DisplayName("Testing return from null inputs")
    public void testSetCurrentUserNull(){
        UserState userState = new UserState();
        User testUser = new Consumer(null,null,null, null,null);
        userState.setCurrentUser(testUser);
        assertEquals(testUser, userState.getCurrentUser());
    }
    @Test
    @DisplayName("testing if a user is added")
    public void testAddUser(){
        UserState userState = new UserState();
        User testUser = new Consumer("euan","email@com","other",
                "password","email");
        userState.addUser(testUser);
        assertTrue(userState.getAllUsers().containsKey("email@com"));
    }
    @Test
    @DisplayName("test all users")
    public void testAllUsers(){
        UserState userState = new UserState();
//it should be the 3 gov officials
        assertEquals(3, userState.getAllUsers().size());
        assertTrue(userState.getAllUsers().containsKey("gov1@gov.uk"));
        assertTrue(userState.getAllUsers().containsKey("gov2@gov.uk"));
        assertTrue(userState.getAllUsers().containsKey("margaret.thatcher@gov.uk"));
    }
    @Test
    @DisplayName("test get current user is null if not set")
    public void testGetCurrentUserISNull(){
        UserState userState = new UserState();
        assertEquals(null, userState.getCurrentUser());
    }
}

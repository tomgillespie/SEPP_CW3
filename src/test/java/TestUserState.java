
import model.Consumer;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state.UserState;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserState {

    // Tests on Consumers:
    @Test
    @DisplayName("Test setCurrentUser to Consumer")
    public void testSetCurrentUserToConsumer(){
        UserState userState = new UserState();
        User testUser = new Consumer("euan","email@com","other",
                "password","email@email.com");
        userState.setCurrentUser(testUser);
        assertEquals(testUser ,userState.getCurrentUser());
    }
    @Test
    @DisplayName("Test setCurrentUser to Consumer with null inputs")
    public void testSetCurrentUserToConsumerWithNullInputs(){
        UserState userState = new UserState();
        User testUser = new Consumer(null,null,null, null,null);
        userState.setCurrentUser(testUser);
        assertEquals(testUser, userState.getCurrentUser());
    }

    @Test
    @DisplayName("Test if a Consumer is added")
    public void testAddConsumer(){
        UserState userState = new UserState();
        User testUser = new Consumer("euan","email@com","other",
                "password","email");
        userState.addUser(testUser);
        assertTrue(userState.getAllUsers().containsKey("email@com"));
    }

    // Tests on Entertainment Providers:





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

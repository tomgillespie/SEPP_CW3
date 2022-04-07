
import model.Consumer;
import model.EntertainmentProvider;
import model.GovernmentRepresentative;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import state.UserState;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUserState {

    // addUser tests:

    @Test
    @DisplayName("addConsumerTest")
    public void addConsumerTest(){
        UserState userState = new UserState();
        User testUser = new Consumer("euan","email@com","other",
                "password","email");
        userState.addUser(testUser);
        assertTrue(userState.getAllUsers().containsKey("email@com"));
        assertNull(userState.getCurrentUser());
    }
    @Test
    @DisplayName("addEntertainmentProviderTest")
    public void addEntertainmentProviderTest(){
        UserState userState = new UserState();
        User testUser = new EntertainmentProvider(
                "Edinburgh University Surf Club",
                "40 George Square",
                "edisurf@ed.ac.uk",
                "Cameron",
                "cam@surfsoc.ac.uk",
                "surf",
                List.of("Jacob"),
                List.of("surf@com")
        );
        userState.addUser(testUser);
        assertTrue(userState.getAllUsers().containsKey("cam@surfsoc.ac.uk"));
        assertNull(userState.getCurrentUser());
    }
    @Test
    @DisplayName("addGovernmentRepresentativeTest")
    void addGovernmentRepresentativeTest(){
        UserState userState = new UserState();
        User testUser = new GovernmentRepresentative(
                "bojo@gov.uk","carrie",
                "rishi@gov.uk"
        );
        userState.addUser(testUser);
        assertTrue(userState.getAllUsers().containsKey("bojo@gov.uk"));
        assertNull(userState.getCurrentUser());
    }

    // getAllUser tests:

    @Test
    @DisplayName("getAllInitialUsersTest")
    void getAllInitialUsersTest(){
        UserState userState = new UserState();
        Map<String,User> allUsers = userState.getAllUsers();
        assertEquals(3, allUsers.size());
        assertTrue(allUsers.containsKey("gov1@gov.uk"));
        assertTrue(allUsers.containsKey("gov2@gov.uk"));
        assertTrue(allUsers.containsKey("margaret.thatcher@gov.uk"));
    }
    @Test
    @DisplayName("AddUsersThenGetAllUsersTest")
    void addUsersThenGetAllUsersTest(){
        UserState userState = new UserState();
        Map<String,User> allUsers = userState.getAllUsers();
        User addedUser1 = new GovernmentRepresentative(
                "bojo@gov.uk","carrie",
                "rishi@gov.uk"
        );
        userState.addUser(addedUser1);
        User addedUser2 = new EntertainmentProvider(
                "Edinburgh University Surf Club",
                "40 George Square",
                "edisurf@ed.ac.uk",
                "Cameron",
                "cam@surfsoc.ac.uk",
                "surf",
                List.of("Jacob"),
                List.of("surf@com")
        );
        userState.addUser(addedUser2);
        assertEquals(5, allUsers.size());
        assertTrue(allUsers.containsKey("gov1@gov.uk"));
        assertTrue(allUsers.containsKey("gov2@gov.uk"));
        assertTrue(allUsers.containsKey("margaret.thatcher@gov.uk"));
        assertTrue(allUsers.containsKey("bojo@gov.uk"));
        assertTrue(allUsers.containsKey("cam@surfsoc.ac.uk"));
    }

    // setCurrentUser and getCurrentUser tests:

    @Test
    @DisplayName("initalGetCurrentUserTest")
    void initialGetCurrentUserTest(){
        UserState userState = new UserState();
        User currUser = userState.getCurrentUser();
        assertNull(currUser);
        assertEquals(null, userState.getCurrentUser());
    }

    @Test
    @DisplayName("setThenGetCurrentUserGovRepTest")
    void setThenGetCurrentUserGovRepTest(){
        UserState userState = new UserState();
        User addedUser1 = new GovernmentRepresentative(
                "bojo@gov.uk","carrie",
                "rishi@gov.uk"
        );
        userState.addUser(addedUser1);
        userState.setCurrentUser(addedUser1);
        User currUser = userState.getCurrentUser();
        assertEquals(addedUser1, currUser);
        assertEquals("bojo@gov.uk", currUser.getEmail());
        assertEquals("rishi@gov.uk", currUser.getPaymentAccountEmail());
    }

    @Test
    @DisplayName("setThenGetCurrentUserEntProvTest")
    void setThenGetCurrentUserEntProvTest(){
        UserState userState = new UserState();
        User addedUser1 = new EntertainmentProvider(
                "Edinburgh University Surf Club",
                "40 George Square",
                "edisurf@ed.ac.uk",
                "Cameron",
                "cam@surfsoc.ac.uk",
                "surf",
                List.of("Jacob"),
                List.of("surf@com")
        );
        userState.addUser(addedUser1);
        userState.setCurrentUser(addedUser1);
        User currUser = userState.getCurrentUser();
        assertEquals(addedUser1, currUser);
        assertEquals("cam@surfsoc.ac.uk", currUser.getEmail());
        assertEquals("edisurf@ed.ac.uk", currUser.getPaymentAccountEmail());
    }

    @Test
    @DisplayName("setThenGetCurrentUserConsumerTest")
    void setThenGetCurrentUserConsumerTest(){
        UserState userState = new UserState();
        Consumer addedUser1 = new Consumer(
                "TomG",
                "tom@gmail.com",
                "098765",
                "pass",
                "tg@btinternet"
        );
        userState.addUser(addedUser1);
        userState.setCurrentUser(addedUser1);
        User currUser = userState.getCurrentUser();
        assertEquals(addedUser1, currUser);
        assertEquals("tom@gmail.com", currUser.getEmail());
        assertEquals("tg@btinternet", currUser.getPaymentAccountEmail());
    }
}

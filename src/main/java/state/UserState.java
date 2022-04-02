package state;

import model.GovernmentRepresentative;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserState implements IUserState {
    private Map<String, User> users;
    private User currentUser;

    public UserState(){
        this.currentUser = null;
        this.users = new HashMap<>();
        GovernmentRepresentative governmentRepresentative1 = new GovernmentRepresentative("gov1@gov.uk", "Gov123", "gov1pay@gov.uk");
        GovernmentRepresentative governmentRepresentative2 = new GovernmentRepresentative("gov2@gov.uk", "Gov456", "gov2pay@gov.uk");
        GovernmentRepresentative governmentRepresentative3 = new GovernmentRepresentative("margaret.thatcher@gov.uk", "The Good times  ", "ding.dong@gov.uk");
        addUser(governmentRepresentative1);
        addUser(governmentRepresentative2);
        addUser(governmentRepresentative3);
        //Add government representatives
    }

    public UserState(IUserState other){
        this.currentUser = other.getCurrentUser();
        this.users = (Map<String, User>) other.getAllUsers();
    }


    @Override
    public void addUser(User user) {
        this.users.put(user.getEmail(), user);
    }

    @Override
    public Map<String, User> getAllUsers() {
        return users;
    }


    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}

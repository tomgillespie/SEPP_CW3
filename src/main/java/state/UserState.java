package state;

import model.User;

import java.util.HashMap;
import java.util.Map;

public class UserState implements IUserState {
    private HashMap<String, User> users;
    private User currentUser;

    public UserState(){
        currentUser = null;
        users = null;
        //Add government representatives
    }

    public UserState(IUserState other){
        new UserState();
        this.currentUser = other.getCurrentUser();
        this.users = (HashMap<String, User>) other.getAllUsers();
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

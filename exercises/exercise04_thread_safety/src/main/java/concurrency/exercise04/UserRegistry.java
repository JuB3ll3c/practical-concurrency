package concurrency.exercise04;

import java.util.ArrayList;
import java.util.List;

public class UserRegistry {
    private final List<String> users = new ArrayList<>();

    public void registerUser(String username) {
        if (!users.contains(username)) {
            users.add(username);
        }
    }

    public void unregisterUser(String username) {
        users.remove(username);
    }

    public boolean containsUser(String username) {
        return users.contains(username);
    }

    public int getUserCount() {
        return users.size();
    }

    public List<String> getAllUsers() {
        return new ArrayList<>(users);
    }
}

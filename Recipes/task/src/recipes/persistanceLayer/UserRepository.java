package recipes.persistanceLayer;

import org.springframework.data.repository.CrudRepository;
import recipes.businessLayer.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);
}

//@Component
//public class UserRepository {
//    final private Map<String, User> users = new ConcurrentHashMap<>();
//
//    public User findUsersByName(String username) {
//        return users.get(username);
//    }
//
//    public void save(User user) {
//        users.put(user.getUsername(), user);
//    }
//}

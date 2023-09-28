package recipes.persistanceLayer;

import org.springframework.data.repository.CrudRepository;
import recipes.businessLayer.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByEmail(String email);
}

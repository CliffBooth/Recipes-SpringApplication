package recipes.businessLayer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import recipes.persistanceLayer.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    //this method is not used, so might as well have used just custom UserService class without implementing UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            throw new UsernameNotFoundException("Not found " + email);
        return new UserDetailsImpl(user);
    }

    public Optional<User> findUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null)
            return Optional.empty();
        return Optional.of(user);
    }

    public List<User> findAll() {
        var it = userRepository.findAll();
        var users = new ArrayList<User>();
        it.forEach(users::add);
        return users;
    }

    public void save(User user) {
        userRepository.save(user);
    }
}

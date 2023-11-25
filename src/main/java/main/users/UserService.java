package main.users;

import main.activity.Activity;
import main.activity.ActivityNotFoundException;
import main.activity.ActivityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    UserService(UserRepository userRepository, ActivityRepository activityRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    List<UserDAO> getAllUsers()
    {
        return userRepository.findAll()
                             .stream()
                             .map(User::mapToDAO)
                             .collect(Collectors.toList());
    }

    UserDAO getUserById(Long id)
    {
        User user =  userRepository.findById(id)
                                   .orElseThrow(() -> new UserNotFoundException(id));
        return user.mapToDAO();
    }

    void saveUser(User user)
    {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        userRepository.save(user);
    }

    UserDAO addActivityForUser(Long userId, Long activityId)
    {
        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Activity> optionalActivity = activityRepository.findById(activityId);

        if (optionalUser.isEmpty())
        {
            throw new UserNotFoundException(userId);
        }
        if (optionalActivity.isEmpty())
        {
            throw new ActivityNotFoundException(activityId);
        }

        User user = optionalUser.get();
        Activity activity = optionalActivity.get();
        user.addActivity(activity);
        userRepository.save(user);

        return user.mapToDAO();
    }

    void deleteUser(Long id)
    {
        userRepository.deleteById(id);
    }
}

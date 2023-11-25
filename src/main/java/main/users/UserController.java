package main.users;

import jakarta.validation.Valid;
import main.activity.Activity;
import main.activity.ActivityNotFoundException;
import main.activity.ActivityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController
{
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository userRepository, ActivityRepository activityRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    List<UserDAO> all()
    {
        return userRepository.findAll()
                             .stream()
                             .map(User::mapToDAO)
                             .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    UserDAO one(@PathVariable Long id)
    {
        User user =  userRepository.findById(id)
                                   .orElseThrow(() -> new UserNotFoundException(id));
        return user.mapToDAO();
    }

    @PostMapping("/users")
    ResponseEntity<?> createUser(@RequestBody @Valid User user)
    {
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/users/{userId}/activities/{activityId}")
    UserDAO addActivityForUser(@PathVariable Long userId, @PathVariable Long activityId)
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

    @DeleteMapping("users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable long id)
    {
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}

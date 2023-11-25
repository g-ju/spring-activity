package main.users;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController
{
    private final UserService service;

    UserController(UserService service)
    {
        this.service = service;
    }

    @GetMapping("/users")
    List<UserDAO> all()
    {
        return service.getAllUsers();
    }

    @GetMapping("/users/{id}")
    UserDAO one(@PathVariable Long id)
    {
        return service.getUserById(id);
    }

    @PostMapping("/users")
    ResponseEntity<?> createUser(@RequestBody @Valid User user)
    {
        service.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(user.mapToDAO());
    }

    @PutMapping("/users/{userId}/activities/{activityId}")
    UserDAO addActivityForUser(@PathVariable Long userId, @PathVariable Long activityId)
    {
        return service.addActivityForUser(userId, activityId);
    }

    @DeleteMapping("users/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Long id)
    {
        service.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}

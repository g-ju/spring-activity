package main.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController
{
    private final UserRepository repository;

    UserController(UserRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<User> all()
    {
        return repository.findAll();
    }

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id)
    {
        return repository.findById(id)
                         .orElseThrow(() -> new UserNotFoundException(id));
    }

    @PostMapping("/users")
    void createUser(@RequestBody @Valid User user)
    {
        repository.save(user);
    }
}

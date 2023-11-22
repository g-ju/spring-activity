package main.users;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController
{
    private final UserRepository repository;

    UserController(UserRepository repository)
    {
        this.repository = repository;
    }

    @GetMapping("/users")
    List<UserDAO> all()
    {
        return repository.findAll()
                         .stream()
                         .map(User::mapToDAO)
                         .collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    UserDAO one(@PathVariable Long id)
    {
        User user =  repository.findById(id)
                               .orElseThrow(() -> new UserNotFoundException(id));
        return user.mapToDAO();
    }

    @PostMapping("/users")
    void createUser(@RequestBody @Valid User user)
    {
        repository.save(user);
    }
}

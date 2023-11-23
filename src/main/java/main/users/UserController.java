package main.users;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController
{
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    UserController(UserRepository repository, PasswordEncoder passwordEncoder)
    {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
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
        user.setPwd(passwordEncoder.encode(user.getPwd()));
        repository.save(user);
    }
}

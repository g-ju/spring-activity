package main.authentication;

import main.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService
{
    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<main.users.User> optionalUser = repository.findUserByUsername(username);

        if (optionalUser.isEmpty())
        {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        main.users.User user = optionalUser.get();

        return User.withDefaultPasswordEncoder()
                   .username(user.getUsername())
                   .password(user.getPwd())
                   .roles(user.getRole())
                   .build();
    }
}

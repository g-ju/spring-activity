package main;

import main.activity.Activity;
import main.activity.ActivityRepository;
import main.users.User;
import main.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
class LoadDatabase
{
    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(ActivityRepository activityRepository, UserRepository userRepository)
    {
        return args ->
        {
            activityRepository.save(new Activity("Eat at a restaurant", "Explore some delicious restaurants"));
            activityRepository.save(new Activity("Play games", "Find a fun game to play"));

            User admin = new User("admin", passwordEncoder.encode("admin"), "ADMIN");
            User user = new User("user", passwordEncoder.encode("password"), "USER");
            userRepository.save(admin);
            userRepository.save(user);
        };
    }
}

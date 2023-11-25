package main.users;

import main.activity.Activity;
import main.activity.ActivityNotFoundException;
import main.activity.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests
{
    private UserRepository userRepository;
    private ActivityRepository activityRepository;

    private UserService sut;

    @BeforeEach
    void setUp()
    {
        userRepository = mock(UserRepository.class);
        activityRepository = mock(ActivityRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        sut = new UserService(userRepository, activityRepository, passwordEncoder);
    }

    @Test
    void get_all_users_returns_all_users()
    {
        User user1 = mock(User.class);
        User user2 = mock(User.class);
        UserDAO dao1 = mock(UserDAO.class);
        UserDAO dao2 = mock(UserDAO.class);

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(user1.mapToDAO()).thenReturn(dao1);
        when(user2.mapToDAO()).thenReturn(dao2);

        List<UserDAO> users = sut.getAllUsers();
        assertThat(users, contains(dao1, dao2));
    }

    @Test
    void get_user_by_id_returns_correct_user()
    {
        final Long id = 12L;
        User user = mock(User.class);
        UserDAO dao = mock(UserDAO.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(user.mapToDAO()).thenReturn(dao);

        UserDAO foundUser = sut.getUserById(id);
        assertEquals(dao, foundUser);
    }

    @Test
    void get_user_by_id_throws_exception_when_user_not_found()
    {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> sut.getUserById(12L));
        assertEquals("Could not find User 12", exception.getMessage());
    }

    @Test
    void can_save_user()
    {
        User user = mock(User.class);

        sut.saveUser(user);

        verify(user).setPwd(nullable(String.class));
        verify(userRepository).save(user);
    }

    @Test
    void add_activity_for_user_throws_exception_when_user_not_found()
    {
        when(userRepository.findById(any())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> sut.addActivityForUser(12L, 23L));
        assertEquals("Could not find User 12", exception.getMessage());
    }

    @Test
    void add_activity_for_user_throws_exception_when_activity_not_found()
    {
        when(userRepository.findById(any())).thenReturn(Optional.of(mock(User.class)));
        when(activityRepository.findById(any())).thenReturn(Optional.empty());

        ActivityNotFoundException exception = assertThrows(ActivityNotFoundException.class, () -> sut.addActivityForUser(12L, 23L));
        assertEquals("Could not find Activity 23", exception.getMessage());
    }

    @Test
    void can_add_activity_for_user()
    {
        User user = mock(User.class);
        UserDAO dao = mock(UserDAO.class);
        Activity activity = mock(Activity.class);

        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(activityRepository.findById(any())).thenReturn(Optional.of(activity));
        when(user.mapToDAO()).thenReturn(dao);

        UserDAO result = sut.addActivityForUser(12L, 23L);

        verify(user).addActivity(activity);
        verify(userRepository).save(user);
        assertEquals(dao, result);
    }

    @Test
    void can_delete_user()
    {
        Long id = 12L;

        sut.deleteUser(id);

        verify(userRepository).deleteById(id);
    }
}

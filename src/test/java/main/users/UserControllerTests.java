package main.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp()
    {
        UserDAO dao = new UserDAO(1L, "testUser", "TEST_ROLE", new HashSet<>());

        given(userService.getAllUsers()).willReturn(Collections.singletonList(dao));
        given(userService.getUserById(1L)).willReturn(dao);
    }

    @Test
    void get_returns_all_users() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("*.id").value(1))
               .andExpect(jsonPath("*.username").value("testUser"))
               .andExpect(jsonPath("*.role").value("TEST_ROLE"))
               .andExpect(jsonPath("*.pwd").doesNotExist());
    }

    @Test
    void get_by_id_returns_correct_user() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.username").value("testUser"))
               .andExpect(jsonPath("$.role").value("TEST_ROLE"))
               .andExpect(jsonPath("*.pwd").doesNotExist());
    }

    @Test
    void get_by_id_returns_not_found_if_no_such_id() throws Exception
    {
        given(userService.getUserById(10L)).willThrow(new UserNotFoundException(10L));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/10"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Could not find User 10"));
    }

    @Test
    void post_creates_new_user() throws Exception
    {
        String newUsername = "newUser";
        String newPwd = "newpass";
        String newRole = "NEW_ROLE";
        User reqUser = new User(1L, newUsername, newPwd, newRole);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                                              .with(csrf())
                                              .content(asJsonString(reqUser))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
    }

    @Test
    void delete_returns_no_content() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/10")
                                              .with(csrf()))
               .andExpect(status().isNoContent());
    }

    static String asJsonString(final Object obj)
    {
        try
        {
            return new ObjectMapper().writeValueAsString(obj);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
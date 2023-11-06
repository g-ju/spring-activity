package main.activity;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ActivityController.class)
public class ActivityControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActivityRepository activityRepository;
    @SpyBean
    private ActivityModelAssembler assembler;

    @BeforeEach
    void setUp()
    {
        Activity activity = new Activity(1L, "name", "test");

        given(activityRepository.findById(1L)).willReturn(Optional.of(activity));
        given(activityRepository.findAll()).willReturn(List.of(activity));
    }

    @Test
    void get_returns_all_activities() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/activities"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("*.activityList").exists())
               .andExpect(jsonPath("*.activityList").isNotEmpty())
               .andExpect(jsonPath("*.activityList.[0].id").value(1))
               .andExpect(jsonPath("*.activityList.[0].name").value("name"))
               .andExpect(jsonPath("*.activityList.[0].description").value("test"));
    }

    @Test
    void get_by_id_returns_correct_activity() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/activities/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("name"))
               .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void get_by_id_returns_not_found_if_no_such_id() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.get("/activities/10"))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Could not find Activity 10"));
    }

    @Test
    void post_creates_new_activity() throws Exception
    {
        Long id = 2L;
        String newName = "newActivity";
        String newDesc = "new description";
        Activity reqActivity = new Activity(null, newName, newDesc);
        Activity newActivity = new Activity(id, newName, newDesc);

        given(activityRepository.save(reqActivity)).willReturn(newActivity);

        mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                                              .content(asJsonString(reqActivity))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(id))
               .andExpect(jsonPath("$.name").value(newName))
               .andExpect(jsonPath("$.description").value(newDesc));
    }

    @Test
    void post_returns_bad_request_if_invalid() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/activities")
                                              .content("")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest());
    }

    @Test
    void can_update_name_of_activity() throws Exception
    {
        Activity changedActivity = activityRepository.findById(1L).get();
        String newName = "newActivity";

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), "name");
        assertEquals(changedActivity.getDescription(), "test");

        mockMvc.perform(MockMvcRequestBuilders.put("/activities/1")
                                              .content("{ \"name\": \"" + newName + "\" }")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value(newName))
               .andExpect(jsonPath("$.description").value("test"));

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), newName);
        assertEquals(changedActivity.getDescription(), "test");
    }

    @Test
    void can_update_description_of_activity() throws Exception
    {
        Activity changedActivity = activityRepository.findById(1L).get();
        String newDescription = "new description";

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), "name");
        assertEquals(changedActivity.getDescription(), "test");

        mockMvc.perform(MockMvcRequestBuilders.put("/activities/1")
                                              .content("{ \"description\": \"" + newDescription + "\" }")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("name"))
               .andExpect(jsonPath("$.description").value(newDescription));

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), "name");
        assertEquals(changedActivity.getDescription(), newDescription);
    }

    @Test
    void activity_not_updated_when_name_or_description_are_null() throws Exception
    {
        Activity changedActivity = activityRepository.findById(1L).get();
        Activity newActivity = new Activity(null, null, null);

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), "name");
        assertEquals(changedActivity.getDescription(), "test");

        mockMvc.perform(MockMvcRequestBuilders.put("/activities/1")
                                              .content(asJsonString(newActivity))
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("name"))
               .andExpect(jsonPath("$.description").value("test"));

        assertEquals(changedActivity.getId(), 1L);
        assertEquals(changedActivity.getName(), "name");
        assertEquals(changedActivity.getDescription(), "test");
    }

    @Test
    void update_returns_not_found_if_no_such_id() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.put("/activities/10")
                                              .content("{ \"name\": \"newName\" }")
                                              .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNotFound())
               .andExpect(content().string("Could not find Activity 10"));
    }

    @Test
    void delete_returns_no_content() throws Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/activities/10"))
               .andExpect(status().isNoContent());
    }

    public static String asJsonString(final Object obj)
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

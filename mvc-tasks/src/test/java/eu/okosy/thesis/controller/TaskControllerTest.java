package eu.okosy.thesis.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import eu.okosy.thesis.model.Task;
import eu.okosy.thesis.service.TaskService;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTest {
    
    private MockMvc mvc;
    
    @Mock
    private TaskService taskServiceMock;
    
    @InjectMocks
    private TaskController taskController;
    
    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders
                .standaloneSetup(taskController)
                .build();
    }
    
  @Test
  public void getTaskPass() throws Exception {
      final Long ID = 500l;
      final String DESCRIPTION = "any description is a good description";
      final boolean DONE = false;
      Task task = new Task(ID, DESCRIPTION, DONE);
      when(taskServiceMock.getTask(ID)).thenReturn(task);
      mvc.perform(get("/500"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.task_id", is(ID.intValue())))
        .andExpect(jsonPath("$.description", is(DESCRIPTION)))
        .andExpect(jsonPath("$.done", is(DONE)));
  }
}

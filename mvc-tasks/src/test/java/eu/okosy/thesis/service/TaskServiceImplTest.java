package eu.okosy.thesis.service;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import eu.okosy.thesis.model.Task;

import static org.junit.Assert.*;

public class TaskServiceImplTest {
    
    
    private TaskServiceImpl taskService;
    

    @Before
    public void init() {
        taskService = new TaskServiceImpl();
    }
    
    @Test
    public void createTasks() throws Exception {
        // prepare tasks
        Task task1 = new Task();
        task1.setDescription("desc 1");
        task1.setDone(true);
        task1.setTaskId(1);
        Task task2 = new Task();
        task2.setDescription("desc 1");
        task2.setDone(false);
        task2.setTaskId(2);
        
        // insert tasks
        taskService.createTask(task1);
        taskService.createTask(task2);
        
        // verify
        List<Task> retrieved = taskService.getTasks();
        assertTrue("contains first task", retrieved.contains(task1));
        assertTrue("contains second task", retrieved.contains(task2));
        assertEquals("contains two tasks", retrieved.size(), 2);
    }
    
    @Test
    public void updateTask() throws Exception {
        // prepare task
        Task task = new Task();
        task.setDescription("desc 1");
        task.setDone(false);
        
        // insert task
        taskService.createTask(task);
        
        // verify
        List<Task> retrieved = taskService.getTasks();
        assertTrue("contains task", retrieved.contains(task));
        assertEquals("contains only one task", retrieved.size(), 1);
        
        // update task
        task.setDone(true);
        task.setDescription("new desc");
        taskService.updateTask(1, task);
        
        // retrieve
        Task retrievedTask = taskService.getTask(1);
        assertEquals("retrieved tasks equals expected", task, retrievedTask);
    }
    
    @Test
    public void removeTask() throws Exception {
        // prepare task
        Task task = new Task();
        task.setDescription("desc 1");
        task.setDone(false);
        
        // insert task
        taskService.createTask(task);
        
        // verify
        List<Task> retrieved = taskService.getTasks();
        assertTrue("contains task", retrieved.contains(task));
        
        // remove
        taskService.removeTask(1);
        
        // verify
        retrieved = taskService.getTasks();
        assertEquals("retrieved tasks are empty", 0, retrieved.size());
    }
}
package eu.okosy.thesis.service;

import java.util.List;
import eu.okosy.thesis.model.Task;

/**
 *
 *
 */
public interface TaskService {
    public Task createTask(Task task);
    public Task getTask(long id);
    public List<Task> getTasks();
    public Task updateTask(long id, Task task);
    public Task removeTask(long id);    
}

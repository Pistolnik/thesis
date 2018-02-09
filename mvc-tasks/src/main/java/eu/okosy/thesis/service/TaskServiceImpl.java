package eu.okosy.thesis.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import eu.okosy.thesis.exception.TaskException;
import eu.okosy.thesis.model.Task;


@Service
public class TaskServiceImpl implements TaskService {
    
    private final Map<Long, Task> tasks;
    private long counter;
    
    public TaskServiceImpl() {
        this.tasks = Collections.synchronizedMap(new HashMap<Long, Task>());
        this.counter = 0;
    }

    public synchronized Task createTask(Task task) {
        task.setTaskId(++counter);
        tasks.put(counter, task);
        return task;
    }

    public Task removeTask(long id) {
        Task task = tasks.remove(id);
        throwIfNull(id, task);
        return task;
    }
    
    public Task getTask(long id) {
        Task task = tasks.get(id);
        throwIfNull(id, task);
        return task;
    }
    
    public List<Task> getTasks() {
        return new ArrayList<Task>(tasks.values());
    }

    public Task updateTask(long id, Task task) {
        task.setTaskId(id);
        tasks.put(id, task);
        return task;
    }
    
    private void throwIfNull(long id, Task task) {
        if (task == null) {
            throw new TaskException("task with id " + id + " not  found");
        }
    }
    
}

package eu.okosy.thesis.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import eu.okosy.thesis.exception.TaskException;
import eu.okosy.thesis.model.Task;
import eu.okosy.thesis.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class TaskController {
    
    private final Logger logger = LoggerFactory.getLogger(TaskController.class);
    
    @Autowired
    TaskService taskService;
    
    @RequestMapping(path = "/", method = RequestMethod.POST, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Task createTask(@RequestBody Task task){
        logger.info("creating " + task);
        return taskService.createTask(task);       
    }

    @RequestMapping(path="/{taskId}", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Task getTask(@PathVariable long taskId){
        logger.info("getting {}", taskId);
        return taskService.getTask(taskId);       
    }
    
    @RequestMapping(path="/", method= RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Task> getTasks(){
        logger.info("getting all tasks"); 
        return taskService.getTasks();       
    }
    
    @RequestMapping(path="/{taskId}", method= RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public Task updateTask(@PathVariable long taskId, @RequestBody Task task){
        logger.info("updating {}", taskId);
        return taskService.updateTask(taskId, task);       
    }
    
    @RequestMapping(path="/", method= RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public Task deleteTask(long id){
        logger.info("deleting {}", id);
        return taskService.removeTask(id);       
    }
    
    @ResponseStatus(value=HttpStatus.NOT_FOUND,
            reason="Task not found!")
    @ExceptionHandler(TaskException.class)
    public void notFound(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }
    

}

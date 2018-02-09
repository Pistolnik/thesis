package eu.okosy.thesis.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Task {
    
    @JsonProperty(value = "task_id")
    private long taskId;
    private String description;
    private boolean done;
    
    public Task() {
        super();
    }
    
    public Task(long taskId, String description, boolean done) {
        super();
        this.taskId = taskId;
        this.description = description;
        this.done = done;
    }

    public long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + (done ? 1231 : 1237);
        result = prime * result + (int) (taskId ^ (taskId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (done != other.done)
            return false;
        if (taskId != other.taskId)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Task [taskId=" + taskId + ", description=" + description + ", done=" + done + "]";
    }
}

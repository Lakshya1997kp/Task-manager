
package TaskManagerApp.Taskly;

        import org.springframework.stereotype.Service;
        import java.util.List;
        import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    // Constructor-based Dependency Injection
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Fetch all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get a single task by ID
    public Optional<Task> getTaskById(long id) {
        return taskRepository.findById(id);
    }


    // Create a new task
    public Task createTask(Task task) {

        return taskRepository.save(task);
    }

    // Delete task by ID
    public boolean deleteTask(long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Search tasks by title (case insensitive, partial match)
    public List<Task> searchByTitle(String title) {

        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    // Filter tasks by completion status
    public List<Task> filterByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    // Filter tasks by priority
    public List<Task> filterByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    // Update a task by ID
    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setCompleted(updatedTask.isCompleted());
            task.setDueDate(updatedTask.getDueDate());
            task.setDueTime(updatedTask.getDueTime());
            task.setPriority(updatedTask.getPriority());
            task.setCategory(updatedTask.getCategory());
            return taskRepository.save(task);
        });
    }
}


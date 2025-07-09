package TaskManagerApp.Taskly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskViewController {

    @Autowired
    private TaskService taskService;

    // 🔁 Redirect root URL to /home
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/home";
    }

    // 📋 View all tasks
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        return "tasks"; // loads templates/tasks.html
    }

    // ➕ Add task
    @PostMapping("/tasks/add")
    public String addTask(@ModelAttribute Task task) {
        taskService.createTask(task);
        return "redirect:/home";
    }

    // 🗑️ Delete task
    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/home";
    }

    // 🔁 Toggle task completion
    @PostMapping("/tasks/toggle/{id}")
    public String toggleTask(@PathVariable Long id) {
        taskService.getTaskById(id).ifPresent(task -> {
            task.setCompleted(!task.isCompleted());
            taskService.createTask(task); // save the updated task
        });
        return "redirect:/home";
    }

    // ✏️ Show edit form
    @GetMapping("/tasks/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        taskService.getTaskById(id).ifPresent(task -> model.addAttribute("task", task));
        return "edit-task"; // you'll need to create this template
    }

    // ✏️ Submit edited task
    @PostMapping("/tasks/edit/{id}")
    public String updateTask(@PathVariable Long id, @ModelAttribute Task updatedTask) {
        taskService.updateTask(id, updatedTask);
        return "redirect:/home";
    }

    // 🔍 Search & filter
    /*@GetMapping("/tasks/search")
    public String searchTasks(@RequestParam(required = false) String query,
                              @RequestParam(required = false) Boolean completed,
                              Model model) {
        if (query != null && !query.isBlank()) {
            model.addAttribute("tasks", taskService.searchByTitle(query));
        } else if (completed != null) {
            model.addAttribute("tasks", taskService.filterByCompleted(completed));
        } else {
            model.addAttribute("tasks", taskService.getAllTasks());
        }
        return "tasks";
    }*/

    @GetMapping("/tasks/search")
    public String searchTasks(@RequestParam(required = false) String query,
                              @RequestParam(required = false) Boolean completed,
                              @RequestParam(required = false) String priority,
                              @RequestParam(required = false) String category,
                              Model model) {

        System.out.println("query=" + query + ", completed=" + completed + ", priority=" + priority + ", category=" + category);

        List<Task> tasks = taskService.getAllTasks();

        if (query != null && !query.isBlank()) {
            tasks = taskService.searchByTitle(query);
        }

        if (completed != null) {
            tasks = tasks.stream()
                    .filter(task -> task.isCompleted() == completed)
                    .toList();
        }

        if (priority != null && !priority.isBlank()) {
            String finalPriority = priority.trim().toLowerCase();
            tasks = tasks.stream()
                    .filter(task -> task.getPriority() != null &&
                            task.getPriority().trim().toLowerCase().equals(finalPriority))
                    .toList();
        }

        if (category != null && !category.isBlank()) {
            String finalCategory = category.trim().toLowerCase();
            tasks = tasks.stream()
                    .filter(task -> task.getCategory() != null &&
                            task.getCategory().trim().toLowerCase().equals(finalCategory))
                    .toList();
        }

        model.addAttribute("tasks", tasks);
        return "tasks";
    }

}

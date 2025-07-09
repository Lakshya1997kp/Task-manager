
package TaskManagerApp.Taskly;

        import org.springframework.data.jpa.repository.JpaRepository;
        import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContainingIgnoreCase(String title);
    List<Task> findByCompleted(boolean completed);
    List<Task> findByPriority(String priority);
}


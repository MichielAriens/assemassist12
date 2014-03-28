package logic.car;

import java.util.List;
import logic.workstation.Task;

public abstract class OrderDetails {
	public abstract List<Task> getPendingTasks();
}

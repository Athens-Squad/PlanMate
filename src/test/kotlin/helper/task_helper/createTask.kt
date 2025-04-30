package helper.task_helper

import logic.entities.Task
import net.thechance.logic.entities.State

object FakeTask {
    val fakeTask = createTask()
    private fun createTask(
        id: String = "t1",
        title: String = "Task Title",
        description: String = "Task Description",
        currentStateId: String = "s1",
        currentStateName: String = "State Name",
        projectId: String = "p1"
    ) = Task(
        id = id,
        title = title,
        description = description,
        currentState = State(
            id = currentStateId,
            name = currentStateName,
            projectId = projectId
        ),
        projectId = projectId
    )
}
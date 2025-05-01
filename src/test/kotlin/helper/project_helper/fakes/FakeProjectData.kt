package helper.project_helper.fakes

import logic.entities.Task
import logic.entities.User
import net.thechance.logic.entities.State
import net.thechance.logic.entities.UserType

object FakeProjectData {
    val adminUser = User("1", "admin user", "abc123", UserType.AdminUser)
    val mateUserForAdminUser = User("2", "mate user", "123", UserType.MateUser("1"))

    val alexAdminUser = User("3", "second admin user", "1232#$", UserType.AdminUser)
    val firstState = State("1", "state1", "1")
    val secondState = State("2", "state2", "1")
    val firstTask = Task("1", "task1", "task des", firstState, "1")
    val secondTask = Task("2", "task2", "task des", secondState, "1")

    val tasks = mutableListOf(firstTask, secondTask)
    val states = mutableListOf(firstState, secondState)

    val newUpdatedStates = mutableListOf(
        firstState.copy(projectId = "project2"),
        secondState.copy(projectId = "project2")
    )
}
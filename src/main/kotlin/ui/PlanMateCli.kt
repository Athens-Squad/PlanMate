package ui

import ui.featuresui.*
import ui.io.ConsoleIO

class PlanMateCli(
    private val consoleIO: ConsoleIO,
    private val authenticationUi: AuthenticationUi,
    private val projectsUi: ProjectsUi,
    private val tasksUi: TasksUi,
    private val statesUi: StatesUi,
    private val auditLogUi: AuditLogUi
) {
    fun run() {
        consoleIO.printer.printWelcomeMessage("Welcome to Athens Plan Mate...")
        authenticationUi.runAuthenticationUi { navigateToUserFeatures() }

    }



    private fun navigateToUserFeatures() {
        showUserOptions()

    }

    private fun showUserOptions() {
        consoleIO.printer.printInfoLine("3 : Edit Project")
        consoleIO.printer.printInfoLine("4 : Delete Project")

        consoleIO.printer.printInfoLine("5 : ")
        showAdminOptions()
    }

    private fun showAdminOptions() {
        //-----------nate -> option 1 only-------------
        consoleIO.printer.printInfoLine("1 : Show Projects.")
        consoleIO.printer.printInfoLine("2 : Create Project.")
        consoleIO.printer.printInfoLine("3 : Create Mate.")

        //1 -> show all projects
        // user selects a project
        // show project with tasks swimlanes
        // options:
        //------------mate -> option 2 only------------
            // 1 -> edit projects
                    // name, description
            // 2 -> manage project tasks
                    //select task
                        // edit(title, current state, description), delete, create, show task history
            // 3 -> manage project state
                    // select state
                        // edit(name), delete, create
            // 4 -> show project history
            // 5 -> delete project -> showAdminOptions()
        // 2 -> create project -> showAllProjects()
        // 3 -> create mate -> showAdminOptions()

    }

}
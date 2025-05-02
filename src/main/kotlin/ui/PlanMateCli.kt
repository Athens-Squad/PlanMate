package ui

import logic.entities.Project
import logic.entities.UserType
import net.thechance.data.authentication.UserSession
import net.thechance.ui.options.AdminOptions
import net.thechance.ui.options.audit_log.AuditLogOptions
import net.thechance.ui.options.project.ProjectOptions
import ui.featuresui.*
import ui.io.ConsoleIO

class PlanMateCli(
    private val consoleIO: ConsoleIO,
    private val authenticationUi: AuthenticationUi,
    private val session: UserSession,
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
        when (session.currentUser.type) {
            UserType.AdminUser -> showAdminOptions()
            is UserType.MateUser -> showMateOptions()
        }

    }

    private fun showMateOptions() {
        //
    }

    private fun showAdminOptions() {
        //-----------nate -> option 1 only-------------
        do {
            consoleIO.printer.printOptions(AdminOptions.entries)

            val userInput = consoleIO.reader.readNumberFromUser()
            when (userInput) {
                AdminOptions.SHOW_ALL_PROJECTS.optionNumber -> handleShowAllProjectsOption()
                AdminOptions.CREATE_PROJECT.optionNumber -> projectsUi.createProject()
                AdminOptions.CREATE_MATE.optionNumber -> authenticationUi.createMate(session.currentUser.id)
                AdminOptions.EXIT.optionNumber -> {
                    consoleIO.printer.printGoodbyeMessage("We will miss you.")
                }

            }
        } while (userInput != AdminOptions.EXIT.optionNumber)
        //1 -> show all projects
        // user selects a project
        // show project with tasks swimlanes
        // options:
        //------------mate -> option 2 only------------

            // 2 -> manage project tasks  /*In progress() */
            //select task
            //      edit(title, current state, description), delete, create, show task history
            // 3 -> manage project state
            //      select state
            //      edit(name), delete, create
            // 4 -> show project history  /*DONE*/
            // 5 -> delete project -> showAdminOptions()  /*DONE*/
        // 3 -> create mate -> showAdminOptions()  /*DONE*/

    }

    private fun handleShowAllProjectsOption() {
        projectsUi.getAllProjects(session.currentUser.id)
            .onSuccess { projects ->
                projects.forEach { project ->
                    consoleIO.printer.printPlainText(project.name)
                }
                selectProject(projects)
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
            }
    }

    private fun selectProject(projects: List<Project>) {
        consoleIO.printer.printTitle("Select A Project :")
        val inputProjectName = consoleIO.reader.readStringFromUser()
        projectsUi.getProject(getProject(inputProjectName, projects).id)
            .onSuccess {
                showProjectSwimlanes(it)
            }
            .onFailure {
                consoleIO.printer.printError(it.message.toString())
                handleShowAllProjectsOption()
            }
    }

    private fun showProjectSwimlanes(project: Project) {
        //TODO show swimlanes
        do {
            consoleIO.printer.printTitle("Select Option (1 to 5):")
            consoleIO.printer.printOptions(ProjectOptions.entries)

            val inputProjectOption = consoleIO.reader.readNumberFromUser()

            when (inputProjectOption) {
                ProjectOptions.EDIT.optionNumber -> projectsUi.editProject(project)
                ProjectOptions.MANAGE_STATES.optionNumber -> statesUi.manageStates(project.progressionStates)
                ProjectOptions.MANAGE_TASKS.optionNumber -> tasksUi.manageTasks(project.tasks)
                ProjectOptions.SHOW_HISTORY.optionNumber -> {
                    auditLogUi.getProjectHistory(project.id)
                    showHistoryOption()
                }

                ProjectOptions.DELETE.optionNumber -> {
                    projectsUi.deleteProject(project.id)
                        .onSuccess {
                            consoleIO.printer.printCorrectOutput("Project Deleted Successfully")
                        }
                        .onFailure {
                            consoleIO.printer.printError(it.message.toString())
                        }

                }
            }
        } while (inputProjectOption != ProjectOptions.BACK.optionNumber ||
            inputProjectOption != ProjectOptions.DELETE.optionNumber
        )

    }

    private fun showHistoryOption() { //TODO() call from task history
        consoleIO.printer.printTitle("Select Option (1)")
        consoleIO.printer.printOptions(AuditLogOptions.entries)
        val inputHistoryOption = consoleIO.reader.readNumberFromUser()

        when (inputHistoryOption) {
            AuditLogOptions.CLEAR_LOG.optionNumber ->{
                auditLogUi.clearLog()
                    .onSuccess {
                        consoleIO.printer.printCorrectOutput("History Deleted Successfully.")
                    }
                    .onFailure {
                        consoleIO.printer.printError(it.message.toString())
                    }
            }
        }

    }

    private fun getProject(inputProjectName: String, projects: List<Project>): Project {
        return projects.first { it.name == inputProjectName }
    }


    // log
    /*
    a created b

    select
    clear log

    1
     */

}
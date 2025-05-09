package di


import data.authentication.utils.PasswordHashing
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidatorImpl
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidatorImpl
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidatorImp
import org.koin.dsl.module

val appModule = module {

	single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
	single<ProgressionStateValidator> { ProgressionStateValidatorImpl(get(), get()) }
	single<UserValidator> { UserValidatorImpl(get()) }
	single <ProjectValidator>{ ProjectValidatorImp()  }

	single { PasswordHashing() }

}

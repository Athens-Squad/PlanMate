package di


import data.authentication.utils.PasswordHashing
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidatorImpl
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidatorImpl
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import org.koin.dsl.module

val appModule = module {

	single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
	single<ProgressionStateValidator> { ProgressionStateValidatorImpl(get(), get()) }
	single<UserValidator> { UserValidatorImpl(get()) }

	single { PasswordHashing() }


}

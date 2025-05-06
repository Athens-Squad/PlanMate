package di


import data.authentication.utils.PasswordHashing
import logic.entities.ProgressionState
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateUseCaseValidatorImpl
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidatorImpl
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.UseCaseValidator
import org.koin.dsl.module

val appModule = module {

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
	single<UseCaseValidator<ProgressionState>> { ProgressionStateUseCaseValidatorImpl(get(), get()) }
	single <UserValidator>{UserValidatorImpl(get())  }

    single { PasswordHashing() }


}

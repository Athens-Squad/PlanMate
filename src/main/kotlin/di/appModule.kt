package di


import data.authentication.utils.PasswordHashing
import logic.use_cases.progression_state.progressionStateValidations.ProgressionProgressionStateValidationImp
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import org.koin.dsl.module

val appModule = module {

    single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
    single<ProgressionStateValidator> { ProgressionProgressionStateValidationImp(get(), get()) }

    single { PasswordHashing() }


}

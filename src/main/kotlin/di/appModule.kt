package di


import data.authentication.utils.PasswordHashing
import logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidatorImpl
import logic.use_cases.task.taskvalidations.TaskValidator
import logic.use_cases.task.taskvalidations.TaskValidatorImpl
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidator
import net.thechance.logic.use_cases.audit_log.auditLogValidations.AuditLogValidatorImpl
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidator
import net.thechance.logic.use_cases.authentication.uservalidation.UserValidatorImpl
import net.thechance.logic.use_cases.progression_state.progressionStateValidations.ProgressionStateValidator
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidatorImpl
import org.koin.dsl.module

val appModule = module {

	single<UserValidator> { UserValidatorImpl(get()) }
	single<ProjectValidator> { ProjectValidatorImpl(get(), get()) }
	single<ProgressionStateValidator> { ProgressionStateValidatorImpl(get(), get()) }
	single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
	single<AuditLogValidator> { AuditLogValidatorImpl() }

	single { PasswordHashing() }


}

package di


import data.authentication.utils.PasswordHashing
import net.thechance.logic.validators.progressionStateValidations.ProgressionStateValidatorImpl
import net.thechance.logic.validators.taskvalidations.TaskValidator
import net.thechance.logic.validators.taskvalidations.TaskValidatorImpl
import net.thechance.logic.validators.auditLogValidations.AuditLogValidator
import net.thechance.logic.validators.auditLogValidations.AuditLogValidatorImpl
import net.thechance.logic.validators.uservalidation.UserValidator
import net.thechance.logic.validators.uservalidation.UserValidatorImpl
import net.thechance.logic.validators.progressionStateValidations.ProgressionStateValidator
import net.thechance.logic.validators.projectValidations.ProjectValidator
import net.thechance.logic.validators.projectValidations.ProjectValidatorImpl
import org.koin.dsl.module

val appModule = module {

	single<UserValidator> { UserValidatorImpl(get()) }
	single<ProjectValidator> { ProjectValidatorImpl(get(), get()) }
	single<ProgressionStateValidator> { ProgressionStateValidatorImpl(get(), get()) }
	single<TaskValidator> { TaskValidatorImpl(get(), get(), get()) }
	single<AuditLogValidator> { AuditLogValidatorImpl() }

	single { PasswordHashing() }


}

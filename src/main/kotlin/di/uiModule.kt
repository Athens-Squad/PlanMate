package di

import ui.PlanMateCli
import ui.featuresui.*
import ui.io.ConsoleIO
import ui.io.Printer
import ui.io.Reader
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.utils.Colors

val uiModule = module {
    single { Colors() }

    singleOf(::Printer)
    singleOf(::Reader)
    singleOf(::ConsoleIO)


    singleOf(::AuditLogUi)
    singleOf(::ProjectsUi)
    singleOf(::StatesUi)
    singleOf(::TasksUi)
    singleOf(::AuthenticationUi)

    singleOf(::PlanMateCli)
}


package di

import net.thechance.ui.handlers.*
import net.thechance.ui.utils.ProjectSelector
import net.thechance.ui.utils.ShowProjectSwimlanes
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

    singleOf(::ShowProjectSwimlanes)
    singleOf(::ProjectSelector)


    singleOf(::ProjectOptionsHandler)
    singleOf(::AdminOptionsHandler)
    singleOf(::MateOptionsHandler)

    singleOf(::PlanMateCli)
}


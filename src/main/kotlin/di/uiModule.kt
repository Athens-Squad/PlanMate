package di

import net.thechance.ui.handlers.AdminOptionsHandler
import net.thechance.ui.handlers.MateOptionsHandler
import net.thechance.ui.handlers.ProjectOptionsHandler
import net.thechance.ui.utils.ProjectSelector
import net.thechance.ui.utils.ShowProjectSwimlanes
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ui.PlanMateCli
import ui.featuresui.*
import ui.io.ConsoleIO
import ui.io.Printer
import ui.io.Reader

val uiModule = module {

    singleOf(::Printer)
    singleOf(::Reader)
    singleOf(::ConsoleIO)


    singleOf(::AuditLogUi)
    singleOf(::ProjectsUi)
    singleOf(::ProgressionStateUi)
    singleOf(::TasksUi)
    singleOf(::AuthenticationUi)

    singleOf(::ShowProjectSwimlanes)
    singleOf(::ProjectSelector)


    singleOf(::ProjectOptionsHandler)
    singleOf(::AdminOptionsHandler)
    singleOf(::MateOptionsHandler)

    singleOf(::PlanMateCli)
}


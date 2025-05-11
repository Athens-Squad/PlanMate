package di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import net.thechance.ui.core.io.*
import net.thechance.ui.featuresui.*
import net.thechance.ui.presenters.*
import net.thechance.ui.PlanMateAppRunner
import net.thechance.ui.core.io.ConsoleIO


val uiModule = module {

//core
    //io
    singleOf(::Printer)
    singleOf(::Reader)
    singleOf(::ConsoleIO)

//features ui
    singleOf(::AuditLogUi)
    singleOf(::ProjectsUi)
    singleOf(::ProgressionStateUi)
    singleOf(::TasksUi)
    singleOf(::AuthenticationUi)

//presenters
    singleOf(::ProjectPresenter)
    singleOf(::MatePresenter)
    singleOf(::AdminPresenter)
    singleOf(::AuthenticationPresenter)

//App Runner
    singleOf(::PlanMateAppRunner)

}


package di

import net.thechance.ui.PlanMateAppRunner
import net.thechance.ui.core.io.ConsoleIO
import net.thechance.ui.core.io.Printer
import net.thechance.ui.core.io.Reader
import net.thechance.ui.featuresui.*
import net.thechance.ui.presenters.AdminPresenter
import net.thechance.ui.presenters.AuthenticationPresenter
import net.thechance.ui.presenters.MatePresenter
import net.thechance.ui.presenters.ProjectPresenter
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


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


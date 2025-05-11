package net.thechance


import di.appModule
import di.repositoriesModule
import di.uiModule
import di.useCasesModule
import net.thechance.di.dataSourceModule
import net.thechance.di.mongoModule
import net.thechance.ui.PlanMateAppRunner
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    startKoin {
        modules(appModule, repositoriesModule, useCasesModule, uiModule, dataSourceModule, mongoModule)
    }
    val app = getKoin().get<PlanMateAppRunner>()
    app.run()

}
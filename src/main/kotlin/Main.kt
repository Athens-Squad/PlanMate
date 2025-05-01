package net.thechance


import di.appModule
import di.repositoriesModule
import di.uiModule
import di.useCasesModule
import ui.PlanMateCli
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import java.util.UUID

fun main() {
    startKoin {
        modules(appModule, repositoriesModule, useCasesModule, uiModule)
    }
    val cli = getKoin().get<PlanMateCli>()
    cli.run()

}
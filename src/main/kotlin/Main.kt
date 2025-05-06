package net.thechance


import di.appModule
import di.repositoriesModule
import di.uiModule
import di.useCasesModule
import net.thechance.di.dataSourceModule
import ui.PlanMateCli
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main()

{
        startKoin {
            modules(appModule, repositoriesModule, useCasesModule, uiModule, dataSourceModule)
        }


        val cli = getKoin().get<PlanMateCli>()
        cli.run()

}
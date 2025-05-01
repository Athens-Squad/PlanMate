import di.appModule
import di.repositoriesModule
import di.uiModule
import di.useCasesModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import ui.PlanMateCli

fun main() {
    startKoin {
        modules(appModule, repositoriesModule, useCasesModule, uiModule)
    }
    val cli = getKoin().get<PlanMateCli>()
    cli.run()

}
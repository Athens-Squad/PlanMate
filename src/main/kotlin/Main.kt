package net.thechance


import logic.repositories.StatesRepository
import logic.use_cases.state.CreateStateUseCase
import logic.use_cases.state.GetStateByIdUseCase
import net.thechance.di.appModule
import net.thechance.di.repositoriesModule
import net.thechance.logic.entities.State
import org.koin.core.context.GlobalContext.get
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    startKoin {
        modules(appModule, repositoriesModule)
    }
    val repo = getKoin().get<StatesRepository>()
    val createStateUseCase : CreateStateUseCase = getKoin().get()
    val getStateByIdUseCase: GetStateByIdUseCase = getKoin().get()

    val state = State(
        id = "Task 1",
        name = "Done",
        projectId = "p1"
    )
    createStateUseCase.execute(state)
    getStateByIdUseCase.execute(state.id)

}
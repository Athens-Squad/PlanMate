package net.thechance.di

import logic.repositories.AuditRepository
import net.thechance.data.aduit_log_csvfile.repository.AuditLogRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {
    single<AuditRepository> {AuditLogRepositoryImpl(get()) }
}
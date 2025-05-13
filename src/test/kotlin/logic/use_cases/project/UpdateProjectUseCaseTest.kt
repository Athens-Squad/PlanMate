@file:OptIn(ExperimentalUuidApi::class)

package logic.use_cases.project

import helper.project_helper.createProject
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.entities.EntityType
import logic.repositories.*
import logic.use_cases.audit_log.CreateAuditLogUseCase
import net.thechance.logic.exceptions.InvalidProjectFieldsException
import net.thechance.logic.use_cases.project.projectValidations.ProjectValidator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.uuid.ExperimentalUuidApi

class UpdateProjectUseCaseTest {
    private lateinit var updateProject: UpdateProjectUseCase
    private lateinit var projectRepository: ProjectsRepository
    private lateinit var projectValidator: ProjectValidator
    private lateinit var createAuditLogUseCase: CreateAuditLogUseCase

    @BeforeEach
    fun setUp() {
        projectRepository = mockk()
        projectValidator = mockk()
        createAuditLogUseCase = mockk()
        updateProject = UpdateProjectUseCase(projectRepository, projectValidator, createAuditLogUseCase)
    }

    @Test
    fun `should throw exception when validation fails`() = runTest {
        // given
        val project = createProject()
        coEvery { projectValidator.validateProjectFieldsNotBlank(project) } throws InvalidProjectFieldsException()

        // when & then
        assertThrows<InvalidProjectFieldsException> {
            updateProject.execute(project)
        }

        coVerify(exactly = 0) { projectRepository.updateProject(any()) }
        coVerify(exactly = 0) { createAuditLogUseCase.execute(any()) }
    }

    @Test
    fun `should update project and create audit log when validation succeeds`() = runTest {
        // given
        val project = createProject()

        // Mock successful validations
        coEvery { projectValidator.validateProjectFieldsNotBlank(project) } returns true
        coEvery { projectValidator.validateUserIsAuthorized(project.createdByUserName) } returns true
        coEvery { projectValidator.validateProjectAlreadyExists(project.id) } returns true
        coEvery { projectValidator.validateUserIsTheProjectOwner(project.id, project.createdByUserName) } returns true

        // Mock dependencies
        coEvery { projectRepository.updateProject(project) } just Runs
        coEvery { createAuditLogUseCase.execute(any()) } just Runs

        // when
        updateProject.execute(project)

        // then
        coVerify(exactly = 1) { projectRepository.updateProject(project) }
        coVerify(exactly = 1) {
            createAuditLogUseCase.execute(
                match { auditLog ->
                    auditLog.entityType == EntityType.PROJECT &&
                            auditLog.entityId == project.id &&
                            auditLog.description == "Project updated successfully." &&
                            auditLog.userName == project.createdByUserName
                }
            )
        }
    }

}
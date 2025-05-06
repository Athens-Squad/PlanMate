package net.thechance.data.projects.dto

import logic.entities.Project
import org.bson.codecs.pojo.annotations.BsonId
import java.util.*

data class ProjectDto(
    @BsonId
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val createdBy: String
) {
    fun toProject() = Project(
        id = id,
        name = name,
        description = description,
        createdBy = createdBy
    )

    companion object {
        fun fromProject(project: Project)  = ProjectDto(
            id = project.id,
            name = project.name,
            description = project.description,
            createdBy = project.createdBy
        )
    }
}

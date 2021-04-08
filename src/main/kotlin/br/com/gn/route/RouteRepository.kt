package br.com.gn.route

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface RouteRepository : JpaRepository<Route, UUID> {
    fun existsByNameAndExporterCodeAndImporterPlantAndType(
        name: String,
        exporterCode: String,
        importerPlant: String,
        type: OperationType
    ): Boolean

    fun findByName(name: String): List<Route>
}
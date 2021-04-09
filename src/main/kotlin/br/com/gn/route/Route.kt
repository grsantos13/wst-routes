package br.com.gn.route

import br.com.gn.RouteResponse
import br.com.gn.route.event.RouteEventRequest
import br.com.gn.utils.toEnum
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.PERSIST
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import br.com.gn.OperationType as GrpcOperationType

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "route_name_uk", columnNames = ["name"])
    ]
)
class Route(
    @field:NotBlank @Column(nullable = false, unique = true) val name: String,
    @field:NotBlank @field:Size(min = 8, max = 8) val exporterCode: String,
    @field:NotBlank @field:Size(max = 4) val importerPlant: String,
    @field:NotNull @Enumerated(EnumType.STRING) val type: OperationType?,
    events: List<RouteEventRequest>,
    manager: EntityManager
) {

    @field:Valid
    @field:Size(min = 1)
    @field:NotNull
    @OneToMany(mappedBy = "route", cascade = [PERSIST])
    val events = events.map { it.toModel(manager, this) }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: UUID? = null

    fun toGrpcRouteResponse(): RouteResponse{
        return RouteResponse.newBuilder()
            .setId(id.toString())
            .setName(name)
            .addAllRouteEvent(events.map { it.toGrpcRouteEventResponse() })
            .setExporterCode(exporterCode)
            .setImporterPlant(importerPlant)
            .setType(type!!.name.toEnum<GrpcOperationType>())
            .build()
    }

}

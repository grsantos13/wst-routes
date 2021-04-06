package br.com.gn.route

import br.com.gn.RouteEventResponse
import br.com.gn.event.Event
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "route_event_index_order_uk", columnNames = ["index_order", "route_id"])
    ]
)
class RouteEvent(
    @field:NotNull @field:Valid @ManyToOne @JoinColumn(nullable = false) val event: Event,
    @field:NotNull @field:PositiveOrZero @Column(nullable = false) val leadTime: Int,
    @field:NotNull @field:Positive @Column(nullable = false, name = "index_order") val indexOrder: Int,
    @field:NotNull @field:Valid @ManyToOne @JoinColumn(nullable = false) val route: Route
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toGrpcRouteEventResponse(): RouteEventResponse{
        return RouteEventResponse.newBuilder()
            .setId(id!!)
            .setEvent(event.toGrpcEventResponse())
            .setLeadtime(leadTime)
            .setIndex(indexOrder)
            .build()
    }
}

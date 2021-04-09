package br.com.gn.route.event

import br.com.gn.event.Event
import br.com.gn.route.Route
import br.com.gn.shared.exception.ObjectNotFoundException
import io.micronaut.core.annotation.Introspected
import javax.persistence.EntityManager
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Introspected
data class RouteEventRequest(
    @field:NotNull val eventId: Long,
    @field:NotNull @field:PositiveOrZero val leadTime: Int,
    @field:NotNull @field:Positive val indexOrder: Int
) {

    fun toModel(manager: EntityManager, route: Route): RouteEvent {
        val event = (manager.find(Event::class.java, eventId)
            ?: throw ObjectNotFoundException("Event not found with id $eventId"))
        return RouteEvent(event, leadTime, indexOrder, route)
    }

}

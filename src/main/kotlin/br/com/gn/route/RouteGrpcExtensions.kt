package br.com.gn.route

import br.com.gn.NewRouteRequest
import br.com.gn.RouteEventRequest
import br.com.gn.utils.toEnum
import javax.persistence.EntityManager
import br.com.gn.route.RouteEventRequest as RouteEventRequestModel

fun NewRouteRequest.toModel(manager: EntityManager): Route {
    return Route(
        name = name,
        exporterCode = exporterCode,
        importerPlant = importerPlant,
        events = routeEventList.map { it.toRequestModel() },
        manager = manager,
        type = type.name.toEnum<OperationType>()
    )
}

fun RouteEventRequest.toRequestModel(): RouteEventRequestModel {
    return RouteEventRequestModel(
        eventId = eventId,
        leadTime = leadtime,
        indexOrder = index
    )
}

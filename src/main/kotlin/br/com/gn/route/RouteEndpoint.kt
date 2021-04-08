package br.com.gn.route

import br.com.gn.*
import br.com.gn.register.Register
import br.com.gn.shared.exception.ErrorHandler
import io.grpc.stub.StreamObserver
import io.micronaut.validation.validator.Validator
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.validation.ConstraintViolationException

@ErrorHandler
@Singleton
class RouteEndpoint(
    private val repository: RouteRepository,
    private val manager: EntityManager,
    private val validator: Validator
) : RouteServiceGrpc.RouteServiceImplBase() {

    @Transactional
    override fun create(request: NewRouteRequest, responseObserver: StreamObserver<RouteResponse>) {
        val route = request.toModel(manager)
        val validation = validator.validate(route)

        if (validation.isNotEmpty())
            throw ConstraintViolationException(validation)

        manager.createQuery(
            " select r from Register r where " +
                    " name = :name and type = :type and importPlant = :plant and exporterCode = :code ",
            Register::class.java
        )
            .setParameter("name", request.name)
            .setParameter("plant", request.importerPlant)
            .setParameter("code", request.exporterCode)
            .setParameter("type", request.type)
            .resultList
            .forEach { manager.remove(it) }

        repository.save(route)
        responseObserver.onNext(route.toGrpcRouteResponse())
        responseObserver.onCompleted()

    }

    @Transactional
    override fun read(request: ReadRouteRequest, responseObserver: StreamObserver<RoutesResponse>) {
        var routes = when (request.name) {
            null -> repository.findAll()
            else -> repository.findByName(request.name)
        }.map { it.toGrpcRouteResponse() }

        responseObserver.onNext(
            RoutesResponse.newBuilder()
                .addAllRoutes(routes)
                .build()
        )
        responseObserver.onCompleted()
    }

    @Transactional
    override fun awaitingRegistration(
        request: AwaitingRegistrationRequest,
        responseObserver: StreamObserver<AwaitingRegistrationResponse>
    ) {
        val list = manager.createQuery(" select r from Register r ", Register::class.java)
            .resultList
            .map { it.toGrpcRouteResponse() }

        responseObserver.onNext(
            AwaitingRegistrationResponse.newBuilder()
                .addAllRoutes(list)
                .build()
        )
        responseObserver.onCompleted()
    }
}
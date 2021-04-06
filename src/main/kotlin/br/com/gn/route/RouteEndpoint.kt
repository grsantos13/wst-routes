package br.com.gn.route

import br.com.gn.*
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

        repository.save(route)
        responseObserver.onNext(route.toGrpcRouteResponse())
        responseObserver.onCompleted()

    }

    @Transactional
    override fun exists(request: ExistsRouteRequest, responseObserver: StreamObserver<ExistsRouteResponse>) {
        responseObserver.onNext(
            ExistsRouteResponse.newBuilder()
                .setExists(repository.existsByName(request.name))
                .build()
        )
        responseObserver.onCompleted()
    }
}
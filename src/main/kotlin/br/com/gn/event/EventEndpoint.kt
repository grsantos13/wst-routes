package br.com.gn.event

import br.com.gn.*
import br.com.gn.shared.exception.ErrorHandler
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional

@ErrorHandler
@Singleton
class EventEndpoint(
    private val manager: EntityManager
) : EventServiceGrpc.EventServiceImplBase() {

    @Transactional
    override fun create(request: NewEventRequest, responseObserver: StreamObserver<EventResponse>) {
        if (request.name.isNullOrBlank())
            throw IllegalArgumentException("Name must not be blank")

        val event = Event(request.name)
        manager.persist(event)

        val response = EventResponse.newBuilder()
            .setId(event.id.toString())
            .setName(event.name)
            .build()
        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    @Transactional
    override fun read(request: ReadEventRequest, responseObserver: StreamObserver<EventsResponse>) {
        val events = manager.createQuery(" select e from Event e order by name ", Event::class.java)
            .resultList
            .map {
                EventResponse.newBuilder()
                    .setId(it.id.toString())
                    .setName(it.name)
                    .build()
            }

        responseObserver.onNext(
            EventsResponse.newBuilder()
                .addAllEvent(events)
                .build()
        )
        responseObserver.onCompleted()

    }
}
package br.com.gn.shared.kafka

import br.com.gn.register.Register
import br.com.gn.route.OperationType
import br.com.gn.route.RouteRepository
import br.com.gn.utils.toEnum
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.messaging.annotation.Body
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Singleton
@KafkaListener(groupId = "routes")
class Consumer(
    private val manager: EntityManager,
    private val repository: RouteRepository
) {

    @Transactional
    @Topic("routes")
    fun receive(@KafkaKey name: String, @Valid @Body message: RouteMessage) {
        val exists = with(message) {
            val type = type.name.toEnum<OperationType>()
                ?: throw IllegalArgumentException("Operation type must not be null")

            val exists = repository.existsByNameAndExporterCodeAndImporterPlantAndType(
                name = name,
                exporterCode = exporterCode,
                importerPlant = importerPlant,
                type = type
            )

            if (!exists)
                manager.persist(Register(name, exporterCode, importerPlant, type))
        }
    }
}
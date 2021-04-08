package br.com.gn.register

import br.com.gn.AwaitingRegistrationResponse.AwaitingRegistrationRouteResponse
import br.com.gn.route.OperationType
import br.com.gn.utils.toEnum
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Register(
    @field:NotBlank @Column(nullable = false, unique = true) val name: String,
    @field:NotBlank @field:Size(min = 8, max = 8) val exporterCode: String,
    @field:NotBlank @field:Size(max = 4) val importerPlant: String,
    @field:NotNull @Enumerated(EnumType.STRING) val type: OperationType?
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toGrpcRouteResponse(): AwaitingRegistrationRouteResponse {
        return AwaitingRegistrationRouteResponse.newBuilder()
            .setName(name)
            .setExporterCode(exporterCode)
            .setImporterPlant(importerPlant)
            .setType(type!!.name.toEnum<br.com.gn.OperationType>())
            .build()
    }
}
package br.com.gn.event

import br.com.gn.EventResponse
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "event_name_uk", columnNames = ["name"])
    ]
)
class Event(@field:NotBlank @Column(nullable = false) val name: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun toGrpcEventResponse(): EventResponse {
        return EventResponse.newBuilder()
            .setId(id.toString())
            .setName(name)
            .build()
    }

}

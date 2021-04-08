package br.com.gn.register

import br.com.gn.route.OperationType
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
}
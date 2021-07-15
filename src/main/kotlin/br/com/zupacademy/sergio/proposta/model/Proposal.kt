package br.com.zupacademy.sergio.proposta

import CpfOrCnpj
import br.com.zupacademy.sergio.proposta.validation.UniqueValue
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

@Entity
class Proposal(

  @Column(nullable = false)
  private var nationalRegistryId: String,

  @Column(nullable = false)
  private var email: String,

  @Column(nullable = false)
  private var name: String,

  @Column(nullable = false)
  private var address: String,

  @Column(nullable = false)
  private var salary: BigDecimal,

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private var id: String? = null
) {
  fun getId(): String? = this.id
  fun getSalary(): BigDecimal = this.salary

  fun obfuscatedNationalRegistryId(): String {
    if (14 == this.nationalRegistryId.length)
      return this.nationalRegistryId.replaceRange(4, 11, "***.***")
    return this.nationalRegistryId.replaceRange(3, 15, "***.***/****")
  }
}

class ProposalRequest(

  @field:CpfOrCnpj
  @UniqueValue(domainClass = Proposal::class, fieldName = "nationalRegistryId")
  private val nationalRegistryId: String,

  @field:Email
  @field:NotEmpty
  private val email: String,

  @field:NotBlank
  private val name: String,

  @field:NotBlank
  private val address: String,

  @field:Positive
  private val salary: BigDecimal
) {
  fun toProposal() = Proposal(
    nationalRegistryId = this.nationalRegistryId,
    email = this.email,
    name = this.name,
    address = this.address,
    salary = this.salary
  )
}

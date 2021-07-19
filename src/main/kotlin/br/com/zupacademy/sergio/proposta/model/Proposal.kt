package br.com.zupacademy.sergio.proposta.model

import CpfOrCnpj
import br.com.zupacademy.sergio.proposta.model.external.AnalysisResponse
import br.com.zupacademy.sergio.proposta.model.external.CreditCardResponse
import br.com.zupacademy.sergio.proposta.validation.UniqueValue
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Positive

@Entity
class Proposal(

  @Column(nullable = false)
  val nationalRegistryId: String,

  @Column(nullable = false)
  private val email: String,

  @Column(nullable = false)
  val name: String,

  @Column(nullable = false)
  private val address: String,

  @Column(nullable = false)
  private val salary: BigDecimal
) {

  @Id
  val id: String = UUID.randomUUID().toString()

  @Enumerated(EnumType.STRING)
  private var state: ProposalState? = null

  private var creditCardNumber: String? = null
    get() = field?.replaceRange(5, 14, "****-****")

  fun withStateFrom(analysisResponse: AnalysisResponse): Proposal {
    this.state = analysisResponse.proposalState()
    return this
  }

  fun withCreditCardNumberFrom(creditCardResponse: CreditCardResponse): Proposal {
    this.creditCardNumber = creditCardResponse.id
    return this
  }

  private fun obfuscatedNationalRegistryId(): String {
    if (14 == this.nationalRegistryId.length)
      return this.nationalRegistryId.replaceRange(4, 11, "***.***")
    return this.nationalRegistryId.replaceRange(3, 15, "***.***/****")
  }

  override fun toString(): String {
    return "Proposal(" +
    "nationalRegistryId='${this.obfuscatedNationalRegistryId()}', " +
    "salary=$salary, " +
    "state=$state," +
    "creditCardNumber=$creditCardNumber)"
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

enum class ProposalState { NAO_ELEGIVEL, ELEGIVEL }

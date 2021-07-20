package br.com.zupacademy.sergio.proposta.model

import CpfOrCnpj
import br.com.zupacademy.sergio.proposta.validation.UniqueValue
import org.hibernate.annotations.GenericGenerator
import java.math.BigDecimal
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
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  var id: String? = null

  @Enumerated(EnumType.STRING)
  private var state: ProposalState? = null

  private var creditCardNumber: String? = null

  private constructor(
    proposal: Proposal,
    state: ProposalState
  ) : this(
    nationalRegistryId = proposal.nationalRegistryId,
    email = proposal.email,
    name = proposal.name,
    address = proposal.address,
    salary = proposal.salary
  ) {
    this.id = proposal.id
    this.state = state
    this.creditCardNumber = proposal.creditCardNumber
  }

  private constructor(
    proposal: Proposal,
    creditCardNumber: String
  ) : this(
    nationalRegistryId = proposal.nationalRegistryId,
    email = proposal.email,
    name = proposal.name,
    address = proposal.address,
    salary = proposal.salary
  ) {
    this.id = proposal.id
    this.state = proposal.state
    this.creditCardNumber = creditCardNumber
  }

  fun withState(state: ProposalState): Proposal =
    Proposal(proposal = this, state = state)

  fun withCreditCardNumber(creditCardNumber: String): Proposal =
    Proposal(proposal = this, creditCardNumber = creditCardNumber)

  fun obfuscatedCreditCardNumber(): String? =
    this.creditCardNumber?.replaceRange(5, 14, "****-****")

  fun obfuscatedNationalRegistryId(): String {
    if (14 == this.nationalRegistryId.length)
      return this.nationalRegistryId.replaceRange(4, 11, "***.***")
    return this.nationalRegistryId.replaceRange(3, 15, "***.***/****")
  }

  override fun toString(): String {
    return "Proposal(" +
    "nationalRegistryId='${this.obfuscatedNationalRegistryId()}', " +
    "salary=$salary, " +
    "state=$state," +
    "creditCardNumber=${obfuscatedCreditCardNumber()})"
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

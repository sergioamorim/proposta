package br.com.zupacademy.sergio.proposal.model

import CpfOrCnpj
import br.com.zupacademy.sergio.proposal.validation.UniqueValue
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
  val salary: BigDecimal
) {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  var id: String? = null
    private set

  @Enumerated(EnumType.STRING)
  var state: ProposalState? = null
    private set

  @OneToOne(cascade = [CascadeType.MERGE])
  var creditCard: CreditCard? = null
    private set

  private constructor(proposal: Proposal, state: ProposalState) : this(
    nationalRegistryId = proposal.nationalRegistryId,
    email = proposal.email,
    name = proposal.name,
    address = proposal.address,
    salary = proposal.salary
  ) {
    this.id = proposal.id
    this.state = state
    this.creditCard = proposal.creditCard
  }

  private constructor(proposal: Proposal, creditCard: CreditCard) : this(
    nationalRegistryId = proposal.nationalRegistryId,
    email = proposal.email,
    name = proposal.name,
    address = proposal.address,
    salary = proposal.salary
  ) {
    this.id = proposal.id
    this.state = proposal.state
    this.creditCard = creditCard
  }

  fun withState(state: ProposalState): Proposal =
    Proposal(proposal = this, state = state)

  fun withCreditCard(creditCard: CreditCard): Proposal =
    Proposal(proposal = this, creditCard = creditCard)

  fun obfuscatedNationalRegistryId(): String =
    if (14 == this.nationalRegistryId.length) {
      this.nationalRegistryId.replaceRange(4, 11, "***.***")
    } else {
      this.nationalRegistryId.replaceRange(3, 15, "***.***/****")
    }

  override fun toString(): String {
    return "Proposal(" +
    "nationalRegistryId='${this.obfuscatedNationalRegistryId()}', " +
    "salary=${this.salary}, " +
    "state=${this.state}, " +
    "creditCard=${this.creditCard})"
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

class ProposalDetail(
  proposal: Proposal
) {
  val nationalRegistryId: String = proposal.obfuscatedNationalRegistryId()
  val salary: BigDecimal = proposal.salary
  val state: ProposalState? = proposal.state
  val creditCard =
    if (null == proposal.creditCard) null
    else CreditCardDetail(proposal.creditCard!!)
}

enum class ProposalState { NAO_ELEGIVEL, ELEGIVEL }

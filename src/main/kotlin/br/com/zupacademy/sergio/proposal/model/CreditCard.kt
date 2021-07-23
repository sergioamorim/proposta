package br.com.zupacademy.sergio.proposal.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
class CreditCard(@Column(nullable = false) val number: String) {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private var id: String? = null

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditCard")
  var biometrics: Collection<Biometry> = ArrayList()
    private set

  @OneToOne(mappedBy = "creditCard")
  var block: Block? = null
    private set

  var state: String? = null
    private set

  private constructor(creditCard: CreditCard, state: String) : this(
    number = creditCard.number
  ) {
    this.id = creditCard.id
    this.biometrics = creditCard.biometrics
    this.block = creditCard.block
    this.state = state
  }

  fun blocked() = CreditCard(creditCard = this, state = "BLOQUEADO")

  fun obfuscatedNumber(): String = this.number.replaceRange(
    startIndex = 5, endIndex = 14, replacement = "****-****"
  )

  override fun toString(): String =
    "CreditCard(" +
    "number='${obfuscatedNumber()}', " +
    "biometrics=$biometrics, " +
    "block=$block, " +
    "state=$state" +
    ")"
}

class CreditCardDetail(creditCard: CreditCard) {
  val number: String = creditCard.obfuscatedNumber()
  val biometrics: Collection<BiometryDetail> =
    creditCard.biometrics.map { biometry -> BiometryDetail(biometry) }
  val block: BlockDetail? = creditCard.block?.let { BlockDetail(it) }
  val state: String? = creditCard.state
}

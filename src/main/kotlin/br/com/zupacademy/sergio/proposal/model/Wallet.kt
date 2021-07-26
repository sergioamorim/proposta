package br.com.zupacademy.sergio.proposal.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

@Entity
class Wallet(
  @ManyToOne(optional = false)
  private val creditCard: CreditCard,

  @Column(nullable = false)
  val email: String,

  @Column(nullable = false)
  val type: WalletType
) {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  var id: String? = null
    private set

  fun getCreditCardNumber(): String = this.creditCard.number

  fun getCreditCardId(): String? = this.creditCard.id

  override fun toString(): String {
    return "Wallet(type=$type)"
  }
}

class WalletRequest(
  @field:Email @field:NotEmpty val email: String, val type: WalletType
) {
  fun toWallet(creditCard: CreditCard) = Wallet(
    creditCard = creditCard, email = this.email, type = this.type
  )
}

class WalletDetail(wallet: Wallet) {
  val type: WalletType = wallet.type
}

enum class WalletType { PAYPAL }

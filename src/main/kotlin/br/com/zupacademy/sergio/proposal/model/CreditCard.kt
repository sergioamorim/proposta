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

  fun obfuscatedNumber(): String =
    this.number.replaceRange(5, 14, "****-****")

  override fun toString(): String =
    "CreditCard(number='${obfuscatedNumber()}', biometrics=$biometrics)"
}

class CreditCardDetail(creditCard: CreditCard) {
  val number: String = creditCard.obfuscatedNumber()
  val biometrics: Collection<BiometryDetail> =
    creditCard.biometrics.map { biometry -> BiometryDetail(biometry) }
}

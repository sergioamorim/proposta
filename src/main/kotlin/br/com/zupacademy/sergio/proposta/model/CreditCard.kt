package br.com.zupacademy.sergio.proposta.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class CreditCard(@Column(nullable = false) val number: String) {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private var id: String? = null

  fun obfuscatedNumber(): String =
    this.number.replaceRange(5, 14, "****-****")

  override fun toString(): String =
    "CreditCard(number='${obfuscatedNumber()}')"
}

class CreditCardDetail(creditCard: CreditCard) {
  val number: String = creditCard.obfuscatedNumber()
}

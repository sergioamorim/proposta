package br.com.zupacademy.sergio.proposta.model

import br.com.zupacademy.sergio.proposta.validation.Base64
import com.fasterxml.jackson.annotation.JsonCreator
import org.hibernate.annotations.GenericGenerator
import java.time.ZonedDateTime
import javax.persistence.*

@Entity
class Biometry(
  @Column(nullable = false)
  private val fingerprint: String,

  @ManyToOne(optional = false)
  private val creditCard: CreditCard
) {
  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  var id: String? = null
    private set

  private val creation = ZonedDateTime.now()

  fun obfuscatedFingerprint(): String =
    this.fingerprint.replaceRange(0, 4, "****")

  override fun toString(): String =
    "Biometry(fingerprint='${obfuscatedFingerprint()}')"
}

class BiometryRequest @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
  @field:Base64 private val fingerprint: String
) {
  fun toBiometry(creditCard: CreditCard): Biometry =
    Biometry(this.fingerprint, creditCard)
}

class BiometryDetail(biometry: Biometry) {
  val fingerprint: String = biometry.obfuscatedFingerprint()
}

package br.com.zupacademy.sergio.proposal.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.crypto.encrypt.Encryptors
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
@ConfigurationProperties(prefix = "encryption")
class Encryption : AttributeConverter<String, String> {

  lateinit var password: String
  lateinit var salt: String

  override fun convertToDatabaseColumn(value: String): String =
    Encryptors.queryableText(this.password, this.salt).encrypt(value)

  override fun convertToEntityAttribute(value: String): String =
    Encryptors.queryableText(this.password, this.salt).decrypt(value)

}

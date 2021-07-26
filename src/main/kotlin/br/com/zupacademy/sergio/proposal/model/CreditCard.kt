package br.com.zupacademy.sergio.proposal.model

import org.hibernate.annotations.GenericGenerator
import javax.persistence.*

@Entity
class CreditCard(@Column(nullable = false) val number: String) {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  var id: String? = null
    private set

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditCard")
  private var biometrics: Set<Biometry> = HashSet()

  @OneToOne(mappedBy = "creditCard")
  var block: Block? = null
    private set

  var state: String? = null
    private set

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditCard")
  private var travelNotices: Set<TravelNotice> = HashSet()

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "creditCard")
  private var wallets: Set<Wallet> = HashSet()

  private constructor(creditCard: CreditCard, state: String) : this(
    number = creditCard.number
  ) {
    this.id = creditCard.id
    this.biometrics = creditCard.biometrics
    this.block = creditCard.block
    this.state = state
    this.travelNotices = creditCard.travelNotices
    this.wallets = creditCard.wallets
  }

  fun blocked() = CreditCard(creditCard = this, state = "BLOQUEADO")

  fun hasWalletOfType(walletType: WalletType) =
    this.wallets.any { it.type == walletType }

  fun <T> mapBiometrics(mapper: (biometry: Biometry) -> T): Collection<T> =
    this.biometrics.map { mapper(it) }

  fun <T> mapTravelNotices(mapper: (travelNotice: TravelNotice) -> T): Collection<T> =
    this.travelNotices.map { mapper(it) }

  fun <T> mapWallets(mapper: (wallet: Wallet) -> T): Collection<T> =
    this.wallets.map { mapper(it) }

  fun obfuscatedNumber(): String = this.number.replaceRange(
    startIndex = 5, endIndex = 14, replacement = "****-****"
  )

  override fun toString(): String =
    "CreditCard(" +
    "number='${obfuscatedNumber()}', " +
    "biometrics=$biometrics, " +
    "block=$block, " +
    "state=$state, " +
    "travelNotices=$travelNotices, " +
    "wallets=$wallets" +
    ")"
}

class CreditCardDetail(creditCard: CreditCard) {
  val number: String = creditCard.obfuscatedNumber()
  val biometrics: Collection<BiometryDetail> =
    creditCard.mapBiometrics { BiometryDetail(it) }
  val block: BlockDetail? = creditCard.block?.let { BlockDetail(it) }
  val state: String? = creditCard.state
  val travelNotices: Collection<TravelNoticeDetail> =
    creditCard.mapTravelNotices { TravelNoticeDetail(it) }
  val wallets: Collection<WalletDetail> =
    creditCard.mapWallets { WalletDetail(it) }
}

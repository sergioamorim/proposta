package br.com.zupacademy.sergio.proposal.model.external

import br.com.zupacademy.sergio.proposal.model.Wallet
import br.com.zupacademy.sergio.proposal.model.WalletType

class WalletNotificationRequest(wallet: Wallet) {
  val email: String = wallet.email
  val carteira: WalletType = wallet.type
}

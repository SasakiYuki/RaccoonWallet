package wacode.yamada.yuki.nempaymentapp.usecase

import wacode.yamada.yuki.nempaymentapp.repository.AccountRepository
import wacode.yamada.yuki.nempaymentapp.repository.HarvestRepository
import wacode.yamada.yuki.nempaymentapp.repository.TransactionRepository
import javax.inject.Inject

class HomeUseCase @Inject constructor(
        private val transactionRepository: TransactionRepository,
        private val accountRepository: AccountRepository,
        private val harvestRepository: HarvestRepository) {

    fun getAllTransaction(address: String) = transactionRepository.geAllTransaction(address)

    fun getAccountInfo(address: String) = accountRepository.getAccountInfo(address)

    fun getHarvestInfo(address: String) = harvestRepository.getHarvestInfo(address)
}
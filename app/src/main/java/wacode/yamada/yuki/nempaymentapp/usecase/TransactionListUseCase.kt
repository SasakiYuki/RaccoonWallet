package wacode.yamada.yuki.nempaymentapp.usecase

import io.reactivex.Observable
import wacode.yamada.yuki.nempaymentapp.repository.AccountRepository
import wacode.yamada.yuki.nempaymentapp.repository.MosaicRepository
import wacode.yamada.yuki.nempaymentapp.repository.TransactionRepository
import wacode.yamada.yuki.nempaymentapp.viewmodel.TransactionListViewModel
import javax.inject.Inject


class TransactionListUseCase @Inject constructor(private val transactionRepository: TransactionRepository,
                                                 private val accountRepository: AccountRepository, private val mosaicRepository: MosaicRepository) {

    fun getAllTransaction(address: String, id: Int) = transactionRepository.getAllTransaction(address = address, id = id)
            .flatMapObservable {
                return@flatMapObservable if (it.isEmpty()) {
                    throw IllegalArgumentException(TransactionListViewModel.ERROR_LIST_EMPTY)
                } else {
                    Observable.fromIterable(it)
                }
            }

    fun getUnconfirmedTransactions(address: String) = transactionRepository.getUnconfirmedTransactions(address)
            .flatMapObservable {
                return@flatMapObservable if (it.isEmpty()) {
                    Observable.empty()
                } else {
                    Observable.fromIterable(it)
                }
            }

    fun getAccountAddressFromPublicKey(publicKey: String) = accountRepository.getAccountInfoFromPublicKey(publicKey)
            .map {
                it.account.address
            }
            .toObservable()

    fun getNamespaceMosaics(nameSpace: String) =
            mosaicRepository.getNamespaceMosaics(nameSpace)
}
package wacode.yamada.yuki.nempaymentapp.repository

import wacode.yamada.yuki.nempaymentapp.rest.service.AccountService
import javax.inject.Inject


class AccountRepository @Inject constructor(private val accountService: AccountService) {

    fun getAccountInfo(address: String) = accountService.getAccountInfo(address)
}
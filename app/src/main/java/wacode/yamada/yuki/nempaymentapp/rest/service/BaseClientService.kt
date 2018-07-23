package wacode.yamada.yuki.nempaymentapp.rest.service

import com.ryuta46.nemkotlin.client.RxNemApiClient
import wacode.yamada.yuki.nempaymentapp.rest.ApiManager

abstract class BaseClientService {
    fun getClient() = RxNemApiClient(ApiManager.getBaseUrl())
}
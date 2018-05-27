package wacode.yamada.yuki.nempaymentapp.rest.service

import io.reactivex.Single
import retrofit2.http.GET
import wacode.yamada.yuki.nempaymentapp.rest.model.ActiveNodeEntity


interface NodeExplorerApiService {

    /**
     * 利用可なNode一覧を取得します
     */
    @GET("api_openapi")
    fun getActiveNodeList() :Single<ActiveNodeEntity>
}
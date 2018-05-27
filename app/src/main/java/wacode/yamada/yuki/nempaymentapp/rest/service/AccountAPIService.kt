package wacode.yamada.yuki.nempaymentapp.rest.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import wacode.yamada.yuki.nempaymentapp.rest.model.OwnedMosaicDefinitionEntity

interface AccountAPIService {
    /**
     * 取得しているモザイク一覧を返す
     * nem-kotlinに存在しないので応急処置的に追加
     */
    @GET("account/mosaic/owned/definition")
    fun getOwnedMosaicDefinition(@Query("address") address: String): Single<OwnedMosaicDefinitionEntity>
}
package wacode.yamada.yuki.nempaymentapp.rest.service

import io.reactivex.Single
import retrofit2.http.GET
import wacode.yamada.yuki.nempaymentapp.rest.model.ZaifNemEntity


interface ZaifApiService {
    /**
     * ZaifのNemの価格を取得する
     */
    @GET("api/1/last_price/xem_jpy")
    fun getNemPrice(): Single<ZaifNemEntity>
}
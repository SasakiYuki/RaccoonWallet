package wacode.yamada.yuki.nempaymentapp.rest.service

import io.reactivex.Single
import retrofit2.http.GET
import wacode.yamada.yuki.nempaymentapp.rest.model.NEMGalleryEntity

interface NemGalleryMosaicAPIService {
    @GET("gallery/mosaic_list.json")
    fun getMosaicGallery(): Single<ArrayList<NEMGalleryEntity>>
}
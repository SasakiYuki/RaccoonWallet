package wacode.yamada.yuki.nempaymentapp.repository

import wacode.yamada.yuki.nempaymentapp.rest.service.HarvestService
import javax.inject.Inject


class HarvestRepository @Inject constructor(private val harvestService: HarvestService) {

    fun getHarvestInfo(address: String) = harvestService.getHarvestInfo(address)
}
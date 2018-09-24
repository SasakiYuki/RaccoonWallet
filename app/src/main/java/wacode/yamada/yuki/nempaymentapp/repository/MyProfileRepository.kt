package wacode.yamada.yuki.nempaymentapp.repository

import android.content.Context
import com.google.gson.Gson
import io.reactivex.Single
import wacode.yamada.yuki.nempaymentapp.NemPaymentApplication
import wacode.yamada.yuki.nempaymentapp.model.MyProfileEntity
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao
import wacode.yamada.yuki.nempaymentapp.utils.SharedPreferenceUtils

class MyProfileRepository(val context: Context) {
    private val walletInfoDao: WalletInfoDao = NemPaymentApplication.database.walletInfoDao()

    fun insertWalletInfo(entity: WalletInfo): Single<WalletInfo> {
        return Single.create { emitter ->
            entity.let {
                WalletInfo(walletInfoDao.insert(it),
                        it.walletName,
                        it.walletAddress,
                        it.isMaster).let {
                    emitter.onSuccess(it)
                }
            }
        }
    }

    fun updateWalletInfo(walletInfo: WalletInfo): Single<WalletInfo> {
        return Single.create { emitter ->
            walletInfo.let {
                walletInfoDao.update(it)
                emitter.onSuccess(it)
            }
        }
    }

    fun loadMyProfile(): Single<MyProfileEntity> {
        return Single.create { emitter ->
            val myProfileString = SharedPreferenceUtils[context, KEY_PREF_MY_PROFILE, Gson().toJson(MyProfileEntity())]
            emitter.onSuccess(Gson().fromJson(myProfileString, MyProfileEntity::class.java))
        }
    }

    fun updateMyProfile(entity: MyProfileEntity): Single<MyProfileEntity> {
        return Single.create { emitter ->
            val myProfileString = Gson().toJson(entity)
            SharedPreferenceUtils.put(context, KEY_PREF_MY_PROFILE, myProfileString)
            emitter.onSuccess(entity)
        }
    }

    companion object {
        const val KEY_PREF_MY_PROFILE = "key_pref_my_profile"
    }
}
package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao
interface WalletInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(walletInfo: WalletInfo):Long

    @Query("SELECT * FROM WalletInfo")
    fun findAll(): List<WalletInfo>

    @Query("SELECT * FROM WalletInfo WHERE id = :id")
    fun select(id: Long): WalletInfo

    @Update
    fun update(walletInfo: WalletInfo)

    @Delete
    fun delete(walletInfo: WalletInfo)
}
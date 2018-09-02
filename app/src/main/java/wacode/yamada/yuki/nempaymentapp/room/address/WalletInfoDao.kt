package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.*

@Dao
interface WalletInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(walletInfo: WalletInfo)

    @Query("SELECT * FROM WalletInfo")
    fun findAll(): List<WalletInfo>

    @Update
    fun update(walletInfo: WalletInfo)

    @Delete
    fun delete(walletInfo: WalletInfo)
}
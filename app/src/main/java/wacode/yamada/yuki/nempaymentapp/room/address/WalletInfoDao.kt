package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.*

@Dao
interface WalletInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(walletInfo: WalletInfo): Long

    @Query("SELECT * FROM WalletInfo")
    fun findAll(): List<WalletInfo>

    @Query("SELECT * FROM WalletInfo WHERE id = :id")
    fun select(id: Long): WalletInfo

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(walletInfo: WalletInfo)

    @Delete
    fun delete(walletInfo: WalletInfo)

    @Query("DELETE FROM WalletInfo WHERE id = :id")
    fun delete(id: Long)
}
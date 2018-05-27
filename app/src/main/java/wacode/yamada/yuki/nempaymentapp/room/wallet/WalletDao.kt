package wacode.yamada.yuki.nempaymentapp.room.wallet

import android.arch.persistence.room.*

@Dao
interface WalletDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(wallet: Wallet)

    @Query("SELECT * FROM Wallet")
    fun findAll(): List<Wallet>

    @Query("SELECT COUNT(*) FROM Wallet")
    fun getSize(): Int

    @Update
    fun update(wallet: Wallet)

    @Delete
    fun delete(wallet: Wallet)

    @Query("SELECT * FROM Wallet WHERE id = :id")
    fun getById(id: Long):Wallet
}
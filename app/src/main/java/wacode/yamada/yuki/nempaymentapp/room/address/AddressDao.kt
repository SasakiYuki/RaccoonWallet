package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.*
import wacode.yamada.yuki.nempaymentapp.room.address.Address

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createAddress(address: Address)

    @Query("SELECT * FROM Address")
    fun findAll(): List<Address>

    @Update
    fun updateAddress(address: Address)

    @Delete
    fun delete(address: Address)
}


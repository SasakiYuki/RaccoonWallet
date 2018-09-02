package wacode.yamada.yuki.nempaymentapp.room.address

import android.arch.persistence.room.*

@Dao
interface MyAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(myAddress: MyAddress)

    @Query("SELECT * FROM MyAddress")
    fun findAll(): List<MyAddress>

    @Update
    fun update(myAddress: MyAddress)

    @Delete
    fun delete(myAddress: MyAddress)
}

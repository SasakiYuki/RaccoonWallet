package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(friendInfo: FriendInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(friendWallet: FriendWallet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(friendIcon: FriendIcon)

    @Query("SELECT * FROM FriendInfo")
    fun findAllFriendInfo(): List<FriendInfo>
}
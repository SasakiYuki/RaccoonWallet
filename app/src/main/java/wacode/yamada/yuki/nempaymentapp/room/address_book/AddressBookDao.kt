package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single


@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(friendInfo: FriendInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(friendWallet: FriendWallet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(friendAddress: FriendAddress)

    @Query("SELECT * FROM FriendInfo")
    fun queryAllFriendInfo(): Single<List<FriendInfo>>

    @Query("SELECT * FROM FriendInfo ORDER BY id DESC LIMIT 1")
    fun queryLatestFriendInfo(): Single<FriendInfo>

    @Query("SELECT * FROM FriendInfo WHERE id = :friendId")
    fun queryFriendInfo(friendId: Long): Single<FriendInfo>

    @Query("SELECT * FROM FriendInfo WHERE name LIKE :queryName ORDER BY name")
    fun queryFriendInfoOrderByName(queryName: String): Single<List<FriendInfo>>

    @Query("SELECT * FROM FriendInfo WHERE(name LIKE :queryName) AND isTwitterAuth = :isTwitterAuth ORDER BY name")
    fun queryFriendInfoOrderByName(queryName: String, isTwitterAuth: Boolean): Single<List<FriendInfo>>
}
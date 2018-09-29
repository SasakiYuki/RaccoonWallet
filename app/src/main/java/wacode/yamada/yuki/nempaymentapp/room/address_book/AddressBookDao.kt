package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.*
import io.reactivex.Single


@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(friendInfo: FriendInfo)

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

    @Query("SELECT * FROM FriendAddress WHERE friendId = :friendId")
    fun queryFriendAddress(friendId: Long): Single<List<FriendAddress>>

    @Query("DELETE FROM FriendAddress WHERE walletInfoId = :walletInfoId")
    fun removeFriendAddress(walletInfoId: Long)

    @Query("DELETE FROM FriendAddress WHERE friendId = :friendId")
    fun removeFriendAddressByFriendId(friendId: Long)

    @Query("DELETE FROM FriendInfo WHERE id = :friendId")
    fun removeFriendInfoByFriendId(friendId: Long)
}
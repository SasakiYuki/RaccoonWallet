package wacode.yamada.yuki.nempaymentapp.room.address_book

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single


@Dao
interface AddressBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(friendInfo: FriendInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(friendWallet: FriendWallet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(friendIcon: FriendIcon)

    @Query("SELECT * FROM FriendInfo")
    fun findAllFriendInfo(): Single<List<FriendInfo>>

    @Query("SELECT * FROM FriendInfo ORDER BY id DESC LIMIT 1")
    fun getLatestFriendInfo(): Single<FriendInfo>

    @Query("SELECT * FROM FriendInfo WHERE id = :friendId")
    fun getSingleFriendInfo(friendId: Long): Single<FriendInfo>

    @Query("SELECT * FROM FriendIcon WHERE friendId = :friendId")
    fun getSingleFriendIcon(friendId: Long): Single<FriendIcon>

    @Query("SELECT * FROM FriendInfo WHERE name LIKE :word OR nameRuby LIKE :word")
    fun findPatterMatchFriendInfoByName(word: String): Single<List<FriendInfo>>

    @Query("SELECT * FROM FriendInfo WHERE(name LIKE :word OR nameRuby LIKE :word) AND isTwitterAuth = :isTwitterAuth")
    fun findFriendInfoByNameAndTwitterAuth(word: String, isTwitterAuth: Boolean): Single<List<FriendInfo>>
}
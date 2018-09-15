package wacode.yamada.yuki.nempaymentapp.room.profile

import android.arch.persistence.room.*

@Dao
interface MyProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(myProfile: MyProfile): Long

    @Query("SELECT * FROM MyProfile")
    fun findAll(): List<MyProfile>

    @Query("SELECT * FROM MyProfile WHERE id = :id")
    fun select(id: Long): MyProfile

    @Update
    fun update(myProfile: MyProfile)

    @Delete
    fun delete(myProfile: MyProfile)
}

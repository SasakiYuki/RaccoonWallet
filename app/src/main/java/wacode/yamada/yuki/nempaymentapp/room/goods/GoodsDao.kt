package wacode.yamada.yuki.nempaymentapp.room.goods

import android.arch.persistence.room.*

@Dao
interface GoodsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createGoods(goods: Goods)

    @Query("SELECT * FROM Goods")
    fun findAll() : List<Goods>

    @Delete
    fun delete(goods: Goods)
}
package wacode.yamada.yuki.nempaymentapp.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import wacode.yamada.yuki.nempaymentapp.room.address.Address
import wacode.yamada.yuki.nempaymentapp.room.address.AddressDao
import wacode.yamada.yuki.nempaymentapp.room.goods.Goods
import wacode.yamada.yuki.nempaymentapp.room.goods.GoodsDao
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.room.wallet.WalletDao

@Database(entities = arrayOf(Goods::class, Address::class, Wallet::class), version = 1)
abstract class DataBase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
    abstract fun addressDao(): AddressDao
    abstract fun walletDao(): WalletDao
}


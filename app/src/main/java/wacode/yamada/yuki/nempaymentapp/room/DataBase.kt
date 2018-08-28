package wacode.yamada.yuki.nempaymentapp.room

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import wacode.yamada.yuki.nempaymentapp.room.address.Address
import wacode.yamada.yuki.nempaymentapp.room.address.AddressDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendWallet
import wacode.yamada.yuki.nempaymentapp.room.goods.Goods
import wacode.yamada.yuki.nempaymentapp.room.goods.GoodsDao
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.room.wallet.WalletDao

@Database(entities = arrayOf(Goods::class, Address::class, Wallet::class,
        FriendInfo::class, FriendIcon::class, FriendWallet::class), version = 3)
abstract class DataBase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
    abstract fun addressDao(): AddressDao
    abstract fun walletDao(): WalletDao
    abstract fun addressBookDao(): AddressBookDao

    companion object {
        @JvmField
        val MIGRATION_1_2 = Migration1To2()
    }
}

class Migration1To2 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `FriendInfo` (`id` Long  , `lastName` TEXT, `firstName` TEXT, `lastNameRuby` TEXT, `firstNameRuby` TEXT, `phoneNumber` TEXT, `mailAddress` TEXT, `isTwitterAuth` Boolean　, PRIMARY KEY(`id`))")
        database.execSQL("CREATE TABLE `FriendIcon` (`friendId` Long, `iconPath` TEXT, PRIMARY KEY(`friendId`))")
        database.execSQL("CREATE TABLE `FriendWallet` (`id` Long,　`friendId` Long,`walletName` TEXT,`walletAddress` TEXT,`isMaster` Boolean, PRIMARY KEY(`id`))")

//        val sql1 = "CREATE TABLE `FriendInfo` (`id` Long  , `lastName` TEXT, `firstName` TEXT, `lastNameRuby` TEXT, `firstNameRuby` TEXT, `phoneNumber` TEXT, `mailAddress` TEXT, `isTwitterAuth` Boolean　, PRIMARY KEY(`id`));"
//        val sql2 = "CREATE TABLE `FriendIcon` (`friendId` Long, `iconPath` TEXT, PRIMARY KEY(`friendId`));"
//        val sql3 = "CREATE TABLE `FriendWallet` (`id` Long,　`friendId` Long,`walletName` TEXT,`walletAddress` TEXT,`isMaster` Boolean, PRIMARY KEY(`id`));"
//        database.execSQL(sql1 + sql2 + sql3)
    }
}


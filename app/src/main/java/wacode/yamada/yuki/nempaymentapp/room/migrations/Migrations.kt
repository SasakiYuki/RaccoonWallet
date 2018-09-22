package wacode.yamada.yuki.nempaymentapp.room.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

private const val FRIEND_INFO_NAME = "FriendInfo"
private const val FRIEND_ICON_NAME = "FriendIcon"
private const val FRIEND_WALLET_NAME = "FriendWallet"
private const val WALLET_INFO_NAME = "WalletInfo"
private const val MY_ADDRESS_NAME = "MyAddress"
private const val ADDRESS_NAME = "Address"
private const val GOODS_NAME = "Goods"

fun dropUnnecessaryTables(database: SupportSQLiteDatabase) {
    database.execSQL("DROP TABLE IF EXISTS $ADDRESS_NAME;")
    database.execSQL("DROP TABLE IF EXISTS $GOODS_NAME;")
}

val migration1To2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_INFO_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lastName` TEXT NOT NULL, `firstName` TEXT NOT NULL, `lastNameRuby` TEXT NOT NULL, `firstNameRuby` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `mailAddress` TEXT NOT NULL, `isTwitterAuth` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_ICON_NAME` (`friendId` INTEGER NOT NULL, `iconPath` TEXT NOT NULL, PRIMARY KEY(`friendId`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_WALLET_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `friendId` INTEGER NOT NULL, `walletName` TEXT NOT NULL, `walletAddress` TEXT NOT NULL, `isMaster` INTEGER NOT NULL)")
    }
}

val migration2To3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `$WALLET_INFO_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletName` TEXT NOT NULL, `walletAddress` TEXT NOT NULL, `isMaster` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$MY_ADDRESS_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletInfoId` INTEGER NOT NULL)")
    }
}

val migration3To4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        dropUnnecessaryTables(database)
    }
}

val migration4To5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        val tempFriendInfoName = "TempFriendInfo"
        database.execSQL("ALTER TABLE `$FRIEND_INFO_NAME` RENAME TO `$tempFriendInfoName`")
        database.execSQL("DROP TABLE IF EXISTS `$FRIEND_INFO_NAME`")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_INFO_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `nameRuby` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `mailAddress` TEXT NOT NULL, `isTwitterAuth` INTEGER NOT NULL)")
        database.execSQL("INSERT INTO FriendInfo(`id`, `name`, `nameRuby`, `phoneNumber`, `mailAddress`, `isTwitterAuth`) SELECT `id`, `lastName`||`firstName`, `lastNameRuby`||`firstNameRuby`, `phoneNumber`, `mailAddress`, `isTwitterAuth` FROM `$tempFriendInfoName`")
        database.execSQL("DROP TABLE IF EXISTS `$tempFriendInfoName`")
    }
}

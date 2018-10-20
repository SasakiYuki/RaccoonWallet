package wacode.yamada.yuki.nempaymentapp.room.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

private const val FRIEND_INFO_NAME = "FriendInfo"
private const val WALLET_INFO_NAME = "WalletInfo"
private const val MY_ADDRESS_NAME = "MyAddress"
private const val ADDRESS_NAME = "Address"
private const val GOODS_NAME = "Goods"
private const val FRIEND_ADDRESS_NAME = "FriendAddress"
private const val WALLET_NAME = "Wallet"


val migration1To2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS $ADDRESS_NAME;")
        database.execSQL("DROP TABLE IF EXISTS $GOODS_NAME;")

        database.execSQL("CREATE TABLE IF NOT EXISTS `$WALLET_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `salt` BLOB NOT NULL, `name` TEXT NOT NULL, `publicKey` TEXT NOT NULL, `encryptedSecretKey` BLOB NOT NULL, `address` TEXT NOT NULL, `isMultisig` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_INFO_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `nameRuby` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `mailAddress` TEXT NOT NULL, `isTwitterAuth` INTEGER NOT NULL, `iconPath` TEXT NOT NULL, `sendCount` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$WALLET_INFO_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletName` TEXT NOT NULL, `walletAddress` TEXT NOT NULL, `isMaster` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$MY_ADDRESS_NAME` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletInfoId` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `$FRIEND_ADDRESS_NAME` (`walletInfoId` INTEGER NOT NULL, `friendId` INTEGER NOT NULL, PRIMARY KEY(`walletInfoId`))")
    }
}
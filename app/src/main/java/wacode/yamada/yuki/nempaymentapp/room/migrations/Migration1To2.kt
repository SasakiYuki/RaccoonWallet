package wacode.yamada.yuki.nempaymentapp.room.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

class Migration1To2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `FriendInfo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `lastName` TEXT NOT NULL, `firstName` TEXT NOT NULL, `lastNameRuby` TEXT NOT NULL, `firstNameRuby` TEXT NOT NULL, `phoneNumber` TEXT NOT NULL, `mailAddress` TEXT NOT NULL, `isTwitterAuth` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `FriendIcon` (`friendId` INTEGER NOT NULL, `iconPath` TEXT NOT NULL, PRIMARY KEY(`friendId`))")
        database.execSQL("CREATE TABLE IF NOT EXISTS `FriendWallet` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `friendId` INTEGER NOT NULL, `walletName` TEXT NOT NULL, `walletAddress` TEXT NOT NULL, `isMaster` INTEGER NOT NULL)")
    }
}
package wacode.yamada.yuki.nempaymentapp.room.migrations

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration

/**
 * add:WalletInfo
 * add:MyAddress
 */
class Migration2To3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE IF NOT EXISTS `WalletInfo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletName` TEXT NOT NULL, `walletAddress` TEXT NOT NULL, `isMaster` INTEGER NOT NULL)")
        database.execSQL("CREATE TABLE IF NOT EXISTS `MyAddress` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `walletInfoId` INTEGER NOT NULL)")
    }
}

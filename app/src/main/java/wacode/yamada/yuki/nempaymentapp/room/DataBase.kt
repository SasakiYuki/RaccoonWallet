package wacode.yamada.yuki.nempaymentapp.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddress
import wacode.yamada.yuki.nempaymentapp.room.address.MyAddressDao
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfo
import wacode.yamada.yuki.nempaymentapp.room.address.WalletInfoDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendAddress
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.room.wallet.WalletDao

@Database(entities = arrayOf(Wallet::class,
        FriendInfo::class,
        WalletInfo::class,
        MyAddress::class,
        FriendAddress::class), version = 2)
abstract class DataBase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
    abstract fun addressBookDao(): AddressBookDao
    abstract fun walletInfoDao(): WalletInfoDao
    abstract fun myAddressDao(): MyAddressDao
}
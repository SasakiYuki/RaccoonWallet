package wacode.yamada.yuki.nempaymentapp.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import wacode.yamada.yuki.nempaymentapp.room.address.*
import wacode.yamada.yuki.nempaymentapp.room.address_book.AddressBookDao
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendIcon
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendInfo
import wacode.yamada.yuki.nempaymentapp.room.address_book.FriendWallet
import wacode.yamada.yuki.nempaymentapp.room.goods.Goods
import wacode.yamada.yuki.nempaymentapp.room.goods.GoodsDao
import wacode.yamada.yuki.nempaymentapp.room.wallet.Wallet
import wacode.yamada.yuki.nempaymentapp.room.wallet.WalletDao

@Database(entities = arrayOf(Goods::class, Address::class, Wallet::class,
        FriendInfo::class, FriendIcon::class, FriendWallet::class, WalletInfo::class, MyAddress::class), version = 3)
abstract class DataBase : RoomDatabase() {
    abstract fun goodsDao(): GoodsDao
    abstract fun addressDao(): AddressDao
    abstract fun walletDao(): WalletDao
    abstract fun addressBookDao(): AddressBookDao
    abstract fun walletInfoDao(): WalletInfoDao
    abstract fun myAddressDao(): MyAddressDao
}


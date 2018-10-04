# RaccoonWallet

<img src="https://raccoonwallet.com/wp-content/uploads/2018/06/RaccoonWallet_Githubimage.jpg" alt="RaccoonWallet " title="RaccoonWallet ">

- [HomePage](https://raccoonwallet.com/)
- [Medium](https://medium.com/raccoonwallet)

# RaccoonWalletDeeplink

RaccoonWalletはなんちゃってDeeplinkが扱えます。
URLは二種類存在します

## 種類

- DynamicLink
- 通常の起動リンク

### DynamicLink

DynamicLinkはFirebaseの機能です。
こちらのURLは普通の起動リンクとは違います。

Raccoonがすでにインストールされている場合は期待通りの動きを、されていない場合はグーグルプレイストアに。
Android端末でなかったりする場合はWebPage https://raccoonwallet.com/ に飛びます。

こちらを推奨したいところですが少々めんどくさいです。
以下に例を記載します。

ex
対象アドレス:NCMKWNFWUILEVCKBSON2MS65BXU4NJ2GBJTIJBTK
送付XEM:1XEM
メッセージ:hello,RaccoonDeeplink
自分の名前:yuki

こちらをURLに起こします

```
https://raccoonwallet.com/payment?addr=NCMKWNFWUILEVCKBSON2MS65BXU4NJ2GBJTIJBTK&amount=10&msg=hello,RaccoonDeeplink&name=yuki,yamada&apn=wacode.yamada.yuki.nempaymentapp
```

クエリにAPNを追加するのを忘れないでください。
`&apn=wacode.yamada.yuki.nempaymentapp`

でこちらにDynamiclinkの起動リンクのクエリにパーセントコーディングして追加します。

`https://raccoonwallet.page.link/?link=`

```
https://raccoonwallet.page.link/?link=https%3a%2f%2fraccoonwallet%2ecom%2fpayment%3faddr%3dNCMKWNFWUILEVCKBSON2MS65BXU4NJ2GBJTIJBTK%26amount%3d1000000%26msg%3dhello%2cRaccoonDeeplink%26name%3dyuki%2cyamada&apn=wacode.yamada.yuki.nempaymentapp
```

## 通常の起動リンク
通常の起動リンクは特にアプリが入ってなかったときが考慮されていない起動リンクです。
その代わり直観的にURLを生成することができます。

ex
対象アドレス:NCMKWNFWUILEVCKBSON2MS65BXU4NJ2GBJTIJBTK
送付XEM:1XEM
メッセージ:若造

```
https://raccoonwallet.com/payment?addr=NCMKWNFWUILEVCKBSON2MS65BXU4NJ2GBJTIJBTK&amount=1000000&msg=若造
```

`https://raccoonwallet.com/payment?` の後にクエリ形式で
`addr`,`msg`,`amount`,`name` を追加していってください。

package wacode.yamada.yuki.nempaymentapp.flux

interface Action<out T> {
    val type: String
    val data: T
}
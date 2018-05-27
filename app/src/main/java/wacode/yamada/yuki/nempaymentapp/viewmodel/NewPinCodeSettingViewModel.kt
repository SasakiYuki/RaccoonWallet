package wacode.yamada.yuki.nempaymentapp.viewmodel

import io.reactivex.subjects.PublishSubject

class NewPinCodeSettingViewModel {
    var stringBuilder = StringBuilder()
    var cachePinCode: ByteArray = byteArrayOf()
    var eventPublisher: PublishSubject<Pair<Event, ByteArray>> = PublishSubject.create()

    fun addPinCode(value: Int) {
        stringBuilder.append(value)
    }

    fun isMaxLength() = stringBuilder.length == PIN_MAX_LENGTH

    fun resetStringBuilder() {
        stringBuilder = StringBuilder()
    }

    fun getPinLength() = stringBuilder.length

    fun correctPinCode(): Boolean {
        return cachePinCode.toString(Charsets.UTF_8) == String(stringBuilder)
    }

    fun sendEvent(event: Event, data: ByteArray = byteArrayOf()) {
        when (event) {
            Event.CONFIRM -> {
                cachePinCode = String(stringBuilder).toByteArray(Charsets.UTF_8)
                resetStringBuilder()
            }
            Event.SUCCESS -> clearCache()
        }

        eventPublisher.onNext(Pair(event, data))
    }

    private fun clearCache() {
        resetStringBuilder()
        cachePinCode = byteArrayOf()
    }

    companion object {
        private const val PIN_MAX_LENGTH = 6
    }

    enum class Event {
        CONFIRM,
        SUCCESS
    }
}
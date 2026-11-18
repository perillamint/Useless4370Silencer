package com.example.useless4370silencer.hook

/**
 * Matches the cell broadcast that should be silenced.
 *
 * [message] is a runtime `android.telephony.SmsCbMessage`. The module intentionally does not
 * compile against that system API, so all fields are read through reflection.
 */
internal object CellBroadcastFilter {
    /** Republic of Korea MCC. */
    const val TARGET_MCC = "450"

    /** GSM/UMTS message identifier. */
    const val TARGET_MESSAGE_ID = 4370

    /**
     * @return `true` when [message] has the target message identifier and was received from
     * a network whose PLMN starts with [TARGET_MCC].
     */
    fun matches(message: Any?): Boolean {
        if (message == null) return false
        val messageId = message.invokeNoArg("getServiceCategory") as? Number ?: return false
        if (messageId.toInt() != TARGET_MESSAGE_ID) return false
        val location = message.invokeNoArg("getLocation") ?: return false
        val plmn = location.invokeNoArg("getPlmn") as? String ?: return false
        return plmn.take(3) == TARGET_MCC
    }

    private fun Any.invokeNoArg(name: String): Any? = runCatching {
        javaClass.methods
            .firstOrNull { it.name == name && it.parameterTypes.isEmpty() }
            ?.apply { isAccessible = true }
            ?.invoke(this)
    }.getOrNull()
}

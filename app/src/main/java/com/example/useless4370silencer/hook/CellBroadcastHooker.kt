package com.example.useless4370silencer.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.method
import com.highcapable.yukihookapi.hook.factory.toClassOrNull
import com.highcapable.yukihookapi.hook.log.YLog

/**
 * Hooks the AOSP CellBroadcastService duplicate filter.
 *
 * Returning `true` from `CellBroadcastHandler.isDuplicate(SmsCbMessage)` reuses the normal
 * duplicate-drop path, so the module does not have to cut off `handleBroadcastSms()` directly
 * or leave the broadcast state machine waiting for a completion event.
 */
@Suppress("DEPRECATION")
internal class CellBroadcastHooker : YukiBaseHooker() {
    override fun onHook() {
        val handlerClass =
            "com.android.cellbroadcastservice.CellBroadcastHandler".toClassOrNull()
                ?: return YLog.warn("CellBroadcastHandler was not found on this device")

        handlerClass.method {
            name = "isDuplicate"
            paramCount(1)
        }.hook {
            before {
                val message = args.firstOrNull() ?: return@before
                if (CellBroadcastFilter.matches(message)) {
                    YLog.debug("Ignoring cell broadcast id=${CellBroadcastFilter.TARGET_MESSAGE_ID}")
                    resultTrue()
                }
            }
        }
    }
}

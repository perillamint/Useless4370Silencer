package com.example.useless4370silencer.hook

import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.example.useless4370silencer.BuildConfig

@InjectYukiHookWithXposed
object HookEntry : IYukiHookXposedInit {
    override fun onInit() {
        YukiHookAPI.configs {
            debugLog {
                tag = "Useless4370Silencer"
                isEnable = true
            }
            isDebug = BuildConfig.DEBUG
        }
    }

    override fun onHook() {
        YukiHookAPI.encase {
            loadApp(name = "com.android.cellbroadcastservice") {
                loadHooker(CellBroadcastHooker())
            }
        }
    }
}

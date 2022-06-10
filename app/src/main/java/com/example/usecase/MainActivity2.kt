package com.example.usecase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sourcepoint.cmplibrary.NativeMessageController
import com.sourcepoint.cmplibrary.SpClient
import com.sourcepoint.cmplibrary.core.nativemessage.MessageStructure
import com.sourcepoint.cmplibrary.creation.config
import com.sourcepoint.cmplibrary.creation.delegate.spConsentLibLazy
import com.sourcepoint.cmplibrary.data.network.util.CampaignsEnv
import com.sourcepoint.cmplibrary.exception.CampaignType
import com.sourcepoint.cmplibrary.model.ConsentAction
import com.sourcepoint.cmplibrary.model.MessageLanguage
import com.sourcepoint.cmplibrary.model.exposed.SPConsents
import com.sourcepoint.cmplibrary.model.exposed.SpConfig
import org.json.JSONObject

class MainActivity2 : AppCompatActivity() {

    val cmpConfig : SpConfig = config {
        accountId = 22
        propertyName = "mobile.multicampaign.demo"
        messLanguage = MessageLanguage.ENGLISH // Optional, default ENGLISH
        campaignsEnv = CampaignsEnv.PUBLIC // Optional, default PUBLIC
        messageTimeout = 4000 // Optional, default 3000ms
        +CampaignType.CCPA
        +CampaignType.GDPR
    }

    private val spConsentLib by spConsentLibLazy {
        activity = this@MainActivity2
        spClient = LocalClient()
        spConfig = cmpConfig
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        spConsentLib.loadMessage()
    }

    internal inner class LocalClient : SpClient {
        override fun onUIFinished(view: View) {
            spConsentLib.removeView(view)
        }
        override fun onUIReady(view: View) {
            spConsentLib.showView(view)
        }
        override fun onMessageReady(message: JSONObject) {} // Deprecated
        override fun onNativeMessageReady(message: MessageStructure, messageController: NativeMessageController) { }
        override fun onError(error: Throwable) { }
        override fun onConsentReady(consent: SPConsents) { }
        override fun onAction(view: View, consentAction: ConsentAction): ConsentAction = consentAction
        override fun onNoIntentActivitiesFound(url: String) {}
        override fun onSpFinished(sPConsents: SPConsents) { }
    }
}
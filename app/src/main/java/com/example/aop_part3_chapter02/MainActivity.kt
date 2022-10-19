package com.example.aop_part3_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {
    val viewPager: ViewPager2 by lazy {
        findViewById(R.id.view_pager)
    }

    val progressBar: ProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main)

    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val quotesJson = remoteConfig.getString("quotes")
                val quoteList = parseJsonString(quotesJson)
                attachPagerAdapter(quoteList)
            }
        }
    }

    private fun parseJsonString(jsonString: String):List<Quote> {
        val jsonArray = JSONArray(jsonString)
        var jsonList : List<JSONObject> = emptyList()
        for (index in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList = jsonList + it
            }
        }
        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }

    private fun attachPagerAdapter(quotes: List<Quote>) {
        val adapter = QuotesPagerAdapter(quotes)
        viewPager.adapter = adapter
    }

}
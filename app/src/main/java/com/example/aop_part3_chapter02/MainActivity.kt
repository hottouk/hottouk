package com.example.aop_part3_chapter02

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.view_pager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById(R.id.progress_bar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initViews()
    }

    private fun initViews() {
        viewPager.setPageTransformer { page, position ->
            when {
                //절대값 -1 = 1/
                position.absoluteValue >= 1.0f -> {
                    page.alpha - 0f
                }
                position == 0f -> {
                    page.alpha = 1f
                }
                else -> {
                    page.alpha - 2 * position.absoluteValue
                }
            }

        }
    }

    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
                //앱을 작동할 때 마다 패치가 진행된다. 간격이 0임
            }
        )
        //setDefaultAsync도 현업에선 필수적으로 설정해줘야 한다. 여기선 건너뛴다.
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                progressBar.visibility = View.GONE
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                Log.d("quo","JsonString : ${remoteConfig.getString("quotes")}")
                val isNameRevealed = remoteConfig.getBoolean("is_name_revealed")
                displayQuotesPager(quotes, isNameRevealed)

            }
        } //서버 통신이기 때문에 비동기다. 그래서 리스너를 따로 등록해야 한다. 이 리스너는 remoteConfig가 설정이
        //완료된 시점을 받는다.
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameRevealed: Boolean) {

        val adapter = QuotesPagerAdapter(
            quotes = quotes,
            isNameRevealed = isNameRevealed
        )
        viewPager.adapter = adapter
        viewPager.setCurrentItem(adapter.itemCount/2, false)
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        //jsonArray: jsonString 값을 가져와 Array로 반환한다.
        val jsonArray = JSONArray(json)
        //Array를 List로 변환시켜준다.
        Log.d("quo","JsonArray: $jsonArray")
        var jsonList = emptyList<JSONObject>()
        for (index in 0 until jsonArray.length()) {
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

}
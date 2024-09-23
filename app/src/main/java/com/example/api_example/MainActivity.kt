package tech.crackle.financecalculator

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.api_example.BasicAPIClient
import com.example.api_example.HttpHeaders
import com.example.api_example.HttpQueryParam
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.crackle.core_sdk.AdSize
import tech.crackle.core_sdk.AdsError
import tech.crackle.core_sdk.CrackleAd
import tech.crackle.core_sdk.CrackleReward
import tech.crackle.core_sdk.CrackleSdk
import tech.crackle.core_sdk.ads.CrackleAdLoader
import tech.crackle.core_sdk.ads.CrackleAdView
import tech.crackle.core_sdk.ads.CrackleInterstitialAd
import tech.crackle.core_sdk.ads.CrackleRewardedAd
import tech.crackle.core_sdk.ads.nativeads.CrackleNativeAdView
import tech.crackle.core_sdk.ads.nativeads.CrackleNativeAdViewBinder
import tech.crackle.core_sdk.listener.CrackleAdListener
import tech.crackle.core_sdk.listener.CrackleAdViewAdListener
import tech.crackle.core_sdk.listener.CrackleUserRewardListener
import tech.crackle.financecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var crackleAdLoader: CrackleAdLoader

//    private val BASE_URL = "https://api.restful-api.dev/objects"
    private val BASE_URL = "https://crackle.co.in/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CrackleSdk.initialize(this@MainActivity) {}


        val apiClient = BasicAPIClient.getInstance(BASE_URL)
        val headers = HttpHeaders().getHeaders()
        val queryParam = HttpQueryParam().getParams()


//        loadUnifiedAd()
//
//        binding.btn.setOnClickListener {
//            crackleAdLoader.destroy()
//            loadUnifiedAd()
//        }

//        loadNativeAd()
//
//        binding.btn.setOnClickListener {
//            crackleAdLoader.destroy()
//            loadNativeAd()
//        }

//        binding.btnInt.setOnClickListener{
//
//            val adView = CrackleRewardedInterstitialAd
//            adView.loadAd(this@MainActivity)
//
//            adView.setListener(object : CrackleAdListener{
//                override fun onAdClicked() {}
//
//                override fun onAdDismissed() {}
//
//                override fun onAdDisplayed() {}
//
//                override fun onAdFailedToLoad(adsError: AdsError) {}
//
//                override fun onAdFailedToShow(adsError: AdsError) {}
//
//                override fun onAdLoaded() {
//                    if (adView.isReady()) {
//                        adView.showAd(this@MainActivity, object : CrackleUserRewardListener {
//                            override fun onUserRewarded(crackleReward: CrackleReward) {
//                                // reward user for watching video
//                                Log.d("Manan", "reward granted.")
//                            }
//                        })
//                    }
//                }
//            })
//        }

        binding.btnRwd.setOnClickListener {

            val adView = CrackleRewardedAd
            adView.loadAd(this@MainActivity)

            adView.setListener(object : CrackleAdListener {
                override fun onAdClicked() {}

                override fun onAdDismissed() {}

                override fun onAdDisplayed() {}

                override fun onAdFailedToLoad(adsError: AdsError) {
                    Log.d("Manan", "MainActivity rewarded ad failed to load. = $adsError")
                }

                override fun onAdFailedToShow(adsError: AdsError) {
                    Log.d("Manan", "MainActivity rewarded ad failed to show. = $adsError")
                }

                override fun onAdLoaded() {
                    Log.d("Manan", "MainActivity rewarded ad loaded.")
                    if (adView.isReady()) {
                        adView.showAd(this@MainActivity, object : CrackleUserRewardListener {
                            override fun onUserRewarded(crackleReward: CrackleReward) {
                                // reward user for watching video
                                Log.d("Manan", "MainActivity reward granted.")
                            }
                        })
                    }
                }
            })
        }

        binding.btnBanner.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val getData =
                    apiClient.get("user-module/v2/getAppIdAdUnitsMap", headers, queryParam)
                Log.d("API_TAG", "API calling!")
                Log.d("API_TAG", getData.toString())
            }

//            val adView = CrackleAdView(this)
//            adView.setListener(object : CrackleAdViewAdListener {
//                override fun onAdLoaded() {
//                    Log.d("Manan:", "MainActivity banner onAdLoaded")
//                    binding.adView.removeAllViews()
//                    binding.adView.addView(adView.getView())
//                }
//
//                override fun onAdFailedToLoad(adsError: AdsError) {
//                    Log.d("Manan:", "MainActivity banner onAdFailedToLoad $adsError")
//                }
//            })
//            adView.setAdSizes(AdSize.BANNER)
//            adView.loadAd()
        }


        binding.btnInt.setOnClickListener {
            val adView = CrackleInterstitialAd
            adView.loadAd(this)
            adView.setListener(object : CrackleAdListener {
                override fun onAdClicked() {}

                override fun onAdDismissed() {}

                override fun onAdDisplayed() {}

                override fun onAdFailedToLoad(adsError: AdsError) {
                    Log.d("Manan:", "MainActivity interstitial onAdFailedToLoad $adsError")
                }

                override fun onAdFailedToShow(adsError: AdsError) {
                    Log.d("Manan:", "MainActivity interstitial onAdFailedToShow $adsError")
                }

                override fun onAdLoaded() {
                    Log.d("Manan:", "MainActivity interstitial onAdLoaded")
//                    binding.adView.removeAllViews()
//                    binding.adView.addView(adView.getView())
                    if (adView.isReady()) {
                        adView.showAd(this@MainActivity)
                    }
                }
            })
        }

    }


    private fun loadNativeAd() {
        // CrackleAdLoader.Builder(requireContext()) method can be utilized when the default ad unit ID is intended to be used
        crackleAdLoader = CrackleAdLoader.Builder(this@MainActivity)
            .forNativeAd { crackleNativeAd ->
                val binder = CrackleNativeAdViewBinder.Builder(R.layout.native_medium_layout)
                    .setHeadlineTextViewId(R.id.headline_txt)
                    .setBodyTextViewId(R.id.body_txt)
                    .setAdvertiserTextViewId(R.id.secondary_txt)
                    .setIconImageViewId(R.id.ad_icon)
                    .setMediaContentViewGroupId(R.id.ad_media)
                    .setStarRatingContentViewGroupId(R.id.rating_bar_frame)
                    .setOptionsContentViewGroupId(R.id.options_view)
                    .setCallToActionButtonId(R.id.cta)
                    .build()
                binding.adView.removeAllViews()
                binding.adView.addView(
                    CrackleNativeAdView(
                        binder,
                        crackleNativeAd,
                        this@MainActivity
                    )
                )
            }
            .withCrackleListener(object : CrackleAdViewAdListener {
                override fun onAdLoaded() {
                    // Handle ad loaded event
                    Log.d("Manan", "MainActivity native onAdLoaded.")
                }

                override fun onAdFailedToLoad(adsError: AdsError) {
                    Log.d("Manan", "MainActivity native onAdFailedToLoad $adsError.")
                }

            })
            .build()
        crackleAdLoader.loadAd()
    }

    private fun loadUnifiedAd() {
        // CrackleAdLoader.Builder(requireContext()) method can be utilized when the default ad unit ID is intended to be used
        crackleAdLoader = CrackleAdLoader.Builder(this@MainActivity)
            .forNativeAd { crackleNativeAd ->
                val binder = CrackleNativeAdViewBinder.Builder(R.layout.native_medium_layout)
                    .setHeadlineTextViewId(R.id.headline_txt)
                    .setBodyTextViewId(R.id.body_txt)
                    .setAdvertiserTextViewId(R.id.secondary_txt)
                    .setIconImageViewId(R.id.ad_icon)
                    .setMediaContentViewGroupId(R.id.ad_media)
                    .setStarRatingContentViewGroupId(R.id.rating_bar_frame)
                    .setOptionsContentViewGroupId(R.id.options_view)
                    .setCallToActionButtonId(R.id.cta)
                    .build()
                binding.adView.removeAllViews()
                binding.adView.addView(CrackleNativeAdView(binder, crackleNativeAd, this))
            }
            .forCrackleAdView({ adView ->
                binding.adView.removeAllViews()
                binding.adView.addView(adView.getView())
            }, AdSize.BANNER, AdSize.RECTANGLE)
            .withCrackleListener(object : CrackleAdViewAdListener {
                override fun onAdLoaded() {
                    Log.d("Manan", "MainActivity unified onAdLoaded.")
                }

                override fun onAdFailedToLoad(adsError: AdsError) {
                    Log.d("Manan", "MainActivity unified onAdFailedToLoad $adsError.")
                }

            })
            .build()
        crackleAdLoader.loadAd()
    }

}
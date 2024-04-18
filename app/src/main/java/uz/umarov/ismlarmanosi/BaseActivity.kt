package uz.umarov.ismlarmanosi

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import uz.umarov.ismlarmanosi.databinding.AboutDialogBinding
import uz.umarov.ismlarmanosi.databinding.ActivityBaseBinding
import uz.umarov.ismlarmanosi.vm.BaseViewModel

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private val viewModel: BaseViewModel by viewModels()

    private var mInterstitialAd: InterstitialAd? = null
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        showBannerAd()
        setupUI()
        setupListeners()
        observeViewModel()
    }

    private fun showBannerAd() {
        MobileAds.initialize(this)
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        InterstitialAd.load(this,
            "ca-app-pub-2989575196315667/9858817680",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })


    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun setupUI() {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#ecf0f3")
    }

    private fun setupListeners() {
        binding.cardBoys.setOnClickListener {
            showInterstitialAd()
            navigateToNamesActivity("m")
        }
        binding.cardGirls.setOnClickListener {
            showInterstitialAd()
            navigateToNamesActivity("f")
        }
        binding.cardSearch.setOnClickListener {
            showInterstitialAd()
            navigateToNamesActivity("a")
        }
        binding.cardLiked.setOnClickListener {
            showInterstitialAd()
            navigateToNamesActivity("like")
        }
        binding.cardAbout.setOnClickListener {
            showAboutDialog()
        }
        binding.cardTranslate.setOnClickListener {
            startActivity(Intent(this, SwitchLanguageActivity::class.java))
        }
    }

    private fun observeViewModel() {
        viewModel.boysCount.observe(this) { count ->
            binding.tvCountBoys.text = count.toString()
        }
        viewModel.girlsCount.observe(this) { count ->
            binding.tvCountGirls.text = count.toString()
        }
    }

    private fun navigateToNamesActivity(gender: String) {
        val intent = Intent(this, NamesActivity::class.java).apply {
            putExtra("gender", gender)
        }
        startActivity(intent)
    }

    private fun showAboutDialog() {
        val alertDialog = AlertDialog.Builder(this).create()
        val dialogView = AboutDialogBinding.inflate(layoutInflater, binding.root, false)
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.setView(dialogView.root)
        alertDialog.show()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}

package uz.umarov.ismlarmanosi

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import uz.umarov.ismlarmanosi.databinding.ActivityMainBinding
import uz.umarov.ismlarmanosi.utils.NamesObject
import uz.umarov.ismlarmanosi.vm.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        setupUI()
        animateTextAndLoadData()
    }

    private fun setupUI() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.parseColor("#ecf0f3")
        }
        supportActionBar?.hide()
    }

    private fun animateTextAndLoadData() {
        val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.text.startAnimation(scaleAnimation)
        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) { }

            override fun onAnimationEnd(p0: Animation?) {
                // Determine the language code based on your app's settings
                val languageCode = determineLanguageCode()
                viewModel.loadNames(applicationContext, languageCode).observe(this@MainActivity) { properties ->
                    if (properties.isNotEmpty()) {
                        NamesObject.properties = properties
                        startActivity(Intent(this@MainActivity, BaseActivity::class.java))
                    }
                }
            }

            override fun onAnimationRepeat(p0: Animation?) { }
        })
    }

    private fun determineLanguageCode(): String {
        // Retrieve the selected language from SharedPreferences
        val sharedPreferences = getSharedPreferences("language_pref", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", "uz") ?: "uz"
    }

}

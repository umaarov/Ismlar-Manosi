package uz.umarov.ismlarmanosi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import uz.umarov.ismlarmanosi.databinding.ActivitySwitchLanguageBinding
import java.util.Locale

class SwitchLanguageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySwitchLanguageBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySwitchLanguageBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("language_pref", Context.MODE_PRIVATE)

        val selectedLanguage = sharedPreferences.getString("language", "uz")

        if (selectedLanguage == "en") {
            binding.englishCard.strokeColor = Color.parseColor("#037CCD")
            binding.uzbekCard.strokeColor = Color.parseColor("#FFFFFFFF")
        } else {
            binding.uzbekCard.strokeColor = Color.parseColor("#037CCD")
            binding.englishCard.strokeColor = Color.parseColor("#FFFFFFFF")
        }

        binding.englishLayout.setOnClickListener {
            binding.englishCard.strokeColor = Color.parseColor("#037CCD")
            binding.uzbekCard.strokeColor = Color.parseColor("#FFFFFFFF")
        }

        binding.uzbekLayout.setOnClickListener {
            binding.uzbekCard.strokeColor = Color.parseColor("#037CCD")
            binding.englishCard.strokeColor = Color.parseColor("#FFFFFFFF")
        }

        binding.saveButton.setOnClickListener {
            val selectedLanguage =
                if (binding.englishCard.strokeColor == Color.parseColor("#037CCD")) {
                    "en"
                } else {
                    "uz"
                }
            saveLanguagePreference(selectedLanguage)
            switchLanguage(selectedLanguage)
            restartApp()
        }

        binding.backLayout.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun restartApp() {
        val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun switchLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val configuration = Configuration()
        configuration.locale = locale

        resources.updateConfiguration(configuration, resources.displayMetrics)

        recreate()
    }

    private fun saveLanguagePreference(languageCode: String) {
        sharedPreferences.edit().putString("language", languageCode).apply()
    }
}

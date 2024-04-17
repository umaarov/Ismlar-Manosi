package uz.umarov.ismlarmanosi

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.umarov.ismlarmanosi.databinding.AboutDialogBinding
import uz.umarov.ismlarmanosi.databinding.ActivityBaseBinding
import uz.umarov.ismlarmanosi.utils.NamesObject

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        countNames()

        binding.cardBoys.setOnClickListener {
            val intent = Intent(this, NamesActivity::class.java)
            intent.putExtra("gender", "m")
            startActivity(intent)
        }
        binding.cardGirls.setOnClickListener {
            val intent = Intent(this, NamesActivity::class.java)
            intent.putExtra("gender", "f")
            startActivity(intent)
        }
        binding.cardSearch.setOnClickListener {
            val intent = Intent(this, NamesActivity::class.java)
            intent.putExtra("gender", "a")
            startActivity(intent)
        }
        binding.cardLiked.setOnClickListener {
            val intent = Intent(this, NamesActivity::class.java)
            intent.putExtra("gender", "like")
            startActivity(intent)
        }
        binding.cardAbout.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this).create()
            val dialogView = AboutDialogBinding.inflate(layoutInflater, binding.root, false)
            alertDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            alertDialog.setView(dialogView.root)
            alertDialog.show()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun countNames() {
        val boysCount = NamesObject.boys().size
        val girlsCount = NamesObject.girls().size
        binding.tvCountBoys.text = boysCount.toString()
        binding.tvCountGirls.text = girlsCount.toString()
    }
}
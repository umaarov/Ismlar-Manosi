package uz.umarov.ismlarmanosi

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.umarov.ismlarmanosi.databinding.ActivityMainBinding
import uz.umarov.ismlarmanosi.models.Names
import uz.umarov.ismlarmanosi.models.NamesItem
import uz.umarov.ismlarmanosi.models.Properties
import uz.umarov.ismlarmanosi.utils.NamesObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val TAG = "MainActivity"
    lateinit var namesItem: ArrayList<NamesItem>
    lateinit var ismlarList: ArrayList<Properties>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale)
        binding.text.startAnimation(scaleAnimation)

        scaleAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                loading()
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })


    }

    fun loading() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "names.json")!!
        //Log.i("data", jsonFileString)
        val gson = Gson()
        val lisType = object : TypeToken<Names>() {}.type
        val namesList: Names = gson.fromJson(jsonFileString, lisType)
        namesItem = ArrayList()
        namesList.forEach { it ->
            namesItem.add(it)
        }

        var properties = ArrayList<Properties>()

        namesItem.forEach { it ->
            properties.add(it.properties)
        }
        NamesObject.properties = ArrayList()
        NamesObject.properties.addAll(properties)
        startActivity(Intent(this@MainActivity, BaseActivity::class.java))
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
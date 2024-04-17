package uz.umarov.ismlarmanosi

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.umarov.ismlarmanosi.adapters.RvAdapter
import uz.umarov.ismlarmanosi.caching.MySharedPreferences
import uz.umarov.ismlarmanosi.databinding.ActivityNamesBinding
import uz.umarov.ismlarmanosi.databinding.BottomSheetDialogBinding
import uz.umarov.ismlarmanosi.models.Properties
import uz.umarov.ismlarmanosi.service.OnCLick
import uz.umarov.ismlarmanosi.utils.NamesObject
import java.util.Locale

class NamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNamesBinding
    private lateinit var list: ArrayList<Properties>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNamesBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#ecf0f3")

        supportActionBar?.hide()
        val type = intent.getStringExtra("gender")

        list = ArrayList()
        when (type) {
            "m" -> {
                list.addAll(NamesObject.boys())
            }

            "f" -> {
                list.addAll(NamesObject.girls())
            }

            "a" -> {
                list.addAll(NamesObject.properties)
            }

            "like" -> {
                MySharedPreferences.init(this)
                val listLike = MySharedPreferences.objectString
                list.addAll(listLike)
            }

        }
        val adapter = RvAdapter(list, object : OnCLick {
            override fun click(properties: Properties, position: Int) {
                showBottomSheet(properties, position)
            }
        })
        binding.rv.adapter = adapter
        binding.editText.addTextChangedListener {
            val listSearch = ArrayList<Properties>()
            for (properties in list) {
                for (i in 0 until properties.name.length + 1) {
                    if (properties.name.subSequence(0, i).toString()
                            .lowercase(Locale.getDefault()) == binding.editText.text.toString()
                            .lowercase(Locale.getDefault())
                    ) {
                        listSearch.add(properties)
                    }
                }
            }
            val adapter = RvAdapter(listSearch, object : OnCLick {
                override fun click(properties: Properties, position: Int) {
                    showBottomSheet(properties, position)
                }

            })
            binding.rv.adapter = adapter
        }


        binding.tvClear.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                onBackPressed()
            } else {
                binding.editText.text.clear()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun showBottomSheet(properties: Properties, position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
        val item = BottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(item.root)
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        item.tvName.text = properties.name
        item.tvTarif.text = "Kelib chiqishi: ${properties.origin}\nMa'nosi: ${properties.meaning}"



        if (!savedOrNot(properties)) {
            item.likeImage.setImageResource(R.drawable.ic_unlike)
        } else {
            item.likeImage.setImageResource(R.drawable.ic_like)
        }
        savedOrNot(properties)

        item.cardLike.setOnClickListener {
            MySharedPreferences.init(this)
            val list1 = MySharedPreferences.objectString
            if (!savedOrNot(properties)) {
                list1.add(properties)
                MySharedPreferences.objectString = list1
                item.likeImage.setImageResource(R.drawable.ic_like)
            } else {
                list1.remove(properties)
                MySharedPreferences.objectString = list1
                item.likeImage.setImageResource(R.drawable.ic_unlike)
            }
        }
        item.cardShare.setOnClickListener {
            shareText(properties.origin, properties.meaning)
        }
        item.cardCopy.setOnClickListener {
            copyName(properties.origin, properties.meaning)
        }


        bottomSheetDialog.show()
    }

    private fun shareText(origin: String, meaning: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "$origin - $meaning")
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Oxford Dictionary"))
    }

    private fun savedOrNot(properties: Properties): Boolean {
        MySharedPreferences.init(this)
        val list = MySharedPreferences.objectString
        var isHave = false
        for (i in list.indices) {
            val exam = list[i]
            if (exam == properties) {
                isHave = true
                break
            }
        }
        return isHave
    }

    private fun copyName(origin: String, meaning: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", "$origin - $meaning")
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Nusxalandi", Toast.LENGTH_SHORT).show()
    }
}

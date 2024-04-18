package uz.umarov.ismlarmanosi

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import uz.umarov.ismlarmanosi.adapters.RvAdapter
import uz.umarov.ismlarmanosi.databinding.ActivityNamesBinding
import uz.umarov.ismlarmanosi.databinding.BottomSheetDialogBinding
import uz.umarov.ismlarmanosi.models.Properties
import uz.umarov.ismlarmanosi.service.OnCLick
import uz.umarov.ismlarmanosi.vm.NamesViewModel

class NamesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNamesBinding
    private val viewModel: NamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNamesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        val type = intent.getStringExtra("gender") ?: "a"
        viewModel.loadNames(type, this)

        viewModel.currentList.observe(this) { properties ->
            val adapter = RvAdapter(properties, object : OnCLick {
                override fun click(properties: Properties, position: Int) {
                    showBottomSheet(properties, position)
                }
            })
            binding.rv.adapter = adapter
        }

        binding.editText.addTextChangedListener {
            viewModel.filterNames(it.toString())
        }

        binding.tvClear.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                onBackPressed()
            } else {
                binding.editText.text.clear()
            }
        }
    }

    private fun setupUI() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#ecf0f3")
        supportActionBar?.hide()
    }

    @SuppressLint("SetTextI18n")
    private fun showBottomSheet(properties: Properties, position: Int) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.SheetDialog)
        val item = BottomSheetDialogBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(item.root)
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        item.tvName.text = properties.name
        item.tvTarif.text = "Kelib chiqishi: ${properties.origin}\nMa'nosi: ${properties.meaning}"

        val isLiked = viewModel.isLiked(properties, this)
        item.likeImage.setImageResource(if (isLiked) R.drawable.ic_like else R.drawable.ic_unlike)

        item.cardLike.setOnClickListener {
            viewModel.toggleLike(properties, this)
            item.likeImage.setImageResource(if (!isLiked) R.drawable.ic_like else R.drawable.ic_unlike)
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
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    private fun copyName(origin: String, meaning: String) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", "$origin - $meaning")
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}

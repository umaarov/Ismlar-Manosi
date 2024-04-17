package uz.umarov.ismlarmanosi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.umarov.ismlarmanosi.R
import uz.umarov.ismlarmanosi.databinding.ItemRvBinding
import uz.umarov.ismlarmanosi.models.Properties
import uz.umarov.ismlarmanosi.service.OnCLick


class RvAdapter(var list: ArrayList<Properties>, var onCLick: OnCLick) :
    RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(private var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
        fun onBind(properties: Properties, position: Int) {
            when (properties.gender) {
                "M" -> {
                    itemRv.view.setBackgroundResource(R.color.blueLine)
                }

                "F" -> {
                    itemRv.view.setBackgroundResource(R.color.redLine)
                }

            }
            itemRv.tvNames.text = properties.name
            itemRv.root.setOnClickListener {
                onCLick.click(list[position], position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
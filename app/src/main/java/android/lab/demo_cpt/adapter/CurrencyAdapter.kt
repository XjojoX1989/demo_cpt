package android.lab.demo_cpt.adapter

import android.graphics.Color
import android.lab.demo_cpt.data.Currency
import android.lab.demo_cpt.databinding.ListItemCurrencyBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.graphics.drawable.GradientDrawable
import android.view.View
import java.util.*


class CurrencyAdapter(private val onItemClickListener: (Currency) -> Unit) : ListAdapter<Currency, RecyclerView.ViewHolder>(CurrencyDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val biding = ListItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyViewHolder(biding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as? CurrencyViewHolder)?.bindItem(item, onItemClickListener)
    }

    class CurrencyViewHolder(private val binding: ListItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val mRandom = Random()
        fun bindItem(item: Currency, listener: (Currency) -> Unit) {
            binding.currency = item
            val color: Int = Color.argb(255, mRandom.nextInt(256), mRandom.nextInt(256), mRandom.nextInt(256))
            (binding.tvTitle.background as? GradientDrawable)?.setColor(color)
            binding.root.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}

private class CurrencyDiffCallback : DiffUtil.ItemCallback<Currency>() {

    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) = (oldItem.id == newItem.id)

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) = (oldItem == newItem)
}
package com.example.apiretrofitktor.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.apiretrofitktor.R
import com.example.apiretrofitktor.databinding.PokemonItemCardBinding
import com.example.apiretrofitktor.ui.model.Pokemon
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PokemonAdapter @Inject constructor(
    @ApplicationContext val context: Context,
) : RecyclerView.Adapter<PokemonAdapter.PokeVH>() {
    private var data: MutableList<Pokemon> = mutableListOf()
    companion object{
        val listColor = listOf(
            R.color.GTextView1,
            R.color.color1,
            R.color.color2,
            R.color.color3,
            R.color.GBackground
        )
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Pokemon>) {
        this.data = data.toMutableList()
        notifyDataSetChanged()
    }
    fun addMoreItem(newData: List<Pokemon>) {
        val size = this.data.size
        this.data.addAll(newData)
        notifyItemRangeInserted(size, newData.size)
    }
    inner class PokeVH(
        private val binding: PokemonItemCardBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("DefaultLocale")
        fun bind(item: Pokemon) {
            binding.apply {
                viewContainer.setBackgroundColor(ContextCompat.getColor(context, listColor[adapterPosition % 5]))
                pokemonName.text = item.name
                pokeid.text = String.format("#%03d", item.id)
                Glide.with(context)
                    .load(item.image)
                    .placeholder(R.drawable.ghtk)
                    .error(R.drawable.ghtk)
                    .into(image)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PokeVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PokemonItemCardBinding.inflate(layoutInflater, parent, false)
        return PokeVH(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(
        holder: PokeVH,
        position: Int,
    ) {
        holder.bind(data[position])
    }
}

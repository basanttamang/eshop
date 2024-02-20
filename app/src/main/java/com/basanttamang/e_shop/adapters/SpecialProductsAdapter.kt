package com.basanttamang.e_shop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.basanttamang.e_shop.data.Products
import com.basanttamang.e_shop.databinding.SpecialRvItemBinding
import com.bumptech.glide.Glide

class SpecialProductsAdapter: RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(private val binding:SpecialRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
            fun bind(products: Products){
                    binding.apply {
                        Glide.with(itemView).load(products.images[0]).into(imageId)
                        productName.text = products.name
                        productPrice.text = products.price.toString()
                    }
            }
        }

    private val diffCallBack = object : DiffUtil.ItemCallback<Products>(){
        override fun areItemsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Products, newItem: Products): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, null == true
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val products = differ.currentList[position]
        holder.bind(products)

        holder.itemView.setOnClickListener{
            onClick?.invoke(products)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    var onClick:((Products)->Unit)? = null

}
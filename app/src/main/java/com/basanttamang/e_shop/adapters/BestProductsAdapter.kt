package com.basanttamang.e_shop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.basanttamang.e_shop.data.Products
import com.basanttamang.e_shop.databinding.BestDealsRvItemBinding
import com.basanttamang.e_shop.databinding.ProductRvItemBinding
import com.bumptech.glide.Glide

class BestProductsAdapter: RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding:ProductRvItemBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(products: Products){
            binding.apply {
                Glide.with(itemView).load(products.images[0]).into(imgProduct)
                productName.text = products.name
                tvPrice.text = products.price.toString()


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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val products = differ.currentList[position]
        holder.bind(products)

        holder.itemView.setOnClickListener {
            onClick?.invoke(products)
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var onClick:((Products)->Unit)? = null
}
package com.mutiara.storemutiara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProductAdapter(private var productList: MutableList<Product>, private val onDeleteClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductHolder>()  {
    fun updateProduct(newProduct:ArrayList<Product>){
    productList = newProduct
    notifyDataSetChanged()
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductHolder(layout)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val currentProduct = productList[position]

        holder.nameProduct.text = productList.get(position).name
        holder.priceProduct.text = productList.get(position).price
        holder.promoProduct.text = productList.get(position).promo
        holder.descProduct.text = productList.get(position).description
        Picasso.get().load(productList.get(position).image).into(holder.imageProduct);

        holder.btnDelete.setOnClickListener{
            onDeleteClick(currentProduct)
        }
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        val nameProduct:TextView = itemView.findViewById(R.id.productName)
        val priceProduct:TextView = itemView.findViewById(R.id.productPrice)
        val promoProduct:TextView = itemView.findViewById(R.id.productPromo)
        val descProduct:TextView = itemView.findViewById(R.id.productDesc)
        val imageProduct:ImageView = itemView.findViewById(R.id.productImage)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
       }
}
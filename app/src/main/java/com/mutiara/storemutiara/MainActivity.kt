package com.mutiara.storemutiara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var productList:MutableList<Product>
    lateinit var productAdapter: ProductAdapter
    lateinit var searchProduct:EditText
    lateinit var sortPrice:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //initComponenent()
        val recProduct = findViewById<RecyclerView>(R.id.recProduct)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        val swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        searchProduct = findViewById(R.id.editSearch)
        sortPrice = findViewById(R.id.btnPrice)
        productList = ArrayList()
        productAdapter = ProductAdapter(productList) { product ->
            deleteProduct(product, progressBar)
        }
        recProduct.adapter = productAdapter

        fab.setOnClickListener {
            val intentAdd = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intentAdd)
        }
        swipeLayout.setOnRefreshListener {
            getAllProduct()
            swipeLayout.isRefreshing
        }
        getAllProduct()
        searchProduct.addTextChangedListener(
            object:TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    if (s!=null){
                        getSearchProduct(s.toString())
                    }
                }
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            }
        )
        sortPrice.setOnClickListener{
            getPriceProduct()
        }
    }

    private fun deleteProduct(product: Product, progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
        val urlDeleteProduct = "http://10.0.2.2/backend_storetiara/deleteproduct.php"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Method.POST, urlDeleteProduct,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                    productList.remove(product)
                    productAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                } finally {
                    progressBar.visibility = View.GONE
                }
            },
            Response.ErrorListener { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to connect to server: ${error.message}", Toast.LENGTH_LONG).show()
            }) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = product.id.toString()
                return params
            }
        }
        queue.add(request)
    }

    private fun getSearchProduct(query: String) {
        val urlSearchProduct = "http://10.0.2.2/backend_storetiara/searchproduct.php?search=$query"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request = StringRequest(Request.Method.GET, urlSearchProduct, { response ->
            val jsonArray = JSONArray(response)
            val searchNameP = ArrayList<Product>()
            for ( i in 0 until jsonArray.length()){
                val resObj = jsonArray.getJSONObject(i)
                val itemProduct = Product(
                    resObj.getInt("id"),
                    resObj.getString("name"),
                    "Harga Rp. "+resObj.getString("price"),
                    "Promo Rp. "+resObj.getString("promo"),
                    resObj.getString("description"),
                    resObj.getString("image"))
                searchNameP.add(itemProduct)
            }
            productAdapter.updateProduct(searchNameP)
        },{
            Toast.makeText(this@MainActivity, "Product not found.", Toast.LENGTH_LONG).show()
        })
        queue.add(request)
    }

    private fun getAllProduct() {
        //192.168.29.6
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val urlAllProduct = "http://10.0.2.2/backend_storetiara/allproduct.php"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request = JsonArrayRequest(Request.Method.GET, urlAllProduct, null, {
                response ->
            progressBar.visibility = View.GONE
            try{
                for ( i in 0 until response.length()){
                    val resObj = response.getJSONObject(i)
                    val pId = resObj.getInt("id")
                    val pName = resObj.getString("name")
                    val pPrice = "Harga Rp. "+resObj.getString("price")
                    val pPromo = "Promo Rp. "+resObj.getString("promo")
                    val pDesc = resObj.getString("description")
                    val pImage = resObj.getString("image")
                    productList.add(Product(pId,pName, pPrice, pPromo, pDesc, pImage))
                    productAdapter.notifyDataSetChanged()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }, {
                error->
            Toast.makeText(this@MainActivity, "Gagal mendapatkan data dari server", Toast.LENGTH_LONG).show()
        } )
        queue.add(request)
    }

    private fun getPriceProduct() {
        var progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val urlSortProduct = "http://10.0.2.2/backend_storetiara/sortbyprice.php"
        val queue = Volley.newRequestQueue(this@MainActivity)
        val request = JsonArrayRequest(Request.Method.GET, urlSortProduct, null, {
                response ->
            progressBar.visibility = View.GONE
            try{
                productList.clear()
                for ( i in 0 until response.length()){
                    val resObj = response.getJSONObject(i)
                    val pId = resObj.getInt("id")
                    val pName = resObj.getString("name")
                    val pPrice = "Harga Rp. "+resObj.getString("price")
                    val pPromo = "Promo Rp. "+resObj.getString("promo")
                    val pDesc = resObj.getString("description")
                    val pImage = resObj.getString("image")
                    productList.add(Product(pId,pName, pPrice, pPromo, pDesc, pImage))
                    productAdapter.notifyDataSetChanged()
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }, {
                error->
            Toast.makeText(this@MainActivity, "Gagal mendapatkan data dari server", Toast.LENGTH_LONG).show()
        } )
        queue.add(request)
        }
}
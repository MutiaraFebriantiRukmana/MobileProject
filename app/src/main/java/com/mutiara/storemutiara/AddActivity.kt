package com.mutiara.storemutiara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class AddActivity : AppCompatActivity() {
    lateinit var name:EditText
    lateinit var price:EditText
    lateinit var promo:EditText
    lateinit var desc:EditText
    lateinit var image:EditText
    lateinit var btnAdd:Button
    lateinit var btnBack:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        name = findViewById(R.id.addName)
        price = findViewById(R.id.addPrice)
        promo = findViewById(R.id.addPromo)
        desc = findViewById(R.id.addDesc)
        image = findViewById(R.id.addImage)
        btnAdd = findViewById(R.id.btnAdd)
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener{
            val intentBack = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(intentBack)
        }
        btnAdd.setOnClickListener{
            val nameP = name.text.toString().trim()
            val priceP = price.text.toString().trim()
            val promoP = promo.text.toString().trim()
            val descP = desc.text.toString().trim()
            val imageP = image.text.toString().trim()
            if(TextUtils.isEmpty(nameP)){
                name.setError("Please enter product name")
            }
            else if(TextUtils.isEmpty(priceP)){
                price.setError("Please enter product price")
            }
            else if(TextUtils.isEmpty(promoP)){
                promo.setError("Please enter product promo")
            }
            else if(TextUtils.isEmpty(descP)){
                desc.setError("Please enter product description")
            }
            else if(TextUtils.isEmpty(imageP)){
                image.setError("Please enter product image")
            }
            else{
                addNewProduct(nameP, priceP, promoP, descP, imageP)
            }
        }
    }

    private fun addNewProduct(nameP: String, priceP: String, promoP: String, descP: String, imageP: String) {
        val urlAddProduct = "http://10.0.2.2/backend_storetiara/addproduct.php"
        val queue = Volley.newRequestQueue(this@AddActivity)
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, urlAddProduct, object :
                Listener<String>{
                override fun onResponse(response: String?) {
                    try {
                        val jsonObject = JSONObject(response)
                        Toast.makeText(
                            this@AddActivity,
                            "New product successfull added!",
                            Toast.LENGTH_LONG
                        ).show()
                        clearText()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddActivity,
                            "Error : , ${e.toString()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }, Response.ErrorListener {
                Toast.makeText(this@AddActivity, "New product failed to add", Toast.LENGTH_LONG)
                    .show()
            }){
                override fun getBodyContentType(): String {
                    return "application/x-www-form-urlencoded; charset= UTF-8"
                }

                override fun getParams(): Map<String, String>? {
                    val params:MutableMap<String, String> = HashMap()
                    params.put("name", nameP)
                    params.put("price", priceP)
                    params.put("promo", promoP)
                    params.put("description", descP)
                    params.put("image", imageP)
                    return params
                }
            }
        queue.add(request)
    }

    fun clearText(){
        name.setText("")
        price.setText("")
        promo.setText("")
        desc.setText("")
        image.setText("")
        }
}

package com.astro.test.edwinasuciati

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.astro.test.edwinasuciati.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var adapter: Adapter? = null
    var list = ArrayList<ModelUser>()
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initContent()
    }

    private fun initContent() {
        restApi("https://api.github.com/users?Authorization=token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")
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        adapter = Adapter(list,this)
        binding.recyclerView.adapter = adapter
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                Log.i("cek", "page$page")
                val pageGuests = page+1
                restApi(pageGuests.toString())
            }
        }
        binding.recyclerView.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener)
    }
    private fun restApi(url: String) {
        showLoading()
        val request = Volley.newRequestQueue(this)
        val stringReq = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                Log.i("cek","response"+response)
                hideLoading()
                try {
                    val jsonObj = JSONArray(response)
                    callbackResponseListener(jsonObj)
                } catch (ex: JSONException) {
                    Log.i("cek","catch"+ex)
                }
            },
            Response.ErrorListener {
                hideLoading()
                Log.i("cek","error"+it)
//                PropertyToast().toast(activity,"Terjadi Kesalahan")
            }
        ){
            override fun getParams(): Map<String, String> {
                //Creating HashMap
                val params = HashMap<String, String>()
                putParams(params)
                return params
            }
        }
        stringReq.retryPolicy = DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(stringReq)
    }

    private fun putParams(params: HashMap<String,String>): HashMap<String,String>{
        return params
    }

    private fun callbackResponseListener(array: JSONArray) {
        for (i in 0 until array.length()){
            val name = array.getJSONObject(i).getString("login")
            val avatar = array.getJSONObject(i).getString("avatar_url")
            list.add(ModelUser(name, avatar))
        }
        adapter?.notifyDataSetChanged()
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

}
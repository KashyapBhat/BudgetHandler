package `in`.kashyap.personalbudgethandler

import `in`.kashyap.personalbudgethandler.model.PurchasedItem
import `in`.kashyap.personalbudgethandler.recyclerView.BudgetDataRVAdapter
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.hawk.Hawk

import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.Exception


class MainActivity : AppCompatActivity() {

    var data = ArrayList<PurchasedItem>()
    var dataMap = HashMap<String, Double>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Hawk.init(applicationContext).build()
        getHawk()
        setRecyclerView()
        fab.setOnClickListener {
            addItemToBuy()
        }
        fabDel.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Are you sure you want to delete all data?")
                .setCancelable(false)
                .setPositiveButton("Proceed") { dialog, id -> clearAllData() }
                .setNegativeButton("Cancel") { dialog, id -> dialog.cancel() }
                .create().show()
        }
    }

    private fun clearAllData() {
        Hawk.deleteAll()
        data = ArrayList()
        dataMap = HashMap()
        dataMap["TotalCostOfAllItemsCombined"] = 0.0
        Hawk.put("DATA", data)
        Hawk.put("DMAP", dataMap)
        setRecyclerView()
    }

    private fun getHawk() {
        if (Hawk.contains("DATA"))
            data = Hawk.get("DATA")
        if (Hawk.contains("DMAP"))
            dataMap = Hawk.get("DMAP")
        else dataMap["TotalCostOfAllItemsCombined"] = 0.0
    }

    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvBudetData)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val bdata = data
        val adapter =
            BudgetDataRVAdapter(
                bdata
            )
        recyclerView.adapter = adapter
        val totalItemsCost = findViewById<TextView>(R.id.tvTotalCost)
        val total: String = dataMap["TotalCostOfAllItemsCombined"].toString()
        totalItemsCost.text = total
    }

    private fun addItemToBuy() {
        val dialog = BottomSheetDialog(this, R.style.DialogStyle)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.add_item_layout)
        val body = dialog.findViewById(R.id.tvTitle) as TextView?
        val btnAdd = dialog.findViewById(R.id.btnAdd) as Button?
        val btnCancel = dialog.findViewById(R.id.btnCancel) as Button?
        val etName = dialog.findViewById(R.id.etAddName) as EditText?
        val etCost = dialog.findViewById(R.id.etAddCost) as EditText?
        if (body == null || btnAdd == null || btnCancel == null || etName == null || etCost == null)
            return
        body.text = "Please add your item and it's cost"
        btnAdd.setOnClickListener {
            if (etName.text.isNotEmpty() && etCost.text.isNotEmpty()) {
                try {
                    val cost: Double = etCost.text.toString().toDouble()
                    data.add(PurchasedItem(etName.text.toString(), cost))
                    var total: Double? = dataMap["TotalCostOfAllItemsCombined"]
                    total = total?.plus(cost)
                    total?.let { it1 -> dataMap.put("TotalCostOfAllItemsCombined", it1) }
                    dataMap[etName.text.toString()] = cost
                    Log.d("Map: ", dataMap.toString())
                } catch (e: Exception) {
                    Log.e("Exception", "" + e.localizedMessage)
                }
                setRecyclerView()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Please assign proper values", Toast.LENGTH_SHORT).show()
            }
        }
        btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        getHawk()
    }

    override fun onPause() {
        super.onPause()
        Hawk.put("DATA", data)
        Hawk.put("DMAP", dataMap)
    }

    override fun onDestroy() {
        super.onDestroy()
        Hawk.put("DATA", data)
        Hawk.put("DMAP", dataMap)
    }
}

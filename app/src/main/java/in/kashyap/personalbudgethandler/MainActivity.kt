package `in`.kashyap.personalbudgethandler

import `in`.kashyap.personalbudgethandler.model.PurchasedItem
import `in`.kashyap.personalbudgethandler.recyclerView.BudgetDataRVAdapter
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var data = ArrayList<PurchasedItem>()
    var dataMap: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataMap = 0.0
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
        data = ArrayList()
        dataMap = 0.0
        setRecyclerView()
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
        val total: String = dataMap.toString()
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
                    dataMap.plus(cost).let { it1 -> dataMap = it1 }
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
}

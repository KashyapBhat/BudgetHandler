package `in`.kashyap.personalbudgethandler.recyclerView

import `in`.kashyap.personalbudgethandler.model.PurchasedItem
import `in`.kashyap.personalbudgethandler.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class BudgetDataRVAdapter(val bData: ArrayList<PurchasedItem>) :RecyclerView.Adapter<BudgetDataRVAdapter.ViewHolder>(){
    override fun getItemCount(): Int {
        return bData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(bData[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_budget_data, parent, false)
        return ViewHolder(
            v
        )
    }

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(data: PurchasedItem) {
            val tvName = itemView.findViewById(R.id.tvItemName) as TextView
            val tvCost  = itemView.findViewById(R.id.tvItemCost) as TextView
            tvName.text = data.name
            tvCost.text = data.cost.toString()
        }
    }

}
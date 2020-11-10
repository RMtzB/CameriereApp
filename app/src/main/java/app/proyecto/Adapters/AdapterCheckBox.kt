package app.proyecto.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_pago.view.*
import kotlinx.android.synthetic.main.item_check.view.*
import java.util.*

class AdapterCheckBox (val lista: ArrayList<String>, val context: Context):
        RecyclerView.Adapter<AdapterCheckBox.ViewHolder>(){

    class ViewHolder(val view:View): RecyclerView.ViewHolder(view){
        val Ad_Check = view.checkbox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check,parent,false))
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    override fun onBindViewHolder(holder: AdapterCheckBox.ViewHolder, position: Int) {
        holder.Ad_Check.text = lista[position]
        TODO("Not yet implemented")
    }

}
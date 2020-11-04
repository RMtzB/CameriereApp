package app.proyecto.Adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.proyecto.Activities.PromocionesActivity
import app.proyecto.Models.Promo
import app.proyecto.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_promo.view.*


class AdapterPromo (val lista: ArrayList<Promo>,val context: Context):
        RecyclerView.Adapter<AdapterPromo.ViewHolder>(){

    class ViewHolder(val view:View): RecyclerView.ViewHolder(view){
        val URL= view.img_promo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_promo,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(lista[position].URL).fitCenter().centerCrop().into(holder.URL)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

}
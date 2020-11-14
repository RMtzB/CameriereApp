package app.proyecto.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.proyecto.DataAccess.DBUser
import app.proyecto.Models.Integrante
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_pago.view.*
import kotlinx.android.synthetic.main.item_check.view.*
import org.w3c.dom.Text
import kotlin.collections.ArrayList

class AdapterCheckBox (val lista :ArrayList<Integrante>,val todos:CheckBox, val textF:TextView , val context: Context):
        RecyclerView.Adapter<AdapterCheckBox.ViewHolder>(){

    class ViewHolder(val view:View): RecyclerView.ViewHolder(view){
        val Check = view.Card_CB
        val totalp=view.txtCard_Total
        val TotalF=view.txtCard_Total
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ViewHolder=ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_check,parent,false))
        return ViewHolder
    }

    override fun getItemCount(): Int {
        return lista.size
    }


    override fun onBindViewHolder(holder: AdapterCheckBox.ViewHolder, position: Int) {
        holder.Check.text = lista[position].nombre
        holder.totalp.text="$ "+lista[position].totalParcial
        lista[position].chek=holder.Check

        holder.Check.setOnClickListener(){
            if(holder.Check.isChecked) {
                textF.setText("$ " + obtenerTotal(textF, lista[position].totalParcial, true))
                DBUser.i++
                if(DBUser.i==lista.size) {
                    todos.isChecked = true
                    todos.isSelected = true
                }
            }
            else {
                textF.setText("$ " + obtenerTotal(textF, lista[position].totalParcial, false))
                todos.isChecked=false
                todos.isSelected=false
                DBUser.i--
            }
        }

    }

    private fun obtenerTotal(Tf:TextView,tp:Double,v:Boolean):Double{
        var Total :Double
        Total = DBUser.TotalGeneral
        if(v)
            Total+=tp
        else
            Total-=tp
        DBUser.TotalGeneral=Total
        return Total
    }



}
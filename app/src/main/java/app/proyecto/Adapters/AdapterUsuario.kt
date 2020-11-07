package app.proyecto.Adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.proyecto.Models.Usuario
import kotlinx.android.synthetic.main.activity_home.view.*

class AdapterUsuario(val lista:ArrayList<Usuario>, val context:Context):
        RecyclerView.Adapter<AdapterUsuario.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val Nombre=view.txtVNombreC
        val CorreoE= view.txtvCorreo

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUsuario.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: AdapterUsuario.ViewHolder, position: Int) {
        holder.Nombre.text= lista[0].Nombre
        holder.CorreoE.text= lista[0].CorreoE
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}

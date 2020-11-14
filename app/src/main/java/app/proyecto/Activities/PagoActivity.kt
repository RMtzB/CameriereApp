package app.proyecto.Activities

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import app.proyecto.Adapters.AdapterCheckBox
import app.proyecto.DataAccess.DBUser
import app.proyecto.Models.Integrante
import app.proyecto.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pago.*

class PagoActivity : AppCompatActivity() {
    lateinit var restaurant:String
    lateinit var mesa:String
    var Total:Double=0.0
    private val lista:ArrayList<Integrante> = ArrayList();
    lateinit var adap:AdapterCheckBox
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        adap= AdapterCheckBox(lista,ChPago_Todos,txtPago_Total,this)
        RVPago_ListaClientes.layoutManager=GridLayoutManager(this,1)
        RVPago_ListaClientes.adapter=adap

        restaurant = intent.getStringArrayExtra("Code")?.get(1).toString()
        mesa = intent.getStringArrayExtra("Code")?.get(2).toString()
        setup()
    }

    private fun setup() {
        llenarLista()

        ChPago_Todos.setOnClickListener(){
            if(ChPago_Todos.isChecked)
                marcarTodos(1)
            else
                marcarTodos(2)
        }

        btnPago_Confirmar.setOnClickListener(){
            if (!rBPago_Efectivo.isChecked && !rBPago_PayPal.isChecked)
                Toast.makeText(this,"Selecciona algun metodo de pago", Toast.LENGTH_SHORT).show()
            else{
                var i=0
                for (integrante in lista){
                    if(integrante.chek.isChecked){
                        db.collection("Restaurants")
                                .document(restaurant)
                                .collection(mesa).
                                document(integrante.nombre).delete().addOnCompleteListener(){
                                    if(it.isSuccessful){
                                        DBUser.i--
                                        lista.removeAt(i)

                                    }
                                }
                        i++
                    }
                }
                marcarTodos(2)
                DBUser.TotalGeneral=0.0
                txtPago_Total.text="$ "+DBUser.TotalGeneral
                adap.notifyDataSetChanged()
            }

        }

    }

        private fun llenarLista(){

        db.collection("Restaurants")
                .document(restaurant)
                .collection(mesa)
                .get()
                .addOnCompleteListener(){
                    if(it.isSuccessful) {
                        lista.clear()
                        for (documentos in it.result!!) {
                            val nombre = documentos.getString("Nombre")
                            val totalp:Double?= documentos.get("Total") as Double?

                            if (nombre != null &&totalp != null ) {
                                lista.add( Integrante(nombre,totalp) )

                            }
                        }
                        adap.notifyDataSetChanged()
                    }
                    else
                        Log.w(ContentValues.TAG, "No pude leer nada")
        }

    }

    private fun marcarTodos(i:Int){
        var valor:Boolean
        valor = i==1

        for (it in lista){
            it.chek.isChecked=valor
            it.chek.isSelected=valor
            if (valor)
                Total+=it.totalParcial
            else
                Total-=it.totalParcial
            }
        DBUser.TotalGeneral=Total
        txtPago_Total.text="$ "+DBUser.TotalGeneral



    }
}


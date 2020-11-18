package app.proyecto.Activities

import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import app.proyecto.Adapters.AdapterCheckBox
import app.proyecto.DataAccess.DBUser
import app.proyecto.Models.Integrante
import app.proyecto.R
import app.proyecto.Utils.BusinessLogic
import com.google.common.collect.ArrayListMultimap
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pago.*

class PagoActivity : AppCompatActivity() {
    lateinit var restaurant:String
    lateinit var logiB:BusinessLogic
    lateinit var mesa:String
    var Total:Double=0.0
    private val lista:ArrayList<Integrante> = ArrayList();
    lateinit var adap:AdapterCheckBox
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)
        logiB=BusinessLogic(this)
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
                for (integrante in lista){
                    if(integrante.chek.isChecked){
                        db.collection("Restaurants")
                            .document(restaurant)
                            .collection(mesa).
                            document(integrante.nombre).delete().addOnCompleteListener(){
                                if(it.isSuccessful){
                                    DBUser.i--
                                    lista.remove(integrante)
                                    adap.notifyDataSetChanged()
                                    Buscar()
                                }
                            }
                    }
                }
                marcarTodos(2)
                DBUser.TotalGeneral=0.0
                txtPago_Total.text="$ "+DBUser.TotalGeneral
            }

        }

    }

    private fun Buscar(){
        var bandera=false
        for (elem in lista){
            if(elem.nombre.equals(DBUser.miUsuario.Nombre)) {
                bandera = true
                break
            }
        }
        if (!bandera) {
            logiB.deleteLists()
            val prefs = getSharedPreferences(getString(R.string.prefs_codigo), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()
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

            db.collection("Restaurants")
                    .document(restaurant)
                    .collection(mesa).addSnapshotListener{snapshot,exception ->
                        if(exception != null){
                            Toast.makeText(this,"ah sucedido un error",Toast.LENGTH_SHORT).show()
                        }
                        lista.clear();
                        snapshot?.forEach(){
                            val nombre = it.getString("Nombre")
                            val totalp:Double?= it.get("Total") as Double?

                            if (nombre != null &&totalp != null ) {
                                lista.add( Integrante(nombre,totalp) )

                            }

                        }
                        adap.notifyDataSetChanged()
                        Buscar()
                        if(lista.isEmpty())
                            showAlert()
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

    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Mesa Cerrada")
        builder.setMessage("Gracias por su visita, vuelva pronto")
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
            startActivity(Intent(this, PromocionesActivity::class.java))
            finishAffinity()
            finish()

        })
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}


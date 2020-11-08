package app.proyecto.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import app.proyecto.Adapters.AdapterUsuario
import app.proyecto.Models.Usuario
import app.proyecto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_iniciosesion.*

class InicioSesionActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val lista:ArrayList<Usuario> = ArrayList()
    private val adapterUsuario= AdapterUsuario(lista,this)
    private lateinit var  user: Usuario


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciosesion)
        session()
        setup()

    }

    override fun onStart() {
        super.onStart()
        lyini.visibility = View.VISIBLE
    }

    private fun setup() {

        db.collection("user").get().addOnCompleteListener() {
            if(it.isSuccessful){
                lista.clear()
                for(documentos in it.result!!){
                    val name = documentos.getString("Nombre")
                    val ape = documentos.getString("Apellido")
                    val aux=documentos.getString("CorreoE")
                    if (name!=null && ape!=null && aux!=null)
                        lista.add(Usuario(name,ape,aux))
                }
                adapterUsuario.notifyDataSetChanged()
            }
        }

        btnIniciarSesion.setOnClickListener {
            if (datosCorrectos()) {
                //Cargando
                var pd = ProgressDialog(this)
                pd.setMessage("Iniciando Sesion...")
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                pd.setCancelable(false)
                pd.show()
                val correo = txtCorreo.text.toString()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,txtContraseña.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful) {
                        buscarEnLista(correo)
                        guardarLocal(user.Nombre,user.Apellido,user.CorreoE)
                        pd.dismiss()
                        showhome(user.Nombre,user.Apellido,user.CorreoE)
                    }
                    else{
                        pd.dismiss()
                        showAlert(0)

                    }
                }
            } else
                showAlert(1)
        }
        btnRegistrarte1.setOnClickListener() {
            showRegistro()
        }
        txtOlvC.setOnClickListener() {
            showRecuperacion()

        }

    }

    private fun datosCorrectos(): Boolean {
        if (txtCorreo.text.isNotEmpty() && txtContraseña.text.isNotEmpty())
            return true
        return false
    }


    fun showAlert(error: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        if (error == 0)
            builder.setMessage("Correo o contraseña incorrectos")
        if (error == 1)
            builder.setMessage("Todos los Campos son requeridos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showhome(name:String , ape:String , correo:String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("name",name)
            putExtra("ape",ape)
            putExtra("correo",correo)

        }
        startActivity(homeIntent)
        finish()
    }


    private fun showRegistro() {
        val RegistroIntent = Intent(this, RegistroActivity::class.java).apply {
        }
        startActivity(RegistroIntent)
        finish()
    }
    private fun showRecuperacion() {
        val RecuperacionIntent = Intent(this, RecuperacionActivity::class.java).apply {
        }
        startActivity(RecuperacionIntent)
        finish()
    }

    private fun session() {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val name = prefs.getString("name", null)
        val ape = prefs.getString("ape", null)

        if (email != null) {
            lyini.visibility = View.INVISIBLE
            showhome(name?:"",ape?:"",email?:"")
            lista.clear()
        }
    }
    private fun buscarEnLista(correo:String){
        for (Usu in lista){
            if(Usu.CorreoE==correo) {
                user = Usu
                break
            }
        }
        lista.clear()
    }

    private fun guardarLocal(Nombre:String, Apellido:String, CorreoE:String) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", Nombre)
        prefs.putString("ape", Apellido)
        prefs.putString("email",CorreoE)
        prefs.apply()
    }


}
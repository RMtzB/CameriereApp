package app.proyecto.Activities

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBFirebase
import app.proyecto.Models.Usuario
import app.proyecto.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_iniciosesion.*

class InicioSesionActivity : AppCompatActivity() {
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
        DBFirebase.iniciarLista()

        btnIniciarSesion.setOnClickListener {
            if (datosCorrectos()) {
                //Cargando
                var pd = ProgressDialog(this)
                pd.setMessage("Iniciando Sesion...")
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                pd.setCancelable(false)
                pd.show()
                val correo = txtInicio_CorreoElect.text.toString()
                FirebaseAuth.getInstance().signInWithEmailAndPassword(correo,txtInicio_Contraseña.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful) {
                        DBFirebase.optenerUsuario(correo,this)
                        guardarLocal(DBFirebase.miUsuario)
                        pd.dismiss()
                        showhome(DBFirebase.miUsuario)
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
        if (txtInicio_CorreoElect.text.isNotEmpty() && txtInicio_Contraseña.text.isNotEmpty())
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

    private fun showhome(u:Usuario) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("name",u.Nombre)
            putExtra("correo",u.CorreoE)
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

        if (email != null) {
        //    lyini.visibility = View.INVISIBLE
            val u=Usuario(name?:"",email?:"")
            DBFirebase.optenerUsuario(u)
            showhome(u)

        }
    }


    private fun guardarLocal(u: Usuario) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", u.Nombre)
        prefs.putString("email",u.CorreoE)
        prefs.apply()
    }


}
package app.proyecto.Activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBUser
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
        DBUser.iniciarLista()

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
                        DBUser.optenerUsuario(correo,this)
                        DBUser.guardarLocal(this)
                        pd.dismiss()
                        showhome()
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

    private fun showhome() {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
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

        if (DBUser.leerLocal(this)) {
            showhome()

        }
    }
}
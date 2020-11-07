package app.proyecto.Activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_recuperacion.*

class RecuperacionActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperacion)
        setup()
    }


    private fun setup(){
        btnRecuperarAction.setOnClickListener {
            recuperarContraseña()
        }

        btnCancelarAction.setOnClickListener{
            showInicioSesion()
        }
    }


    private fun recuperarContraseña(){

        var email:String=txtCorreoRecuperar.text.toString()

        Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showAlert(0)
                    }
                    else
                        showAlert(1)
                }

    }

    fun showAlert(error: Int) {
        val builder = AlertDialog.Builder(this)

        if (error == 0) {
            builder.setTitle("Correo Enviado")
            builder.setMessage("Se ah enviado un correo electoronico para cambiar su contraseña")
        }
        if (error == 1) {
            builder.setTitle("Error")
            builder.setMessage("Ocurrio un Error Verfique el correo")
        }
        builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener(){ dialogInterface: DialogInterface, i: Int ->
            showInicioSesion()
        })

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun showInicioSesion(){
        val inicioSesionIntent = Intent(this, InicioSesionActivity::class.java).apply {
        }
        startActivity(inicioSesionIntent)
    }

}
package app.proyecto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBFirebase
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_cambiar_contrase.*

class CambiarContraseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cambiar_contrase)
        setup()
    }
    private fun setup(){
        btnConfirmar.setOnClickListener(){
            if (txtCambiar_nuevaContra.equals(txtCambiar_verifContra)) {
                DBFirebase.reauth(txtCambiar_contra.text.toString(),this)
                DBFirebase.cambiarContra(txtCambiar_verifContra.text.toString())
            }
            else
                showAlert("Tu nueva contraseña no coincide ")

        }

        btnCancelarContra.setOnClickListener(){
            showInicioSesion()
        }
    }

    private fun showInicioSesion(){
        val inicioSesionIntent = Intent(this, InicioSesionActivity::class.java).apply {
        }
        startActivity(inicioSesionIntent)
        finish()
    }

    private fun showAlert(Error:String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(Error)
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
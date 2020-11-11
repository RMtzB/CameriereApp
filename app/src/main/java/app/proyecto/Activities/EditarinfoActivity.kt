package app.proyecto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_editarinfo.*
import app.proyecto.DataAccess.DBUser


class EditarinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarinfo)
        setup()

    }

    override fun onStart() {
        super.onStart()
        txtActualizar_Nombre.setText(DBUser.miUsuario.Nombre)
        txtActualizar_Correo.setText(DBUser.miUsuario.CorreoE)

    }

    private fun setup(){
        btnEditar_actualizar.setOnClickListener(){
            if(txtActualizar_Correo.text.isNotEmpty() && txtActualizar_Nombre.text.isNotEmpty())
                DBUser.actualizarUsuarioData(txtActualizar_Nombre.text.toString(),txtActualizar_Correo.text.toString())
            else
                showAlert("Todos los campos son requeridos")

        }

        btnEditar_cancelar.setOnClickListener(){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    private fun showAlert(Error: String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(Error)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
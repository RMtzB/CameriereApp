package app.proyecto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.proyecto.DataAccess.DBFirebase
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_editarinfo.*

class EditarinfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editarinfo)
        setup()
    }
    private fun setup(){
        btnEditar_actualizar.setOnClickListener(){
            if (DBFirebase.miUsuario.CorreoE.equals(txtActualizarCorreo.text.toString())){

            }

            if (DBFirebase.miUsuario.Nombre.equals(txtActualizarNombre.text.toString())){

            }


        }

        btnEditar_cancelar.setOnClickListener(){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

}
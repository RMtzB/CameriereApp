package app.proyecto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        }

        btnCancelarContra.setOnClickListener(){
            showInicioSesion()
        }
    }

    private fun showInicioSesion(){
        val inicioSesionIntent = Intent(this, InicioSesionActivity::class.java).apply {
        }
        startActivity(inicioSesionIntent)
    }

}
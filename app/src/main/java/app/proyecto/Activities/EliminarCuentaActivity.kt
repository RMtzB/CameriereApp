package app.proyecto.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBFirebase
import app.proyecto.R
import kotlinx.android.synthetic.main.activity_eliminar_cuenta.*

class EliminarCuentaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_cuenta)
        setup()
    }
    private fun setup(){
        btnEliminar_Confirmar.setOnClickListener(){
            if(switchConfirmar.isChecked){
                if (txtEliminar_Contra.text.isNotEmpty()){
                    DBFirebase.reauth(txtEliminar_Contra.text.toString(),this)
                    DBFirebase.eliminarCuenta(this)
                    startActivity(Intent(this,InicioSesionActivity::class.java))
                    finish()
                }
                else
                    showAlert(1)
            }
            else
                showAlert(0)
        }

        btnEliminar_Cancelar.setOnClickListener(){
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    fun showAlert(error: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        if (error == 0)
            builder.setMessage("Debe marcar seguro al eliminar")
        if (error == 1)
            builder.setMessage("Debe verificar su contraseña")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

}
package app.proyecto.Activities
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBUser
import app.proyecto.Models.Usuario
import app.proyecto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*
class RegistroActivity : AppCompatActivity() {

    private val db=FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        // Setup
        setup()
    }
    private fun setup(){
        title="Registrate"
        btnRegistrar.setOnClickListener{
            if(datosCorrectos()){
                var pd = ProgressDialog(this)
                pd.setMessage("Registrando Nuevo Usuario...")
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
                pd.setCancelable(false)
                pd.show()
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtCorreoEle.text.toString(),txtConfirmContrase.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        DBUser.miUsuario=Usuario(txtNombre.text.toString(),txtCorreoEle.text.toString())
                        DBUser.subirUsuarioData()
                        DBUser.guardarLocal(this)
                        pd.dismiss()
                        showhome()
                    }
                    else {
                        showAlert(0)
                        pd.dismiss()
                    }
                }
            }
            else
                showAlert(1)
        }
    }
    private fun datosCorrectos():Boolean{
        if(txtNombre.text.isNotEmpty()&&
                txtCorreoEle.text.isNotEmpty()&&
                txtContrase.text.isNotEmpty()&&
                txtConfirmContrase.text.isNotEmpty())
        {
            if(txtConfirmContrase.text.toString().equals( txtContrase.text.toString() )) {
                return true;
            }
            else {
                showAlert(2)
                return false
            }
        }
        else{
            showAlert(1)
            return false
        }
    }
    private fun showAlert(error: Int){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        if(error==0)
            builder.setMessage("Ocurrio un error al Registrar usuario")
        if(error==1)
            builder.setMessage("Todos los Campos son requeridos")
        if(error==2)
            builder.setMessage("La contraseña no coinside")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog= builder.create()
        dialog.show()

    }
    private fun showhome(){
        val homeIntet = Intent(this, HomeActivity::class.java).apply {
        }
        startActivity(homeIntet)
        finish()
    }

}
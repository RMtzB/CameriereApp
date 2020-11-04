package app.proyecto.Activities
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.btnRegistrar2
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
        btnRegistrar2.setOnClickListener{
            if(datosCorrectos()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(txtCorreoEle.text.toString(),txtConfirmContrase.text.toString()).addOnCompleteListener{
                    if(it.isSuccessful){
                        db.collection("user").document(txtCorreoEle.text.toString()).set(
                                hashMapOf("CorreoE" to txtCorreoEle.text.toString(),
                                        "Nombre" to txtNombre.text.toString(),
                                        "Apellido" to txtApellidos.text.toString())
                        )
                        guardarLocal(txtNombre.text.toString(),txtApellidos.text.toString(),txtCorreoEle.text.toString())
                        showhome(txtCorreoEle.text.toString(),txtNombre.text.toString(),txtApellidos.text.toString())
                    }
                    else
                        showAlert(0)
                }
            }
        }

    }
    private fun datosCorrectos():Boolean{
        if(txtNombre.text.isNotEmpty()&&
                txtApellidos.text.isNotEmpty()&&
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
    private fun showhome(email:String, name: String, ape:String){
        val homeIntet = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("name", name)
            putExtra("ape",ape)
        }
        startActivity(homeIntet)
    }
    private fun guardarLocal(Nombre:String, Apellido:String, CorreoE:String) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", Nombre)
        prefs.putString("ape", Apellido)
        prefs.putString("email",CorreoE)
        prefs.apply()

    }
}
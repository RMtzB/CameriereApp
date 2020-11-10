package app.proyecto.Activities
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import app.proyecto.DataAccess.DBFirebase
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
                        DBFirebase.miUsuario=Usuario(txtNombre.text.toString(),txtCorreoEle.text.toString())
                        DBFirebase.subirUsuarioData()
                        guardarLocal(DBFirebase.miUsuario)
                        pd.dismiss()
                        showhome(DBFirebase.miUsuario)
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
    private fun showhome(u:Usuario){
        val homeIntet = Intent(this, HomeActivity::class.java).apply {
            putExtra("correo",u.CorreoE)
            putExtra("name", u.Nombre)
        }
        startActivity(homeIntet)
    }
    fun guardarLocal(u:Usuario) {
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", u.Nombre)
        prefs.putString("email",u.CorreoE)
        prefs.apply()

    }
}
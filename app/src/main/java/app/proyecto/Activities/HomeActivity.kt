package app.proyecto.Activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import app.proyecto.DataAccess.DBFirebase
import app.proyecto.R
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        codigoGuardado()
        super.onCreate(savedInstanceState)
        val bundle=intent.extras
        setContentView(R.layout.activity_home)

        //setup
        setup()
        btnEscaner.setOnClickListener(mOnclickListener)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_cambiar_contra -> {
                startActivity(Intent(this, CambiarContraseActivity::class.java))
                true
            }

            R.id.menu_cerrar_sesion -> {
                showAlert()
                true
            }
            R.id.menu_editar_info -> {
                startActivity(Intent(this, EditarinfoActivity::class.java))
                true
            }
            R.id.menu_eliminar_cuenta -> {
                startActivity(Intent(this, EliminarCuentaActivity::class.java))
                true
            }

            else-> super.onOptionsItemSelected(item)

        }

    }


    override fun onStart() {
        super.onStart()
        layoutHome.visibility = View.VISIBLE
        txtVNombreC.text=DBFirebase.miUsuario.Nombre
        txtvCorreo.text=DBFirebase.miUsuario.CorreoE
    }

    private fun setup(){
        title="Inicio"

        //txtvCodigo.text="Codigo"

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) if (result.contents != null) {
            if (verificarCodigo(result.contents)) {
                localCodigo(result.contents)
                showMain(result.contents)
            }
            else
                showAlert("Codigo no registrado en la aplicacion")
        } else  showAlert("Codigo no registrado en la aplicacion")
    }
    private val mOnclickListener = View.OnClickListener { IntentIntegrator(this@HomeActivity).initiateScan() }

    private fun showMain(codigo: String) {
        val MainIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("codigo", codigo)
        }
        startActivity(MainIntent)
    }

    private fun verificarCodigo(codigo: String):Boolean{
        if("CCLOM"==codigo.split("-")[0] )
            return true
        return false

    }

    private fun localCodigo(codigo: String){
        val prefs = getSharedPreferences(getString(R.string.prefs_codigo), Context.MODE_PRIVATE).edit()
        prefs.putString("codigo", codigo)
        prefs.apply()
    }
    private fun codigoGuardado(){
        val prefs = getSharedPreferences(getString(R.string.prefs_codigo), Context.MODE_PRIVATE)
        val codigo = prefs.getString("codigo", null)

        if (codigo != null) {
//            layoutHome.visibility = View.INVISIBLE
            showMain(codigo)
        }
    }
    private fun cerrarSesion(){
        val prefs= getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
        FirebaseAuth.getInstance().signOut()
        onBackPressed()
    }

    private fun showAlert(Error: String){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(Error)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
                builder.setTitle("Confirmar")
                builder.setMessage("Seguro que desea Cerrar Sesion?")
                builder.setPositiveButton("Aceptar", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                    cerrarSesion()
                    startActivity(Intent(this, InicioSesionActivity::class.java))
                })
                builder.setNegativeButton("Cancelar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
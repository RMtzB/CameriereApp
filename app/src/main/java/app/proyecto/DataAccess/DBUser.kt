package app.proyecto.DataAccess

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings.Global.getString
import app.proyecto.Models.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import android.widget.Toast
import app.proyecto.R
import com.google.firebase.auth.EmailAuthProvider


object DBUser {
    lateinit var miUsuario: Usuario
    private val lista:ArrayList<Usuario> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var TotalGeneral:Double= 0.0
    var i:Int=0
    fun subirUsuarioData(){
        db.collection("user").add(
                hashMapOf("CorreoE" to miUsuario.CorreoE,
                        "Nombre" to miUsuario.Nombre)
        )
    }

    fun actualizarUsuarioData(nombre:String,correo:String,c:Context) {
        lateinit var aux:String
        db.collection("user").get().addOnCompleteListener() {
            if (it.isSuccessful) {
                lista.clear()
                for (documentos in it.result!!) {
                    if (documentos.getString("CorreoE").equals(miUsuario.CorreoE))
                        aux=documentos.id
                }

                if (miUsuario.Nombre != nombre) {
                    //Se cambiara correo y contraseña
                    if (miUsuario.CorreoE != correo) {
                        miUsuario = Usuario(nombre, correo)
                        cambiarDato(2,aux)
                        cambiarCorreoUser(c)

                    } else {
                        //Cambiar solo nombre
                        miUsuario.Nombre = nombre
                        cambiarDato(0,aux)
                    }
                } else
                    if (miUsuario.CorreoE != correo) {
                        //Cambiar solo Correo
                        miUsuario.CorreoE = correo
                        cambiarDato(1,aux)
                        cambiarCorreoUser(c)
                    }
            }
        }
    }
    fun obtenerUsuario(correo:String, c:Context){
        for (Usu in lista){
            if(Usu.CorreoE==correo) {
                miUsuario = Usuario(Usu.Nombre, Usu.CorreoE)

                break
            }
        }
        lista.clear()
    }
    fun iniciarLista(){
        db.collection("user").get().addOnCompleteListener() {
            if(it.isSuccessful){
                lista.clear()
                for(documentos in it.result!!){
                    val name = documentos.getString("Nombre")
                    val aux=documentos.getString("CorreoE")
                    if (name!=null && aux!=null)
                        lista.add(Usuario(name,aux))
                }
            }
        }

    }
    fun reauth(contra:String,c: Context){

        val user = Firebase.auth.currentUser!!
        val credential = EmailAuthProvider
                .getCredential(miUsuario.CorreoE, contra)

        user.reauthenticate(credential)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                        Toast.makeText(c,"re auth",Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(c,"no se pudo auth",Toast.LENGTH_SHORT).show()
                }
    }
    fun eliminarCuenta(c:Context){
        val user = Firebase.auth.currentUser!!
        user.delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(c,"Cuenta eliominada",Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(c,"No se pudo eliminar la cuenta",Toast.LENGTH_SHORT).show()
                }
    }
    fun eliminarDatosCuenta() {
        lateinit var aux: String
        db.collection("user").get().addOnCompleteListener() {
            if (it.isSuccessful) {
                for (documentos in it.result!!) {
                    if (documentos.getString("CorreoE").equals(miUsuario.CorreoE))
                        aux = documentos.id
                }
            }
            db.collection("user").document(aux)
                    .delete()
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully deleted!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }

        }
    }
    fun cambiarContra(newPassword: String){
        val user = Firebase.auth.currentUser
        user!!.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Cambiar", "User password updated.")
                    }
                }
    }
    /**
     * 0 para nombre
     *1 para correo
     */
    fun cambiarDato(dato:Int , id:String){
        val usuario = db.collection("user").document(id)
        if(dato==0)
        usuario.update("Nombre", miUsuario.Nombre)
               .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
               .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        else if(dato==1)
            usuario.update("CorreoE", miUsuario.CorreoE)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        else {
            usuario.update("Nombre", miUsuario.Nombre)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
            usuario.update("CorreoE", miUsuario.CorreoE)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }
    fun cambiarCorreoUser(c:Context){
        val user = Firebase.auth.currentUser
        user!!.updateEmail(miUsuario.CorreoE)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        Log.w(TAG, "Error adding document")

                }
    }
    fun guardarLocal(c:Context) {
        val prefs:SharedPreferences.Editor = c.getSharedPreferences(c.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.putString("name", miUsuario.Nombre)
        prefs.putString("email", miUsuario.CorreoE)
        prefs.apply()
    }
    fun leerLocal(c:Context):Boolean{
        val prefs = c.getSharedPreferences(c.getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val name = prefs.getString("name", null)
        if (email != null) {
            miUsuario=Usuario(name?:"",email?:"")
            return true
        }
        return false

    }
    fun borrarLocal(c:Context){
        val prefs= c.getSharedPreferences(c.getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        prefs.clear()
        prefs.apply()
    }

}
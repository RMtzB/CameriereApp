package app.proyecto.DataAccess

import android.content.Context
import app.proyecto.Models.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider


object DBFirebase {
    lateinit var miUsuario: Usuario
    private val lista:ArrayList<Usuario> = ArrayList()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun subirUsuarioData(){
        db.collection("user").document(miUsuario.CorreoE).set(
                hashMapOf("CorreoE" to miUsuario.CorreoE,
                        "Nombre" to miUsuario.Nombre)
        )
    }

    fun actualizarUsuarioData(user:Usuario){

    }


    fun optenerUsuario(correo:String,c:Context){
        for (Usu in lista){
            if(Usu.CorreoE==correo) {
                miUsuario = Usuario(Usu.Nombre, Usu.CorreoE)

                break
            }
        }
        lista.clear()
    }

    fun optenerUsuario(u:Usuario){
        miUsuario=u
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
        user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(c,"Cuenta eliominada",Toast.LENGTH_SHORT).show()
                    }
                    else
                        Toast.makeText(c,"No se pudo eliminar la cuenta",Toast.LENGTH_SHORT).show()
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






}
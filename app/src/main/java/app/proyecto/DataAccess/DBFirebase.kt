package app.proyecto.DataAccess

import app.proyecto.Models.Usuario
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class DBFirebase {
    lateinit var miUsuario: Usuario
    val user = Firebase.auth.currentUser
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    constructor(user:Usuario){
        this.miUsuario=user
    }

    fun subirUsuarioAuth(correo:String,contra:String):Boolean{
        return true
    }

    fun actualizarUsuarioAuth(correo:String,contra:String){

    }
    fun iniciarSesion(correo: String,contra:String){

    }


    fun subirUsuarioData(use:Usuario){

    }

    fun actualizarUsuarioData(user:Usuario){

    }

    private fun obtenerUsuario(){

    }




}
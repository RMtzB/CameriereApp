package app.proyecto.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import app.proyecto.Adapters.AdapterPromo
import app.proyecto.Models.Promo
import app.proyecto.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_promociones.*
import java.net.URI

class PromocionesActivity : AppCompatActivity() {
    private val  lista: ArrayList<Promo> = ArrayList()
    private val adapterPromo= AdapterPromo(lista,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promociones)

        rv_promos.layoutManager = GridLayoutManager(this,2)
        rv_promos.adapter=adapterPromo
        setup()

        val db : FirebaseFirestore= FirebaseFirestore.getInstance()
        db.collection("Promos").get().addOnCompleteListener(){
            if(it.isSuccessful){
                lista.clear()
                for(documentos in it.result!!){
                    val URL =documentos.getString("URL")
                    if(URL!=null)
                        lista.add(Promo(URL))
                }
                adapterPromo.notifyDataSetChanged()
            }
        }
    }

    private fun setup(){
        btnPromo_home.setOnClickListener(){
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

}
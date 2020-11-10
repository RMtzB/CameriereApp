package app.proyecto.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collection;

import app.proyecto.Adapters.AdapterCheckBox;
import app.proyecto.Adapters.CustomAdapter;
import app.proyecto.R;

public class PagoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference MesaRef = db.collection( "user"); //Cambiar Valores cuando Rulin haga el método
    private TextView textViewData;
    private EditText Total;
    private RadioButton rb_PayPal, rb_Efectivo;
    private CheckBox Todos;
    private int listo = 0;
    int a_cliente [];
    int clientes = 0;

    private ListView L_ListaClientes;
    private AdapterCheckBox adapter;
    private ArrayList<String> S_ListaClientes = new ArrayList<>();

    public PagoActivity() {
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewData = findViewById(R.id.txt_Total);

        rb_PayPal = (RadioButton) findViewById(R.id.rB_PayPal);
        rb_Efectivo = (RadioButton) findViewById(R.id.rB_Efectivo);
        Todos = (CheckBox) findViewById(R.id.Ch_Todos);
        Total = (EditText) findViewById(R.id.txt_Total);

        L_ListaClientes = (ListView) findViewById(R.id.L_ListaClientes);
        L_ListaClientes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        MesaRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    S_ListaClientes.add((String) documentSnapshot.get("Nombre"));

                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        L_ListaClientes.setAdapter(adapter);

        




    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void ActualizaClientes (View view){

    }

    /*private void ObtenTotales (View view){
        MesaRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            String titulo = documentSnapshot.getString(KEY_TITLE);
                            String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            //Map<String, Object> note = documentSnapshot.getData();

                            textViewData.setText("Title: "+ titulo + "\n" + "Description: " + description);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Documento inexistente", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                })

    }*/

    /*public void CambiaTotal (View view){
        a_cliente = new int [4];
        int i_Total = 0;
        for (int i = 0; i< 4; i++)
            a_cliente[i] = i+3;
        if (Cliente1.isChecked())
            i_Total = i_Total + a_cliente[1];
        if (Cliente2.isChecked())
            i_Total = i_Total + a_cliente[2];
        if (Cliente3.isChecked())
            i_Total = i_Total + a_cliente[3];
        if (Cliente4.isChecked())
            i_Total = i_Total + a_cliente[4];
        Total.setText(i_Total);
    }

    public void SeleccionarTodos (View view){
        if (Todos.isChecked()) {
            Cliente1.setChecked(true);
            Cliente2.setChecked(true);
            Cliente3.setChecked(true);
            Cliente4.setChecked(true);
        }else{
            Cliente1.setChecked(false);
            Cliente2.setChecked(false);
            Cliente3.setChecked(false);
            Cliente4.setChecked(false);
        }
    }*/

    public void Confirmar (View view){

        if (clientes > 0) {
            if (rb_PayPal.isChecked() == true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Pago Realizado con éxito", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //startActivity(oPantallaDespedida);
            } else if (rb_Efectivo.isChecked() == true) {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, "Pago confirmado", Toast.LENGTH_LONG).show();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //startActivity(oPantallaDespedida);
            }else{
                Toast.makeText(this, "Seleccione su forma de pago", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
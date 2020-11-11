package app.proyecto.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import app.proyecto.R;
import app.proyecto.databinding.ActivityMainBinding;
import app.proyecto.databinding.ActivityPagoBinding;

public class PagoActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference MesaRef = db.collection( "user"); //Cambiar Valores cuando Rulin haga el método
    private TextView textViewData;
    private EditText Total;
    private RadioButton rb_PayPal, rb_Efectivo;
    private CheckBox Todos;
    private int listo = 0;
    boolean Clientes[];
    int clientes = 0;
    Double i_Total;
    private int i=0;

    private ListView L_ListaClientes;
    private ArrayList<String> S_ListaClientes = new ArrayList<>();
    private ArrayList<Double> T_ListaClientes = new ArrayList<>();
    private AdapterCheckBox adapter = new AdapterCheckBox(S_ListaClientes,this);
    private ActivityPagoBinding PagoGrafico;
    private LinearLayoutManager layoutManager;

    public PagoActivity() {
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        PagoGrafico = ActivityPagoBinding.inflate(getLayoutInflater());
        PagoGrafico.LListaClientes.setLayoutManager(new LinearLayoutManager(this)); // :O
        PagoGrafico.LListaClientes.setAdapter(adapter);

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
                    T_ListaClientes.add((Double) documentSnapshot.get("Total"));
                    i = i+1;
                }
                adapter.notifyDataSetChanged();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Clientes = new boolean[i];
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void ActualizaClientes (View view){

    }

    private void CambiaTotal (View view) {
        i_Total = 0.0;
        if (L_ListaClientes.getSelectedItem() == null){
            Total.setText("");
        }else{
//            Clientes = L_ListaClientes.getCheckedItemPositions(); //DUDA AAAAAAAAAAAAAAAAAA Guardar
            for (int j=0; j<i; j++){
                if (Clientes[j])
                    i_Total = i_Total + T_ListaClientes.get(j);
            }
            Total.setText("" + i_Total);
        }
    }

    public void SeleccionarTodos (View view){
        int j;
        if (Todos.isChecked()) {
            for (j = 0; j<i; j++)
                L_ListaClientes.setItemChecked(j, true);
            clientes = i;
        }else{
            for (j = 0; j<i; j++)
                L_ListaClientes.setItemChecked(j, false);
            clientes = 0;
        }
        CambiaTotal(view);
    }

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
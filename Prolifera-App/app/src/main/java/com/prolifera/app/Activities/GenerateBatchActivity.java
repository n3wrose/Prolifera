package com.prolifera.app.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.prolifera.app.Activities.GenMainActivity.READ_CODE_REQUEST;

public class GenerateBatchActivity extends AppCompatActivity {

    private RequestQueue rq;
    private TextView tvUserLoggedGenerateBatch;
    private EditText etDescriptionGenerateBatch, etNumberGenerateBatch, etNumberSubGenerateBatch;
    private Button btnGenerateBatch;
    private ListView lstParentGenerateBatch;
    private Spinner spnEtapa;
    private Usuario usuario;
    private ProcessoDTO processo;
    private List<EtapaDTO> etapas;
    private List<Long> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_batch);

        //component attribution
        tvUserLoggedGenerateBatch = findViewById(R.id.tvUserLoggedGenerateBatch);
        etNumberGenerateBatch = findViewById(R.id.etNumberGenerateBatch);
        spnEtapa = findViewById(R.id.spnEtapa);
        etDescriptionGenerateBatch = findViewById(R.id.etDescriptionGenerateBatch);
        etNumberSubGenerateBatch = findViewById(R.id.etNumberSubGenerateBatch);
        btnGenerateBatch = findViewById(R.id.btnGenerateBatch);
        lstParentGenerateBatch = findViewById(R.id.lstParentGenerateBatch);

        // intent parameters retrieval
        usuario = (Usuario)getIntent().getExtras().get("usuario");
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        ids = new ArrayList<>();
        tvUserLoggedGenerateBatch.setText("Logado como: " + usuario.getNome());
        etDescriptionGenerateBatch.bringToFront();
        etNumberGenerateBatch.bringToFront();
        etNumberSubGenerateBatch.bringToFront();
        spnEtapa.bringToFront();
        btnGenerateBatch.setEnabled(false);
        fillEtapaSpinner();

        lstParentGenerateBatch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ids.size() > 0) {
                    ids.remove(i);
                    updateParentList();
                }
                return false;
            }
        });
    }

    public void gerarLote(View view) {

        final Amostra amostra = new Amostra();
        final int sampleNumber = Integer.parseInt(etNumberGenerateBatch.getText().toString()),
                subSampleNumber = Integer.parseInt(etNumberSubGenerateBatch.getText().toString());
        btnGenerateBatch.setEnabled(false);
        Toast.makeText(this, "Enviando dados . . .", Toast.LENGTH_SHORT).show();

        amostra.setDescricao(etDescriptionGenerateBatch.getText().toString());
        amostra.setIdEtapa(etapas.get(spnEtapa.getSelectedItemPosition()).getIdEtapa());
        amostra.setUsuario(usuario.getLogin());
        String url =  getResources().getString(R.string.server_address) + "batch_amostra";
        final ArrayAdapter<String> amostrasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);


        JsonArrayRequest loteRequest = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                if (response.equals(null)) return;
                ArrayList<Bitmap> qrcodes = new ArrayList<>();
                for (int i=0; i<response.length(); i++)
                try {
                        String id = response.getString(i);
                        //Bitmap bitmap = new QRGEncoder(id,null, QRGContents.Type.TEXT, 100).encodeAsBitmap();
                       // qrcodes.add(bitmap);
                    } catch (Exception e) { e.printStackTrace(); }
                //Bitmap result = combineImageIntoOneFlexWidth(qrcodes);
                //saveImage(result,"codeslist"+ new SimpleDateFormat("HH-mm_dd-MM-yyyy").format(new Date()));
                Toast.makeText(getApplicationContext(), "Dados salvos com sucesso!", Toast.LENGTH_SHORT).show();
                finish();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GenerateBatchActivity.this, "Erro de conexão, ver log.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                String json = "{ \"amostra\": "+amostra.fillPayload() + ", " +
                        "\"sample\": "+ sampleNumber + ", \"subsample\": "+ subSampleNumber + ", " +
                        " \"idPais\": [ ";
                for (long id : ids)
                    json = json + id + ",";
                if (ids.size()>0)
                        json = json.substring(0,json.length()-1);
                json = json + " ] } ";
                return json.getBytes();
            }
        };
        rq.add(loteRequest);
    }

    private void fillEtapaSpinner() {
        etapas = new ArrayList<>();
        final ArrayAdapter<String> etapasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);

        spnEtapa.setAdapter(etapasAdapter);

        //setting up ArrayRequest
        String url = getResources().getString(R.string.server_address) + "etapa/started/"+processo.getIdProcesso();
        JsonArrayRequest etapasRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (!response.equals(null))
                        for (int i = 0; i<response.length(); i++) {
                            try {
                                EtapaDTO edto = JsonParser.parseEtapa(response.getJSONObject(i));
                                etapas.add(edto);
                                etapasAdapter.add(edto.getCodigo()+" - "+edto.getNome());
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                        if (etapas.size() == 0) {
                            etapasAdapter.add("Não há etapas em andamento!");

                            btnGenerateBatch.setBackgroundColor(Color.parseColor("#C35F5050"));
                        }
                        else
                            btnGenerateBatch.setEnabled(true);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Houve um erro de conexão, tente novamente.",Toast.LENGTH_SHORT);
                        etapasAdapter.add("Não há etapas!");
                        btnGenerateBatch.setEnabled(false);
                    }
                });
        rq.add(etapasRequest);
    }

    public void buscarAmostra(View view) {

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Apontar para o QR code contendo o número da amostra");
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setCameraId(0);
        integrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        String qrcode;
        if (requestCode == READ_CODE_REQUEST && resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null ) {
                qrcode = result.getContents();
                try
                {
                    ids.add(Long.parseLong(qrcode));
                    updateParentList();
                } catch (Exception e) { return; }

            }
            else
                enterIdManual();
        }
    }

    private void updateParentList() {
        final ArrayAdapter<String> parentAdapter= new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);
        lstParentGenerateBatch.setAdapter(parentAdapter);
        for (long id : ids)
            parentAdapter.add("Amostra id "+id);
        if (ids.size() == 0)
            parentAdapter.add("Nenhum pai será inserido.");
    }

    private void enterIdManual() {
        //humble builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Código não identificado. Deseja digitar o Id da amostra?");
        final EditText input = new EditText(getApplicationContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Id da Amostra");
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("Buscar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ids.add(Long.parseLong(input.getText().toString()));
                updateParentList();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private Bitmap combineImageIntoOneFlexWidth(ArrayList<Bitmap> bitmap) {
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                h = bitmap.get(i).getHeight() > bitmap.get(i + 1).getHeight() ? bitmap.get(i).getHeight() : bitmap.get(i + 1).getHeight();
            }
            w += bitmap.get(i).getWidth();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.e("HTML", "Combine: " + i + "/" + bitmap.size() + 1);

            top = (i == 0 ? 0 : top + bitmap.get(i).getWidth());
            //attributes 1:bitmap,2:width that starts drawing,3:height that starts drawing
            canvas.drawBitmap(bitmap.get(i), top, 0f, null);
        }
        return temp;
    }

    private void saveImage(Bitmap finalBitmap, String image_name) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        myDir.mkdirs();
        String fname = "Image-" + image_name+ ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        Log.i("LOAD", root + fname);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

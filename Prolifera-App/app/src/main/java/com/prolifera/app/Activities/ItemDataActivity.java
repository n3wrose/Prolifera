package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prolifera.R;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraQualificadorDTO;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.AmostraQuantificadorDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ItemDataActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 5;
    private RequestQueue rq;
    private Button btnDestruirAmostra, btnTirarFoto;
    private TextView tvItemNameItemData, tvEtapaItemData, tvUserLoggedItemData;
    private ListView lstMeasurement, lstParentItemData;
    private AmostraDTO amostra;
    private Usuario usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_data);

        tvItemNameItemData = findViewById(R.id.tvItemNameItemData);
        tvEtapaItemData = findViewById(R.id.tvEtapaItemData);
        tvUserLoggedItemData = findViewById(R.id.tvUserLoggedItemData);
        lstMeasurement = findViewById(R.id.lstMeasurement);
        lstParentItemData = findViewById(R.id.lstParentItemData);
        btnDestruirAmostra = findViewById(R.id.btnDestruirAmostra);
        btnTirarFoto = findViewById(R.id.btnTirarFoto);

        usuario = (Usuario)getIntent().getExtras().get("usuario");
        amostra = (AmostraDTO)getIntent().getExtras().get("amostra");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvItemNameItemData.setText("Amostra: " + amostra.getNome());
        tvEtapaItemData.setText(amostra.getEtapa().getCodigo()+ " - "+amostra.getEtapa().getNome());
        tvUserLoggedItemData.setText("Logado como: "+usuario.getNome());


        if (!amostra.getDataFim().equals("null")){
            disableButton();
        }

        updateMetricsList();
    }

    private void updateParentList() {

    }

    private void updateMetricsList() {
        final ArrayAdapter<String> metricsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lstMeasurement.setAdapter(metricsAdapter);
        for (AmostraQuantificadorDTO am : amostra.getMedidas())
            metricsAdapter.add(am.getTexto());
        for (AmostraQualificadorDTO ac : amostra.getClassificacoes())
            metricsAdapter.add(ac.getTexto());
    }

    public void destruir(View view) {
        btnDestruirAmostra.setEnabled(false);
        new AlertDialog.Builder(this)
                .setTitle("Destruir Amostra")
                .setMessage("Tem certeza que deseja destruir esta amostra?")
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            final Amostra a = new Amostra();
                            a.setNome(amostra.getNome());
                            a.setUsuario(amostra.getUsuario().getLogin());
                            a.setIdEtapa(amostra.getEtapa().getIdEtapa());
                            a.setDescricao(amostra.getDescricao());
                            a.setDataCriacao(new SimpleDateFormat("yyyy-MM-dd").parse(amostra.getDataCriacao()));
                            a.setDataFim(new Date());
                            a.setIdAmostra(amostra.getIdAmostra());
                            String url = getResources().getString(R.string.server_address) + "amostra";
                            JsonObjectRequest destroySampleRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    btnDestruirAmostra.setEnabled(true);
                                    if (response.equals(null)) return;
                                    amostra = JsonParser.parseAmostra(response);
                                    if (amostra != null) {
                                        disableButton();
                                    } else
                                        finish();

                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    btnDestruirAmostra.setEnabled(true);
                                }

                            }) {

                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                public byte[] getBody() {
                                    return a.fillPayload().getBytes();
                                }
                            };

                            rq.add(destroySampleRequest);
                        } catch (Exception e) { Log.e("error",e.getMessage());
                        }


                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton("Não", null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        btnDestruirAmostra.setEnabled(true);

    }

    public void tirarFoto(View view) {
        showFileChooser();
    }


    private void disableButton() {
        btnDestruirAmostra.setText("Destruida em "+amostra.getDataFim());
        btnDestruirAmostra.setEnabled(false);
        btnTirarFoto.setEnabled(false);
        btnTirarFoto.setBackgroundColor(Color.parseColor("#C35F5050"));
        btnDestruirAmostra.setTextSize(20);
        btnDestruirAmostra.setBackgroundColor(Color.parseColor("#C35F5050"));
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                //encoding image to string
                final String image = getStringImage(lastBitmap);
                Log.d("test",image);
                //passing the image to volley
                String url = getResources().getString(R.string.server_address) + "amostra/picture/"+amostra.getIdAmostra();
                StringRequest uploadImageRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("teste", response);
                            }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ItemDataActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
                        Log.e("teste",error.getMessage());

                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        String body = "{ \"imagem\": \""+image+"\", \"id\": "+amostra.getIdAmostra()+" }";
                        return body.getBytes();
                    }
                };
                rq.add(uploadImageRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
}

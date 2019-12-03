package com.prolifera.app.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.prolifera.R;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.ProcessoDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenMainActivity extends AppCompatActivity {

    private RequestQueue rq;
    private ProcessoDTO processo ;
    private Usuario usuario;
    private Button btnGerarLote, btnVerGrafo, btnEtapas, btnBuscarAmostra;
    private TextView tvGenNameGenMain, tvUserLoggedGenMain;
    private ImageView imgTesteQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_main);

        //component attribution
        tvGenNameGenMain = findViewById(R.id.tvGenNameGenMain);
        tvUserLoggedGenMain = findViewById(R.id.tvUserLoggedGenMain);
        btnGerarLote = findViewById(R.id.btnGerarLote);
        btnVerGrafo = findViewById(R.id.btnVerGrafo);
        btnEtapas = findViewById(R.id.btnEtapas);
        btnBuscarAmostra = findViewById(R.id.btnBuscarAmostra);

        // intent parameters retrieval
        processo = (ProcessoDTO)getIntent().getExtras().get("processo");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        //RequestQueue instatiation
        rq = RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        tvGenNameGenMain.setText(processo.getLote());
        tvUserLoggedGenMain.setText(("Logado como: "+usuario.getNome()));
    }

    public void etapa(View view) {
        Intent intent = new Intent(GenMainActivity.this, ManageEtapasActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void gerarLote(View view) {
        Intent intent = new Intent(GenMainActivity.this, GenerateBatchActivity.class);
        intent.putExtra("processo",processo);
        intent.putExtra("usuario",usuario);
        startActivity(intent);
    }

    public void verGrafo(View view) {

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
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null ) {
            qrcode = result.getContents();

           /* Log.i("teste","teste");
            Bitmap basicQR = null, fancyQR = null, logo;
            try {
                QRGEncoder qrgEncoder = new QRGEncoder(qrcode, null, QRGContents.Type.TEXT, 500);
                basicQR = qrgEncoder.encodeAsBitmap();Log.i("teste","teste");
            }
            catch (WriterException e) {
                Log.i("error","WriterException "+e.getMessage());
            }
            logo = BitmapFactory.decodeResource(getResources(), R.drawable.qr_image);
*/

            /*float scaleWidth = ((float) 100) / logo.getWidth();
            float scaleHeight = ((float) 100) / logo.getHeight();

            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            logo = Bitmap.createBitmap(
                    logo, 0, 0, 100, 100, matrix, false);

            Log.i("teste","teste");/*/
           /* fancyQR = Bitmap.createBitmap(basicQR.getWidth(), basicQR.getHeight(), basicQR.getConfig());
            Canvas canvas = new Canvas(fancyQR);
            canvas.drawBitmap(basicQR, 0f, 0f, null);
            canvas.drawBitmap(logo, (basicQR.getWidth()-logo.getWidth())/2, (basicQR.getHeight()-logo.getHeight())/2, null);

            imgTesteQR.setImageBitmap(fancyQR);*/
            String url = getResources().getString(R.string.server_address) + "amostra/"+qrcode;
            JsonObjectRequest loginRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                AmostraDTO adto = JsonParser.parseAmostra(response);
                                if (adto != null) {
                                    Intent intent = new Intent(GenMainActivity.this, ItemDataActivity.class);
                                    intent.putExtra("amostra",adto);
                                    intent.putExtra("usuario",usuario);
                                    startActivity(intent);
                                }
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("ERROR",error.getMessage());

                        }
                    });
            rq.add(loginRequest);
        }
    }
}

package com.prolifera.app.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prolifera.R;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.RequestQueueSingleton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SampleMainFragment extends Fragment {

    private AmostraDTO amostra;
    private Button btnDestruirAmostra, btnTirarFoto, btnGallery, btnPrev, btnNext;
    private RequestQueue rq;
    private ImageView imgSampleItemData;
    private List<Bitmap> imageList ;
    private int imageIndex = 0;
    static final int PICK_IMAGE_REQUEST = 5;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sample_main, container, false);

        btnDestruirAmostra = view.findViewById(R.id.btnDestruirAmostra);
        btnTirarFoto = view.findViewById(R.id.btnSampleCamera);
        btnGallery = view.findViewById(R.id.btnPhotoGallery);
        imgSampleItemData = view.findViewById(R.id.imgSampleItemData);
        btnNext = view.findViewById(R.id.btnPhotoNext);
        btnPrev = view.findViewById(R.id.btnPhotoPrevious);


        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        amostra = ((ItemDataActivity)getActivity()).amostra;
        imageList = new ArrayList<>();

        if (!amostra.getDataFim().equals("null")){
            disableButton();
        }

        btnPrev.setEnabled(false);
        if (imageList.size() == 0)
            btnNext.setEnabled(false);

        btnDestruirAmostra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destruir();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });

        btnTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageView(-1);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateImageView(1);
            }
        });



        return view;
    }

    private void disableButton() {
        btnDestruirAmostra.setText("Destruida em "+amostra.getDataFim());
        btnDestruirAmostra.setEnabled(false);
        btnTirarFoto.setEnabled(false);
        btnGallery.setEnabled(false);
        btnTirarFoto.setBackgroundColor(Color.parseColor("#C35F5050"));
        btnDestruirAmostra.setTextSize(20);
        btnDestruirAmostra.setBackgroundColor(Color.parseColor("#C35F5050"));
    }

    public void destruir() {
        btnDestruirAmostra.setEnabled(false);
        new AlertDialog.Builder(getContext())
                .setTitle("Destruir Amostra")
                .setMessage("Tem certeza que deseja destruir esta amostra?")
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
                                    }
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

    public void pickFromGallery() {
        showFileChooser();
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

    private void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
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
                                Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
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
               // rq.add(uploadImageRequest);
                imageList.add(bitmap);
                imageIndex = imageList.size()-1;
                updateImageView(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageList.add(imageBitmap);
            imageIndex = imageList.size()-1;
            updateImageView(0);
        }
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }

    private void updateImageView(int direction) {

        imageIndex += direction;
        imgSampleItemData.setImageBitmap(imageList.get(imageIndex));
        if (imageIndex == 0)
            btnPrev.setEnabled(false);
        else
            btnPrev.setEnabled(true);
        if(imageIndex == imageList.size()-1)
            btnNext.setEnabled(false);
        else
            btnNext.setEnabled(true);



    }
}

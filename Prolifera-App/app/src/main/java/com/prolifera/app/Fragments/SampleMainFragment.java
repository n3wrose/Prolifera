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
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.prolifera.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.prolifera.app.Activities.CreateEtapaActivity;
import com.prolifera.app.Activities.EtapaMainActivity;
import com.prolifera.app.Activities.GenMainActivity;
import com.prolifera.app.Activities.ItemDataActivity;
import com.prolifera.app.JsonParser;
import com.prolifera.app.Model.DB.Amostra;
import com.prolifera.app.Model.DB.AmostraPai;
import com.prolifera.app.Model.DB.Usuario;
import com.prolifera.app.Model.DTO.AmostraDTO;
import com.prolifera.app.Model.DTO.EtapaDTO;
import com.prolifera.app.MultipartRequest;
import com.prolifera.app.RequestQueueSingleton;
import com.prolifera.app.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SampleMainFragment extends Fragment {

    private AmostraDTO amostra;
    private Button btnDestruirAmostra, btnTirarFoto, btnGallery, btnPrev, btnNext;
    private RequestQueue rq;
    private Button btnAdicionarPai;
    private ListView lstParents;
    private ImageView imgSampleItemData;
    private TextView tvSampleDescription;
    private List<Bitmap> imageList ;
    private Usuario usuario;
    private int imageIndex = 0;
    static final int PICK_IMAGE_REQUEST = 5;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int READ_CODE_REQUEST = 49374;

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
        lstParents = view.findViewById(R.id.lstParentItemData);
        btnAdicionarPai=view.findViewById(R.id.btnAdicionarPai);
        tvSampleDescription = view.findViewById(R.id.tvSampleDescription);

        rq = RequestQueueSingleton.getInstance(getContext()).getRequestQueue();

        amostra = ((ItemDataActivity)getActivity()).amostra;
        usuario = ((ItemDataActivity)getActivity()).usuario;
        imageList = new ArrayList<>();

        String descricao;
        if (!amostra.getDescricao().equals("null"))
           descricao = "Descrição:" + amostra.getDescricao();
        else
            descricao = "Essa amostra não possui descrição.";

        if (!amostra.getDataFim().equals("null"))
            disableButton();
        else
        {
            if (!amostra.getDescricao().equals("null"))
                descricao = descricao + " (clique longo para editar)";
            else
                descricao = " Clique longo para adicionar descrição";
        }

        tvSampleDescription.setText(descricao);
        tvSampleDescription.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (amostra.getDataFim().equals("null"))
                    enterDescriptionDialog();
                return false;
            }
        });

        btnPrev.setEnabled(false);
        if (imageList.size() == 0)
            btnNext.setEnabled(false);

        lstParents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                long idAmostra = amostra.getIdPais().get(i);

                String url = getResources().getString(R.string.server_address) + "amostra/"+idAmostra;
                JsonObjectRequest loginRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                if (response != null) {
                                    AmostraDTO adto = JsonParser.parseAmostra(response);
                                    if (adto != null) {
                                        Intent intent = new Intent(getActivity(), ItemDataActivity.class);
                                        intent.putExtra("amostra",adto);
                                        intent.putExtra("usuario",usuario);
                                        startActivity(intent);
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("ERROR",""+error.getMessage());

                            }
                        });
                rq.add(loginRequest);

                return false;
            }
        });

        btnAdicionarPai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addParent();
            }
        });

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

        updateParentList();

        //loadImage();

        return view;
    }

    private void loadImage() {
        String url = getResources().getString(R.string.server_address)+"picture/"+amostra.getIdAmostra();
        ImageRequest imageRequest = new ImageRequest(
                url, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        imageList.add(response);
                        imageIndex = imageList.size()-1;
                        updateImageView(0);
                    }
                },
                0, // Image width
                0, // Image height
                ImageView.ScaleType.CENTER_CROP, // Image scale type
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        rq.add(imageRequest);
    }

    private void disableButton() {
        btnDestruirAmostra.setText("Destruida em "+amostra.getDataFim());
        btnDestruirAmostra.setEnabled(false);
        btnTirarFoto.setEnabled(false);
        btnGallery.setEnabled(false);
        btnTirarFoto.setBackgroundColor(Color.parseColor("#C35F5050"));
        btnDestruirAmostra.setTextSize(20);
        btnDestruirAmostra.setBackgroundColor(Color.parseColor("#C35F5050"));
        btnAdicionarPai.setTextSize(20);
        btnAdicionarPai.setBackgroundColor(Color.parseColor("#C35F5050"));
        btnAdicionarPai.setEnabled(false);
    }

    public void addParent() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Digitar o id da amostra geradora:");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"Enviando dados . . . ",Toast.LENGTH_SHORT).show();
                try {
                    final AmostraPai ap = new AmostraPai();
                    ap.setIdPai(Long.parseLong(input.getText().toString()));
                    ap.setIdFilho(amostra.getIdAmostra());
                    String url = getResources().getString(R.string.server_address)+"/amostra_pai";
                    JsonObjectRequest destroySampleRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                AmostraPai amostraPai = JsonParser.parseAP(response);
                                if (amostraPai != null) {
                                    Toast.makeText(getContext(), "Dados salvos!", Toast.LENGTH_SHORT).show();
                                    List<Long> pais = amostra.getIdPais();
                                    pais.add(amostraPai.getIdPai());
                                    ((ItemDataActivity)getActivity()).amostra = amostra;
                                    ((ItemDataActivity)getActivity()).attachFragments();

                                }
                                else
                                    Toast.makeText(getContext(),"Amostra salva corrompida!",Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(getContext(),"Erro no servidor, tente novamente.",Toast.LENGTH_SHORT).show();
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
                            return ap.fillPayload().getBytes();
                        }
                    };

                    rq.add(destroySampleRequest);
                } catch (Exception e) { Log.e("error",e.getMessage());
                    Toast.makeText(getContext(),"Erro - Amostra corrompida",Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


/*
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Apontar para o QR code contendo o número da amostra");
        integrator.setBarcodeImageEnabled(false);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setCameraId(0);
        integrator.initiateScan();*/

    }

    public void destruir() {
        btnDestruirAmostra.setEnabled(false);
        new AlertDialog.Builder(getContext())
                .setTitle("Destruir Amostra")
                .setMessage("Tem certeza que deseja destruir esta amostra?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Enviando dados . . . ",Toast.LENGTH_SHORT).show();
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
                                    if (response.equals(null)) {
                                        Toast.makeText(getContext(), "Erro no servidor, tente novamente.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    amostra = JsonParser.parseAmostra(response);
                                    if (amostra != null) {
                                        disableButton();
                                        Toast.makeText(getContext(),"Amostra destruída em "+amostra.getDataFim(),Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getContext(),"Amostra salva corrompida!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(getContext(),"Erro de amostra corrompida.",Toast.LENGTH_SHORT).show();
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
        Log.i("teste","ID" + resultCode);
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
                                Log.e("teste",""+ error.getMessage());

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
                imageList.add(bitmap);
                imageIndex = imageList.size()-1;
                updateImageView(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageList.add(imageBitmap);
            imageIndex = imageList.size()-1;
            updateImageView(0);
        }

        if (requestCode == READ_CODE_REQUEST && resultCode == RESULT_OK){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null ) {
                final String id = result.getContents();
                Log.i("teste","id = "+id);
                new AlertDialog.Builder(getContext())
                    .setTitle("Adicionar Parentesco")
                    .setMessage("Deseja adicionar a amostrade id"+id+" como pai da amostra atual?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            final AmostraPai ap = new AmostraPai();
                            try {
                                ap.setIdFilho(amostra.getIdAmostra());
                                ap.setIdPai(Long.parseLong(id));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Problema na leitura de QR Code", Toast.LENGTH_SHORT);
                            }
                            String url = getResources().getString(R.string.server_address) + "amostra_pai";
                            JsonObjectRequest newParentRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    btnAdicionarPai.setEnabled(true);
                                    AmostraPai amostraPai = new AmostraPai();
                                    if (response != null) {
                                        amostraPai = JsonParser.parseAP(response);
                                        if (amostraPai != null) {
                                            Toast.makeText(getContext(), "Pai adicionado!", Toast.LENGTH_SHORT);
                                            List<Long> pais = amostra.getIdPais();
                                            pais.add(amostraPai.getIdPai());
                                            amostra.setIdPais(pais);
                                            updateParentList();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Não foi possível enviar dados!",Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "Erro ao enviar dados: "+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                    btnAdicionarPai.setEnabled(true);
                                }

                            }){

                                @Override
                                public String getBodyContentType() {
                                    return "application/json; charset=utf-8";
                                }

                                @Override
                                public byte[] getBody()  {
                                    return ap.fillPayload().getBytes();
                                }
                            };

                            rq.add(newParentRequest);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton("Não", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
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

    private void updateParentList() {
        final ArrayAdapter<String> parentAdapter= new ArrayAdapter<>(this.getContext(), android.R.layout.simple_list_item_1);
        lstParents.setAdapter(parentAdapter);
        if (amostra.getIdPais() != null) {
            for (long id : amostra.getIdPais())
                parentAdapter.add("Amostra id "+id);
        }
        else
            parentAdapter.add("Não há pai cadastrado.");

    }

    private void sendImageRequest() {
    }

    private void enterDescriptionDialog() {

        //humble builder
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Inserir descrição da amostra:");
        final EditText input = new EditText(getContext());
        if (!amostra.getDescricao().equals("null"))
            input.setText(amostra.getDescricao());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                amostra.setDescricao("Descrição:"+input.getText().toString());
                tvSampleDescription.setText(amostra.getDescricao() + " (clique longo para editar)");
                Toast.makeText(getContext(),"Enviando dados . . . ",Toast.LENGTH_SHORT).show();
                try {
                    final Amostra a = new Amostra();
                    a.setNome(amostra.getNome());
                    a.setUsuario(amostra.getUsuario().getLogin());
                    a.setIdEtapa(amostra.getEtapa().getIdEtapa());
                    a.setDescricao(amostra.getDescricao());
                    a.setDataCriacao(Utils.stringToDate(amostra.getDataCriacao()));
                    a.setDataFim(Utils.stringToDate(amostra.getDataFim()));
                    a.setIdAmostra(amostra.getIdAmostra());
                    String url = getResources().getString(R.string.server_address) + "amostra";
                    JsonObjectRequest destroySampleRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response != null) {
                                AmostraDTO adto = JsonParser.parseAmostra(response);
                                if (adto != null)
                                    Toast.makeText(getContext(), "Amostra salva!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(getContext(),"Amostra salva corrompida!",Toast.LENGTH_SHORT).show();

                            } else
                                Toast.makeText(getContext(),"Erro no servidor, tente novamente.",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(),"Erro - Amostra corrompida",Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

        //fancy builder
        /*AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Digite a descrição da amostra:");
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_password, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();*/
    }
}


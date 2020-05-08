package com.giro.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import mabel_tech.com.scanovate_demo.ScanovateHandler;
import mabel_tech.com.scanovate_demo.ScanovateSdk;
import mabel_tech.com.scanovate_demo.model.CloseResponse;
import mabel_tech.com.scanovate_demo.network.ApiHelper;
import mabel_tech.com.scanovate_demo.network.RetrofitClient;


import android.view.Window;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
 
import okhttp3.ResponseBody;
/**
 * This class echoes a string called from JavaScript.
 */
public class LuncherActivity extends Activity {
	
	 String numberIdentification="";
    Boolean verification;
    Context contect;
	ImageView imageView5;
	
	private ProgressDialog progress;
    Button btn_enrolar;
    Button btn_verificar;
    TextView tv;
	 EditText numberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String package_name = getApplication().getPackageName();
        setContentView(getApplication().getResources().getIdentifier("activity_luncher", "layout", package_name));
		
		
		progress = new ProgressDialog(this);
        progress.setTitle("Procesando estado");
        progress.setMessage("Por favor espere un momento...");
        progress.setIndeterminate(true);
        progress.setCanceledOnTouchOutside(false);
		
		  contect = this;
        verification = false;
		 //capture();
		 
		 imageView5 = findViewById(getResources().getIdentifier("imageView5","id",getPackageName()));
		 btn_enrolar = findViewById(getResources().getIdentifier("btn_enroll","id",getPackageName()));
		 btn_verificar = findViewById(getResources().getIdentifier("btn_verification","id",getPackageName()));
		 tv = findViewById(getResources().getIdentifier("textView","id",getPackageName()));
		 numberId = findViewById(getResources().getIdentifier("numberId","id",getPackageName()));
		 
		 Resources activityRes = this.getResources();
		int backResId = activityRes.getIdentifier("gyf_logo", "drawable",  getPackageName());
		
		Drawable backIcon = activityRes.getDrawable(backResId);
		if (Build.VERSION.SDK_INT >= 16)
		imageView5.setBackground(null);
		else
			imageView5.setBackgroundDrawable(null);
		imageView5.setImageDrawable(backIcon);
		
		
		
		  btn_enrolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
		
		
		btn_verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!btn_verificar.getText().equals("Enviar")){
                numberId.setVisibility(View.VISIBLE);
                btn_enrolar.setVisibility(View.INVISIBLE);
                btn_verificar.setText("Enviar");
                }else{
                    if(numberId.getText().length()!=0){
                        numberIdentification = numberId.getText().toString();
                        verification = true;
                        numberId.setVisibility(View.INVISIBLE);
                        btn_verificar.setVisibility(View.INVISIBLE);
                        capture();

                    }

                }
            }
        });
    }
	
	   public void capture(){
        //Log.d("myTag", "This is my message");
        ScanovateSdk.start(this,
                "1",
                1,
                false,
                "girosyfinanzasqa",
                "db92efc69991",
                "https://adocolumbia.ado-tech.com/girosyfinanzasqa/api/",
                 numberIdentification,
                 verification,
                 new ScanovateHandler()
                 {
            @Override
            public void onSuccess(CloseResponse response, int code, String uuidDevice) {
                //CloseResponse myReponse = response;
                //String responseExtras = response.getExtras().getStateName() + response.getExtras().getIdState();
                //nextscreen();
                progress.show();
                String calificacion = response.getExtras().getStateName();
                evaluateTransaction(response.getTransactionId());
                //Toast.makeText(SplashScreen.this, responseExtras, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(CloseResponse response) {
                // Toast.makeText(this, "Resultado: failure" + "-- Front" + DataContext.getInstance().getResponseServers("FrontSide") + "Back--" + DataContext.getInstance().getResponseServers("BackSide"), Toast.LENGTH_LONG).show();
            }
        });
    }

	
	    private void evaluateTransaction(String transactionId) {

        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.validateTransaction("lulobankqa", transactionId, new ApiHelper.ValidateTransactionHandler() {
            @Override
            public void onSuccess(String stateName) {
                progress.dismiss();
                tv.setText("Resultado de Transacción: " + stateName);
            }

            @Override
            public void onSuccess(int i, ResponseBody responseBody) {
                String algo = "";

            }

            @Override
            public void onConnectionFailed() {
                String algo = "";
            }

            @Override
            public void onFailure(int i, String s) {
                //evaluateTransaction(transactionId, contect);
            }
        }, contect);
    }


}

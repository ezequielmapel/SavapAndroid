package com.example.ezequielmapel.savap;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ezequielmapel.savap.viewsLoged.MainLoged;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton signInButton = (SignInButton) findViewById(R.id.signId);
        signInButton.setOnClickListener(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,
                this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();


    }

    @Override //Se clickado na activity, verifique aqui
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signId:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    }

    private void updateUI(boolean isLogin) {
        if(isLogin){
            startActivity(new Intent(MainActivity.this, MainLoged.class));
            //finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String idAcc = account.getId().toString();
            String nameAcc = account.getDisplayName();
            String emailAcc = account.getEmail();
            String imgAccUrl = account.getPhotoUrl().toString();

            startActivity(new Intent(MainActivity.this, MainLoged.class));
            updateUI(true);
        }else{
            updateUI(false);
            Toast.makeText(this, "Autenticação falhou!", Toast.LENGTH_SHORT);
        }

    }

    private void OnActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}

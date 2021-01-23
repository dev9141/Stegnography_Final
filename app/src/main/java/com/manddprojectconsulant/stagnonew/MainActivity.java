package com.manddprojectconsulant.stagnonew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    public static final int RequestPermissionCode = 7;
    RadioGroup rg_emb_ext;
    Button btnnext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main);
        rg_emb_ext=findViewById(R.id.rg_emb_ext);
        btnnext=findViewById(R.id.btnnext);

        if (CheckingPermissionIsEnabledOrNot()) {
            nextButtonClick();
        }else{
            RequestMultiplePermission();
        }
    }

    public void nextButtonClick(){
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int emb_ext_Id = rg_emb_ext.indexOfChild(rg_emb_ext.findViewById(rg_emb_ext.getCheckedRadioButtonId()));

                if(emb_ext_Id==0)
                {
                    startActivity(new Intent(MainActivity.this, EncryptionActivity.class));
                    finish();
                }
                else if(emb_ext_Id==1)
                {
                    startActivity(new Intent(MainActivity.this, ExtractionActivity.class));
                    finish();
                }
            }
        });
    }

    private void RequestMultiplePermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE
                        ,READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED ;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean WriteExternalStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (WriteExternalStoragePermission && ReadExternalStoragePermission) {
                        //Toast.makeText(ActivityLogin.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        nextButtonClick();
                    } else {
                        finish();
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}
package cn.com.condenast.coverdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToCamera(View view) {
        Intent myIntent = new Intent(MainActivity.this, CameraActivity.class);
        startActivity(myIntent);
    }
}

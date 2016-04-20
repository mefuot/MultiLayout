package com.excelbkk.pong.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excelbkk.pong.multilayout.MultiLayout;
import com.excelbkk.pong.multilayout.OnRetryListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MultiLayout multiLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        multiLayout = (MultiLayout) findViewById(R.id.multi_layout);

        Button clearButton = (Button) findViewById(R.id.button_clear);
        Button emptyButton = (Button) findViewById(R.id.button_empty);
        Button loadingButton = (Button) findViewById(R.id.button_loading);
        Button failedButton = (Button) findViewById(R.id.button_failed);
        Button failedWithRetryButton = (Button) findViewById(R.id.button_failed_with_retry);

        if (clearButton != null) {
            clearButton.setOnClickListener(this);
        }
        if (emptyButton != null) {
            emptyButton.setOnClickListener(this);
        }
        if (loadingButton != null) {
            loadingButton.setOnClickListener(this);
        }
        if (failedButton != null) {
            failedButton.setOnClickListener(this);
        }
        if (failedWithRetryButton != null) {
            failedWithRetryButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_clear:
                multiLayout.removeAllViews();
                break;
            case R.id.button_empty:
                multiLayout.showEmpty();
                break;
            case R.id.button_loading:
                multiLayout.showLoading();
                break;
            case R.id.button_failed:
                multiLayout.showFail();
                break;
            case R.id.button_failed_with_retry:
                multiLayout.showFail(new OnRetryListener() {
                    @Override
                    public void onRetry() {
                        Toast.makeText(MainActivity.this,"Retry!",Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}

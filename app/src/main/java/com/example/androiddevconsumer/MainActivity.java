package com.example.androiddevconsumer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ContentResolver resolver = this.getContentResolver();


        findViewById(R.id.button).setOnClickListener(v -> {
            Uri uri = Uri.parse("content://com.example.androiddev/coord");
            Cursor cursor = resolver.query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {

                int id = cursor.getInt(0);

                double lat = cursor.getDouble(1);

                double lon = cursor.getDouble(2);

                int action = cursor.getInt(3);

                long timestamp = cursor.getLong(4);

                Toast.makeText(getApplicationContext(), "" + timestamp, Toast.LENGTH_LONG).show();
            }
        });
    }
}
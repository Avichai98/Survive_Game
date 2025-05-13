package com.app.survivegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class MenuActivity extends AppCompatActivity {
    private MaterialButton menu_BTN_start;
    /* access modifiers changed from: private */
    public TextInputEditText menu_EDT_id;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViews();
        initViews();
    }

    private void initViews() {
        this.menu_BTN_start.setOnClickListener(v -> MenuActivity.this.makeServerCall());
    }

    private void findViews() {
        this.menu_BTN_start = findViewById(R.id.menu_BTN_start);
        this.menu_EDT_id = findViewById(R.id.menu_EDT_id);
    }

    /* access modifiers changed from: private */
    public void makeServerCall() {
        new Thread() {
            public void run() {
                String data = MenuActivity.getJSON(MenuActivity.this.getString(R.string.url));
                Log.d("pttt", data);
                if (!data.trim().isEmpty()) {
                    MenuActivity activity_Menu = MenuActivity.this;
                    activity_Menu.startGame(
                            Objects.requireNonNull(activity_Menu.menu_EDT_id.getText()).toString(),
                            data);
                }
                else {
                    Log.e("pttt", "Empty or invalid response from server");
                    runOnUiThread(() ->
                            Toast.makeText(MenuActivity.this, "Server error. Please try again later.", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void startGame(String id, String data) {
        if (id.length() == 8){
            runOnUiThread(() ->
                    Toast.makeText(this, "Your ID must be 8 digits. Please add a leading zero if needed.", Toast.LENGTH_SHORT).show()
            );
            return;
        }
        if (id.length() != 9) {
            runOnUiThread(() ->
                Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
            );
            return;
        }
        String state = data.split(",")[Integer.parseInt(String.valueOf(id.charAt(7)))];
        Intent intent = new Intent(getBaseContext(), GameActivity.class);
        intent.putExtra(GameActivity.EXTRA_ID, id);
        intent.putExtra(GameActivity.EXTRA_STATE, state);
        startActivity(intent);
    }

    public static String getJSON(String url) {
        String data = "";
        HttpsURLConnection con = null;
        try {
            HttpsURLConnection con2 = (HttpsURLConnection) new URL(url).openConnection();
            con2.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(con2.getInputStream()));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = br.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            br.close();
            data = sb.toString();
            if (con2 != null) {
                try {
                    con2.disconnect();
                } catch (Exception ex) {
                    Log.d("pttt", ex.toString());
                }
            }
        } catch (MalformedURLException ex2) {
           Log.d("pttt", ex2.toString());
            if (con != null) {
                con.disconnect();
            }
        } catch (IOException ex3) {
            Log.d("pttt", ex3.toString());
            if (con != null) {
                con.disconnect();
            }
        } catch (Throwable th) {
            if (con != null) {
                try {
                    con.disconnect();
                } catch (Exception ex4) {
                   Log.d("pttt", ex4.toString());
                }
            }
            throw th;
        }
        return data;
    }
}

package com.app.survivegame;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_STATE = "EXTRA_STATE";
    private ImageButton[] arrows;
    int currentLevel = 0;
    private boolean goodToGo = true;
    int[] steps = {1, 1, 1, 2, 2, 2, 3, 3, 3};

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        String id = getIntent().getStringExtra(EXTRA_ID);
        assert id != null;
        if (id.length() == this.steps.length) {
            int i = 0;
            while (true) {
                int[] iArr = this.steps;
                if (i >= iArr.length) {
                    break;
                }
                iArr[i] = Integer.parseInt(String.valueOf(id.charAt(i))) % 4;
                i++;
            }
        }
        findViews();
        initViews();
    }

    /* access modifiers changed from: private */
    public void arrowClicked(int direction) {
        if (this.goodToGo && direction != this.steps[this.currentLevel]) {
            this.goodToGo = false;
        }
        int i = this.currentLevel + 1;
        this.currentLevel = i;
        if (i >= this.steps.length) {
            finishGame();
        }
    }

    private void finishGame() {
        String state = getIntent().getStringExtra(EXTRA_STATE);
        if (this.goodToGo) {
            Toast.makeText(this, "Survived in " + state, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "You Failed ", Toast.LENGTH_LONG).show();
        }
        finish();
    }

    private void initViews() {
        int i = 0;
        while (true) {
            ImageButton[] imageButtonArr = this.arrows;
            if (i < imageButtonArr.length) {
                final int finalI = i;
                imageButtonArr[i].setOnClickListener(v -> GameActivity.this.arrowClicked(finalI));
                i++;
            } else {
                return;
            }
        }
    }

    private void findViews() {
        this.arrows = new ImageButton[]{findViewById(R.id.game_BTN_left),
                findViewById(R.id.game_BTN_right), findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down)};
    }
}

package com.example.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView[] grid = new ImageView[9];
    String[] gridStatus = {"", "", "", "", "", "", "", "", ""};

    int[][] combs = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {2, 4, 6}, {0, 4, 8}};

    ImageView statusView;
    ImageView combView;
    Button playBtn;

    Boolean player1 = true;
    //    Boolean gameOver = false;
    int stepCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create views
        statusView = findViewById(R.id.player_turn);
        statusView.setImageResource(R.drawable.xplay);
        combView = findViewById(R.id.comb);
        playBtn = findViewById(R.id.play_btn);

        // create clickListener for each grid cell
        for (int i = 0; i < 9; i++) {
            String gridCellId = "grid_" + i;
            int resId = getResources().getIdentifier(gridCellId, "id", getPackageName());
            grid[i] = findViewById(resId);
            grid[i].setOnClickListener(this);
        }

        playBtn.setOnClickListener(v -> {
            resetGame();
        });
    }

    // handle click on cell
    @Override
    public void onClick(View v) {
        ImageView gridCell = ((ImageView) v);
        gridCell.setEnabled(false);

        int clickedId = Integer.parseInt(v.getTag().toString());

        if (player1) {
            gridCell.setImageResource(R.drawable.x);
            statusView.setImageResource(R.drawable.oplay);
            gridStatus[clickedId] = "X";
        } else {
            gridCell.setImageResource(R.drawable.o);
            statusView.setImageResource(R.drawable.xplay);
            gridStatus[clickedId] = "O";
        }

        stepCounter++;

        // check for a win or a tie
        if (!checkIfWins() && stepCounter == 9) {
            statusView.setImageResource(R.drawable.nowin);
            endGame();
        }

        // change player's turn
        player1 = !player1;
    }

    private Boolean checkIfWins() {
        // loop over all possibilities for a win
        for (int combInx = 0; combInx < 8; combInx++) {
            int[] currComb = combs[combInx];

            // Check if there is a win combo in our gridStatus
            if (gridStatus[currComb[0]] == gridStatus[currComb[1]] &&
                    gridStatus[currComb[1]] == gridStatus[currComb[2]] &&
                    gridStatus[currComb[0]] != "") {

                // Set win combo line
                int combId = getResources().getIdentifier("comb" + combInx, "drawable", getPackageName());
                combView.setImageResource(combId);

                if (player1) {
                    statusView.setImageResource(R.drawable.xwin);
                } else {
                    statusView.setImageResource(R.drawable.owin);
                }
                endGame();
                return true;
            }
        }
        return false;
    }

    private void endGame() {
        // disable grid
        for (int i = 0; i < 9; i++) {
            grid[i].setEnabled(false);
        }

        playBtn.setVisibility(View.VISIBLE);
    }

    private void resetGame() {
        Intent intent = new Intent(this, MainActivity.class);

        finish();
        startActivity(intent);
    }
}
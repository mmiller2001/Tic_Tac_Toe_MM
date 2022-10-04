package com.example.tictactoemm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button[][] buttons = new Button[3][3];

    private boolean player1Turn = true;

    private int roundCount;
    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    MediaPlayer player;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }

        if (player1Turn) {
            ((Button)v).setText("♚");
            ((Button)v).setBackgroundColor(Color.BLACK);
        } else {
            ((Button)v).setText("♜");
            ((Button)v).setBackgroundColor(Color.RED);
        }

        roundCount++;

        if(checkForWin()) {
            if(player1Turn) {
                waitingPlayer1(v);
                //player1Wins();
            } else {
                waitingPlayer2(v);
                //player2Wins();
            }
        } else if (roundCount == 9) {
            waitingTie(v);
            //draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }

        for(int i = 0; i < 3; i++) {
            if(field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                return true;
            }
        }

        for(int i = 0; i < 3; i++) {
            if(field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                return true;
            }
        }

        if(field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            return true;
        }

        if(field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private Handler mhandler = new Handler();

    public void waitingPlayer1(View v) {
        mhandler.postDelayed(mToastRunnable,3000);
    }

    public void waitingPlayer2(View v) {
        mhandler.postDelayed(mToastRunnable2,3000);
    }

    public void waitingTie(View v) {
        mhandler.postDelayed(mToastRunnable3,3000);
    }


    private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            player1Wins();
        }
    };

    private Runnable mToastRunnable2 = new Runnable() {
        @Override
        public void run() {
            player2Wins();
        }
    };

    private Runnable mToastRunnable3 = new Runnable() {
        @Override
        public void run() {
            draw();
        }
    };

    private void player1Wins() {

        player1Points++;
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show();
        player = MediaPlayer.create(this,R.raw.erenjaegar);
        player.start();
        updatePointsText();
        resetBoard();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show();
        player = MediaPlayer.create(this,R.raw.oioioi);
        player.start();
        updatePointsText();
        resetBoard();
    }

    private void draw() {
        Toast.makeText(this,"Draw!", Toast.LENGTH_SHORT).show();
        player = MediaPlayer.create(this,R.raw.fail);
        player.start();
        resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText("Player 1: " + player1Points);
        textViewPlayer2.setText("Player 2: "+player2Points);
    }

    private void resetBoard() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(color);
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame() {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount",roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points",player2Points);
        outState.putBoolean("player1Turn",player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}
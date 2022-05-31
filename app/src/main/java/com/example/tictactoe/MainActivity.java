package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Game game = new Game(this);

    }
}


class Game {
    int X = R.drawable.cross;
    int O = R.drawable.cricle;
    Player p1, p2;
    AppCompatActivity activity;
    TextView header;
    Button restartButton;
    int[] boxesImages = {
            R.id.box1, R.id.box2, R.id.box3,
            R.id.box4, R.id.box5, R.id.box6,
            R.id.box7, R.id.box8, R.id.box9
    };
    int[] areaTemplate;

    public Game(AppCompatActivity Context) {
        //get the activity and  setControls
        activity = Context;
        setControls(activity);
        initializeGame();
    }


    public void initializeGame() {
        // the area template must be reseted to make win or draw verification function work
        areaTemplate = new int[9];
        //set the players info name id state ...
        setPlayersInfo();
        header.setText(R.string.headertxt);
        restartButton.setVisibility(View.INVISIBLE);
        Log.i("Game", "Game Initialized");
        // remove all image resources and set the onClickListener to make them clickable
        for (int box : boxesImages) {

            ImageView boxImage = activity.findViewById(box);
            boxImage.setImageResource(0);
            boxImage.setOnClickListener(boxClick);
        }
    }

    public void finishGame() {
        Log.i("Game", "Displaying on finish");
        header.setText(R.string.finishedGameText);
        restartButton.setVisibility(View.VISIBLE);
        //prevent user from clicking after game the game is finish
        for (int box : boxesImages) {
            ImageView boxImage = activity.findViewById(box);
            boxImage.setOnClickListener(null);
        }

    }

    private void setControls(AppCompatActivity Context) {
        header = Context.findViewById(R.id.title);
        restartButton = Context.findViewById(R.id.restartButton);
        restartButton.setOnClickListener(restartClick);
    }

    private void setPlayersInfo() {
        p1 = new Player();
        p2 = new Player();
        p1.id = 1;
        p1.name = "Player 1";
        p1.myTurn = true;
        p2.id = 2;
        p2.name = "Player 2";
    }

    View.OnClickListener restartClick = view -> initializeGame();

    View.OnClickListener boxClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (p1.myTurn) {
                processPlay(view, p1, p2, O);
            } else {
                processPlay(view, p2, p1, X);
            }
        }

        void processPlay(View box, Player me, Player other, int XorO) {
            ((ImageView) box).setImageResource(XorO);
            box.setOnClickListener(null);
            fillAreaTemplate(me.id, box);
            if(!checkWinOrDrawProbability(me, areaTemplate)){switchTurn(me, other);}
        }
    };

    void fillAreaTemplate(int idPlayer, View Context) {
        int index = Arrays.binarySearch(boxesImages, Context.getId());
        areaTemplate[index] = idPlayer;
        Log.i("Game", "Player" + idPlayer + " Clicked On " + index);
    }

    void switchTurn(Player giver, Player taker) {
        giver.myTurn = false;
        taker.myTurn = true;
        header.setText(String.format("%s's Turn", taker.name));
        Log.i("Game", giver.name + "Turns to " + taker.name);
    }

    public boolean checkWinOrDrawProbability(Player p, int[] at) {
        boolean full = true;
        //horizontal
        for (int i = 0; i < 9; i += 3) {
            if (at[i + 1] == at[i] && at[i] == at[i + 2] && at[i] == p.id) {
                Log.i("Game", p.name + " wins Horizontal!");
                p.hasWon = true;
                break;
            }
        }

        //vertical
        for (int i = 0; i < 3; i++) {
            if (at[i + 3] == at[i] && at[i] == at[i + 6] && at[i] == p.id) {
                Log.i("Game", p.name + " wins Vertical!");
                p.hasWon = true;
                break;
            }
        }
        // /
        if (at[4 - 2] == at[4] && at[4 + 2] == at[4] && at[4] == p.id) {
            Log.i("Game", p.name + " wins Cross /!");
            p.hasWon = true;
        }
        // \
        if (at[0] == at[4] && at[8] == at[4] && at[4] == p.id) {
            Log.i("Game", p.name + " wins Cross \\ !");
            p.hasWon = true;
        }
        // if Win Repeat the Game
        if (p.hasWon) {
            Toast.makeText(activity, "Congrats "+p.name, Toast.LENGTH_SHORT).show();
            finishGame();
            Log.i("Game", "Game Finished Repeating Game...");

        } else {
            for (int v : at) {
                if (v == 0) {
                    full = false;
                    break;
                }
            }
            // if full so its a Tie
            if (full) {
                Toast.makeText(activity, "Tie -_-", Toast.LENGTH_SHORT).show();
                finishGame();
                Log.i("Game", "Game Finished With Tie Repeating Game...");
            }
        }
        return p.hasWon || full;
    }


    @Override
    protected void finalize() {
        Log.i("Game", "Game Destroyed");

    }
}


class Player {
    public int id;
    public String name;
    public boolean hasWon;
    public boolean myTurn;


}
package edu.apsu.csci.blackjack3devs;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.id;

public class BlackJackActivity extends AppCompatActivity
        implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);

        int[] buttonsID = {R.id.chip10, R.id.chip25, R.id.chip75, R.id.chip100, R.id.clearBet,
            R.id.hitButton, R.id.standButton, R.id.doubleButton,R.id.dealButton};

        clearBoard();
        shuffleCards();

        for(int id : buttonsID){
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.chip10){
            Toast.makeText(getApplicationContext(),"Bet 10",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.chip25){
            Toast.makeText(getApplicationContext(),"Bet 25",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.chip50){
            Toast.makeText(getApplicationContext(),"Bet 50",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.chip75){
            Toast.makeText(getApplicationContext(),"Bet 75",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.chip100){
            Toast.makeText(getApplicationContext(),"Bet 100",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.clearBet){
            Toast.makeText(getApplicationContext(),"Clear Bet",Toast.LENGTH_SHORT).show();
        }else if(v.getId()==R.id.dealButton){
            // Toast.makeText(getApplicationContext(),"DEAL!",Toast.LENGTH_SHORT).show();
            deal();
        }else if(v.getId()==R.id.hitButton){
            Toast.makeText(getApplicationContext(),"HIT!",Toast.LENGTH_SHORT).show();
            hit();

        }else if(v.getId()==R.id.standButton){
            Toast.makeText(getApplicationContext(),"STAND!",Toast.LENGTH_SHORT).show();

            stand();
        }else if(v.getId()==R.id.doubleButton){
            Toast.makeText(getApplicationContext(),"DOUBLE!",Toast.LENGTH_SHORT).show();

            doubleBet();
        }

    }

    public void clearBoard(){

        int [] cardsID = {R.id.houseC1,R.id.houseC2,R.id.houseC3,R.id.houseC4,R.id.houseC5,R.id.houseC6,
            R.id.houseC7,R.id.houseC8,R.id.houseC9,R.id.playerC1,R.id.playerC2,R.id.playerC3,
            R.id.playerC4,R.id.playerC5,R.id.playerC6,R.id.playerC7,R.id.playerC8,R.id.playerC9,};
        for(int id : cardsID){
            ImageView iv = (ImageView) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }
        int [] buttonsID = {R.id.doubleButton, R.id.hitButton, R.id.standButton};
        for(int id : buttonsID){
            ImageButton iv = (ImageButton) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }
        int [] textID = {R.id.houseScore, R.id.playerScore};
        for(int id : textID){
            TextView tv = (TextView) findViewById(id);
            tv.setText("");
        }

        TextView tv = (TextView) findViewById(R.id.betTextView);
        tv.setText("");
    }

    public void shuffleCards(){

    }

    public void deal(){
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        dealClear.setVisibility(View.INVISIBLE);

        //Check to make sure there is a bet value before executing hitShow & standShow
        //Can set betTextView to say "Place bet" or something.
        ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
        hitShow.setVisibility(View.VISIBLE);

        ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
        standShow.setVisibility(View.VISIBLE);

        //This would have to be different to show shuffled cards
        ImageView hC1 = (ImageView) findViewById(R.id.houseC1);
        hC1.setVisibility(View.VISIBLE);
        hC1.setImageResource(R.drawable.facedown);

        ImageView hC2 = (ImageView) findViewById(R.id.houseC2);
        hC2.setVisibility(View.VISIBLE);
        hC2.setImageResource(R.drawable.sa);

        ImageView hC3 = (ImageView) findViewById(R.id.houseC3);
        hC3.setVisibility(View.VISIBLE);
        hC3.setImageResource(R.drawable.sa);

        ImageView hC4 = (ImageView) findViewById(R.id.houseC4);
        hC4.setVisibility(View.VISIBLE);
        hC4.setImageResource(R.drawable.sa);

        ImageView hC5 = (ImageView) findViewById(R.id.houseC5);
        hC5.setVisibility(View.VISIBLE);
        hC5.setImageResource(R.drawable.sa);

        ImageView hC6 = (ImageView) findViewById(R.id.houseC6);
        hC6.setVisibility(View.VISIBLE);
        hC6.setImageResource(R.drawable.sa);

        ImageView hC7 = (ImageView) findViewById(R.id.houseC7);
        hC7.setVisibility(View.VISIBLE);
        hC7.setImageResource(R.drawable.sa);

        ImageView hC8 = (ImageView) findViewById(R.id.houseC8);
        hC8.setVisibility(View.VISIBLE);
        hC8.setImageResource(R.drawable.sa);

        ImageView hC9 = (ImageView) findViewById(R.id.houseC9);
        hC9.setVisibility(View.VISIBLE);
        hC9.setImageResource(R.drawable.sa);

        ImageView pC1 = (ImageView) findViewById(R.id.playerC1);
        pC1.setVisibility(View.VISIBLE);
        pC1.setImageResource(R.drawable.c5);

        ImageView pC2 = (ImageView) findViewById(R.id.playerC2);
        pC2.setVisibility(View.VISIBLE);
        pC2.setImageResource(R.drawable.h10);

        ImageView pC3 = (ImageView) findViewById(R.id.playerC3);
        pC3.setVisibility(View.VISIBLE);
        pC3.setImageResource(R.drawable.h8);

        ImageView pC4 = (ImageView) findViewById(R.id.playerC4);
        pC4.setVisibility(View.VISIBLE);
        pC4.setImageResource(R.drawable.h4);

        ImageView pC5 = (ImageView) findViewById(R.id.playerC5);
        pC5.setVisibility(View.VISIBLE);
        pC5.setImageResource(R.drawable.h5);

        ImageView pC6 = (ImageView) findViewById(R.id.playerC6);
        pC6.setVisibility(View.VISIBLE);
        pC6.setImageResource(R.drawable.h6);

        ImageView pC7 = (ImageView) findViewById(R.id.playerC7);
        pC7.setVisibility(View.VISIBLE);
        pC7.setImageResource(R.drawable.h8);

        ImageView pC8 = (ImageView) findViewById(R.id.playerC8);
        pC8.setVisibility(View.VISIBLE);
        pC8.setImageResource(R.drawable.h8);

        ImageView pC9 = (ImageView) findViewById(R.id.playerC9);
        pC9.setVisibility(View.VISIBLE);
        pC9.setImageResource(R.drawable.h9);

        updateCardTotal();
    }
    public void hit(){

    }
    public void stand(){

    }
    public void doubleBet(){

    }
    public void updateCardTotal(){
        //Temp hardcode
        TextView hScore = (TextView) findViewById(R.id.houseScore);
        hScore.setText("11");

        TextView pScore = (TextView) findViewById(R.id.playerScore);
        pScore.setText("15");

    }
}

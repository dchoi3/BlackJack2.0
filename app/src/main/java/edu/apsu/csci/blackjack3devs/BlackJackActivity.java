package edu.apsu.csci.blackjack3devs;

import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static android.R.attr.id;
import static android.R.attr.logoDescription;

public class BlackJackActivity extends AppCompatActivity
        implements View.OnClickListener{
    // Variable to hold bet amount
    int CurrentBetAmt = 0;
    int walletAmt = 2000;

    // Initialize Card drawable/value array.
    private int[][] cardsArray = {
        {R.drawable.c2,2},{R.drawable.h2,2},{R.drawable.d2,2},{R.drawable.s2,2},
        {R.drawable.c3,3},{R.drawable.h3,3},{R.drawable.d3,3},{R.drawable.s3,3},
        {R.drawable.c4,4},{R.drawable.h4,4},{R.drawable.d4,4},{R.drawable.s4,4},
        {R.drawable.c5,5},{R.drawable.h5,5},{R.drawable.d5,5},{R.drawable.s5,5},
        {R.drawable.c6,6},{R.drawable.h6,6},{R.drawable.d6,6},{R.drawable.s6,6},
        {R.drawable.c7,7},{R.drawable.h7,7},{R.drawable.d7,7},{R.drawable.s7,7},
        {R.drawable.c8,8},{R.drawable.h8,8},{R.drawable.d8,8},{R.drawable.s8,8},
        {R.drawable.c9,9},{R.drawable.h9,9},{R.drawable.d9,9},{R.drawable.s9,9},
        {R.drawable.c10,10},{R.drawable.h10,10},{R.drawable.d10,10},{R.drawable.s10,10},
        {R.drawable.cj,10},{R.drawable.hj,10},{R.drawable.dj,10},{R.drawable.sj,10},
        {R.drawable.cq,10},{R.drawable.hq,10},{R.drawable.dq,10},{R.drawable.sq,10},
        {R.drawable.ck,10},{R.drawable.hk,10},{R.drawable.dk,10},{R.drawable.sk,10},
        {R.drawable.ca,11},{R.drawable.ha,11},{R.drawable.da,11},{R.drawable.sa,11},
    };

    // Put card view ids in array.
    private int[] houseCardsID = {
        R.id.houseC1,R.id.houseC2,R.id.houseC3,R.id.houseC4,R.id.houseC5,
        R.id.houseC6,R.id.houseC7,R.id.houseC8,R.id.houseC9,
    };
    private int[] playerCardsID = {
        R.id.playerC1,R.id.playerC2,R.id.playerC3,R.id.playerC4,R.id.playerC5,
        R.id.playerC6,R.id.playerC7,R.id.playerC8,R.id.playerC9,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);

        int[] buttonsID = {R.id.chip10, R.id.chip25, R.id.chip50, R.id.chip75, R.id.chip100, R.id.clearBet,
            R.id.hitButton, R.id.standButton, R.id.doubleButton,R.id.dealButton};

        clearBoard();
        // shuffleCards(); I don't think we need this here.

        for(int id : buttonsID){
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);

        if(v.getId()==R.id.chip10){
            if(walletAmt >= 10){
                CurrentBetAmt += 10;
                walletAmt -= 10;
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet =  Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);
            }
        }else if(v.getId()==R.id.chip25){
            if(walletAmt >= 25){
                CurrentBetAmt += 25;
                walletAmt -= 25;
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet =  Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);
            }
        }else if(v.getId()==R.id.chip50){
            if(walletAmt >= 50){
                CurrentBetAmt += 50;
                walletAmt -= 50;
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet =  Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);
            }
        }else if(v.getId()==R.id.chip75){
            if(walletAmt >= 75){
                CurrentBetAmt += 70;
                walletAmt -= 70;
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet =  Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);
            }
        }else if(v.getId()==R.id.chip100){
            if(walletAmt >= 100){
                CurrentBetAmt += 100;
                walletAmt -= 100;
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet =  Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);
            }
        }else if(v.getId()==R.id.clearBet){
            walletAmt+=CurrentBetAmt;
            CurrentBetAmt=0;
            String newBet = Integer.toString(CurrentBetAmt);
            String newWallet =  Integer.toString(walletAmt);
            betAmtTV.setText("$" + newBet);
            WalletAmtTV.setText("Wallet: $" + newWallet);
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
        tv.setText("$0");
    }

    /**
     * shuffleCards uses multidimensional array to set a
     * randomly selected card's image and value.
     * @return Returns the index of a single random card.
     */
    public int shuffleCards() {
        Random randomCardGenerator = new Random();
        // We know there are 52 cards, so 51 is hardcoded for the index of 0-51.
        return randomCardGenerator.nextInt(51);
    }

    public void deal(){
        //Check to make sure there is a bet value before executing hitShow & standShow
        if(CurrentBetAmt > 0) {
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        dealClear.setVisibility(View.INVISIBLE);
        //Can set betTextView to say "Place bet" or something.
        ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
        hitShow.setVisibility(View.VISIBLE);

        ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
        standShow.setVisibility(View.VISIBLE);

        // Place two cards for player and dealer.
        for (int i = 0; i < 2; i++) {

            // Player card.
            int playerCardIndex = shuffleCards();
            int playerCardValue = cardsArray[playerCardIndex][1];
            ImageView playerCard = (ImageView) findViewById(playerCardsID[i]);
            playerCard.setVisibility(View.VISIBLE);
            playerCard.setImageResource(cardsArray[playerCardIndex][0]);

            // House card.
            int houseCardIndex = shuffleCards();
            int houseCardValue = cardsArray[houseCardIndex][1];
            ImageView houseCard = (ImageView) findViewById(houseCardsID[i]);
            houseCard.setVisibility(View.VISIBLE);
            if (i == 0) {
                houseCard.setImageResource(R.drawable.facedown);
            } else {
                houseCard.setImageResource(cardsArray[houseCardIndex][0]);
            }
        }
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

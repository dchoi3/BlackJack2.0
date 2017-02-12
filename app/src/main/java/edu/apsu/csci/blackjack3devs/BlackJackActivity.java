package edu.apsu.csci.blackjack3devs;

/**
 * Application: Blackjack
 * Course: CSCI4020 Spring 2017
 * Team Name: TBD
 * Developers: John Schmitt, Daniel Choi, Charles Fannin
 */

import com.google.android.gms.common.api.BooleanResult;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
//
import static android.graphics.Color.BLACK;


public class BlackJackActivity extends AppCompatActivity
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    // Constant for data file.
    private final String fn = "blackjack_data.txt";

    // Variable to hold bet amount
    int currentBetAmt = 0;
    int walletAmt = 2000;

    // Vars for card values dealt to player and house.
    int playerCardValue = 0;
    int houseCardValue = 0;

    // Vars for tracking iteration of card positions.
    int playerCardPosition = 0;
    int houseCardPosition = 0;
    int deckCardPosition = 0;
    int previousRoundCardCount = 0;

    //Remembering facedown card
    int faceDownIndex; // Isn't face down index always the same? Maybe i don't understand what this is for.

    //Check shuffle boolean
    boolean shuffleCheck = true;
    boolean playerTurn = true;
    boolean playerBlackJack = false;
    boolean playerBust = false;
    boolean houseBust = false;
    boolean faceDownFlipped = false;

    // How many cards have been dealt
    int numHouseCardsDealt = 0;
    int numPlayerCardsDealt = 0;

    int playerAceCount = 0;
    int houseAceCount = 0;
    boolean cardsDealt = false;
    int buttonID = R.drawable.deal; // ID for Deal Button and ClearBet(TEMP)
    String winnerString = "None";

    // Initialize Card drawable/value array.
    private int[][] cardsArray = new int[][]{
            {R.drawable.c2, 2}, {R.drawable.h2, 2}, {R.drawable.d2, 2}, {R.drawable.s2, 2},
            {R.drawable.c3, 3}, {R.drawable.h3, 3}, {R.drawable.d3, 3}, {R.drawable.s3, 3},
            {R.drawable.c4, 4}, {R.drawable.h4, 4}, {R.drawable.d4, 4}, {R.drawable.s4, 4},
            {R.drawable.c5, 5}, {R.drawable.h5, 5}, {R.drawable.d5, 5}, {R.drawable.s5, 5},
            {R.drawable.c6, 6}, {R.drawable.h6, 6}, {R.drawable.d6, 6}, {R.drawable.s6, 6},
            {R.drawable.c7, 7}, {R.drawable.h7, 7}, {R.drawable.d7, 7}, {R.drawable.s7, 7},
            {R.drawable.c8, 8}, {R.drawable.h8, 8}, {R.drawable.d8, 8}, {R.drawable.s8, 8},
            {R.drawable.c9, 9}, {R.drawable.h9, 9}, {R.drawable.d9, 9}, {R.drawable.s9, 9},
            {R.drawable.c10, 10}, {R.drawable.h10, 10}, {R.drawable.d10, 10}, {R.drawable.s10, 10},
            {R.drawable.cj, 10}, {R.drawable.hj, 10}, {R.drawable.dj, 10}, {R.drawable.sj, 10},
            {R.drawable.cq, 10}, {R.drawable.hq, 10}, {R.drawable.dq, 10}, {R.drawable.sq, 10},
            {R.drawable.ck, 10}, {R.drawable.hk, 10}, {R.drawable.dk, 10}, {R.drawable.sk, 10},
            {R.drawable.ca, 11}, {R.drawable.ha, 11}, {R.drawable.da, 11}, {R.drawable.sa, 11},
    };

    // Put card view ids in array.
    private int[] houseCardsID = {
        R.id.houseC1, R.id.houseC2, R.id.houseC3, R.id.houseC4, R.id.houseC5,
        R.id.houseC6, R.id.houseC7, R.id.houseC8, R.id.houseC9,
    };
    private int[] playerCardsID = {
        R.id.playerC1, R.id.playerC2, R.id.playerC3, R.id.playerC4, R.id.playerC5,
        R.id.playerC6, R.id.playerC7, R.id.playerC8, R.id.playerC9,
    };

    private int[] buttonsID = {R.id.chip5, R.id.chip10, R.id.chip25, R.id.chip50, R.id.chip75, R.id.chip100,
        R.id.clearBet, R.id.hitButton, R.id.standButton, R.id.doubleButton, R.id.dealButton};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_jack);

        for (int id : buttonsID) {
            ImageButton ib = (ImageButton) findViewById(id);
            if(ib != null) {
                ib.setOnClickListener(this);
            }
        }

        ImageButton betAll = (ImageButton) findViewById(R.id.chip100);
        if(betAll != null){
        betAll.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!cardsDealt && walletAmt > 0){
                    updateWalletAndBet(walletAmt);
                    Toast toast = Toast.makeText(getApplicationContext(), "All in!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -260);
                    toast.show();
                    return true;
                }else
                    return false;
            }
        });
        }
    }//OnCreate

    @Override
    public void onClick(View v) {
        //Log.i("Button Pressed", "In onClick");
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Allow betting only before Cards are dealt.
        if (!cardsDealt) {
            if (v.getId() == R.id.chip5) {
                if (walletAmt >= 5) {
                    vb.vibrate(10);
                    updateWalletAndBet(5);
                }
            } else if (v.getId() == R.id.chip10) {
                if (walletAmt >= 10) {
                    vb.vibrate(10);
                    updateWalletAndBet(10);
                }
            } else if (v.getId() == R.id.chip25) {
                if (walletAmt >= 25) {
                    vb.vibrate(10);
                    updateWalletAndBet(25);
                }
            } else if (v.getId() == R.id.chip50) {
                if (walletAmt >= 50) {
                    vb.vibrate(10);
                    updateWalletAndBet(50);
                }
            } else if (v.getId() == R.id.chip75) {
                if (walletAmt >= 75) {
                    vb.vibrate(10);
                    updateWalletAndBet(75);
                }
            } else if (v.getId() == R.id.chip100) {
                if (walletAmt >= 100) {
                    vb.vibrate(10);
                    updateWalletAndBet(100);
                }
            } else if (v.getId() == R.id.clearBet) {
                vb.vibrate(10);
                TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
                TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
                walletAmt += currentBetAmt;
                String newWallet = "Wallet: $" + walletAmt;
                currentBetAmt = 0;
                if (betAmtTV != null) {
                    betAmtTV.setText(R.string.SetBet);
                }
                if (WalletAmtTV != null) {
                    WalletAmtTV.setText(newWallet);
                }
            }
            if (v.getId() == R.id.dealButton) {
                vb.vibrate(10);
                if (buttonID == R.drawable.deal) {
                    if (currentBetAmt > 0) {
                        cardsDealt = true;
                        deal();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Place a bet!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
            }
        } else {

            if (v.getId() == R.id.hitButton) {
                vb.vibrate(10);
                hit();
            }
            if (v.getId() == R.id.standButton) {
                vb.vibrate(100);
                playerTurn = false;
                endTurn();
                checkIfWinner();
            }
            if (v.getId() == R.id.doubleButton) {
                vb.vibrate(10);
                doubleBet();
            }
            if (v.getId() == R.id.dealButton && buttonID == R.drawable.refresh) {
                vb.vibrate(10);
                if (walletAmt == 0) {
                    gameOver();
                } else clearBoard(true);
            }
            // Has to be here so you cant bet until refresh button clicked this is 1 of 2 requirements
        }//OnClick
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(item.getItemId() == R.id.actionRestart){
            vb.vibrate(10);
            restartGame();
            return true;
        }else if(item.getItemId() == R.id.actionQuit){
            vb.vibrate(10);
            Intent aboutIntent = new Intent(this, HomeActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return false;
    }//Menu Item Click

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }//On back pressed

    public void popupMenu(View v) {
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(10);
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(BlackJackActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.show();
    }//PopUpMenu

    public void clearBoard(boolean first) {

        if(first) {
            int[] chipButtonsID = {R.id.chip5, R.id.chip10, R.id.chip25, R.id.chip50, R.id.chip75, R.id.chip100, R.id.clearBet};
            for (int id : chipButtonsID) {
                ImageButton ib = (ImageButton) findViewById(id);
                if (ib != null) {
                    ib.setClickable(true);
                    ib.setLongClickable(true);
                }
            }
            ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
            if (ib != null) {
                ib.setImageResource(R.drawable.deal);
                ib.setVisibility(View.VISIBLE);
            }
            TextView betTv = (TextView) findViewById(R.id.betTextView);
            if (betTv != null) {
                betTv.setText(R.string.SetBet);
                betTv.setBackgroundColor(0x00000000);
            }

            int[] buttonsID = {R.id.doubleButton, R.id.hitButton, R.id.standButton};
            int[] textID = {R.id.houseScore, R.id.playerScore};

            for (int id : playerCardsID) {
                ImageView iv = (ImageView) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide all player cards
            for (int id : houseCardsID) {
                ImageView iv = (ImageView) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide all house cards
            for (int id : buttonsID) {
                ImageButton iv = (ImageButton) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide buttons
            for (int id : textID) {
                TextView tv = (TextView) findViewById(id);
                if (tv != null) {
                    tv.setText("");
                }
            }//Clear scores

            checkForShuffle();
            if (shuffleCheck)
                shuffleCards();

            buttonID = R.drawable.deal;
            currentBetAmt = 0;
            playerCardPosition = 0;
            houseCardPosition = 0;
            playerCardValue = 0;
            houseCardValue = 0;
            numHouseCardsDealt = 0;
            numPlayerCardsDealt = 0;
            playerAceCount = 0;
            houseAceCount = 0;
            cardsDealt = false;
            playerBlackJack = false;
            playerTurn = true;
            playerBust = false;
            houseBust = false;
            faceDownFlipped = false;

            setHouseCardValue(0);
            setPlayerCardValue(0);
        }else{ // this is on file reload
            int[] buttonsID = {R.id.doubleButton, R.id.hitButton, R.id.standButton};
            for (int id : buttonsID) {
                ImageButton iv = (ImageButton) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide buttons

            for (int id : playerCardsID) {
                ImageView iv = (ImageView) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide all player cards
            for (int id : houseCardsID) {
                ImageView iv = (ImageView) findViewById(id);
                if (iv != null) {
                    iv.setVisibility(View.INVISIBLE);
                }
            }//Hide all house cards

        }
    }//Clear Board

    public void shuffleCards() {

        Toast toast = Toast.makeText(getApplicationContext(), "Shuffling Cards", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -260);
        toast.show();

        cardsArray = new int[][]{
                {R.drawable.c2, 2}, {R.drawable.h2, 2}, {R.drawable.d2, 2}, {R.drawable.s2, 2},
                {R.drawable.c3, 3}, {R.drawable.h3, 3}, {R.drawable.d3, 3}, {R.drawable.s3, 3},
                {R.drawable.c4, 4}, {R.drawable.h4, 4}, {R.drawable.d4, 4}, {R.drawable.s4, 4},
                {R.drawable.c5, 5}, {R.drawable.h5, 5}, {R.drawable.d5, 5}, {R.drawable.s5, 5},
                {R.drawable.c6, 6}, {R.drawable.h6, 6}, {R.drawable.d6, 6}, {R.drawable.s6, 6},
                {R.drawable.c7, 7}, {R.drawable.h7, 7}, {R.drawable.d7, 7}, {R.drawable.s7, 7},
                {R.drawable.c8, 8}, {R.drawable.h8, 8}, {R.drawable.d8, 8}, {R.drawable.s8, 8},
                {R.drawable.c9, 9}, {R.drawable.h9, 9}, {R.drawable.d9, 9}, {R.drawable.s9, 9},
                {R.drawable.c10, 10}, {R.drawable.h10, 10}, {R.drawable.d10, 10}, {R.drawable.s10, 10},
                {R.drawable.cj, 10}, {R.drawable.hj, 10}, {R.drawable.dj, 10}, {R.drawable.sj, 10},
                {R.drawable.cq, 10}, {R.drawable.hq, 10}, {R.drawable.dq, 10}, {R.drawable.sq, 10},
                {R.drawable.ck, 10}, {R.drawable.hk, 10}, {R.drawable.dk, 10}, {R.drawable.sk, 10},
                {R.drawable.ca, 11}, {R.drawable.ha, 11}, {R.drawable.da, 11}, {R.drawable.sa, 11},
        };

        Random randCard = new Random();
        int a;
        int b;
        for(int x = cardsArray.length-1; x >= 0; x--){

            int index = randCard.nextInt(x+1);
            //Store random number location between (0 to x) to temp variable
            a = cardsArray[index][0];
            b = cardsArray[index][1];
            //Store last card location to random number location
            cardsArray[index][0] = cardsArray[x][0];
            cardsArray[index][1] = cardsArray[x][1];
            //Store random number location to last card location so it is stored.
            cardsArray[x][0] = a;
            cardsArray[x][1] = b;
            //Log.i("INDEX", index+"  C:"+x);
            //Log.i("Card value chosen", " "+b);
        }

        shuffleCheck = false;
        deckCardPosition = 0;
    }//Shuffle Cards

    public void updateWalletAndBet(int m) {
        //Updates the wallet and bet textviews. Just to reduce the code a little bit.
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        currentBetAmt += m;
        walletAmt -= m;
        String newBet = "$" + currentBetAmt;
        String newWallet = "Wallet: $" + walletAmt;
        if(betAmtTV != null && WalletAmtTV != null) {
            betAmtTV.setText(newBet);
            WalletAmtTV.setText(newWallet);
        }

    }//Update Wallet and Bet

    public void deal() {
        //Log.i("BEFORE DEAL ", "" + deckCardPosition);
        setDealtView();
        previousRoundCardCount = deckCardPosition;
        for (int i = 0; i < 2; i++) {// Deal cards; Pass iterator so method knows what card we are on.
            hit();
            int y = dealHouseCard();
            setHouseCardValue(y);
            updateCardTotalDisplay(); // Update display of card values.
            //Log.i("IN DEAL LOOP", i+ "=" + deckCardPosition);
        }

    }//Deal cards

    public int dealPlayerCard() {
        ImageView playerCard = (ImageView) findViewById(playerCardsID[playerCardPosition]);
        if(playerCard != null) {
            playerCard.setImageResource(cardsArray[deckCardPosition][0]);
            playerCard.setVisibility(View.VISIBLE);
        }
        int x = cardsArray[deckCardPosition][1];
        if(x == 11){//is Ace
            playerAceCount++;}
        playerCardPosition++;
        numPlayerCardsDealt++;
        deckCardPosition++;
        return x;
    }

    public int dealHouseCard() {

        ImageView houseCard = (ImageView) findViewById(houseCardsID[houseCardPosition]);
        if(houseCardPosition==0){
            if(houseCard != null) {
                houseCard.setImageResource(R.drawable.facedown);
                houseCard.setVisibility(View.VISIBLE);
            }
            faceDownIndex = deckCardPosition;
            deckCardPosition++;
            houseCardPosition++;

            return 0;
        }else if(houseCardPosition == 2 && !faceDownFlipped){
            ImageView fdCard = (ImageView) findViewById(houseCardsID[0]);//Gets first card IV
            if(fdCard != null){
                fdCard.setImageResource(cardsArray[faceDownIndex][0]);//Flips the card
            }
            faceDownFlipped = true;
            return cardsArray[faceDownIndex][1];//return facedown card value

        }else{
            if(houseCard != null){
                houseCard.setImageResource(cardsArray[deckCardPosition][0]);
            }
            int x = cardsArray[deckCardPosition][1];
            if(x == 11){//is Ace
                houseAceCount++;}
            houseCardPosition++;
            numHouseCardsDealt++;
            deckCardPosition++;
            if(houseCard != null) {
                houseCard.setVisibility(View.VISIBLE);
            }
            return x;
        }
    }//dealHouseCard

    public void hit() {
        if (playerCardPosition < 9) {
            int x = dealPlayerCard();//deal player card and return card value
            setPlayerCardValue(x);
            //Log.i("Player card Value",": "+x);
            checkIfWinner();
            updateCardTotalDisplay();
            }
    }

    public void checkIfWinner(){
        if(playerTurn) {
            if (playerCardValue > 21) {
                if (playerAceCount > 0){
                    handlePlayerAces();
                    if(playerCardValue == 21){
                        playerBlackJack = true;
                        playerTurn = false;
                        endTurn();
                    }
                }else {
                    playerBust = true;
                    playerTurn = false;
                    endTurn();
                }
            } else if (playerCardValue == 21) {//Player got blackjack
                playerBlackJack = true;
                playerTurn = false;
                endTurn();
            }
        }

       if(!playerTurn){
           Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
           if(playerBust){
                winnerString = "Bust!";
                vb.vibrate(200);
                //Log.i("In ", "Bust");
                showResult();
            }else if(houseCardValue == playerCardValue){
                winnerString = "Push";//At the moment just made it so he made his money back
                walletAmt += (currentBetAmt);
                vb.vibrate(200);
                showResult();
            }else if (playerBlackJack){
                winnerString = "BLACKJACK!";
                walletAmt += (currentBetAmt * 2);
                //Log.i("In ", "Blackjack");
                vb.vibrate(200);
                showResult();
            }else if(houseBust){
                winnerString = "You won: $" + (currentBetAmt * 2) + "!";
                walletAmt += (currentBetAmt * 2);
                vb.vibrate(200);
                //Log.i("In ", "House Bust");
                showResult();
            }else if (houseCardValue > playerCardValue) {
                winnerString = "House wins!";
                //Log.i("In ", "House wins");
                vb.vibrate(200);
                showResult();
            }else if (houseCardValue < playerCardValue) {
                winnerString = "You won: $" + (currentBetAmt * 2) + "!";
                walletAmt += (currentBetAmt * 2);
                vb.vibrate(200);
                //Log.i("In ", "Player won");
                showResult();
            }
        }

    }//Called by hit and endTurn

    public void handlePlayerAces(){
        //Log.i("====","Removing 10");
        playerCardValue = playerCardValue - 10;
        //Log.i("====", "# of Ace: "+ playerAceCount);
        playerAceCount--;
        //Log.i("====", "# of Ace: "+ playerAceCount);
        updateCardTotalDisplay();

    }//Handle player Aces

    public void handleHouseAces(){
        houseCardValue = houseCardValue - 10;
        houseAceCount--;
        updateCardTotalDisplay();

    }//Handle player Aces

    public void endTurn() {
        ImageButton stb = (ImageButton) findViewById(R.id.standButton);
                if(stb != null){
                    stb.setVisibility(View.INVISIBLE);
                }
        ImageButton htb = (ImageButton) findViewById(R.id.hitButton);
                if(htb != null) {
                    htb.setVisibility(View.INVISIBLE);
                }
        if(!faceDownFlipped){
            int x = dealHouseCard();
            setHouseCardValue(x);

            if (houseCardValue > 21) {
                if (houseAceCount > 0) handleHouseAces();
                else houseBust = true;
            }
            updateCardTotalDisplay();

        }
        while (houseCardValue < 17 && !houseBust && !playerBust) {
            int y = dealHouseCard();
            setHouseCardValue(y);

            if (houseCardValue > 21) {
                if (houseAceCount > 0) handleHouseAces();
                else houseBust = true;
            }
            updateCardTotalDisplay();
        }


    }

    public void checkForShuffle(){
        if(deckCardPosition > 30){
            shuffleCheck = true;
        }
    }

    public void doubleBet(){}

    public void setPlayerCardValue(int pCardVal) {playerCardValue += pCardVal;}//Set player card value

    public void setHouseCardValue(int hCardVal) {houseCardValue += hCardVal;}//Set house player value

    public void updateCardTotalDisplay() {
        // Display player and house card values.
        TextView pScore = (TextView) findViewById(R.id.playerScore);
        if(pScore != null) {
            pScore.setText(String.valueOf(playerCardValue));
            //Log.i("Player total:", " "+playerCardValue);
        }
        TextView hScore = (TextView) findViewById(R.id.houseScore);
        if(hScore != null) {
            hScore.setText(String.valueOf(houseCardValue));
            //Log.i("House total:", " "+houseCardValue);
        }
    }//Update card total display

    public void showResult(){

        cardsDealt = true;
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        if(betAmtTV != null) {
            betAmtTV.setText(winnerString);
            betAmtTV.setBackgroundColor(BLACK);
        }
        String newWallet = "Wallet: $"+walletAmt;
        if(WalletAmtTV != null) {
            WalletAmtTV.setText(newWallet);
        }
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        if(dealClear != null) {

            buttonID = R.drawable.refresh;
            dealClear.setVisibility(View.VISIBLE);
            dealClear.setImageResource(buttonID);
        }
        //Log.i("==============","In show result");
    }

    public void gameOver(){
        Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vb.vibrate(500);
        //Log.i("===", "In gameover");
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        if(betAmtTV != null) {
            winnerString = "GAMEOVER";
            betAmtTV.setText(winnerString);
            betAmtTV.setBackgroundColor(BLACK);
        }
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        if(WalletAmtTV != null) {
            WalletAmtTV.setText(R.string.RestartGame);
        }
        ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
        if(ib != null) {
            ib.setVisibility(View.INVISIBLE);
        }
    }//Called when wallet is zero after refreshButton Clicked

    public void restartGame(){
        walletAmt = 2000;
        String newWallet = "Wallet: $"+walletAmt;
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        if(WalletAmtTV != null) {
            WalletAmtTV.setText(newWallet);
        }
        shuffleCheck = true;
        clearBoard(true);
    }//Called by popUpMenu onClick

    public void setDealtView(){
        //Make Poker chips anc clear bet un clickable after deal
        int[] chipButtonsID = {R.id.chip5, R.id.chip10, R.id.chip25, R.id.chip50, R.id.chip75, R.id.chip100, R.id.clearBet};
        for(int id : chipButtonsID) {
            ImageButton ib = (ImageButton) findViewById(id);
            if (ib != null) {
                ib.setClickable(false);
                ib.setLongClickable(false);
            }
        }
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        if(dealClear != null) {
            dealClear.setVisibility(View.INVISIBLE);
        }
        ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
        if(hitShow != null) {
            hitShow.setVisibility(View.VISIBLE);
        }
        ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
        if(standShow != null) {
            standShow.setVisibility(View.VISIBLE);
        }
    }//Called by reloadData if card is dealt

    public void setWalletView(){
        // Put stuff back the way it was.
        TextView walletTV = (TextView) findViewById(R.id.walletTextView);
        String wV = "Wallet: $" + walletAmt;
        if(walletTV != null)
            walletTV.setText(wV);

    }// Called by reloadData

    public void setDeckBack(String id, String value){

        //Log.i("===ID: ", id);
        //Log.i("===Value: ", value);
        String[] idArray = id.split(",");
        String[] valueArray = value.split(",");
        for(int x=0; x<51; x++){
            cardsArray[x][0] = Integer.parseInt(idArray[x]);
            cardsArray[x][1] = Integer.parseInt(valueArray[x]);
        }//Set deck


    } //Called by reloadData

    public void setCardViewsBack(){

        //Log.i("In ", "setCardsViewBack");

        ImageButton stb = (ImageButton) findViewById(R.id.standButton);
        if(stb != null){
            stb.setVisibility(View.INVISIBLE);
        }
        ImageButton htb = (ImageButton) findViewById(R.id.hitButton);
        if(htb != null) {
            htb.setVisibility(View.INVISIBLE);
        }

        if(faceDownFlipped){//If player turn
            //Log.i("In ", "faceDownTrue");

            int x = previousRoundCardCount;
            int y = 0;
            while(x<deckCardPosition) {
                if(y<playerCardPosition) {
                    ImageView pIV = (ImageView) findViewById(playerCardsID[y]);
                    if (pIV != null) {
                        pIV.setImageResource(cardsArray[x][0]);
                        pIV.setVisibility(View.VISIBLE);
                    }
                }else x--;
                if (y == 0) {
                    x++;
                    ImageView hIV = (ImageView) findViewById(houseCardsID[y]);
                    if(hIV != null){
                        hIV.setImageResource(cardsArray[faceDownIndex][0]);
                        hIV.setVisibility(View.VISIBLE);

                    }
                } else if (y < houseCardPosition) {
                    x++;
                    ImageView hIV = (ImageView) findViewById(houseCardsID[y]);
                    if(hIV != null) {
                        hIV.setImageResource(cardsArray[x][0]);
                        hIV.setVisibility(View.VISIBLE);
                    }
                }
                x++;
                y++;
            }
            showResult();

        }else{

            if(cardsDealt) {
                //Log.i("In else", "cardView");
                int x = previousRoundCardCount;
                int y = 0;
                while (x < deckCardPosition) {
                    ImageView pIV = (ImageView) findViewById(playerCardsID[y]);
                    if (pIV != null) {
                        pIV.setImageResource(cardsArray[x][0]);
                        pIV.setVisibility(View.VISIBLE);

                    }
                    if (y == 0) {
                        x++;
                        ImageView hIV = (ImageView) findViewById(houseCardsID[y]);
                        if(hIV != null){
                            hIV.setImageResource(R.drawable.facedown);
                            hIV.setVisibility(View.VISIBLE);

                        }
                    } else if (y == 1) {
                        x++;
                        ImageView hIV = (ImageView) findViewById(houseCardsID[y]);
                        if(hIV != null) {
                            hIV.setImageResource(cardsArray[x][0]);
                            hIV.setVisibility(View.VISIBLE);
                        }
                    }
                    x++;
                    y++;

                }//while
            }//cardDealt

        }


    }

    public void saveData(){

        try {

            FileOutputStream fos = openFileOutput(fn, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw);

            /*****************To take care of arrays*********/
            int[] tempId = new int[52];
            int[] tempValue = new int[52];
            for(int x=0; x<52; x++){
                tempId[x]= cardsArray[x][0];
                tempValue[x]= cardsArray[x][1];
            }//This splits up the 2D array to be rebuilt on write.
            String cardsIDString = Arrays.toString(tempId).replace("[", "").replace("]", "").replace(" ", "");
            String cardValueString = Arrays.toString(tempValue).replace("[", "").replace("]", "").replace(" ", "");

            Log.i("Saving ID", cardsIDString);
            Log.i("Saving Value", cardValueString);


            pw.println(walletAmt);
            pw.println(currentBetAmt);
            pw.println(playerCardValue);
            pw.println(houseCardValue);
            pw.println(playerCardPosition);
            pw.println(houseCardPosition);
            pw.println(deckCardPosition);
            pw.println(previousRoundCardCount);
            pw.println(faceDownIndex);
            pw.println(shuffleCheck);
            pw.println(playerBlackJack);
            pw.println(playerBust);
            pw.println(houseBust);
            pw.println(playerTurn);
            pw.println(faceDownFlipped);
            pw.println(numHouseCardsDealt);
            pw.println(numPlayerCardsDealt);
            pw.println(playerAceCount);
            pw.println(houseAceCount);
            pw.println(cardsDealt);
            pw.println(buttonID);
            pw.println(winnerString);
            pw.println(cardsIDString);
            pw.println(cardValueString);

            pw.close();
        } catch (FileNotFoundException e) {
            Log.e("WRITE_ERR", "Cannot save data: " + e.getMessage());
            e.printStackTrace();
        }

    } //Called by on pause

    public void reloadData() {//Called by on resume

        try {
            FileInputStream fis = openFileInput(fn);
            Scanner scanner = new Scanner(fis);
            if (scanner.hasNextLine()) {
                String wallet = scanner.nextLine();
                String bet = scanner.nextLine();
                String pCardVal = scanner.nextLine();
                String hCardVal = scanner.nextLine();
                String pCardPos = scanner.nextLine();
                String hCardPos = scanner.nextLine();
                String dCardPos = scanner.nextLine();
                String previousCount = scanner.nextLine();
                String faceDownI = scanner.nextLine();
                String shuffleBool = scanner.nextLine();
                String playerBJ = scanner.nextLine();
                String playerB = scanner.nextLine();
                String houseB = scanner.nextLine();
                String playerTurnString = scanner.nextLine();
                String faceDownFlip = scanner.nextLine();
                String numHCardDealt = scanner.nextLine();
                String numPCardDealt = scanner.nextLine();
                String pAceCount = scanner.nextLine();
                String hAceCount = scanner.nextLine();
                String cardsDealtBool = scanner.nextLine();
                String buttonIdString = scanner.nextLine();
                String winString = scanner.nextLine();
                String cardsIdString = scanner.nextLine();
                String cardsValueString = scanner.nextLine();

                walletAmt = Integer.parseInt(wallet);
                currentBetAmt = Integer.parseInt(bet);
                playerCardValue = Integer.parseInt(pCardVal);
                houseCardValue = Integer.parseInt(hCardVal);
                playerCardPosition = Integer.parseInt(pCardPos);
                houseCardPosition = Integer.parseInt(hCardPos);
                deckCardPosition = Integer.parseInt(dCardPos);
                previousRoundCardCount = Integer.parseInt(previousCount);
                faceDownIndex = Integer.parseInt(faceDownI);
                shuffleCheck = Boolean.parseBoolean(shuffleBool);
                playerBlackJack = Boolean.parseBoolean(playerBJ);
                playerBust = Boolean.parseBoolean(playerB);
                houseBust = Boolean.parseBoolean(houseB);
                playerTurn = Boolean.parseBoolean(playerTurnString);
                faceDownFlipped = Boolean.parseBoolean(faceDownFlip);
                numHouseCardsDealt = Integer.parseInt(numHCardDealt);
                numPlayerCardsDealt = Integer.parseInt(numPCardDealt);
                playerAceCount = Integer.parseInt(pAceCount);
                houseAceCount = Integer.parseInt(hAceCount);
                buttonID = Integer.parseInt(buttonIdString);
                cardsDealt = Boolean.parseBoolean(cardsDealtBool);
                winnerString = winString;

                TextView bV = (TextView) findViewById(R.id.betTextView);
                TextView pCardValTV = (TextView) findViewById(R.id.playerScore);
                TextView hCardValTV = (TextView) findViewById(R.id.houseScore);

                if (pCardValTV != null && hCardValTV != null && bV != null) {
                    if (cardsDealt) {                       //Mid round
                        clearBoard(false);
                        setWalletView();
                        setDeckBack(cardsIdString, cardsValueString);
                        setCardViewsBack();

                        pCardValTV.setText(String.valueOf(playerCardValue));
                        hCardValTV.setText(String.valueOf(houseCardValue));

                        if (playerTurn) {//Player's turn and hasnt hit stand yet or busted
                            String st = "$" + currentBetAmt;
                            bV.setText(st);
                            setDealtView();
                            //Log.i("In:", "playerTurn true");

                        } else {//House Turn (Should display winner/loser)
                            showResult();
                            if (walletAmt == 0) gameOver();

                        }
                    } else {//Round hasnt started yet.
                        clearBoard(true);
                        setWalletView();
                        setDeckBack(cardsIdString, cardsValueString);
                        setCardViewsBack();
                        pCardValTV.setText("");
                        hCardValTV.setText("");

                        if (currentBetAmt == 0) {
                            String st = "Set bet";
                            bV.setText(st);
                        } else {//bets been placed but not dealt
                            String st = "$" + currentBetAmt;
                            bV.setText(st);
                        }
                    }
                }
            }
            }catch(FileNotFoundException e){
                // OK if file doesn't exist.
                clearBoard(true);
            }
        }

    public void onPause(){
        super.onPause();
        saveData();
    }

    public void onResume(){
        super.onResume();
        reloadData();

    }

}//BlackJackActivity

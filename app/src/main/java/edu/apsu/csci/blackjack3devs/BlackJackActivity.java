package edu.apsu.csci.blackjack3devs;

/**
 * Application: Blackjack
 * Course: CSCI4020 Spring 2017
 * Team Name: TBD
 * Developers: John Schmitt, Daniel Choi, Charles Fannin
 */

import android.provider.ContactsContract;
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

    // Vars for card values dealt to player and house.
    int playerCardValue = 0;
    int houseCardValue = 0;

    // Vars for tracking iteration of card positions.
    int playerCardPosition = 0;
    int houseCardPosition = 0;
    
    boolean cardsDealt = false;
    boolean playerStands = false;
    int dealerCardTotal = 0;
    int playerCardTotal = 0;
    int buttonID = R.drawable.deal; // ID for Deal Button and ClearBet(TEMP)
    
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

        for(int id : buttonsID){
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        // Allow betting only before Cards are dealt.
        if(!cardsDealt) {
            if (v.getId() == R.id.chip10) {
                if (walletAmt >= 10) {
                    updateWalletAndBet(10);
                }
            } else if (v.getId() == R.id.chip25) {
                if (walletAmt >= 25) {
                    updateWalletAndBet(25);
                }
            } else if (v.getId() == R.id.chip50) {
                if (walletAmt >= 50) {
                    updateWalletAndBet(50);
                }
            } else if (v.getId() == R.id.chip75) {
                if (walletAmt >= 75) {
                        updateWalletAndBet(75);
                }
            } else if (v.getId() == R.id.chip100) {
                if (walletAmt >= 100) {
                        updateWalletAndBet(100);
                }
            } else if (v.getId() == R.id.clearBet) {
                walletAmt += (CurrentBetAmt*2);
                CurrentBetAmt = 0;
                TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
                TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
                String newBet = Integer.toString(CurrentBetAmt);
                String newWallet = Integer.toString(walletAmt);
                betAmtTV.setText("$" + newBet);
                WalletAmtTV.setText("Wallet: $" + newWallet);

            }
            if (v.getId() == R.id.dealButton) {
                if (buttonID == R.drawable.deal) {
                    if (CurrentBetAmt > 0) {
                        cardsDealt = true;
                        deal();
                    } else {
                        Toast.makeText(getApplicationContext(), "Place a bet.", Toast.LENGTH_SHORT).show();
                    }
                }
                if (buttonID == R.drawable.clearbet) {
                    buttonID = R.drawable.deal;
                    ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
                    ib.setImageResource(R.drawable.deal);
                    cardsDealt = false;
                    clearBoard();
                }
            }
        }
        if (v.getId() == R.id.hitButton) {
            hit();
        }
        if (v.getId() == R.id.standButton) {
            Toast.makeText(getApplicationContext(), "STAND!", Toast.LENGTH_SHORT).show();
            stand();
        }
        if (v.getId() == R.id.doubleButton) {
            Toast.makeText(getApplicationContext(), "DOUBLE!", Toast.LENGTH_SHORT).show();
            doubleBet();
        }
    }

    public void updateWalletAndBet(int m){
        //Updates the wallet and bet textviews. Just to reduce the code a little bit.
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        CurrentBetAmt += m;
        walletAmt -= m;
        String newBet = Integer.toString(CurrentBetAmt);
        String newWallet = Integer.toString(walletAmt);
        betAmtTV.setText("$" + newBet);
        WalletAmtTV.setText("Wallet: $" + newWallet);

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
        ImageButton iv2 = (ImageButton) findViewById(R.id.dealButton);
            iv2.setVisibility(View.VISIBLE);
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
        return randomCardGenerator.nextInt(cardsArray.length - 1);
    }

    /**
     * deal supplies two initial cards to the player and the house to begin the game.
     */
    public void deal() {
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        dealClear.setVisibility(View.INVISIBLE);

        ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
        hitShow.setVisibility(View.VISIBLE);

        ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
        standShow.setVisibility(View.VISIBLE);

        // Place two cards for player and dealer.
        for (int i = 0; i < 2; i++) {

            // Deal cards; Pass iterator so method knows what card we are on.
            dealPlayerCard(i);
            dealHouseCard(i);
        }
        // Update display of card values.
        updateCardTotal();
    }

    public void dealPlayerCard(int i){
        int playerCardIndex = shuffleCards();
        setPlayerCardValue(cardsArray[playerCardIndex][1]);
        ImageView playerCard = (ImageView) findViewById(playerCardsID[i]);
        playerCard.setImageResource(cardsArray[playerCardIndex][0]);
        playerCard.setVisibility(View.VISIBLE);
        playerCardPosition++;
    }

    public void dealHouseCard(int i){
        int houseCardIndex = shuffleCards();
        setHouseCardValue(cardsArray[houseCardIndex][1]);
        ImageView houseCard = (ImageView) findViewById(houseCardsID[i]);
        houseCard.setVisibility(View.VISIBLE);
        if (i == 0) {
            houseCard.setImageResource(R.drawable.facedown);
        } else {
            houseCard.setImageResource(cardsArray[houseCardIndex][0]);
        }
        houseCardPosition++;
    }

    public void hit() {
        TextView walletTV2 = (TextView) findViewById(R.id.walletTextView);
        // Now using get methods to retrieve house and card totals for comparison.
        if (playerStands) {
            if (getHouseCardValue() > 21 && getPlayerCardValue() <= 21) {
                walletAmt += (CurrentBetAmt*2);
                walletTV2.setText("Wallet: "+ walletAmt);
                buttonID = R.drawable.clearbet;
                Toast.makeText(getApplicationContext(), "Player won from Stand", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Player Lost", Toast.LENGTH_SHORT).show();
            }
            ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
            dealClear.setVisibility(View.VISIBLE);
            dealClear.setImageResource(buttonID);

            ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
            hitShow.setVisibility(View.INVISIBLE);

            ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
            standShow.setVisibility(View.INVISIBLE);
        } else {
            dealPlayerCard(playerCardPosition);
            updateCardTotal();
        }
    }
    public void stand(){
        playerStands = true;
    }
    public void doubleBet(){

    }

    /**
     * setPlayerCardValue accumulates the value of the player's cards.
     * @param pCardVal
     */
    public void setPlayerCardValue(int pCardVal) {
        playerCardValue += pCardVal;
    }

    /**
     * setHouseCardValue accumulates the value of the house's cards.
     * @param hCardVal
     */
    public void setHouseCardValue(int hCardVal) {
        houseCardValue += hCardVal;
    }

    /**
     * getPlayerCardValue returns the value of the player's card.
     * @return The value of the player's card.
     */
    public int getPlayerCardValue() {
        return playerCardValue;
    }

    /**
     * getHouseCardValue returns the value of the house's card.
     * @return The value of the house's card.
     */
    public int getHouseCardValue() {
        return houseCardValue;
    }

    /**
     * updateCardTotal updates the UI with the player's and house's card totals.
     */
    public void updateCardTotal(){
        // Display player and house card values.
        TextView pScore = (TextView) findViewById(R.id.playerScore);
        pScore.setText(String.valueOf(getPlayerCardValue()));

        TextView hScore = (TextView) findViewById(R.id.houseScore);
        hScore.setText(String.valueOf(getHouseCardValue()));
    }

    @Override
    public void onBackPressed() {
        cardsDealt = false;
        playerStands = false;
        CurrentBetAmt = 0;
        clearBoard();
    }
}

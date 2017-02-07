package edu.apsu.csci.blackjack3devs;

/**
 * Application: Blackjack
 * Course: CSCI4020 Spring 2017
 * Team Name: TBD
 * Developers: John Schmitt, Daniel Choi, Charles Fannin
 */

import android.content.Intent;
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
import java.util.Random;

import static android.graphics.Color.BLACK;


public class BlackJackActivity extends AppCompatActivity
        implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    // Variable to hold bet amount
    int CurrentBetAmt = 0;
    int walletAmt = 2000;

    // Vars for card values dealt to player and house.
    int playerCardValue = 0;
    int houseCardValue = 0;

    // Vars for tracking iteration of card positions.
    int playerCardPosition = 0;
    int houseCardPosition = 0;
    int deckCardPosition = 0;

    //Remembering facedown card
    int faceDownIndex;

    //Check shuffle boolean
    boolean shuffleCheck = true;
    boolean playerTurn = true;
    boolean playerBlackJack = false;
    boolean playerBust = false;
    boolean houseBust = false;
    boolean faceDownFliped = false;

    // How many cards have been dealt
    int numHouseCardsDealt = 0;
    int numPlayerCardsDealt = 0;
    //int[][] houseCardImgId = new int[9][1]; Dont need i think
    int[][] playerCardImgId = new int[9][1];

    int playerAceCount = 0;
    int houseAceCount = 0;
    boolean cardsDealt = false;
    int buttonID = R.drawable.deal; // ID for Deal Button and ClearBet(TEMP)
    String winnerString = "None";

    // Initialize Card drawable/value array.
    private int[][] cardsArray = {
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

        clearBoard();

        for (int id : buttonsID) {
            ImageButton ib = (ImageButton) findViewById(id);
            ib.setOnClickListener(this);
        }
    }//OnCreate

    @Override
    public void onClick(View v) {
        // Allow betting only before Cards are dealt.
        if (!cardsDealt) {
            if (v.getId() == R.id.chip5) {
                if (walletAmt >= 5) {
                    updateWalletAndBet(5);
                }
            } else if (v.getId() == R.id.chip10) {
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
                TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
                TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
                walletAmt += CurrentBetAmt;
                CurrentBetAmt = 0;
                betAmtTV.setText("Set bet");
                WalletAmtTV.setText("Wallet: $" + walletAmt);
            }
            if (v.getId() == R.id.dealButton) {
                if (buttonID == R.drawable.deal) {
                    if (CurrentBetAmt > 0) {
                        cardsDealt = true;
                        deal();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Place a bet!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }else if (buttonID == R.drawable.refresh) {
                    clearBoard();
                }
            }
        }//Chips, clear, & deal

        if (v.getId() == R.id.hitButton)hit();
        if (v.getId() == R.id.standButton){
            playerTurn = false;
            endTurn();}
        if (v.getId() == R.id.doubleButton)doubleBet();
        if(v.getId() == R.id.dealButton && buttonID == R.drawable.refresh)clearBoard();// Has to be here so you cant bet until refresh button clicked this is 1 of 2 requirements
    }//OnClick

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.actionRestart){
            restartGame();
            return true;
        }else if(item.getItemId() == R.id.actionQuit){
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
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(BlackJackActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.show();
    }//PopUpMenu

    public void clearBoard() {

        ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
        ib.setImageResource(R.drawable.deal);
        ib.setVisibility(View.VISIBLE);

        TextView betTv = (TextView) findViewById(R.id.betTextView);
        betTv.setText("Set Bet");
        betTv.setBackgroundColor(0x00000000);

        int[] buttonsID = {R.id.doubleButton, R.id.hitButton, R.id.standButton};
        int[] textID = {R.id.houseScore, R.id.playerScore};

        for (int id : playerCardsID) {
            ImageView iv = (ImageView) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }//Hide all player cards
        for (int id : houseCardsID) {
            ImageView iv = (ImageView) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }//Hide all house cards
        for (int id : buttonsID) {
            ImageButton iv = (ImageButton) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }//Hide buttons
        for (int id : textID) {
            TextView tv = (TextView) findViewById(id);
            tv.setText("");
        }//Clear scores

        checkForShuffle();
        if(shuffleCheck)
            shuffleCards();

        buttonID = R.drawable.deal;
        CurrentBetAmt = 0;
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
        faceDownFliped = false;


        setHouseCardValue(0);
        setPlayerCardValue(0);
    }//Clear Board

    public void shuffleCards() {

        Toast toast = Toast.makeText(getApplicationContext(), "Shuffling Cards", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, -260);
        toast.show();

        Random randCard = new Random();
        for(int x = cardsArray.length - 1; x > 0; x--){

            int index = randCard.nextInt(x+1);
            //Store random number location between (0 to x) to temp variable
            int a = cardsArray[index][0];
            int b = cardsArray[index][1];
            //Store last card location to random number location
            cardsArray[index][0] = cardsArray[x][0];
            cardsArray[index][1] = cardsArray[x][1];
            //Store random number location to last card location so it is stored.
            cardsArray[x][0] = a;
            cardsArray[x][1] = b;
        }
        shuffleCheck = false;
    }//Shuffle Cards

    public void updateWalletAndBet(int m) {
        //Updates the wallet and bet textviews. Just to reduce the code a little bit.
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        CurrentBetAmt += m;
        walletAmt -= m;
        betAmtTV.setText("$" + CurrentBetAmt);
        WalletAmtTV.setText("Wallet: $" + walletAmt);

    }//Update Wallet and Bet

    public void deal() {
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
            dealClear.setVisibility(View.INVISIBLE);
        ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
            hitShow.setVisibility(View.VISIBLE);
        ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
            standShow.setVisibility(View.VISIBLE);

        for (int i = 0; i < 2; i++) {// Deal cards; Pass iterator so method knows what card we are on.

            hit();

            int y = dealHouseCard();
            setHouseCardValue(y);
            updateCardTotalDisplay(); // Update display of card values.
        }

    }//Deal cards

    public int dealPlayerCard() {
        ImageView playerCard = (ImageView) findViewById(playerCardsID[playerCardPosition]);
        playerCard.setImageResource(cardsArray[deckCardPosition][0]);
        playerCard.setVisibility(View.VISIBLE);
        playerCardImgId[numPlayerCardsDealt][0] = cardsArray[deckCardPosition][0];
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
            houseCard.setImageResource(R.drawable.facedown);
            faceDownIndex = deckCardPosition;
            deckCardPosition++;
            houseCardPosition++;
            houseCard.setVisibility(View.VISIBLE);
            return 0;
        }else if(houseCardPosition == 2 && !faceDownFliped){
            ImageView fdCard = (ImageView) findViewById(houseCardsID[0]);//Gets first card IV
            fdCard.setImageResource(cardsArray[faceDownIndex][0]);//Flips the card
            faceDownFliped = true;
            return cardsArray[faceDownIndex][1];//return facedown card value

        }else{
            houseCard.setImageResource(cardsArray[deckCardPosition][0]);

            int x = cardsArray[deckCardPosition][1];
            if(x == 11){//is Ace
                houseAceCount++;}
            houseCardPosition++;
            numHouseCardsDealt++;
            deckCardPosition++;
            houseCard.setVisibility(View.VISIBLE);
            return x;
        }

    }//dealHouseCard

    public void hit() {

        if (playerCardPosition < 9) {
            int x = dealPlayerCard();//deal player card and return card value
            setPlayerCardValue(x);
            checkIfWinner();
            updateCardTotalDisplay();
            }
        if(!playerTurn)
            endTurn();

    }

    public void checkIfWinner(){
        if(playerTurn) {
            if (playerCardValue > 21) {
                if (playerAceCount > 0) handlePlayerAces();
                else {
                    playerBust = true;
                    playerTurn = false;
                }
            } else if (playerCardValue == 21) {//Player got blackjack
                playerBlackJack = true;
                playerTurn = false;
            }
        }else {

            if(playerBust){
                winnerString = "Bust!\nHouse wins!";
                showResult();
            }else if(houseCardValue == 21 && playerBlackJack){
                winnerString = "Push\n Bet returned";
                walletAmt += (CurrentBetAmt);
            }else if (playerBlackJack){
                winnerString = "BLACKJACK!\nYou won: $"+(CurrentBetAmt*2)+"!";
                walletAmt += (CurrentBetAmt * 2);
                showResult();
            }else if(!playerBust && houseBust) {
                winnerString = "Player\nYou won: $"+(CurrentBetAmt*2)+"!";
                walletAmt += (CurrentBetAmt * 2);
                showResult();
            }else if(houseCardValue > playerCardValue){
                winnerString = "House wins!";
                showResult();
            }else if(houseCardValue < playerCardValue){
                winnerString = "Player\nYou won: $"+(CurrentBetAmt*2)+"!";
                walletAmt += (CurrentBetAmt * 2);
                showResult();
            }else if(houseCardValue == playerCardValue){
                winnerString = "Tie\n Bet returned";//At the moment just made it so he made his money back
                walletAmt += (CurrentBetAmt);
                showResult();
            }

        }
    }

    public void handlePlayerAces(){
        Log.i("====","Removing 10");
        playerCardValue = playerCardValue - 10;
        Log.i("====", "# of Ace: "+ playerAceCount);
        playerAceCount = playerAceCount - 1;//for some reason not removing
        Log.i("====", "# of Ace: "+ playerAceCount);
        updateCardTotalDisplay();

    }//Handle player Aces

    public void handleHouseAces(){
        houseCardValue = houseCardValue - 10;
        houseAceCount--;
        updateCardTotalDisplay();

    }//Handle player Aces

    public void endTurn() {
        findViewById(R.id.standButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.hitButton).setVisibility(View.INVISIBLE);

        if(!faceDownFliped){
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
        updateCardTotalDisplay(); // Update display of card values.
        checkIfWinner();

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
        pScore.setText(String.valueOf(playerCardValue));

        TextView hScore = (TextView) findViewById(R.id.houseScore);
        hScore.setText(String.valueOf(houseCardValue));
    }//Update card total display

    public void showResult(){
        cardsDealt = true;// Has to be here so you cant bet until refresh button clicked this is 2 of 2 requirements

        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        betAmtTV.setText(winnerString);
        betAmtTV.setBackgroundColor(BLACK);

        WalletAmtTV.setText("Wallet $"+walletAmt);
        buttonID = R.drawable.refresh;
        ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
        dealClear.setVisibility(View.VISIBLE);
        dealClear.setImageResource(buttonID);

    }

    public void gameover(){

        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        betAmtTV.setText("GAMEOVER");
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        WalletAmtTV.setText("Play again!");
    }//To be called when wallet = 0

    public void restartGame(){
        walletAmt = 2000;
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        WalletAmtTV.setText("Wallet: $"+walletAmt);
        clearBoard();
    }//MenuClickRestart

}//BlackJackActivity

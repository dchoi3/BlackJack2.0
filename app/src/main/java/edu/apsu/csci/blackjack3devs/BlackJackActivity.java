package edu.apsu.csci.blackjack3devs;

/**
 * Application: Blackjack
 * Course: CSCI4020 Spring 2017
 * Team Name: TBD
 * Developers: John Schmitt, Daniel Choi, Charles Fannin
 */

import android.content.Intent;
import android.os.Handler;
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

    // How many cards have been dealt
    int numHouseCardsDealt = 0;
    int numPlayerCardsDealt = 0;
    int[][] houseCardImgId = new int[9][1];
    int[][] playerCardImgId = new int[9][1];

    // Position of Aces (-1 is not drawn)
    int clubAcePos = -1;
    int spadeAcePos = -1;
    int heartAcePos = -1;
    int diamondAcePos = -1;

    // Boolean Vars to track if Ace has been played
    boolean clubAcePlayed = false;
    boolean spadeAcePlayed = false;
    boolean diamondAcePlayed = false;
    boolean heartAcePlayed = false;
    boolean cardsDealt = false;
    boolean playerStands = false;
    int buttonID = R.drawable.deal; // ID for Deal Button and ClearBet(TEMP)

    // Vars for Who wins
    String PlayerString = "Player";
    String HouseString = "House";
    String NoWinner = "None";

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

    private int[] buttonsID = {R.id.chip5, R.id.chip10, R.id.chip25, R.id.chip50, R.id.chip75, R.id.chip100, R.id.clearBet,
            R.id.hitButton, R.id.standButton, R.id.doubleButton, R.id.dealButton};

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
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
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
                walletAmt += CurrentBetAmt;
                CurrentBetAmt = 0;
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
                        Toast toast = Toast.makeText(getApplicationContext(), "Place a bet!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                }
                if (buttonID == R.drawable.refresh) {
                    buttonID = R.drawable.deal;
                    ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
                    ib.setImageResource(R.drawable.deal);
                    clearBoard();
                    clubAcePos = -1;
                    spadeAcePos = -1;
                    heartAcePos = -1;
                    diamondAcePos = -1;
                }
            }
        }
        if (v.getId() == R.id.hitButton) {
            hit();
        }
        if (v.getId() == R.id.standButton) {
            stand();
        }
        if (v.getId() == R.id.doubleButton) {
            Toast.makeText(getApplicationContext(), "DOUBLE!", Toast.LENGTH_SHORT).show();
            doubleBet();
        }
        // Has to be here so you cant bet until refresh button clicked this is 1 of 2 requirements
        if(v.getId() == R.id.dealButton && buttonID == R.drawable.refresh){
            cardsDealt = true;
            buttonID = R.drawable.deal;
            ImageButton ib = (ImageButton) findViewById(R.id.dealButton);
            ib.setImageResource(R.drawable.deal);
            clearBoard();
         }
    }//OnClick

    public void popupMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(BlackJackActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popupmenu, popup.getMenu());
        popup.show();
    }//PopUpMenu

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.actionRestart){
            clearBoard();
            return true;
        }else if(item.getItemId() == R.id.actionQuit){
            Intent aboutIntent = new Intent(this, HomeActivity.class);
            startActivity(aboutIntent);
            return true;
        }
        return false;
    }//Menu Item Click

    public void updateWalletAndBet(int m) {
        //Updates the wallet and bet textviews. Just to reduce the code a little bit.
        TextView betAmtTV = (TextView) findViewById(R.id.betTextView);
        TextView WalletAmtTV = (TextView) findViewById(R.id.walletTextView);
        CurrentBetAmt += m;
        walletAmt -= m;
        String newBet = Integer.toString(CurrentBetAmt);
        String newWallet = Integer.toString(walletAmt);
        betAmtTV.setText("$" + newBet);
        WalletAmtTV.setText("Wallet: $" + newWallet);

    }//Update Wallet and Bet

    public void clearBoard() {

        int[] allCardsID = {R.id.houseC1, R.id.houseC2, R.id.houseC3, R.id.houseC4, R.id.houseC5, R.id.houseC6,
                R.id.houseC7, R.id.houseC8, R.id.houseC9, R.id.playerC1, R.id.playerC2, R.id.playerC3,
                R.id.playerC4, R.id.playerC5, R.id.playerC6, R.id.playerC7, R.id.playerC8, R.id.playerC9,};

        int[] buttonsID = {R.id.doubleButton, R.id.hitButton, R.id.standButton};
        int[] textID = {R.id.houseScore, R.id.playerScore};
        TextView betTv = (TextView) findViewById(R.id.betTextView);

        for (int id : allCardsID) {
            ImageView iv = (ImageView) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }
        for (int id : buttonsID) {
            ImageButton iv = (ImageButton) findViewById(id);
            iv.setVisibility(View.INVISIBLE);
        }
        for (int id : textID) {
            TextView tv = (TextView) findViewById(id);
            tv.setText("");
        }
        findViewById(R.id.dealButton).setVisibility(View.VISIBLE);

        checkForShuffle();
        if(shuffleCheck){
            shuffleCards();
            shuffleCheck = false;
        }
        betTv.setText("$0");
        CurrentBetAmt = 0;
        playerStands = false;
        cardsDealt = false;
        setHouseCardValue(0);
        setPlayerCardValue(0);
        playerCardPosition = 0;
        houseCardPosition = 0;
        cardsDealt = false;
        playerStands = false;
        playerCardValue = 0;
        houseCardValue = 0;
        numHouseCardsDealt = 0;
        numPlayerCardsDealt = 0;
        clubAcePlayed = false;
        spadeAcePlayed = false;
        diamondAcePlayed = false;
        heartAcePlayed = false;
    }//Clear Board

    /**
     * shuffleCards uses multidimensional array to set a
     * randomly selected card's image and value.
     *
     * @return Returns the index of a single random card.
     */
    public void shuffleCards() {

        Toast toast = Toast.makeText(getApplicationContext(), "Shuffling Cards", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
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


        //Random randomCardGenerator = new Random();
        //return randomCardGenerator.nextInt(cardsArray.length-1);
    }//Shuffle Cards

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
        Log.i("--------","playercardvalue 1: "+getPlayerCardValue());
        // Place two cards for player and dealer.
        for (int i = 0; i < 2; i++) {
            // Deal cards; Pass iterator so method knows what card we are on.

            int x = dealPlayerCard(i);
            setPlayerCardValue(x);
            int y = dealHouseCard(i);
            setHouseCardValue(y);
            Log.i("--------","playercardvalue3: "+getPlayerCardValue());
        }
        updateCardTotal(); // Update display of card values.
    }//Deal cards

    public int dealPlayerCard(int i) {
        /*
        int playerCardIndex = shuffleCards();
        setPlayerCardValue(cardsArray[playerCardIndex][1]);
        ImageView playerCard = (ImageView) findViewById(playerCardsID[i]);
        playerCard.setImageResource(cardsArray[playerCardIndex][0]);
        playerCard.setVisibility(View.VISIBLE);
        playerCardPosition++;
        numPlayerCardsDealt++;
        playerCardImgId[numPlayerCardsDealt][0] = cardsArray[playerCardIndex][0];

        //Log.i("=====", "playercdID =    " + cardsArray[playerCardIndex][0]);
        //Log.i("=====", "PlayercdImgId = " + playerCardImgId[numPlayerCardsDealt][0]);
        */


        ImageView playerCard = (ImageView) findViewById(playerCardsID[i]);
        playerCard.setImageResource(cardsArray[deckCardPosition][0]);
        playerCard.setVisibility(View.VISIBLE);
        playerCardImgId[numPlayerCardsDealt][0] = cardsArray[deckCardPosition][0];
        int x = cardsArray[deckCardPosition][1];
        Log.i("--------","playercardvalue2: "+ x);
        playerCardPosition++;
        numPlayerCardsDealt++;
        deckCardPosition++;
        return x;


    }

    public int dealHouseCard(int i) {
        /*
        int houseCardIndex = shuffleCards();
        //removed to correct house value on facedown card setHouseCardValue(cardsArray[houseCardIndex][1]);
        ImageView houseCard = (ImageView) findViewById(houseCardsID[i]);
        houseCard.setVisibility(View.VISIBLE);
        if (i == 0) {
            houseCard.setImageResource(R.drawable.facedown);
             if(getHouseCardValue() == 0) {
                setHouseCardValue(0);
             }else{
                setHouseCardValue(cardsArray[houseCardIndex][1]);
            }
        } else {
            houseCard.setImageResource(cardsArray[houseCardIndex][0]);
            setHouseCardValue(cardsArray[houseCardIndex][1]);
            numHouseCardsDealt++;
            houseCardImgId[numHouseCardsDealt][0] = cardsArray[houseCardIndex][0];
        }
        houseCardPosition++;
        */


        ImageView houseCard = (ImageView) findViewById(houseCardsID[i]);
        if(i==0){
            houseCard.setImageResource(R.drawable.facedown);
            faceDownIndex = deckCardPosition;
            deckCardPosition++;
            houseCardPosition++;
            houseCard.setVisibility(View.VISIBLE);
            return 0;
        }else{
            houseCard.setImageResource(cardsArray[deckCardPosition][0]);
            numHouseCardsDealt++;
            houseCardImgId[numHouseCardsDealt][0] = cardsArray[deckCardPosition][0];
            int score = deckCardPosition;
            deckCardPosition++;
            houseCardPosition++;
            houseCard.setVisibility(View.VISIBLE);
            return cardsArray[score][1];

    }

    }//dealHouseCard

    public String CheckForWinner(boolean PlayerStands) {
        String Winner = NoWinner;
        if(PlayerStands) {
            if (getHouseCardValue() > 21 && getPlayerCardValue() <= 21) {
                Winner = PlayerString;
                //Toast.makeText(getApplicationContext(), "Player won the bet", Toast.LENGTH_SHORT).show();
            } else if (getHouseCardValue() == 21) {
                Winner = HouseString;
            } else if (getHouseCardValue() >= getPlayerCardValue() && getHouseCardValue() <= 21) {
                Winner = HouseString;
                //Toast.makeText(getApplicationContext(), "Player Lost the bet", Toast.LENGTH_SHORT).show();
            } else {
                Winner = NoWinner;
            }
        }else{
            if(getHouseCardValue() == 21){
                Winner = HouseString;
            }
        }
        return Winner;
    }
    public boolean ifPlayerHasAces(){
        boolean checkedAces = false;
        //Log.i("--------","Player Card > 21");
        int[] aces = {R.drawable.ca, R.drawable.ha, R.drawable.da, R.drawable.sa};
        for (int i = 0; i < numPlayerCardsDealt; i++) {
           // Log.i("=====","player card position "+i);
            for(int j=0; j<aces.length; j++) {
                // Out of bounds exception
                if (playerCardImgId[i][0] == aces[j]){
                    if(aces[j] == aces[0]){
                        clubAcePos = i;
                    }else if(aces[j] == aces[1]){
                        heartAcePos = i;
                    }else if(aces[j] == aces[2]){
                        diamondAcePos = i;
                    }else{
                        spadeAcePos = i;
                    }
                }
            }
            if(clubAcePos > 0 && playerCardValue > 21 && !clubAcePlayed){
                playerCardValue -= 10;
                clubAcePlayed = true;
                checkedAces = true;
            }
            if(heartAcePos > 0 && playerCardValue > 21 && !heartAcePlayed){
                playerCardValue -= 10;
                heartAcePlayed= true;
                checkedAces = true;
            }
            if(diamondAcePos > 0 && playerCardValue > 21 && !diamondAcePlayed){
                playerCardValue -= 10;
                diamondAcePlayed = true;
                checkedAces = true;
            }
            if(spadeAcePos > 0 && playerCardValue > 21 && !spadeAcePlayed){
                playerCardValue -=10;
                spadeAcePlayed = true;
                checkedAces = true;
            }
        }
        CheckForWinner(false);
        updateCardTotal();
        return checkedAces;
    }
    public void hit() {
        Log.i("====", "hit");
        TextView walletTV2 = (TextView) findViewById(R.id.walletTextView);
        TextView message = (TextView) findViewById(R.id.betTextView);
        // Now using get methods to retrieve house and card totals for comparison.
        // Check for winner before dealing another card
        // Convert Aces to 1 point if over 21

        String whoWon ="";
        Boolean blackJack; // If dealer has 21
        Boolean playerBust; // If Player hits over 21
        if(getHouseCardValue() == 21){
            blackJack = true;
        }else{
            blackJack = false;
        }
        if(playerCardValue > 21) {
            boolean hasAces = ifPlayerHasAces();

            if (!hasAces) {
                playerBust = true;
            } else {
                playerBust = false;
            }
        }else{
            playerBust = false;
        }
        if(playerStands || blackJack || playerBust) {
            Log.i("------", "Player has busted");
            if(playerBust){
                whoWon = HouseString;
            }else {
                whoWon = CheckForWinner(true);
            }
            if (houseCardPosition < 9) {

                int x = dealHouseCard(houseCardPosition);
                setHouseCardValue(x);
                updateCardTotal();
                if(houseCardValue > 21) {
                        int[] aces = {R.drawable.ca, R.drawable.ha, R.drawable.da, R.drawable.sa};
                        for (int i = 0; i < numHouseCardsDealt; i++) {
                            for(int j=0; j<aces.length; j++) {
                               // Out of bounds Exception
                                if (houseCardImgId[i][0] == aces[j]){
                                    if(aces[j] == aces[0]){
                                        clubAcePos = i;
                                    }else if(aces[j] == aces[1]){
                                        heartAcePos = i;
                                    }else if(aces[j] == aces[2]){
                                        diamondAcePos = i;
                                    }else{
                                        spadeAcePos =i;
                                    }
                                }
                            }
                            if(clubAcePos > 0 && houseCardValue > 21 && !clubAcePlayed){
                                houseCardValue -= 10;
                                clubAcePlayed = true;
                            }
                            if(heartAcePos > 0 && houseCardValue > 21 && !heartAcePlayed){
                                houseCardValue -= 10;
                                heartAcePlayed= true;
                            }
                            if(diamondAcePos > 0 && houseCardValue > 21 && !diamondAcePlayed){
                                houseCardValue -= 10;
                                diamondAcePlayed = true;
                            }
                            if(spadeAcePos > 0 && houseCardValue > 21 && !spadeAcePlayed){
                                houseCardValue -=10;
                                spadeAcePlayed = true;
                            }
                    }
                    updateCardTotal();
                }
            }
            if (whoWon.equals(PlayerString)) {
                walletAmt += (CurrentBetAmt * 2);
                walletTV2.setText("Wallet: " + walletAmt);
                buttonID = R.drawable.refresh;
                message.setText("You Win!");
            } else if (whoWon.equals(HouseString)) {
                //Toast.makeText(getApplicationContext(),"Dealer won game",Toast.LENGTH_SHORT).show();
                walletTV2.setText("Wallet: " + walletAmt);
                buttonID = R.drawable.refresh;
                message.setText("You Lost!");
            } else {
                buttonID = R.drawable.deal;
            }
        } else {
            CheckForWinner(false);
            if (playerCardPosition < 9) {
                int x = dealPlayerCard(playerCardPosition);
                setPlayerCardValue(x);
                updateCardTotal();
                if(playerCardValue > 21) {

                }
                updateCardTotal();
            }
        }
        if (whoWon.equals(PlayerString) || whoWon.equals(HouseString)) {
            cardsDealt = true;// Has to be here so you cant bet until refresh button clicked this is 2 of 2 requirements
            ImageButton dealClear = (ImageButton) findViewById(R.id.dealButton);
            dealClear.setVisibility(View.VISIBLE);
            dealClear.setImageResource(buttonID);

            ImageButton hitShow = (ImageButton) findViewById(R.id.hitButton);
            hitShow.setVisibility(View.INVISIBLE);

            ImageButton standShow = (ImageButton) findViewById(R.id.standButton);
            standShow.setVisibility(View.INVISIBLE);
        }
        updateCardTotal();
    }

    public void stand() {
        playerStands = true;
        findViewById(R.id.standButton).setVisibility(View.INVISIBLE);
    }

    public void checkForShuffle(){
        if(deckCardPosition > 30){
            shuffleCheck = true;
        }
    }

    public void doubleBet() {}

    public void setPlayerCardValue(int pCardVal) {playerCardValue += pCardVal;}//Set player card value

    public void setHouseCardValue(int hCardVal) {houseCardValue += hCardVal;}//Set house player value

    public int getPlayerCardValue() {return playerCardValue;}//Get player card value

    public int getHouseCardValue() {return houseCardValue;}//Get house card Value

    public void updateCardTotal() {
        // Display player and house card values.
        TextView pScore = (TextView) findViewById(R.id.playerScore);
        pScore.setText(String.valueOf(playerCardValue));

        TextView hScore = (TextView) findViewById(R.id.houseScore);
        hScore.setText(String.valueOf(houseCardValue));
    }//Update card total display

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }//On back pressed



}

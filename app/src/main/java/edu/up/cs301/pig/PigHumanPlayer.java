package edu.up.cs301.pig;

import edu.up.cs301.game.GameComputerPlayer;
import edu.up.cs301.game.GameHumanPlayer;
import edu.up.cs301.game.GameMainActivity;
import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameInfo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;

/**
 * A GUI for a human to play Pig. This default version displays the GUI but is incomplete
 *
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class PigHumanPlayer extends GameHumanPlayer implements OnClickListener {

	/* instance variables */

    // These variables will reference widgets that will be modified during play
    private TextView    playerScoreTextView = null;
    private TextView    oppScoreTextView    = null;
    private EditText    turnTotalView   = null;
    private TextView    messageTextView     = null;
    private ImageButton dieImageButton      = null;
    private Button      holdButton          = null;
    private LinearLayout gamePlayLayout = null;
    private TextView playerNameTextView = null;
    private TextView opponentNameTextView = null;
    private TextView opponentScoreTextView = null;


    private int turnValue = 0;
    private int opponentPrevScore = 0;
    private boolean dieClicked = false;
    private boolean holdClicked = false;
    private boolean playersTurn = true;
    private int initialSetup = 1;
    private String currPlayer = "";
    private String playerName = "";
    private String opponentName = "";


    // the android activity that we are running
    private GameMainActivity myActivity;

    /**
     * constructor does nothing extra
     */
    public PigHumanPlayer(String name) {
        super(name);
    }

    /**
     * Returns the GUI's top view object
     *
     * @return
     * 		the top object in the GUI's view heirarchy
     */
    public View getTopView() {
        return myActivity.findViewById(R.id.top_gui_layout);
    }

    /**
     * callback method when we get a message (e.g., from the game)
     *
     * @param info
     * 		the message
     */
    @Override
    public void receiveInfo(GameInfo info) {
        if(!(info instanceof PigGameState)){
            flash(0xFFFF0000, 10);
            return;
        }else{
            playerScoreTextView.setText("" + ((PigGameState) info).getPlayer0Score());
            oppScoreTextView.setText("" + ((PigGameState) info).getPlayer1Score());
            turnTotalView.setText("" + ((PigGameState) info).getRunningTotal());
            switch (((PigGameState) info).getDieValue()){
                case 1:
                    dieImageButton.setImageResource(R.drawable.face1);
                    break;
                case 2:
                    dieImageButton.setImageResource(R.drawable.face2);
                    break;
                case 3:
                    dieImageButton.setImageResource(R.drawable.face3);
                    break;
                case 4:
                    dieImageButton.setImageResource(R.drawable.face4);
                    break;
                case 5:
                    dieImageButton.setImageResource(R.drawable.face5);
                    break;
                case 6:
                    dieImageButton.setImageResource(R.drawable.face6);
                    break;
            }
        }

    }//receiveInfo

    /**
     * this method gets called when the user clicks the die or hold button. It
     * creates a new PigRollAction or PigHoldAction and sends it to the game.
     *
     * @param button
     * 		the button that was clicked
     */
    public void onClick(View button) {
        switch(button.getId()){
            case R.id.holdButton:
                game.sendAction(new PigHoldAction(this));
                dieClicked = false;
                holdClicked = true;
                turnValue = Integer.parseInt(turnTotalView.getText().toString());
                break;
            case R.id.dieButton:
                game.sendAction(new PigRollAction(this));
                dieClicked = true;
                holdClicked = false;
                break;
            default:
                break;
        }
    }// onClick

    /**
     * callback method--our game has been chosen/rechosen to be the GUI,
     * called from the GUI thread
     *
     * @param activity
     * 		the activity under which we are running
     */
    public void setAsGui(final GameMainActivity activity) {

        // remember the activity
        myActivity = activity;

        // Load the layout resource for our GUI
        activity.setContentView(R.layout.pig_layout);

        //Initialize the widget reference member variables
        this.playerScoreTextView = (TextView)activity.findViewById(R.id.yourScoreValue);
        this.oppScoreTextView    = (TextView)activity.findViewById(R.id.oppScoreValue);
        this.turnTotalView   = (EditText)activity.findViewById(R.id.turnTotalValue);
        this.messageTextView     = (TextView)activity.findViewById(R.id.messageTextView);
        this.dieImageButton      = (ImageButton)activity.findViewById(R.id.dieButton);
        this.holdButton          = (Button)activity.findViewById(R.id.holdButton);
        this.gamePlayLayout = activity.findViewById(R.id.gamePlayLayout);
        this.playerNameTextView = activity.findViewById(R.id.yourScoreText);
        this.opponentNameTextView = activity.findViewById(R.id.oppScoreText);
        this.opponentScoreTextView = activity.findViewById(R.id.oppScoreValue);

        //Listen for button presses
        dieImageButton.setOnClickListener(this);
        holdButton.setOnClickListener(this);


        /**
         External Citation
         Date: 13 October 2020
         Problem: Could not add a listener to the TextView to find when the value gets set
         Resource:
         https://www.javatpoint.com/android-edittext-with-textwatcher
         Solution: I used the example code of using a TextWatcher to set up my own
        */
        turnTotalView.setEnabled(false);
        turnTotalView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {}

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(initialSetup != 1) {

                    if (Integer.parseInt(turnTotalView.getText().toString()) == 0) {

                        String messageText;
                        if (playersTurn) {
                            gamePlayLayout.setBackgroundColor(Color.GRAY);
                            opponentPrevScore = Integer.parseInt(opponentScoreTextView.getText().toString());
                            currPlayer = playerName;
                            if (dieClicked) {
                                messageText = "Oh no! " + currPlayer + " rolled a 1 and lost everything!";
                                messageTextView.setText(messageText);
                            } else if (holdClicked) {
                                messageText = currPlayer + " has added " + turnValue + " points to their score";
                                messageTextView.setText(messageText);
                            }

                        } else {
                            gamePlayLayout.setBackgroundColor(Color.RED);
                            currPlayer = opponentName;
                            if (opponentPrevScore == Integer.parseInt(opponentScoreTextView.getText().toString())) {
                                messageText = "Oh no! " + currPlayer + " rolled a 1 and lost everything!";
                                messageTextView.setText(messageText);
                            } else {
                                int oppTurnScore = Integer.parseInt(opponentScoreTextView.getText().toString()) - opponentPrevScore;
                                messageText = currPlayer + " has added " + oppTurnScore + " points to their score";
                                messageTextView.setText(messageText);
                            }
                        }

                        playersTurn = !playersTurn;

                    }
                } else {
                    initialSetup = 0;
                    gamePlayLayout.setBackgroundColor(Color.RED);
                }

            }
        });



    }//setAsGui

    @Override
    protected void initAfterReady() {

        GamePlayer[] players = myActivity.getPlayers();
        GameHumanPlayer tempGHP;
        GameComputerPlayer tempGCP;

        if(players[0] instanceof GameHumanPlayer) {
             tempGHP = (GameHumanPlayer) players[0];
            playerName = tempGHP.getName();
        }else if(players[0] instanceof GameComputerPlayer){
            tempGCP = (GameComputerPlayer) players[0];
            opponentName = tempGCP.getName();
        }

        if(players[1] instanceof GameHumanPlayer) {
            tempGHP = (GameHumanPlayer) players[1];
            playerName = tempGHP.getName();
        }else if(players[1] instanceof GameComputerPlayer){
            tempGCP = (GameComputerPlayer) players[1];
            opponentName = tempGCP.getName();
        }


        playerNameTextView.setText(playerName);
        opponentNameTextView.setText(opponentName);

        messageTextView.setText("");

    }

    // Gui




}// class PigHumanPlayer

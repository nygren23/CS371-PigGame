package edu.up.cs301.pig;

import android.widget.EditText;

import edu.up.cs301.game.R;
import edu.up.cs301.game.infoMsg.GameState;

public class PigGameState extends GameState {

    private int turnPlayerId;
    private int player0Score;
    private int player1Score;
    private int runningTotal;
    private int dieValue;


    public PigGameState(){
        turnPlayerId = 0;
        player0Score = 0;
        player1Score = 0;
        runningTotal = 0;
        dieValue = 0;

    }

    public PigGameState(PigGameState pgs){
        this.player0Score = pgs.player0Score;
        this.player1Score = pgs.player1Score;
        this.turnPlayerId = pgs.turnPlayerId;
        this.runningTotal = pgs.runningTotal;
        this.dieValue = pgs.dieValue;
    }

    public int getTurnPlayerId() {
        return turnPlayerId;
    }

    public int getPlayer0Score() {
        return player0Score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getRunningTotal() {
        return runningTotal;
    }

    public int getDieValue() {
        return dieValue;
    }

    public void setTurnPlayerId(int turnPlayerId) {
        this.turnPlayerId = turnPlayerId;
    }

    public void setPlayer0Score(int player0Score) {
        this.player0Score = player0Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public void setRunningTotal(int runningTotal) {
        this.runningTotal = runningTotal;
    }

    public void setDieValue(int dieValue) {
        this.dieValue = dieValue;
    }

}

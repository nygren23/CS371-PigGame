package edu.up.cs301.pig;

import edu.up.cs301.game.GamePlayer;
import edu.up.cs301.game.LocalGame;
import edu.up.cs301.game.actionMsg.GameAction;
import edu.up.cs301.game.infoMsg.GameState;

import android.util.Log;

// dummy comment, to see if commit and push work from srvegdahl account

/**
 * class PigLocalGame controls the play of the game
 *
 * @author Andrew M. Nuxoll, modified by Steven R. Vegdahl
 * @version February 2016
 */
public class PigLocalGame extends LocalGame {

    PigGameState pigGameState;


    /**
     * This ctor creates a new game state
     */
    public PigLocalGame() {
        pigGameState = new PigGameState();
    }

    /**
     * can the player with the given id take an action right now?
     */
    @Override
    protected boolean canMove(int playerIdx) {
        if(pigGameState.getTurnPlayerId() != playerIdx)
            return false;
        return true;
    }

    /**
     * This method is called when a new action arrives from a player
     *
     * @return true if the action was taken or false if the action was invalid/illegal.
     */
    @Override
    protected boolean makeMove(GameAction action) {

        //HOLD action
        if(action instanceof PigHoldAction){
            if(pigGameState.getTurnPlayerId() == 0) {
                pigGameState.setPlayer0Score(pigGameState.getPlayer0Score() + pigGameState.getRunningTotal());
                if(players.length > 1){
                    pigGameState.setTurnPlayerId(1);
                }
            }
            else if(pigGameState.getTurnPlayerId() == 1) {
                pigGameState.setPlayer1Score(pigGameState.getPlayer1Score() + pigGameState.getRunningTotal());
                if(players.length > 1){
                    pigGameState.setTurnPlayerId(0);
                }
            }
            pigGameState.setRunningTotal(0);
            return true;


        //ROLL action
        }else if(action instanceof PigRollAction){

                pigGameState.setDieValue((int) (Math.random() * 6) + 1);

                if(pigGameState.getDieValue() != 1){
                    pigGameState.setRunningTotal(pigGameState.getRunningTotal() + pigGameState.getDieValue());
                } else {
                    pigGameState.setRunningTotal(0);
                    if (players.length == 2) {
                        if (pigGameState.getTurnPlayerId() == 0) {
                            pigGameState.setTurnPlayerId(1);
                        } else if (pigGameState.getTurnPlayerId() == 1) {
                            pigGameState.setTurnPlayerId(0);
                        }
                    }
                }

            return true;

        }
        return false;
    }//makeMove

    /**
     * send the updated state to a given player
     */
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        PigGameState temp = new PigGameState(this.pigGameState);
        p.sendInfo(temp);
    }//sendUpdatedSate

    /**
     * Check if the game is over
     *
     * @return
     * 		a message that tells who has won the game, or null if the
     * 		game is not over
     */
    @Override
    protected String checkIfGameOver() {
        if(pigGameState.getPlayer0Score() >= 50){
            return "Player 0 won the game with a score of " + pigGameState.getPlayer0Score();
        } else if(pigGameState.getPlayer1Score() >= 50){
            return "Player 1 won the game with a score of " + pigGameState.getPlayer1Score();
        }
        return null;
    }

}// class PigLocalGame

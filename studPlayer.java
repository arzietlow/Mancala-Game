/****************************************************************
 * studPlayer.java
 * Implements MiniMax search with A-B pruning and iterative deepening search (IDS). The static board
 * evaluator (SBE) function is simple: the # of stones in studPlayer's
 * mancala minue the # in opponent's mancala.
 * -----------------------------------------------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for educational purposes provided that
 * (1) you do not distribute or publish solutions, (2) you retain the notice, and (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

public class studPlayer extends Player {


	/*Use IDS search to find the best move. The step starts from 1 and keeps incrementing by step 1 until
	 * interrupted by the time limit. 
	 * 
	 * The best move found in each step should be stored in the
	 * protected variable move of class Player.
	 */
	public void move(GameState state)
	{
		int maxDepth = 1;
		for (int i = 0; i < 120; i++) {
			GameState newState = new GameState(state);
			this.move = maxAction(newState, maxDepth + i);
		}

	}


	// Return best move for max player. Note that this is a wrapper function created for ease to use.
	// In this function, you may do one step of search. 
	// Thus you can decide the best move by comparing the sbe values returned by maxSBE. 
	// This function should call minAction with 5 parameters.

	public int maxAction(GameState state, int maxDepth)
	{
		int[] v = maxAction(state, 0, maxDepth, -1000, 1000, 5);
		return v[1];
	}


	//return sbe value related to the best move for max player
	public int[] maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta, int rootBin)
	{
		int[] result = {alpha, rootBin};
		if (currentDepth == maxDepth) {
			result[0] = sbe(state);
			return result;
		}
		int v = -1000;
		for (int i = 0; i < 6; i++) {
			GameState tempState = new GameState(state);
			if (!tempState.illegalMove(i)) {
				tempState.applyMove(i);
				v = max(v, minAction(tempState, currentDepth+1, maxDepth, alpha, beta, i)[0]);
				result[0] = v;
				result[1] = i;
				if (v >= beta) return result;
				alpha = max(alpha, v);
			}
		}
		return result;

	}


	//return sbe value related to the best move for min player
	public int[] minAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta, int rootBin)
	{
		int[] result = {beta, rootBin};
		if (currentDepth == maxDepth) {
			result[0] = sbe(state);
			return result;
		}
		int v = 1000;
		for (int i = 0; i < 6; i++) {
			GameState tempState = new GameState(state);
			if (!tempState.illegalMove(i)) {
				tempState.applyMove(i);
				v = min(v, maxAction(tempState, currentDepth+1, maxDepth, alpha, beta, i)[0]);
				result[0] = v;
				result[1] = i;
				if (v <= alpha) return result;
				beta = min(beta, v);
			}
		}
		return result;
	}


	//the sbe function for game state. Note that in the game state, the bins for current player are always in the bottom row.
	private int sbe(GameState state)
	{
		int myStones = 0;
		int opStones = 0;
		for (int i = 0; i < 6; i++) {
			myStones += state.state[i];
		}
		for (int j = 7; j < 13; j++) {
			opStones += state.state[j];
		}
		return (state.state[6] - state.state[13]) + (myStones - opStones);
	}

	private int max(int a, int b) {
		if (b > a) return b;
		return a;
	}

	private int min(int a, int b) {
		if (b < a) return b;
		return a;
	}
}


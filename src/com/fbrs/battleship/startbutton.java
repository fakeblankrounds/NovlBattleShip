package com.fbrs.battleship;

import com.fbrs.rebound.UI.IClickable;

public class startbutton implements IClickable {
	
private GameState state;
	
	public startbutton(GameState game)
	{
		state = game;
	}

	@Override
	public void onClick(int x, int y) {
		state.startGame();
	}
}

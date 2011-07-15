package com.fbrs.battleship;

import com.fbrs.rebound.UI.IClickable;

public class UndoButton implements IClickable {
	
	private GameState state;
	
	public UndoButton(GameState game)
	{
		state = game;
	}

	@Override
	public void onClick(int x, int y) {
		state.Undo();
	}

}

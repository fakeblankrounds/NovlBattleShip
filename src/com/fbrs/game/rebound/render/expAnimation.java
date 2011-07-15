package com.fbrs.game.rebound.render;

import org.anddev.andengine.engine.handler.IUpdateHandler;

import com.fbrs.battleship.BattleShip;

public class expAnimation extends Animation implements IUpdateHandler{

	public expAnimation(String texture, int start_x, int start_y, int finish_x,
			int finish_y, int frames, boolean existing) {
		super(texture, start_x, start_y, finish_x, finish_y, frames, existing);
		pass_frames = 1;
	}
	@Override
	public int getX()
	{
		double a = 100 * Math.exp(-1 *(pass_frames * pass_frames));
		return (int) (s_x + (delta_x * a));
	}
	@Override
	public int getY()
	{
		return (int) (s_y + (delta_y * 100 * Math.exp(-1 * (pass_frames * pass_frames))));
	}
	
	@Override
	public void onUpdate(float pSecondsElapsed) {
		
		sprite.setPosition(getX(), getY());
		
		if(!Continue())
		{
			BattleShip.scene.unregisterUpdateHandler(this);
		}
	}

}

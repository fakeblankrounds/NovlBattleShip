package com.fbrs.game.rebound.render;

import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.entity.sprite.Sprite;

import com.fbrs.battleship.BattleShip;

public class Animation implements IUpdateHandler{
	
	protected int s_x, s_y, delta_x,delta_y, t_frames, pass_frames;
	protected boolean persistant = false;
	protected int delay = 0;
	protected Sprite sprite;
	
	public Animation(String texture, int start_x, int start_y, int finish_x, int finish_y, int frames, boolean existing)
	{
		s_x = start_x;
		s_y = start_y;
		delta_x = (finish_x - start_x) / frames;
		delta_y = (finish_y - start_y) / frames;
		t_frames = frames;
		pass_frames = 0;
	
		if(existing == false)
			sprite = TextureLoad.getSprite(TextureLoad.newSprite(start_x, start_y, 0, 1, texture, null));
		else
			sprite = TextureLoad.getSprite(texture);
	}
	
	public Animation(String texture, int resx, int resy, int start_x, int start_y, int finish_x, int finish_y, int frames, boolean existing)
	{
		s_x = start_x;
		s_y = start_y;
		delta_x = (finish_x - start_x) / frames;
		delta_y = (finish_y - start_y) / frames;
		t_frames = frames;
		pass_frames = 0;
		if(existing == false)
			sprite = TextureLoad.getSprite(TextureLoad.newSprite(resx, resy,  start_x, start_y, 0, 1, texture, null));
		else
			sprite = TextureLoad.getSprite(texture);
	}
	
	public void makePersistent()
	{
		persistant = true;
	}
	
	public void setDelay(int i)
	{
		delay = i;
	}
	
	public int getX()
	{
		
		return s_x + (delta_x * pass_frames);
	}
	
	public int getY()
	{
		return s_y + (delta_y * pass_frames);
	}
	
	public boolean Continue()
	{
		if(t_frames <= pass_frames)
		{
			if(delay > 0)
			{
				delay--;
				return true;
			}
			return false;
		}
		else {
			if(delay < 0)
				delay++;
			else
				pass_frames++;
		}
		return true;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		
		sprite.setPosition(getX(), getY());
		if(!Continue())
		{
			BattleShip.scene.unregisterUpdateHandler(this);
			if(!persistant)
			{
				BattleShip.scene.unregisterTouchArea(sprite);
				BattleShip.scene.getLastChild().detachChild(sprite);
			}
				
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

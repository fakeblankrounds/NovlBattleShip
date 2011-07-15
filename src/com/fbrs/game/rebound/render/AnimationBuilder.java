package com.fbrs.game.rebound.render;

import com.fbrs.battleship.BattleShip;
import com.fbrs.rebound.abstraction.AnimationFactory;
import com.fbrs.utils.math.LPoint;

public class AnimationBuilder extends AnimationFactory{
	
	@Override
	public void newAnimation(String texture, LPoint a, LPoint b, int frames, AnimationType type, boolean persistent, int delay)
	{
		Animation anim = null;
		if(type == AnimationType.linear)
			anim= new Animation(texture, (int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, false);
		if(type == AnimationType.explonential)
			anim = new expAnimation(texture, (int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, false);
		if(persistent)
			anim.makePersistent();
		anim.setDelay(delay);
		BattleShip.scene.registerUpdateHandler(anim);
	}
	@Override
	public void newAnimation(String texture,LPoint res, LPoint a, LPoint b, int frames, AnimationType type, boolean persistent, int delay)
	{
		Animation anim = null;
		if(type == AnimationType.linear)
			anim= new Animation(texture, (int) res.X, (int) res.Y,(int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, false);
		if(type == AnimationType.explonential)
			anim = new expAnimation(texture, (int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, false);
		if(persistent)
			anim.makePersistent();
		anim.setDelay(delay);
		BattleShip.scene.registerUpdateHandler(anim);
	}

	@Override
	public void existingAnimation(String texture, LPoint a, LPoint b, int frames, AnimationType type, int delay) {
		
		Animation anim = null;
		if(type == AnimationType.linear)
			anim= new Animation(texture, (int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, true);
		if(type == AnimationType.explonential)
			anim = new expAnimation(texture, (int)a.X, (int)a.Y, (int)b.X, (int)b.Y, frames, true);
		anim.makePersistent();
		anim.setDelay(delay);
		BattleShip.scene.registerUpdateHandler(anim);
		
	}
	
	
	
	

}

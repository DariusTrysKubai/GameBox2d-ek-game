package InputHandlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class PlayerGestureProcessor implements GestureListener{

		private boolean tap = false;
		
		public boolean getTap() {
			return tap;
		}
		
		public void resetTap() {
			tap = false;
		}
	
	   	@Override
	   	public boolean touchDown(float x, float y, int pointer, int button) {
	   		
		   	return false;
	   	}
		   	
		@Override
		public boolean tap(float x, float y, int count, int button) {
			tap = true;
			return false;
		}
			
		@Override
		public boolean longPress(float x, float y) {
			return false;
		}
			
		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
				
			return false;
		}
			
		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
				
			return false;
		}
			
		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
				
			return false;
		}
			
	   	@Override
	   	public boolean zoom (float originalDistance, float currentDistance){
		   		
		   return false;
	   	}

	   	@Override
	   	public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){
		   		
		   return false;
	   	}
	   	@Override
		public void pinchStop () {
		}
	}

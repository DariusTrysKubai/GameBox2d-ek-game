package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Player_control {

	Player player;
	Vector2 clicked_tile;
	
	boolean was_updated_last_frame = false;
	boolean target_updated = false;

	public Player_control(Player player) {
		this.player = player;
		clicked_tile = new Vector2();
	}

	public void create() {

	}

	public void update(OrthographicCamera cam) {

		// Get pressed tile
		if (Gdx.input.isTouched() && !was_updated_last_frame) {
			clicked_tile.x = Level.get_clicked_tile_x(cam);
			clicked_tile.y = Level.get_clicked_tile_y(cam);
			target_updated = true;
			was_updated_last_frame = true;
		}
		
		if(!Gdx.input.isTouched() && was_updated_last_frame) {
			was_updated_last_frame = false;
			target_updated = false;
		}

		// control with keyboard
		if (!player.moving) {
			if (Gdx.input.isKeyPressed(Keys.W)) {
				player.move_up();
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				player.move_right();
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				player.move_left();
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				player.move_down();
			}
		}

		// control with touch

		/*
		 * if (Gdx.input.isTouched()) { float x = Gdx.input.getX() -
		 * (Gdx.graphics.getWidth() / 2); float y = Gdx.input.getY() -
		 * (Gdx.graphics.getHeight() / 2); Vector2 vector = new Vector2(x, y); float
		 * angle = vector.angle(); if (angle < 45) { player.move_right(); } else if
		 * (angle < 135) { player.move_down(); } else if (angle < 225) {
		 * player.move_left(); } else if (angle < 315) { player.move_up(); } else if
		 * (angle < 360) { player.move_right(); }
		 * 
		 * System.out.println("Angle: " + angle + " touch x: " + x + " y: " + y); }
		 */

	}

	public void render(ShapeRenderer shape) {
		shape.begin(ShapeType.Line);
		shape.rect(clicked_tile.x * Level.tile_size, clicked_tile.y * Level.tile_size, Level.tile_size,
				Level.tile_size);
		shape.end();
	}
	
	public Vector2 get_target_tile() {
		return clicked_tile;
	}
	
	public boolean get_target_update() {
		return target_updated;
	}

}

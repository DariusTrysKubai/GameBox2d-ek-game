package player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

import ai_pathfinding.TmxTiledSmoothableGraphPath;
import main.Level;
import nodeNGraph.TmxFlatTiledNode;

public class Player_control {

	boolean debug = false;

	Player player;

	Player_pathfinding pathfinding;
	TmxTiledSmoothableGraphPath<TmxFlatTiledNode> path;
	ArrayList<Integer> path_dir;

	Level level;
	ShapeRenderer shape;

	Vector2 clicked_tile;
	public int move_direction = 0; // 0-up, 1-right, 2-down, 3-left
	public float shift = 0;
	public int dir = 2;
	public int dir_still = 2;
	private int path_movement_counting = 0;

	boolean was_updated_last_frame = false;
	boolean target_updated = false;

	boolean finished_moving = false;
	boolean moving = false;

	public Player_control(Player player, boolean debug) {
		this.player = player;
		this.debug = debug;
		clicked_tile = new Vector2();
		path_dir = new ArrayList<>();
		moving = false;
	}

	public void create(Level level, ShapeRenderer shape) {
		this.level = level;
		this.shape = shape;
		pathfinding = new Player_pathfinding(player, this, level);
		pathfinding.init(shape);
		pathfinding.create();
	}

	public void update(float dt, OrthographicCamera cam) {

		// Movement update

		// Moves the physical Box2d body
		if (moving) {
			switch (move_direction) {
			// up
			case 0:
				player.body.setTransform(player.body.getPosition().x, player.body.getPosition().y + (Player.SPEED * dt),
						0);
				shift += +(Player.SPEED * dt);
				break;
			// right
			case 1:
				player.body.setTransform(player.body.getPosition().x + (Player.SPEED * dt), player.body.getPosition().y,
						0);
				shift += +(Player.SPEED * dt);
				break;
			// down
			case 2:
				player.body.setTransform(player.body.getPosition().x, player.body.getPosition().y - (Player.SPEED * dt),
						0);
				shift += +(Player.SPEED * dt);
				break;
			// left
			case 3:
				player.body.setTransform(player.body.getPosition().x - (Player.SPEED * dt), player.body.getPosition().y,
						0);
				shift += +(Player.SPEED * dt);
				break;
			}
		}

		// check if reached the destination
		if (shift >= Level.tile_size) {
			set_moving(false);
			reset_shift();
			fix_position_in_tile();
		}

		target_updated = false;
		// Get pressed tile
		if (Gdx.input.isTouched() && !was_updated_last_frame) {
			clicked_tile.x = Level.get_clicked_tile_x(cam);
			clicked_tile.y = Level.get_clicked_tile_y(cam);

			// check if the target is not collision tile
			TiledMapTileLayer layer;
			layer = (TiledMapTileLayer) level.getMap().getLayers().get(Level.coll_layer);
			Cell cell = layer.getCell((int) (clicked_tile.x), (int) (clicked_tile.y));
			if (cell == null) {
				target_updated = true;
				was_updated_last_frame = true;
			}

		}

		if (!Gdx.input.isTouched() && was_updated_last_frame) {
			was_updated_last_frame = false;
			target_updated = false;
		}

		// if target was updated "target_updated" switches TRUE for 1 frame and then the
		// path to that location is calculated
		pathfinding.update(target_updated);

		// If target was updated generate new movement list
		if (target_updated) {
			// player.set_moving(true);
			path_dir = new ArrayList<>();
			path = pathfinding.getPath();
			int move_count = path.getCount();
			for (int i = 1; i < move_count; i++) {
				// calculate how to move along the path and generate path_dir array.

				int delta_x = path.get(i).x - path.get(i - 1).x;
				int delta_y = path.get(i).y - path.get(i - 1).y;
				System.out.println(i + " Delta x: " + delta_x + " y: " + delta_y);

				// check if move to right
				if (path.get(i).x > path.get(i - 1).x) {
					path_dir.add(1);
				}

				// left
				if (path.get(i).x < path.get(i - 1).x) {
					path_dir.add(3);
				}

				// up
				if (path.get(i).y > path.get(i - 1).y) {
					path_dir.add(0);
				}

				// down
				if (path.get(i).y < path.get(i - 1).y) {
					path_dir.add(2);
				}

				// System.out.println("node index: " + i + " x: " + path.get(i).x + " y: " +
			}

			System.out.print("Printing directions list: ");
			for (int x : path_dir) {
				System.out.print(x + " ");
			}
			System.out.println();
		}

		// move player along the path_dir
		if (path_dir != null && !path_dir.isEmpty()) {
			if (!moving) {
				switch (path_dir.get(path_movement_counting)) {
				case 0:
					path_movement_counting++;
					move_up();
					break;
				case 1:
					path_movement_counting++;
					move_right();
					break;
				case 2:
					path_movement_counting++;
					move_down();
					break;
				case 3:
					path_movement_counting++;
					move_left();
					break;
				}

			}
		}

		if (path_dir.size() == path_movement_counting) {
			set_moving(false);
			path_movement_counting = 0;
			path_dir.clear();
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

	public void render(OrthographicCamera cam) {

		if (debug) {
			pathfinding.render(cam);
		}

		shape.begin(ShapeType.Line);
		shape.setColor(Color.CORAL);
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

	public void path_dir_remove_first() {
		if (path_dir.size() != 1 && path_dir.size() != 0) {
			path_dir.remove(0);
		}
	}

	public void set_moving(boolean state) {
		this.moving = state;
	}

	public void fix_position_in_tile() {
		// System.out.println("Fixing position");
		// System.out.println("old x: " + body.getPosition().x + " y: " +
		// body.getPosition().y);
		float temp_x = (float) Math.floor(player.body.getPosition().x / Level.tile_size);
		float temp_y = (float) Math.floor(player.body.getPosition().y / Level.tile_size);
		temp_x *= Level.tile_size;
		temp_y *= Level.tile_size;
		temp_x += Level.tile_size / 2;
		temp_y += Level.tile_size / 2;
		player.body.setTransform(new Vector2(temp_x, temp_y), 0);
		// System.out.println("new x: " + body.getPosition().x + " y: " +
		// body.getPosition().y);
	}

	public void reset_shift() {
		shift = 0;
	}

	public void move_up() {
		// body.applyForceToCenter(new Vector2(0, 100000f), true);
		set_moving(true);
		move_direction = 0;
		dir = 0;
		dir_still = 0;
	}

	public void move_right() {
		// body.applyForceToCenter(new Vector2(100000f, 0), true);
		set_moving(true);
		move_direction = 1;
		dir = 1;
		dir_still = 1;
	}

	public void move_down() {
		// body.applyForceToCenter(new Vector2(0, -100000f), true);
		set_moving(true);
		move_direction = 2;
		dir = 2;
		dir_still = 2;
	}

	public void move_left() {
		// body.applyForceToCenter(new Vector2(-100000f, 0), true);
		set_moving(true);
		move_direction = 3;
		dir = 3;
		dir_still = 3;
	}

}

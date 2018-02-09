package player;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import InputHandlers.PlayerGestureProcessor;
import InputHandlers.PlayerInputProcessor;
import ai_pathfinding.TmxTiledSmoothableGraphPath;
import main.Level;
import nodeNGraph.TmxFlatTiledNode;
import scenes.Hud;

public class Player_control {

	boolean debug = true;

	Player player;

	Player_pathfinding pathfinding;
	TmxTiledSmoothableGraphPath<TmxFlatTiledNode> path;
	ArrayList<Integer> path_dir;

	Vector2 position_tile;

	// references
	Level level;
	ShapeRenderer shape;
	Hud hud;

	// Input control
	PlayerInputProcessor input;
	PlayerGestureProcessor input_gesture;

	Vector2 clicked_tile;
	public int move_direction = Player.UP; // 0-up, 1-right, 2-down, 3-left
	public float shift = 0;
	public int dir = Player.DOWN;
	public int dir_still = Player.DOWN;
	public int dir_to_item = Player.DOWN;

	boolean target_updated_last_frame = false;
	boolean target_updated = false;
	boolean is_target = false;
	boolean is_target_item = false;

	boolean waiting_for_path_update = false;
	boolean moving = false;
	boolean moving_to_next_tile = false;
	boolean moving_last_frame = false;

	public Player_control(Player player, boolean debug) {
		this.player = player;
		this.debug = debug;
		clicked_tile = new Vector2();
		path_dir = new ArrayList<Integer>();
		moving = false;
		position_tile = new Vector2();
		input = new PlayerInputProcessor();
		input_gesture = new PlayerGestureProcessor();
	}

	public void create(Level level, ShapeRenderer shape) {
		this.level = level;
		this.shape = shape;
		pathfinding = new Player_pathfinding(player, this, level);
		pathfinding.init(shape);
		pathfinding.create();

		position_tile.x = player.body.getPosition().x / 32;
		position_tile.y = player.body.getPosition().y / 32;
	}

	public void update(float dt, OrthographicCamera cam) {

		// Movement update

		// If player is not moving to another tile and the path is empty. Set movement
		// to false
		if (!moving_to_next_tile && path_dir.isEmpty()) {
			set_moving(false);
		}

		// if player is not moving to another tile, BUT the path has something inside,
		// initiate movement along the path
		if (moving_to_next_tile && !path_dir.isEmpty()) {
			update_movement(dt, path_dir.get(0));
		}

		// move player along the path_dir
		if ((!path_dir.isEmpty() && !moving_to_next_tile) && !waiting_for_path_update) {
			switch (path_dir.get(0)) {
			case 0:
				move_up();
				break;
			case 1:
				move_right();
				break;
			case 2:
				move_down();
				break;
			case 3:
				move_left();
				break;
			}
		}

		// check if reached the destination
		if (shift >= Level.tile_size) {
			set_moving_to_next_tile(false);
			reset_shift();
			fix_position_in_tile();
			// After moving to another tile update the path list with removing the done node
			if (!path_dir.isEmpty()) {
				path_dir.remove(0);
			}
		}

		// update standing tile
		position_tile.x = (float) Math.floor(player.body.getPosition().x / 32);
		position_tile.y = (float) Math.floor(player.body.getPosition().y / 32);

		// ---- CLICK ----
		target_updated = false;
		// Get pressed tile
		if (input.getPressed() && !target_updated_last_frame) {
			clicked_tile.x = Level.get_clicked_tile_x(cam);
			clicked_tile.y = Level.get_clicked_tile_y(cam);

			// check if the target is not collision tile
			TiledMapTileLayer layer;
			layer = (TiledMapTileLayer) level.getMap().getLayers().get(Level.coll_layer);
			Cell cell = layer.getCell((int) (clicked_tile.x), (int) (clicked_tile.y));
			if (cell == null) {
				target_updated = true;
				target_updated_last_frame = true;
				waiting_for_path_update = true;
			}

			// Check if clicked tile has an item
			if (level.checkForItem((int) clicked_tile.x, (int) clicked_tile.y)) {
				setTargetIsOnItem();
			} else if (is_target_item) {
				resetPathToItem();
			}

		}

		if (!input.getPressed() && target_updated_last_frame) {
			target_updated_last_frame = false;
			target_updated = false;
		}

		// if target was updated "target_updated" switches TRUE for 1 frame and then the
		// path to that location is calculated

		//

		if (waiting_for_path_update && !moving_to_next_tile) {
			pathfinding.update(true);
			waiting_for_path_update = false;
			generate_path_dir();

		}

		if ((target_updated && !moving_to_next_tile)) {
			pathfinding.update(target_updated);
			// If target was updated generate new movement list
			if (target_updated) {
				waiting_for_path_update = false;
				generate_path_dir();
			}

			if (is_target_item) {
				setPathToItem();
			}
		}

		// checks if it is the last movement frame
		if (moving) {
			moving_last_frame = true;
		}
		if (moving_last_frame && !moving) {
			moving_last_frame = false;
		}

		// Movement finishing code
		if ((path_dir.isEmpty() && !moving_to_next_tile) && moving_last_frame) {
			set_moving(false);
			if (is_target_item) {
				dir = dir_to_item;
				dir_still = dir_to_item;
				getItemHud();
			} else {
			}
			resetPathToItem();
		}

		// if( (path_dir.isEmpty() && !moving_to_next_tile) && ) { }

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
		float temp_x = (float) Math.floor(player.body.getPosition().x / Level.tile_size);
		float temp_y = (float) Math.floor(player.body.getPosition().y / Level.tile_size);
		temp_x *= Level.tile_size;
		temp_y *= Level.tile_size;
		temp_x += Level.tile_size / 2;
		temp_y += Level.tile_size / 2;
		player.body.setTransform(new Vector2(temp_x, temp_y), 0);
	}

	public void reset_shift() {
		shift = 0;
	}

	public void move_up() {
		// body.applyForceToCenter(new Vector2(0, 100000f), true);
		set_moving(true);
		set_moving_to_next_tile(true);
		move_direction = Player.UP;
		dir = Player.UP;
		dir_still = Player.UP;
	}

	public void move_right() {
		// body.applyForceToCenter(new Vector2(100000f, 0), true);
		set_moving(true);
		set_moving_to_next_tile(true);
		move_direction = Player.RIGHT;
		dir = Player.RIGHT;
		dir_still = Player.RIGHT;
	}

	public void move_down() {
		// body.applyForceToCenter(new Vector2(0, -100000f), true);
		set_moving(true);
		set_moving_to_next_tile(true);
		move_direction = Player.DOWN;
		dir = Player.DOWN;
		dir_still = Player.DOWN;
	}

	public void move_left() {
		// body.applyForceToCenter(new Vector2(-100000f, 0), true);
		set_moving(true);
		set_moving_to_next_tile(true);
		move_direction = Player.LEFT;
		dir = Player.LEFT;
		dir_still = Player.LEFT;
	}

	// This method moves players physical body according to the direction. Also
	// accumulates the shift / delta position.
	public void update_movement(float dt, int move_direction) {
		set_moving_to_next_tile(true);
		switch (move_direction) {
		// up
		case 0:
			player.body.setTransform(player.body.getPosition().x, player.body.getPosition().y + (Player.SPEED * dt), 0);
			shift += +(Player.SPEED * dt);
			break;
		// right
		case 1:
			player.body.setTransform(player.body.getPosition().x + (Player.SPEED * dt), player.body.getPosition().y, 0);
			shift += +(Player.SPEED * dt);
			break;
		// down
		case 2:
			player.body.setTransform(player.body.getPosition().x, player.body.getPosition().y - (Player.SPEED * dt), 0);
			shift += +(Player.SPEED * dt);
			break;
		// left
		case 3:
			player.body.setTransform(player.body.getPosition().x - (Player.SPEED * dt), player.body.getPosition().y, 0);
			shift += +(Player.SPEED * dt);
			break;
		}
	}

	public void set_moving_to_next_tile(boolean state) {
		moving_to_next_tile = state;
	}

	public void generate_path_dir() {
		path_dir.clear();
		path = pathfinding.getPath();
		int move_count = path.getCount();
		for (int i = 1; i < move_count; i++) {
			// calculate how to move along the path and generate path_dir array.

			// int delta_x = path.get(i).x - path.get(i - 1).x;
			// int delta_y = path.get(i).y - path.get(i - 1).y;
			// System.out.println(i + " Delta x: " + delta_x + " y: " + delta_y);

			// check if move to right

			int thisPathX = path.get(i).x;
			int prevPathX = path.get(i - 1).x;

			int thisPathY = path.get(i).y;
			int prevPathY = path.get(i - 1).y;

			if (thisPathX > prevPathX) {
				path_dir.add(1);
			}

			// left
			if (thisPathX < prevPathX) {
				path_dir.add(3);
			}

			// up
			if (thisPathY > prevPathY) {
				path_dir.add(0);
			}

			// down
			if (thisPathY < prevPathY) {
				path_dir.add(2);
			}
		}
	}

	public PlayerInputProcessor getInputProcessor() {
		return input;
	}
	
	public PlayerGestureProcessor getGestureProcessor() {
		return input_gesture;
	}

	public void initHud(Hud hud) {
		this.hud = hud;
	}

	public void setTargetIsOnItem() {
		is_target_item = true;

	}

	public void setPathToItem() {
		Gdx.app.log(this.getClass().getName(), "setting path to item");

		if (path_dir.size() == 1) {
			dir_to_item = path_dir.get(0);
			manualSetAnimation(dir_to_item);
			path_dir.clear();
			getItemHud();
		} else {
			dir_to_item = path_dir.get(path_dir.size() - 1);
			if (!path_dir.isEmpty()) {
				path_dir.remove(path_dir.size() - 1);
			}

		}
	}

	public void resetPathToItem() {
		is_target_item = false;
	}

	public void getItemHud() {
		hud.createItemHud(level.getItem((int) clicked_tile.x, (int) clicked_tile.y));
	}

	public boolean isMoving() {
		return moving;
	}
	
	public void manualSetAnimation(int direction) {
		dir = direction;
		dir_still = direction;
	}

}

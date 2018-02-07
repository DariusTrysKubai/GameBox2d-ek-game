package items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import items.items.Fruit_klerd;
import main.Level;
import player.Player;

public class ItemManager {

	boolean debug = false;

	// all items:

	ArrayList<Item> items_list;

	// Item list
	public static final int Fruit_klerd = 0;

	public static final float collision_size = 60;

	// references
	Player player;
	SpriteBatch sb;
	OrthographicCamera cam;
	OrthographicCamera hudcam;
	ShapeRenderer shape;
	Level level;

	public ItemManager() {

	}

	public void initLevel(Level level) {
		this.level = level;
	}

	public void init(SpriteBatch sb, OrthographicCamera cam, OrthographicCamera hudcam, ShapeRenderer shape,
			Player player) {
		this.sb = sb;
		this.cam = cam;
		this.shape = shape;
		this.player = player;
		this.hudcam = hudcam;
	}

	public void create() {
		items_list = new ArrayList<Item>();
	}

	public void update() {

	}

	public void render() {
		sb.setProjectionMatrix(cam.combined);
		cam.update();

		// Render all items
		for (Item item : items_list) {
			item.render(sb);
		}
	}

	public void dispose() {

	}

	public ItemManager get_manager() {
		return this;
	}

	public boolean check_click(float target_x, float target_y) {
		return false;
	}

	public ArrayList<Item> get_items() {
		return items_list;
	}

	// Adding with TILE coordinates
	public void add_item(int tile_x, int tile_y, int type) {
		switch (type) {
		case ItemManager.Fruit_klerd:
			items_list.add(new Fruit_klerd(tile_x, tile_y, type));
			break;
		}
	}

	// Adding with ABSOLUTE coordinates
	public void add_item(float x, float y, int type) {
		switch (type) {
		case ItemManager.Fruit_klerd:
			items_list.add(new Fruit_klerd(x, y, type));
			break;
		}
	}

	public void create_items() {
		for (Item item : items_list) {
			item.create();
			item.init(sb, cam, hudcam, shape, player, debug);
		}
	}

	public void remove_item(Item removing_item) {
		items_list.remove(removing_item);
		level.removeItem((int)removing_item.get_position_tile().x, (int)removing_item.get_position_tile().y);
		/*
		for (Item item : items_list) {
			if(item.equals(removing_item)) {
				items_list.remove(removing_item);
			}
		}
		*/
	}

}

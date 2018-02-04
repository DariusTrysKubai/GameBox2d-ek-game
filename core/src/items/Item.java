package items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;

import main.Level;
import player.Player;

public class Item {

	protected boolean debug = false;

	protected int index;
	protected int type;
	protected String name;
	protected Texture texture;
	protected Vector2 position_tile;
	protected float x, y;

	protected float width, height;

	public static final float collision_size = 120;
	public static final float item_size = 16;

	protected boolean is_taken;
	protected boolean is_destroyed;
	protected boolean is_pickable;

	protected SpriteBatch sb;
	protected OrthographicCamera cam;
	protected ShapeRenderer shape;
	protected OrthographicCamera hudcam;

	Player player;

	public Item() {
		position_tile = new Vector2();
	}
	
	public Item(int tile_x, int tile_y, int type) {
		position_tile = new Vector2();
		position_tile.x = tile_x;
		position_tile.y = tile_y;
		this.type = type;
		
		this.x = tile_x * Level.tile_size;
		this.y = tile_y * Level.tile_size;
		
		fix_position_in_tile();
		
		is_taken = false;
		is_destroyed = false;
		is_pickable = false;
	}
	
	public Item(float x, float y, int type) {
		position_tile = new Vector2();
		position_tile.x = x / Level.tile_size;
		position_tile.y = y / Level.tile_size;
		this.type = type;
		
		this.x = x;
		this.y = y;
		
		fix_position_in_tile();
		
		is_taken = false;
		is_destroyed = false;
		is_pickable = false;
	}

	public void create() {
	}

	public void init(SpriteBatch sb, OrthographicCamera cam, OrthographicCamera hudcam, ShapeRenderer shape,
			Player player, boolean debug) {
		this.sb = sb;
		this.cam = cam;
		this.shape = shape;
		this.player = player;
		this.hudcam = hudcam;
		this.debug = debug;
	}

	public void update(ItemManager manager) {

	}

	public void render(SpriteBatch sb) {
		sb.begin();
		//sb.setProjectionMatrix(cam.combined);
		sb.draw(texture, x, y);
		sb.end();
	}

	public void dispose() {

	}

	public void loadTexture(String path) {
		texture = new Texture(Gdx.files.internal(path));
	}

	public void pickItem() {
		is_taken = true;
		
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void destroy() {
		is_destroyed = true;
	}

	public void reset() {
		is_taken = false;
	}

	public void set_index(int i) {
		this.index = i;
	}

	public int get_index() {
		return index;
	}

	public boolean is_taken() {
		return is_taken;
	}
	
	public Vector2 get_position_tile() {
		return position_tile;
	}
	
	public void fix_position_in_tile() {
		float temp_x = (float) Math.floor(x / Level.tile_size);
		float temp_y = (float) Math.floor(y / Level.tile_size);
		temp_x *= Level.tile_size;
		temp_y *= Level.tile_size;
		temp_x += Level.tile_size / 2;
		temp_y += Level.tile_size / 2;
		x = temp_x - (item_size / 2);
		y = temp_y - (item_size / 2);
	}
}

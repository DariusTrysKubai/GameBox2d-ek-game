package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import items.Item;
import items.ItemManager;

public class Level {

	boolean debug = false;
	ShapeRenderer shape;

	World world;
	TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	public int collumn, row;

	public static final int coll_layer = 6;
	public static final int width = 100;
	public static final int height = 100;
	public static final int tile_size = 32;

	public Body bodyworld;
	
	Item[][] item_array;

	public Level(OrthographicCamera cam) {
		map = new TmxMapLoader().load("map/island.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1);
		renderer.setView(cam);
		world = new World(new Vector2(0, 0), false);
		item_array = new Item[width][height];
	}

	public void create(boolean debug) {

		this.debug = debug;

		BodyDef bodyDefworld = new BodyDef();
		FixtureDef fixtureDefworld = new FixtureDef();
		PolygonShape shapeworld = new PolygonShape();
	}

	public void init(ShapeRenderer shape) {
		this.shape = shape;
	}

	public void update() {

	}

	public void render(OrthographicCamera cam) {
		renderer.setView(cam);
		renderer.render();

		shape.setProjectionMatrix(cam.combined);
		cam.update();

	}

	public void dispose() {

	}

	public World getWorld() {
		return world;
	}

	public TiledMap getMap() {
		return map;
	}

	public Body getBodyWorld() {
		return bodyworld;
	}

	public static int get_clicked_tile_x(OrthographicCamera cam) {
		return (int) (cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).x / tile_size);
	}

	public static int get_clicked_tile_y(OrthographicCamera cam) {
		return (int) (cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)).y / tile_size);
	}
	
	public void load_items(ItemManager itemmanager) {
		int items_layer_index = (Integer) map.getProperties().get("items_layer");
		for (MapObject object : map.getLayers().get(items_layer_index).getObjects().getByType(RectangleMapObject.class)) {
			int type = (Integer) object.getProperties().get("type");
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			float x = rect.x;
			float y = rect.y;
			System.out.println("Loaded item! x: " + x + " y: " + y + " type: " + type);
			itemmanager.add_item(x, y, type);
		}
		
		// create and init all items
		itemmanager.create_items();
		
		// place items references in Level matrix/tiles
		for (Item item : itemmanager.get_items()) {
			item_array[(int) item.get_position_tile().x][(int) item.get_position_tile().y] = item;
		}
	}
	
	public boolean checkForItem(int x, int y) {
		if(item_array[x][y] != null) {
			return true;
		}else {
			return false;
		}
	}
	
	public Item getItem(int x, int y) {
		return item_array[x][y];
	}
	
	public void setItem(Item item, int x, int y) {
		item_array[x][y] = item;
	}
	
	public void removeItem(int x, int y) {
		item_array[x][y] = null;
	}

}

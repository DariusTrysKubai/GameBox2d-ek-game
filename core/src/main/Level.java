package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import states.GameState;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Level {

	boolean debug;
	ShapeRenderer shape;

	World world;
	TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	public int collumn, row;
	
	public static final int tile_size = 32;

	public Body bodyworld;

	public Level(OrthographicCamera cam) {
		map = new TmxMapLoader().load("map/island.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1);
		renderer.setView(cam);

		world = new World(new Vector2(0, 0), false);
	}

	public void create(boolean debug) {

		this.debug = debug;

		BodyDef bodyDefworld = new BodyDef();
		FixtureDef fixtureDefworld = new FixtureDef();
		PolygonShape shapeworld = new PolygonShape();

		// Extract collision layers from map

		// Rectangles from tiled cillision editor. Water
		/*
		 * for (MapObject object :
		 * map.getLayers().get("Lake").getObjects().getByType(RectangleMapObject.class))
		 * { Rectangle rect = ((RectangleMapObject) object).getRectangle();
		 * 
		 * bodyDefworld.type = BodyDef.BodyType.StaticBody;
		 * bodyDefworld.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() +
		 * rect.getHeight() / 2));
		 * 
		 * bodyworld = world.createBody(bodyDefworld);
		 * 
		 * shapeworld.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
		 * //.out.println(rect.getWidth()); fixtureDefworld.shape = shapeworld;
		 * bodyworld.createFixture(fixtureDefworld); }
		 */

		// Rectangles
		for (MapObject object : map.getLayers().get("collision_blocks").getObjects()
				.getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bodyDefworld.type = BodyDef.BodyType.StaticBody;
			bodyDefworld.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

			bodyworld = world.createBody(bodyDefworld);

			shapeworld.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			// .out.println(rect.getWidth());
			fixtureDefworld.shape = shapeworld;
			bodyworld.createFixture(fixtureDefworld);
		}

		// Polygons

		for (MapObject object : map.getLayers().get("Collision_poly").getObjects().getByType(PolygonMapObject.class)) {
			Polygon poly = ((PolygonMapObject) object).getPolygon();
			bodyDefworld.type = BodyDef.BodyType.StaticBody;
			bodyDefworld.position.set(poly.getX(), poly.getY());

			bodyworld = world.createBody(bodyDefworld);

			shapeworld.set(poly.getVertices());
			fixtureDefworld.shape = shapeworld;
			bodyworld.createFixture(fixtureDefworld);
		}

		for (MapObject object : map.getLayers().get("Lake").getObjects().getByType(RectangleMapObject.class)) {
			object.getName();
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bodyDefworld.type = BodyDef.BodyType.StaticBody;
			bodyDefworld.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));
			bodyworld = world.createBody(bodyDefworld);
			shapeworld.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			fixtureDefworld.shape = shapeworld;
			bodyworld.createFixture(fixtureDefworld);
		}

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
		if (debug) {
			for (int i = 0; i < 50; i++) {
				for (int j = 0; j < 50; j++) {
					shape.begin(ShapeType.Line);
					shape.setColor(Color.BROWN);
					shape.rect(i * 32, j * 32, 32, 32);
					shape.end();
				}
			}
		}
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

}

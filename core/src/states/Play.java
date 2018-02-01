package states;

import java.awt.geom.RectangularShape;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Handlers.GameStateManager;
import scenes.Hud;

public class Play extends GameState {

	boolean debug = false;

	Hud hud;

	World world;
	TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	Box2DDebugRenderer debugRenderer;
	public int collumn, row;
	public float tile_size;

	// Player
	BodyDef bodyDef;
	Body body;
	PolygonShape shape;
	FixtureDef fixtureDef;
	Fixture fixture;
	
	public static final float player_friction = 8;

	boolean moving = false;

	// world
	Body bodyworld;

	public Play(GameStateManager gsm) {
		super(gsm);

		//System.out.println(Gdx.files.internal("TiledMap_box2d_test.tmx").file().getAbsolutePath());
		map = new TmxMapLoader().load("map/TiledMap_box2d_test.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1);
		renderer.setView(cam);
		debugRenderer = new Box2DDebugRenderer();
		world = new World(new Vector2(0, 0), false);

		hud = new Hud(sb);

		// First we create a body definition
		bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we
		// would set it to StaticBody
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(200, 250);
		bodyDef.fixedRotation = true;
		// Create our body in the world using our body definition
		body = world.createBody(bodyDef);
		// Create a circle shape and set its radius to 6
		shape = new PolygonShape();
		shape.setAsBox(32 / 2, 32 / 2);

		// Create a fixture definition to apply our shape to
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.2f;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.5f; // Make it bounce a little bit
		// Create our fixture and attach it to the body
		fixture = body.createFixture(fixtureDef);

		BodyDef bodyDefworld = new BodyDef();
		FixtureDef fixtureDefworld = new FixtureDef();
		PolygonShape shapeworld = new PolygonShape();


		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bodyDefworld.type = BodyDef.BodyType.StaticBody;
			bodyDefworld.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() + rect.getHeight() / 2));

			bodyworld = world.createBody(bodyDefworld);

			shapeworld.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
			//.out.println(rect.getWidth());
			fixtureDefworld.shape = shapeworld;
			bodyworld.createFixture(fixtureDefworld);
		}

		for (MapObject object : map.getLayers().get(4).getObjects().getByType(PolygonMapObject.class)) {
			//System.out.println(object.getName());
			Polygon poly = ((PolygonMapObject) object).getPolygon();
			bodyDefworld.type = BodyDef.BodyType.StaticBody;
			bodyDefworld.position.set(poly.getX(), poly.getY());

			bodyworld = world.createBody(bodyDefworld);

			shapeworld.set(poly.getVertices());
			fixtureDefworld.shape = shapeworld;
			bodyworld.createFixture(fixtureDefworld);
		}
	}

	public void update(float dt) {

		hud.update(dt);

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			cam.position.y += 10;
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			cam.position.y -= 10;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			cam.position.x += 10;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			cam.position.x -= 10;
		}

		moving = false;
		if (Gdx.input.isKeyPressed(Keys.W)) {
			body.applyForceToCenter(new Vector2(0, 100000f), true);
			moving = true;
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			body.applyForceToCenter(new Vector2(100000f, 0), true);
			moving = true;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			body.applyForceToCenter(new Vector2(-100000f, 0), true);
			moving = true;
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			body.applyForceToCenter(new Vector2(0, -100000f), true);
			moving = true;
		}
		
		//System.out.println(body.getLinearVelocity().len());
		if(!moving && body.getLinearVelocity().len() != 0) {
			Vector2 temp_vector = body.getLinearVelocity();
			float temp = (Math.max( (temp_vector.len() - player_friction), 0));
			temp_vector.setLength(temp);
			body.setLinearVelocity(temp_vector);
		}

		cam.update();
		renderer.setView(cam);
	}

	public void render() {
		Gdx.gl.glClearColor(1 / 255f, 75 / 255f, 62 / 255f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		renderer.render();
		debugRenderer.render(world, cam.combined);

		hud.stage.draw();

		world.step(1 / 60f, 6, 2);
	}

	public void dispose() {
		renderer.dispose();
		map.dispose();
		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		shape.dispose();
	}

	@Override
	public void handleInput() {
		// TODO Auto-generated method stub

	}

}

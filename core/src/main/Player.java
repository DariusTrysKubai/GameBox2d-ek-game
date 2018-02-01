package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Player {

	BodyDef bodyDef;
	Body body;
	PolygonShape shape;
	FixtureDef fixtureDef;
	Fixture fixture;
	public static final float player_friction = 8;
	boolean moving = false;

	// references
	Level level;

	public Player(World world) {

		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		shape = new PolygonShape();
		shape.setAsBox(32 / 2, 32 / 2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.2f;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.5f;
		fixture = body.createFixture(fixtureDef);

	}

	public void create() {

	}

	public void init(Level level) {
		this.level = level;
		spawn(0);
	}

	public void update() {

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

		// players friction
		if (!moving && body.getLinearVelocity().len() != 0) {
			Vector2 temp_vector = body.getLinearVelocity();
			float temp = (Math.max((temp_vector.len() - player_friction), 0));
			temp_vector.setLength(temp);
			body.setLinearVelocity(temp_vector);
		}
	}

	public void render() {

	}

	public void dispose() {

	}

	public Body getBody() {
		return body;
	}

	public void spawn(int spawn_point) {

		System.out.println("Spawning player at spawnpoint: " + spawn_point);
		// BodyDef bodyDefworld = new BodyDef();
		// FixtureDef fixtureDefworld = new FixtureDef();
		// shapeworld = new PolygonShape();

		for (MapObject object : level.getMap().getLayers().get("spawn_points").getObjects()
				.getByType(RectangleMapObject.class)) {
			if (Integer.parseInt(object.getName()) == spawn_point) {
				System.out.println("Name: " + object.getName());
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				System.out.println("x: " + rect.x + " y: " + rect.y);
				body.setTransform(new Vector2(rect.x, rect.y), 0);
			}

		}

		// code for collision
		/*
		 * for (MapObject object :
		 * level.getMap().getLayers().get(3).getObjects().getByType(RectangleMapObject.
		 * class)) { Rectangle rect = ((RectangleMapObject) object).getRectangle();
		 * 
		 * bodyDefworld.type = BodyDef.BodyType.StaticBody;
		 * bodyDefworld.position.set((rect.getX() + rect.getWidth() / 2), (rect.getY() +
		 * rect.getHeight() / 2));
		 * 
		 * level.bodyworld = level.getWorld().createBody(bodyDefworld);
		 * 
		 * shapeworld.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
		 * //.out.println(rect.getWidth()); fixtureDefworld.shape = shapeworld;
		 * level.bodyworld.createFixture(fixtureDefworld); }
		 */
	}

}

package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
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
import com.badlogic.gdx.scenes.scene2d.ui.Table.Debug;
import com.mygdx.game.Game;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Player {

	boolean debug;

	// Player classes
	Player_control control;

	int move_direction = 0; // 0-up, 1-right, 2-down, 3-left
	Vector2 position;
	Vector2 position_tile;
	BodyDef bodyDef;
	Body body;
	FixtureDef fixtureDef;
	Fixture fixture;
	public static final float player_friction = 8;
	public static final float max_speed = 35;
	public static final float SPEED = 60;
	public float shift = 0;
	boolean moving = false;

	// Textures
	SpriteBatch sb;
	Texture texture;
	int dir = 2;
	int dir_still = 2;
	Texture anim;
	int FRAME_COLS = 9, FRAME_ROWS = 4;
	float texture_size;

	ShapeRenderer shape;
	OrthographicCamera cam;

	Animation<TextureRegion> walkAnimation_down;
	Animation<TextureRegion> walkAnimation_up;
	Animation<TextureRegion> walkAnimation_right;
	Animation<TextureRegion> walkAnimation_left;

	Animation<TextureRegion> walkAnimation;
	Animation<TextureRegion> still_down;
	Animation<TextureRegion> still_up;
	Animation<TextureRegion> still_right;
	Animation<TextureRegion> still_left;

	float stateTime;

	boolean is_destoyed;

	// references
	Level level;

	public Player(World world, boolean debug) {

		this.debug = debug;
		control = new Player_control(this);

		// box2d create
		PolygonShape shape;
		position = new Vector2();
		position_tile = new Vector2();
		bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(0, 0);
		bodyDef.fixedRotation = true;
		body = world.createBody(bodyDef);
		shape = new PolygonShape();
		shape.setAsBox(16 / 2, 16 / 2);
		fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 0.2f;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.0f;
		fixture = body.createFixture(fixtureDef);

		// textures and animations
		anim = new Texture("player/player_sheet.png");

		TextureRegion[][] tmp = TextureRegion.split(anim, anim.getWidth() / FRAME_COLS, anim.getHeight() / FRAME_ROWS);

		TextureRegion[] walkFrames_down = new TextureRegion[FRAME_COLS];
		TextureRegion[] walkFrames_up = new TextureRegion[FRAME_COLS];
		TextureRegion[] walkFrames_right = new TextureRegion[FRAME_COLS];
		TextureRegion[] walkFrames_left = new TextureRegion[FRAME_COLS];

		TextureRegion walkFrames_still = new TextureRegion();
		TextureRegion walkFrames_still_down = new TextureRegion();
		TextureRegion walkFrames_still_up = new TextureRegion();
		TextureRegion walkFrames_still_right = new TextureRegion();
		TextureRegion walkFrames_still_left = new TextureRegion();

		int index = 0;
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames_up[index++] = tmp[0][i];

		}
		index = 0;
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames_left[index++] = tmp[1][i];

		}
		index = 0;
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames_down[index++] = tmp[2][i];

		}
		index = 0;
		for (int i = 0; i < FRAME_COLS; i++) {
			walkFrames_right[index++] = tmp[3][i];
		}

		walkFrames_still_down = tmp[2][0];
		walkFrames_still_up = tmp[0][0];
		walkFrames_still_right = tmp[3][0];
		walkFrames_still_left = tmp[1][0];

		walkAnimation_down = new Animation<TextureRegion>(0.125f, walkFrames_down);
		walkAnimation_up = new Animation<TextureRegion>(0.125f, walkFrames_up);
		walkAnimation_left = new Animation<TextureRegion>(0.125f, walkFrames_left);
		walkAnimation_right = new Animation<TextureRegion>(0.125f, walkFrames_right);

		// walkAnimation_down.setPlayMode(PlayMode.LOOP_PINGPONG);
		// walkAnimation_up.setPlayMode(PlayMode.LOOP_PINGPONG);
		// walkAnimation_left.setPlayMode(PlayMode.LOOP_PINGPONG);
		// walkAnimation_right.setPlayMode(PlayMode.LOOP_PINGPONG);

		walkAnimation = walkAnimation_down;

		still_down = new Animation<TextureRegion>(0.125f, walkFrames_still_down);
		still_up = new Animation<TextureRegion>(0.125f, walkFrames_still_up);
		still_left = new Animation<TextureRegion>(0.125f, walkFrames_still_left);
		still_right = new Animation<TextureRegion>(0.125f, walkFrames_still_right);

		texture_size = tmp[0][0].getRegionWidth();
		moving = false;

		position.x = ((Game.ORIGINAL_WIDTH / 2) - (texture_size / 2)) * 2;
		position.y = ((Game.ORIGINAL_HEIGHT / 2) - (texture_size / 2)) * 2;

		position_tile.x = body.getPosition().x / 32;
		position_tile.y = body.getPosition().y / 32;
	}

	public void create(SpriteBatch sb, ShapeRenderer shape, OrthographicCamera cam) {
		this.sb = sb;
		this.shape = shape;
		this.cam = cam;
	}

	public void initLevel(Level level) {
		this.level = level;
		spawn(3);
	}

	public void update(float dt) {

		// moving = false;

		control.update(cam);

		// players friction
		/*
		 * if (!moving && body.getLinearVelocity().len() != 0) { Vector2 temp_vector =
		 * body.getLinearVelocity(); float temp = (Math.max((temp_vector.len() -
		 * player_friction), 0)); temp_vector.setLength(temp);
		 * body.setLinearVelocity(temp_vector); }
		 */

		// Max speed
		/*
		 * Vector2 temp_vector = body.getLinearVelocity(); float temp =
		 * (Math.min(temp_vector.len(), Player.max_speed)); temp_vector.setLength(temp);
		 * body.setLinearVelocity(temp_vector);
		 */

		// Moving boolean
		/*
		 * if (body.getLinearVelocity().len() == 0) { moving = false; } else { moving =
		 * true; }
		 */

		// update standing tile
		position_tile.x = (float) Math.floor(body.getPosition().x / 32);
		position_tile.y = (float) Math.floor(body.getPosition().y / 32);

		// MAIN MOVEMENT

		if (moving) {
			switch (move_direction) {
			// up
			case 0:
				body.setTransform(body.getPosition().x, body.getPosition().y + (Player.SPEED * dt), 0);
				shift += +(Player.SPEED * dt);
				break;
			// right
			case 1:
				body.setTransform(body.getPosition().x + (Player.SPEED * dt), body.getPosition().y, 0);
				shift += +(Player.SPEED * dt);
				break;
			// down
			case 2:
				body.setTransform(body.getPosition().x, body.getPosition().y - (Player.SPEED * dt), 0);
				shift += +(Player.SPEED * dt);
				break;
			// left
			case 3:
				body.setTransform(body.getPosition().x - (Player.SPEED * dt), body.getPosition().y, 0);
				shift += +(Player.SPEED * dt);
				break;
			}
		}

		// check if reached the destination
		if (shift >= Level.tile_size) {
			moving = false;
			reset_shift();
			fix_position_in_tile();
		}

		// Update players animation
		if (moving) {
			if (dir == 0) {
				walkAnimation = walkAnimation_up;
			}
			if (dir == 1) {
				walkAnimation = walkAnimation_right;
			}
			if (dir == 2) {
				walkAnimation = walkAnimation_down;
			}
			if (dir == 3) {
				walkAnimation = walkAnimation_left;
			}
		} else {
			if (dir_still == 0) {
				walkAnimation = still_up;
			}
			if (dir_still == 1) {
				walkAnimation = still_right;
			}
			if (dir_still == 2) {
				walkAnimation = still_down;
			}
			if (dir_still == 3) {
				walkAnimation = still_left;
			}
		}
	}

	public void render(OrthographicCamera cam) {

		if (debug) {

			// shape.setProjectionMatrix(cam.combined);
			// cam.update();
			shape.begin(ShapeType.Line);
			shape.setColor(Color.RED);
			shape.rect(position_tile.x * 32, position_tile.y * 32, 32, 32);
			shape.end();

			control.render(shape);
		}

		stateTime += Gdx.graphics.getDeltaTime();
		sb.setProjectionMatrix(cam.combined);
		TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime, true);
		sb.begin();
		sb.draw(currentFrame, body.getPosition().x - (texture_size / 2), body.getPosition().y - 8);
		sb.end();
	}

	public void dispose() {

	}

	public Body getBody() {
		return body;
	}

	public void spawn(int spawn_point) {

		//System.out.println("Spawning player at spawnpoint: " + spawn_point);
		// BodyDef bodyDefworld = new BodyDef();
		// FixtureDef fixtureDefworld = new FixtureDef();
		// shapeworld = new PolygonShape();

		for (MapObject object : level.getMap().getLayers().get("spawn_points").getObjects()
				.getByType(RectangleMapObject.class)) {
			if (Integer.parseInt(object.getName()) == spawn_point) {
				//System.out.println("Name: " + object.getName());
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				//System.out.println("x: " + rect.x + " y: " + rect.y);
				// set the player in the middle of the tile
				float temp_x = (float) Math.floor(rect.x / Level.tile_size);
				float temp_y = (float) Math.floor(rect.y / Level.tile_size);
				temp_x *= Level.tile_size;
				temp_y *= Level.tile_size;
				temp_x += Level.tile_size / 2;
				temp_y += Level.tile_size / 2;

				//System.out.println("tile x: " + temp_x + " tile y: " + temp_y);
				body.setTransform(new Vector2(temp_x, temp_y), 0);
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

	public void move_up() {
		// body.applyForceToCenter(new Vector2(0, 100000f), true);
		moving = true;
		move_direction = 0;
		dir = 0;
		dir_still = 0;
	}

	public void move_right() {
		// body.applyForceToCenter(new Vector2(100000f, 0), true);
		moving = true;
		move_direction = 1;
		dir = 1;
		dir_still = 1;
	}

	public void move_down() {
		// body.applyForceToCenter(new Vector2(0, -100000f), true);
		moving = true;
		move_direction = 2;
		dir = 2;
		dir_still = 2;
	}

	public void move_left() {
		// body.applyForceToCenter(new Vector2(-100000f, 0), true);
		moving = true;
		move_direction = 3;
		dir = 3;
		dir_still = 3;
	}

	public void fix_position_in_tile() {
		//System.out.println("Fixing position");
		//System.out.println("old x: " + body.getPosition().x + " y: " + body.getPosition().y);
		float temp_x = (float) Math.floor(body.getPosition().x / Level.tile_size);
		float temp_y = (float) Math.floor(body.getPosition().y / Level.tile_size);
		temp_x *= Level.tile_size;
		temp_y *= Level.tile_size;
		temp_x += Level.tile_size / 2;
		temp_y += Level.tile_size / 2;
		body.setTransform(new Vector2(temp_x, temp_y), 0);
		//System.out.println("new x: " + body.getPosition().x + " y: " + body.getPosition().y);
	}

	public void reset_shift() {
		shift = 0;
	}
	
	public Player_control get_control() {
		return control;
	}
	
	public Vector2 get_position_tile() {
		return position_tile;
	}

}

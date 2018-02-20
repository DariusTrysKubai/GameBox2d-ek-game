package player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import GameData.GameData;
import main.Level;
import scenes.Hud;

public class Player {

	boolean debug = true;
	boolean pause = false;

	// Player classes
	public Player_control control;
	public Player_stats stats;

	Vector2 position;
	BodyDef bodyDef;
	Body body;
	FixtureDef fixtureDef;
	Fixture fixture;
	public static final float player_friction = 8;
	public static final float max_speed = 35;
	public static final float SPEED = 60;

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	// Textures
	SpriteBatch sb;
	Texture texture;
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
	Hud hud;
	GameData data;

	public Player(World world, boolean debug) {

		// this.debug = debug;

		// box2d create
		PolygonShape shape;
		position = new Vector2();
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

		walkAnimation = walkAnimation_down;

		still_down = new Animation<TextureRegion>(0.125f, walkFrames_still_down);
		still_up = new Animation<TextureRegion>(0.125f, walkFrames_still_up);
		still_left = new Animation<TextureRegion>(0.125f, walkFrames_still_left);
		still_right = new Animation<TextureRegion>(0.125f, walkFrames_still_right);

		texture_size = tmp[0][0].getRegionWidth();

		position.x = ((Game.ORIGINAL_WIDTH / 2) - (texture_size / 2)) * 2;
		position.y = ((Game.ORIGINAL_HEIGHT / 2) - (texture_size / 2)) * 2;

		stats = new Player_stats();
	}

	public void create(SpriteBatch sb, ShapeRenderer shape, OrthographicCamera cam) {
		this.sb = sb;
		this.shape = shape;
		this.cam = cam;
	}

	public void initControl(Viewport viewport) {
		control = new Player_control(this, debug);
		control.create(level, shape);
	}

	public void initLevel(Level level) {
		this.level = level;
		spawn(3);
	}

	public void initHud(Hud hud) {
		this.hud = hud;
		control.initHud(hud);
	}

	public void initGameData(GameData data) {
		if (data == null) {
			Gdx.app.log(this.getClass().getName(), "init data == null");
		} else {
			this.data = data;
		}
		
		// if first time, set level to 1
		if(!data.isNotFirstTime()) {
			stats.set_level(1);
			control.setPositionTile();
			data.setPosition((int)control.getPosition_tile().x, (int)control.getPosition_tile().y);
			data.setPlayer_health(stats.get_health());
			data.setPlayer_hunger(stats.get_hunger());
		}
	}

	public void update(float dt) {
		
		//Gdx.app.log(this.getClass().getName(), "Player moving: " + control.moving_to_next_tile);
		if (!pause) {
			stats.update(dt);
			control.update(dt, cam);

			if (control.moving) {
				if (control.dir == 0) {
					walkAnimation = walkAnimation_up;
				}
				if (control.dir == 1) {
					walkAnimation = walkAnimation_right;
				}
				if (control.dir == 2) {
					walkAnimation = walkAnimation_down;
				}
				if (control.dir == 3) {
					walkAnimation = walkAnimation_left;
				}
			} else {
				if (control.dir_still == 0) {
					walkAnimation = still_up;
				}
				if (control.dir_still == 1) {
					walkAnimation = still_right;
				}
				if (control.dir_still == 2) {
					walkAnimation = still_down;
				}
				if (control.dir_still == 3) {
					walkAnimation = still_left;
				}
			}
		}
	}

	public void render(OrthographicCamera cam) {

		if (debug) {
			shape.begin(ShapeType.Line);
			shape.setColor(Color.RED);
			shape.rect(control.position_tile.x * 32, control.position_tile.y * 32, 32, 32);
			shape.end();
		}

		if (control.moving) {
			control.render(cam);
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

		for (MapObject object : level.getMap().getLayers().get("spawn_points").getObjects()
				.getByType(RectangleMapObject.class)) {
			if (Integer.parseInt(object.getName()) == spawn_point) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();
				float temp_x = (float) Math.floor(rect.x / Level.tile_size);
				float temp_y = (float) Math.floor(rect.y / Level.tile_size);
				temp_x *= Level.tile_size;
				temp_y *= Level.tile_size;
				temp_x += Level.tile_size / 2;
				temp_y += Level.tile_size / 2;
				body.setTransform(new Vector2(temp_x, temp_y), 0);
			}
		}
	}

	public Player_control get_control() {
		return control;
	}
	
	public Player_stats get_stats() {
		return stats;
	}

	public Vector2 get_position_tile() {
		return control.position_tile;
	}

	public void pause() {
		Gdx.app.log(this.getClass().getName(), "pause");
		pause = true;
		if (data == null) {
			Gdx.app.log(this.getClass().getName(), "Data doesn't exist");
		}
		data.savePlayersData(stats.get_health(), stats.get_hunger(), (int) control.getPosition_tile().x,
				(int) control.getPosition_tile().y, stats.get_level());
	}

	public void resume() {
		Gdx.app.log(this.getClass().getName(), "resume");
		pause = false;
	}
	
	public void load(GameData data) {
		Gdx.app.log(this.getClass().getName(), "loading players data");
		Gdx.app.log(this.getClass().getName(), "player x: " + data.getTileX());
		stats.set_health(data.getHealth());
		Gdx.app.log(this.getClass().getName(), "Hunger before: " + stats.get_hunger());
		stats.set_hunger(data.getHunger());
		Gdx.app.log(this.getClass().getName(), "Hunger after: " + stats.get_hunger());
		control.setPositionByTile(data.getTileX(), data.getTileY());
	}
	
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		body.setTransform(new Vector2(x, y), 0);
	}
	
	public Vector2 getPosition() {
		return position;
	}

}

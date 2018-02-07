package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import items.Item;
import items.ItemManager;
import player.Player;

public class Hud implements Disposable {

	boolean debug = false;

	// references
	Player player;
	ItemManager itemmanager;

	Stage stage;
	Viewport viewport;
	Label test_label;
	ProgressBar progressbar_health;
	ProgressBar progressbar_hunger;
	Skin skin;

	BitmapFont font12;

	boolean itemHudActive = false;

	String framerate = "Hello, I should be a frame rate";

	public Hud(SpriteBatch sb, OrthographicCamera hudcam) {

		skin = new Skin(Gdx.files.internal("skins2/flat-earth-ui.json"));

		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("uni0553-webfont.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		font12 = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, hudcam);
		hudcam.update();

		stage = new Stage(viewport, sb);

		// PROGRESS BARS ---------------

		// HEALTH
		// health laber
		Label label_health = new Label("HEALTH", new Label.LabelStyle(font12, Color.WHITE));
		// health progress bar
		progressbar_health = new ProgressBar(0, 100, 1, false, skin, "health-horizontal");
		progressbar_health.setSize(600, 20);

		// HUNGER
		Label label_hunger = new Label("HUNGER", new Label.LabelStyle(font12, Color.WHITE));
		// health progress bar
		progressbar_hunger = new ProgressBar(0, 100, 1, false, skin, "hunger-horizontal");
		progressbar_hunger.setSize(600, 20);
		progressbar_hunger.setValue(40);

		// table
		Table stats_table = new Table();
		stats_table.top();
		stats_table.setDebug(debug);
		stats_table.setFillParent(true);
		stats_table.add(progressbar_health).pad(10).width(200);
		stats_table.add(progressbar_hunger).pad(10).width(200);
		stats_table.row();
		stats_table.add(label_health);
		stats_table.add(label_hunger);
		stats_table.row();

		stage.addActor(stats_table);
		// stage.addActor(button2);
		// ---------------

		Table table = new Table();
		table.setDebug(debug);
		table.setPosition(10, 30);

		test_label = new Label(framerate, new Label.LabelStyle(font12, Color.WHITE));
		test_label.setWrap(true);

		table.add(test_label).align(Align.left);
		table.row();
		table.setName("My table");
		stage.addActor(table);

		// createItemWindow(stage);
	}

	private void createItemWindow(Stage stage) {
		// Window test

		Table table = new Table(skin);
		// Window window = new Window("This is my first window and it is very long",
		// skin);
		// window.setSize(350, 150);
		// window.setPosition(50, 50);

		TextButton button = new TextButton("Eat", skin);
		button.setSize(100, 50);
		// button.setPosition(50, 50);
		button.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				player.stats.add_health(10);
			}
		});

		TextButton button2 = new TextButton("Pick", skin);
		button2.setSize(100, 50);
		// button.setPosition(50, 50);
		button2.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				player.stats.add_health(10);
			}
		});

		// window.addActor(button);

		table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		table.setSize(500, 200);
		// table.top();
		table.add(button).expandX();
		table.row().padTop(10);
		table.add(button2);
		table.setDebug(true);
		table.setName("ClickedItemWindow");
		// stage.addActor(window);
		stage.addActor(table);
	}

	public void update(float dt) {
		framerate = String.valueOf(Gdx.graphics.getFramesPerSecond());
		test_label.setText("fps: " + framerate);
		progressbar_health.setValue(player.stats.get_health());
		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			for (Actor actor : stage.getActors()) {
				if (actor.getName() != null && actor.getName().equalsIgnoreCase("ClickedItemWindow")) {
					actor.remove();
				}
			}
		}

		deleteItemHudIfPlayerMoving();
	}

	public void init_player(Player player) {
		this.player = player;
	}
	
	public void init_itemManager(ItemManager itemmanager) {
		this.itemmanager = itemmanager;
	}

	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	public void createItemHud(final Item item) {
		itemHudActive = true;
		Table table = new Table(skin);

		TextButton button_eat = new TextButton("Eat", skin);
		button_eat.setSize(100, 50);
		button_eat.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				player.stats.add_health(30);
				itemmanager.remove_item(item);
				// remove
				for (Actor x : stage.getActors()) {
					if (x.getName() != null && x.getName().equalsIgnoreCase("ClickedItemWindow")) {
						x.remove();
					}
				}
			}
		});

		TextButton button_cancel = new TextButton("cancel", skin);
		button_cancel.setSize(100, 50);
		button_cancel.addCaptureListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// remove
				for (Actor x : stage.getActors()) {
					if (x.getName() != null && x.getName().equalsIgnoreCase("ClickedItemWindow")) {
						x.remove();
					}
				}
			}
		});

		table.setPosition(Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() / 2) - 100);
		table.setSize(200, 200);
		table.add(button_eat).expandX();
		table.row().padTop(10);
		table.add(button_cancel);
		table.setDebug(debug);
		table.setName("ClickedItemWindow");
		stage.addActor(table);

	}

	public void deleteItemHudIfPlayerMoving() {

		if (player.control.isMoving() && itemHudActive) {
			for (Actor x : stage.getActors()) {
				if (x.getName() != null && x.getName().equalsIgnoreCase("ClickedItemWindow")) {
					x.remove();
				}
			}
		}
	}

}
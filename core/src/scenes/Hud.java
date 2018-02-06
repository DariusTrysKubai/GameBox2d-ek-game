package scenes;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Game;

import player.Player;

public class Hud implements Disposable {

	boolean debug = false;

	Player player;

	public Stage stage;
	public Stage stage2;
	private Viewport viewport;

	private Label test_label;
	ProgressBar progressbar_health;
	ProgressBar progressbar_hunger;

	BitmapFont font12;
	String framerate = "Hello, I should be a frame rate";

	public Hud(SpriteBatch sb, OrthographicCamera hudcam) {

		Skin skin = new Skin(Gdx.files.internal("skins2/flat-earth-ui.json"));

		Label label3 = new Label("This is a Label (skin) on  5 columns ", skin);
		label3.setSize(150, 50);
		label3.setPosition(0, 20);
		
		// Font generation
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("uni0553-webfont.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 30;
		font12 = generator.generateFont(parameter); // font size 12 pixels
		generator.dispose(); // don't forget to dispose to avoid memory leaks!

		viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, hudcam);
		hudcam.update();
		stage = new Stage(viewport, sb);
		Gdx.input.setInputProcessor(stage);
		
		// BUTTON
		// 1
		TextButton button = new TextButton("Feed", skin);
		button.setSize(150, 40);
		button.setPosition( (Game.V_WIDTH / 2 ) - (button.getWidth() / 2), (Game.V_HEIGHT / 2) - (button.getHeight() / 2));
		// 2 
		TextButton button2 = new TextButton("Test", skin);
		button2.setSize(150, 40);
		button2.setPosition( (Game.V_WIDTH / 2 ) - (button.getWidth() / 2) - 40, (Game.V_HEIGHT / 2) - (button.getHeight() / 2) + 250);

		
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

		//table
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
		stats_table.add(button);
		
		button.addCaptureListener(new ChangeListener() {
	        @Override
	        public void changed (ChangeEvent event, Actor actor) {
	            player.stats.add_health(10);
	        }
	    });

		stage.addActor(stats_table);
		//stage.addActor(button2);
		// ---------------

		Table table = new Table();
		table.setDebug(debug);
		table.setPosition(10, 30);

		test_label = new Label(framerate, new Label.LabelStyle(font12, Color.WHITE));
		test_label.setWrap(true);

		table.add(test_label).align(Align.left);
		table.row();

		stage.addActor(table);
		// stage.addActor(label3);
	}

	public void update(float dt) {
		framerate = String.valueOf(Gdx.graphics.getFramesPerSecond());
		test_label.setText("fps: " + framerate);

		progressbar_health.setValue(player.stats.get_health());

	}

	public void init_player(Player player) {
		this.player = player;
	}

	public static void addScore(int value) {

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

}
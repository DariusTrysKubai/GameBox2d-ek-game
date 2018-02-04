package scenes;

import javax.swing.GroupLayout.Alignment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.Game;

public class Hud implements Disposable{

	boolean debug = true;
	
    //Scene2D.ui Stage and its own Viewport for HUD
    public Stage stage;
    private Viewport viewport;

    //Mario score/time Tracking Variables
    private Integer worldTimer;
    private boolean timeUp; // true when the world timer reaches 0
    private float timeCount;
    private static Integer score;

    //Scene2D widgets
   
    private Label test_label;
    
    BitmapFont font12;
    String framerate = "Hello, I should be a frame rate";

    public Hud(SpriteBatch sb, OrthographicCamera hudcam){
    	
    	// Font generation
    	FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("uni0553-webfont.ttf"));
    	FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    	parameter.size = 40;
    	font12 = generator.generateFont(parameter); // font size 12 pixels
    	generator.dispose(); // don't forget to dispose to avoid memory leaks!
    	
        //define our tracking variables
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        //setup the HUD viewport using a new camera seperate from our gamecam
        //define our stage using that viewport and our games spritebatch
        viewport = new FitViewport(Game.V_WIDTH, Game.V_HEIGHT, hudcam);
        hudcam.update();
        stage = new Stage(viewport, sb);

        //define a table used to organize our hud's labels
        Table table = new Table();
        table.setDebug(debug);
        table.setPosition(10, 30);
        //Top-Align table
        //table.top();
        //make the table fill the entire stage
        //table.setFillParent(true);
        
        //define our labels using the String, and a Label style consisting of a font and color
    	test_label = new Label(framerate, new Label.LabelStyle(font12, Color.WHITE));
    	//test_label.setPosition(0, 0);
    	test_label.setWrap(true);
    	//test_label.setBounds(0, 0, 300, 5);

        //add our labels to our table, padding the top, and giving them all equal width with expandX
        table.add(test_label).align(Align.left);

        //add a second row to our table
        table.row();

        //add our table to the stage
        stage.addActor(table);

    }

    public void update(float dt){
    	framerate = String.valueOf(Gdx.graphics.getFramesPerSecond());
    	test_label.setText("fps: "+framerate);
    	
    }

    public static void addScore(int value){

    }

    @Override
    public void dispose() { stage.dispose(); }

    public boolean isTimeUp() { return timeUp; }
}
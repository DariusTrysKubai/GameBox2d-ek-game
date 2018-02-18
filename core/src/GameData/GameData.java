package GameData;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

import items.ItemManager;
import main.Level;
import player.Player;

public class GameData {

	// References
	Player player;
	Level level;
	ItemManager itemmanager;

	Preferences prefs;
	private static final String PREFS_NAME = "My_games_pref";
	private Boolean not_fist_launch;
	private String last_launch_date;
	private int player_health;
	private int player_hunger;
	private int player_tile_x;
	private int player_tile_y;

	protected Preferences getPrefs() {
		if (prefs == null) {
			prefs = Gdx.app.getPreferences(PREFS_NAME);
			resetData();
		}
		return prefs;
	}

	public GameData() {
		getPrefs();
		Date date = new Date(TimeUtils.millis());
	}

	public void save() {
		prefs.putBoolean("NOT_FIRST_LAUNCH", true);
		prefs.putInteger("player_health", player_health);
		prefs.putInteger("player_hunger", player_hunger);
		prefs.putInteger("player_tile_x", player_tile_x);
		prefs.putInteger("player_tile_y", player_tile_y);
		prefs.flush();
		Gdx.app.log(this.getClass().getName(), "data save flush");
	}

	public void load() {
		not_fist_launch = prefs.getBoolean("NOT_FIRST_LAUNCH");
		player_health = prefs.getInteger("player_health");
		player_hunger = prefs.getInteger("player_hunger");
		player_tile_x = prefs.getInteger("player_tile_x");
		Gdx.app.log(this.getClass().getName(), "Loading player tile x: " + player_tile_x);
		player_tile_y = prefs.getInteger("player_tile_y");
	}

	public void resetData() {
		not_fist_launch = false;
	}

	public boolean isFirstTime() {
		return prefs.getBoolean("NOT_FIRST_LAUNCH");
	}

	public void savePlayersData(int health, int hunger, int tile_x, int tile_y) {
		player_health = health;
		player_hunger = hunger;
		player_tile_x = tile_x;
		player_tile_y = tile_y;
	}

	public int getHealth() {
		return player_health;
	}

	public int getHunger() {
		return player_hunger;
	}

	public int getTileX() {
		return player_tile_x;
	}

	public int getTileY() {
		return player_tile_y;
	}

}

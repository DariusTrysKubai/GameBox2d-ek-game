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
	private int player_level;
	long timeClosed;
	long timeOpened;

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
		prefs.putInteger("player_level", player_level);
		prefs.putLong("timeClosed", timeClosed);
		prefs.putLong("timeOpened", timeOpened);
		prefs.flush();
		Gdx.app.log(this.getClass().getName(), "data save flush");
	}

	public void load() {
		not_fist_launch = prefs.getBoolean("NOT_FIRST_LAUNCH");
		player_health = prefs.getInteger("player_health");
		player_hunger = prefs.getInteger("player_hunger");
		player_tile_x = prefs.getInteger("player_tile_x");
		player_tile_y = prefs.getInteger("player_tile_y");
		player_level = prefs.getInteger("player_level");
		timeClosed = prefs.getLong("timeClosed");
		timeOpened = prefs.getLong("timeOpened");
	}

	public void resetData() {
		not_fist_launch = false;
	}

	public boolean isNotFirstTime() {
		return prefs.getBoolean("NOT_FIRST_LAUNCH");
	}

	public void savePlayersData(int health, int hunger, int tile_x, int tile_y, int level) {
		player_health = health;
		player_hunger = hunger;
		player_tile_x = tile_x;
		player_tile_y = tile_y;
		player_level = level;
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

	public void initTimeOpen() {
		timeOpened = TimeUtils.millis();
	}

	public void initTimeClosed() {
		timeClosed = TimeUtils.millis();
	}
	
	public long getTimeElapsed() {
		long temp = TimeUtils.timeSinceMillis(timeClosed);
		return temp;
	}
	
	public void setPosition(int x, int y) {
		player_tile_x = x;
		player_tile_y = y;
	}

	public int getPlayer_health() {
		return player_health;
	}

	public void setPlayer_health(int player_health) {
		this.player_health = player_health;
	}

	public int getPlayer_hunger() {
		return player_hunger;
	}

	public void setPlayer_hunger(int player_hunger) {
		this.player_hunger = player_hunger;
	}

}

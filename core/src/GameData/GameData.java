package GameData;

import java.util.Date;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.TimeUtils;

public class GameData {

	Preferences prefs;
	private static final String PREFS_NAME = "My_games_pref";
	private Boolean not_fist_launch;
	private String last_launch_date;
	private int player_health;
	private int player_hunger;

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
		System.out.println("Year: " + date.getTime());
		System.out.println("Month: " + date.getMonth());
		System.out.println("Day: " + date.getDate());
		System.out.println("Hour: " + date.getHours());
		System.out.println("Minute: " + date.getMinutes());
	}

	public void save() {
		prefs.putBoolean("NOT_FIRST_LAUNCH", true);
		prefs.flush();
	}

	public void load() {
		not_fist_launch = prefs.getBoolean("NOT_FIRST_LAUNCH");
	}
	
	public void resetData() {
		not_fist_launch = false;
	}
	
	public boolean isFirstTime() {
		return prefs.getBoolean("NOT_FIRST_LAUNCH");
	}

}

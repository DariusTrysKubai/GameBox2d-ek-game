package player;

public class Player_stats {
	
	int health;
	int hidratation;
	int hunger;
	
	public Player_stats() {
		
	}
	
	public void add_health(int amount) {
		this.health += amount;
	}
	
	public int get_health() {
		return health;
	}

}

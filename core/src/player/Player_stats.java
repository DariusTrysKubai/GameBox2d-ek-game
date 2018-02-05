package player;

public class Player_stats {
	
	int health;
	int hidratation;
	int hunger;
	float acum;
	
	public Player_stats() {
		health = 100;
		acum = 0;
	}
	
	public void update(float dt) {
		acum += dt;
		if(acum >= 0.25f) {
			acum = 0;
			add_health(-1);
		}
	}
	
	public void add_health(int amount) {
		this.health += amount;
	}
	
	public int get_health() {
		return health;
	}

}

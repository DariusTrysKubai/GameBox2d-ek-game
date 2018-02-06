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
		if (acum >= 0.25f) {
			acum = 0;
			add_health(-1);
		}
	}

	public void add_health(int amount) {
		this.health += amount;
		fix_health();
	}

	public int get_health() {
		return health;
	}
	
	public void fix_health() {
		if (health > 100) {
			health = 100;
		}
		if(health < 0) {
			health = 0;
		}
	}

}

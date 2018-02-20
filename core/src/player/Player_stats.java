package player;

public class Player_stats {

	int health;
	int hidratation;
	int hunger;
	int level;
	float acum;
	float acum2;

	public Player_stats() {
		health = 100;
		hunger = 100;
		acum = 0;
		acum2 = 0;
	}

	public void update(float dt) {
		acum += dt;
		// 20h to 0 = 720f
		if (acum >= 5f) {
			acum = 0;
			add_hunger(-1);
		}
		
		acum2 += dt;
		if(hunger == 0 && acum2 >= 10f) {
			acum2 = 0;
			health -= 5;
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
	
	public void add_hunger(int amount) {
		this.hunger += amount;
		fix_hunger();
	}

	public int get_hunger() {
		return hunger;
	}
	
	public void fix_hunger() {
		if (hunger > 100) {
			hunger = 100;
		}
		if(hunger < 0) {
			hunger = 0;
		}
	}
	
	public void set_health(int health) {
		this.health = health;
	}
	
	public void set_hunger(int hunger) {
		this.hunger = hunger;
	}
	
	public void add_level() {
		level++;
	}
	
	public void set_level(int value) {
		level = value;
	}
	
	public int get_level() {
		return level;
	}


}

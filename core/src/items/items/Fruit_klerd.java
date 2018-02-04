package items.items;

import items.Item;

public class Fruit_klerd extends Item {

	public Fruit_klerd() {
		super();
	}
	
	public Fruit_klerd(int tile_x, int tile_y, int type) {
		super();
	}
	
	public Fruit_klerd(float x, float y, int type) {
		super(x, y, type);
	}
	
	public void create() {
		loadTexture("items/Klerd_fruit.png");
		name = "Fruit_klerd";
		width = texture.getWidth();
		height = texture.getHeight();
		is_taken = false;
	}

}

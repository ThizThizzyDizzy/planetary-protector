package planetaryprotector.enemy;
import planetaryprotector.menu.MenuGame;
public enum AsteroidMaterial{
    STONE("stone", 250, 500),
    COAL("coal", 1000, 2000),
    IRON("iron", 125, 250);
    public final int[] images = new int[19];
    public int timer;
    public final String texture;
    public final int min;
    public final int max;
    private AsteroidMaterial(String texture, int min, int max){
        this.texture = texture;
        this.min = min;
        this.max = max;
        timer = MenuGame.rand.nextInt(max-min)+min;
    }
}
package planetaryprotector.enemy;
public enum AsteroidMaterial{
    STONE("stone", 250, 500),
    COAL("coal", 1000, 2000),
    IRON("iron", 125, 250);
    public String[] images = new String[19];
    public final String texture;
    public final int min;
    public final int max;
    public boolean forceDrop = false;
    private AsteroidMaterial(String texture, int min, int max){
        this.texture = texture;
        this.min = min;
        this.max = max;
    }
    private AsteroidMaterial forceDrop(){
        forceDrop = true;
        return this;
    }
}
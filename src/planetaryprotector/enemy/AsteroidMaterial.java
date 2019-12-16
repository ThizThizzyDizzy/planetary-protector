package planetaryprotector.enemy;
import planetaryprotector.menu.MenuGame;
public enum AsteroidMaterial{
    STONE("stone", 250, 500),
    COAL("coal", 1000, 2000),
    IRON("iron", 125, 250);
    public static void resetTimers(){
        for(AsteroidMaterial material : values()){
            material.resetTimer();
        }
    }
    public String[] images = new String[19];
    public int timer;
    public final String texture;
    public final int min;
    public final int max;
    public boolean forceDrop = false;
    private AsteroidMaterial(String texture, int min, int max){
        this.texture = texture;
        this.min = min;
        this.max = max;
        resetTimer();
    }
    private AsteroidMaterial forceDrop(){
        forceDrop = true;
        return this;
    }
    public void resetTimer(){
        if(min==-1||max==-1){
            timer = Integer.MAX_VALUE;
            return;
        }
        timer = MenuGame.rand.nextInt(max-min)+min;
    }
}
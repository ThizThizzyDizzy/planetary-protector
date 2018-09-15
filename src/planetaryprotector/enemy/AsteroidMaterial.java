package planetaryprotector.enemy;
import planetaryprotector.menu.MenuGame;
public enum AsteroidMaterial{
    STONE("stone", 250, 500),
    COAL("coal", 1000, 2000),
    IRON("iron", 125, 250),
    SHOOTING_STAR("star", -1, -1);
    static{
        SHOOTING_STAR.setSpeedMult(.5).forceDrop();
        SHOOTING_STAR.images = new int[19*4];
    }
    public int[] images = new int[19];
    public int timer;
    public final String texture;
    public final int min;
    public final int max;
    public double speedMult = 2;
    public boolean forceDrop = false;
    private AsteroidMaterial(String texture, int min, int max){
        this.texture = texture;
        this.min = min;
        this.max = max;
        if(min==-1||max==-1){
            timer = Integer.MAX_VALUE;
            return;
        }
        timer = MenuGame.rand.nextInt(max-min)+min;
    }
    private AsteroidMaterial setSpeedMult(double mult){
        speedMult = mult;
        return this;
    }
    private AsteroidMaterial forceDrop(){
        forceDrop = true;
        return this;
    }
}
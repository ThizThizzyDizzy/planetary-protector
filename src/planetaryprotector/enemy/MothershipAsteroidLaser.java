package planetaryprotector.enemy;
import planetaryprotector.GameObject;
import planetaryprotector.game.Game;
public class MothershipAsteroidLaser extends GameObject{
    int time = 0;
    int[] target = new int[]{0,0};
    int speed = 1;
    int damageMult = 1;
    public MothershipAsteroidLaser(Game game){
        super(game, 0, 0, 0, 0);
//        super(game, game.getCityBoundingBox().x-150, game.getCityBoundingBox().getCenterY(), 0, 0);
        target = new int[]{x,y};
    }
    public void tick(){
        double oldx = x;
        double oldy = y;
        if(x<target[0]){
            x+=speed;
        }
        if(x>target[0]){
            x-=speed;
        }
        if(y<target[1]){
            y+=speed;
        }
        if(y>target[1]){
            y-=speed;
        }
        if(oldx==x&&oldy==y){
            target = Enemy.getBestStrike(game);
            if(target==null){
                target = new int[]{x,y};
            }
        }
        time++;
        int radius = 75;
        int c = 4;
        for(int i = 0; i<c; i++){
            double r = i+time/10D;
            while(r>=c){
                r-=c;
            }
            int X = (int)(Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2);
            int Y = (int)(Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2);
            for(int j = 0; j<damageMult; j++){
                Asteroid a = new Asteroid(game, X-25, Y-25, AsteroidMaterial.STONE, 0);
                Asteroid A = new Asteroid(game, x-25, y-25, AsteroidMaterial.STONE, 0);
                a.drop = A.drop = false;
                game.addAsteroid(a);
                game.addAsteroid(A);
            }
        }
    }
    @Override
    public void draw(){}
}
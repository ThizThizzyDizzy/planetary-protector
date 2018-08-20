package planetaryprotector.enemy;
import planetaryprotector.Core;
 import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MothershipAsteroidLaser extends MenuComponent{
    int time = 0;
    double[] target = new double[]{0,0};
    int speed = 1;
    int damageMult = 1;
    public MothershipAsteroidLaser(){
        super(-150, Display.getHeight()/2, 0, 0);
        target = new double[]{x,y};
    }
    @Override
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
            target = MenuComponentEnemy.getBestStrike();
            if(target==null){
                target = new double[]{x,y};
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
            double X = Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2;
            double Y = Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2;
            for(int j = 0; j<damageMult; j++){
                MenuComponentAsteroid a = new MenuComponentAsteroid(X-25, Y-25, AsteroidMaterial.STONE, false);
                MenuComponentAsteroid A = new MenuComponentAsteroid(x-25, y-25, AsteroidMaterial.STONE, false);
                a.drop = A.drop = false;
                Core.game.add(a);
                Core.game.add(A);
            }
        }
    }
    @Override
    public void render(){
    }
}
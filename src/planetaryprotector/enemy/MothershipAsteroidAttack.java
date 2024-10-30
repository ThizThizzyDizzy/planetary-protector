package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import planetaryprotector.GameObject;
public class MothershipAsteroidAttack extends GameObject{
    private final EnemyMothership ship;
    int type = 0;
    int types = 2;
    int time = 0;
    public MothershipAsteroidAttack(EnemyMothership ship){//TODO redo entirely
        super(ship.game, 0, 0, DizzyEngine.screenSize.x, DizzyEngine.screenSize.y);
        this.ship = ship;
        type = game.rand.nextInt(types);
    }
    int direction = 1;
    boolean out = false;
    public void tick(){
        time+=out?10:1;
        switch(type){
            case 0:
                if((!out&&time%10==0)||(out&&time%20==0)){
                    int radius = (int) (Math.sqrt(Math.pow(DizzyEngine.screenSize.x,2)+Math.pow(DizzyEngine.screenSize.y, 2))/2D)-time;
                    if(radius<=0){
                        out = true;
                    }
                    if(radius<-DizzyEngine.screenSize.x*2){
                        ship.asteroidAttack = null;
                    }
                    int c = 20;
                    for(int i = 0; i<c; i++){
                        double r = i+(time/100D);
                        while(r>=c){
                            r-=c;
                        }
                        int X = (int)(Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2);
                        int Y = (int)(Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2);
                        Asteroid a = new Asteroid(game, X-25, Y-25, AsteroidMaterial.STONE, 0);
                        a.drop = false;
                        game.addAsteroid(a);
                    }
                }
            case 1:
                if((!out&&time%50==0)||(out&&time%100==0)){
                    direction*=-1;
                }
                if((!out&&time%10==0)||(out&&time%20==0)){
                    int radius = (int) (Math.sqrt(Math.pow(DizzyEngine.screenSize.x,2)+Math.pow(DizzyEngine.screenSize.y, 2))/2D)-time;
                    if(radius<=0){
                        out = true;
                    }
                    if(radius<-DizzyEngine.screenSize.x*2){
                        ship.asteroidAttack = null;
                    }
                    int c = 20;
                    for(int i = 0; i<c; i++){
                        double r = i+(time/25D*direction);
                        while(r>=c){
                            r-=c;
                        }
                        int X = (int)(Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2);
                        int Y = (int)(Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2);
                        Asteroid a = new Asteroid(game, X-25, Y-25, AsteroidMaterial.STONE, 0);
                        a.drop = false;
                        game.addAsteroid(a);
                    }
                }
        }
    }
    @Override
    public void draw(){}
}
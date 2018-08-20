package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import org.lwjgl.opengl.Display;
import simplelibrary.opengl.gui.components.MenuComponent;
public class MothershipAsteroidAttack extends MenuComponent{
    private final EnemyMothership ship;
    int type = 0;
    int types = 2;
    int time = 0;
    public MothershipAsteroidAttack(EnemyMothership ship) {
        super(0, 0, Display.getWidth(), Display.getHeight());
        this.ship = ship;
        type = MenuGame.rand.nextInt(types);
    }
    int direction = 1;
    boolean out = false;
    @Override
    public void tick(){
        time+=out?10:1;
        switch(type){
            case 0:
                if((!out&&time%10==0)||(out&&time%20==0)){
                    int radius = (int) (Math.sqrt(Math.pow(Display.getWidth(),2)+Math.pow(Display.getHeight(), 2))/2D)-time;
                    if(radius<=0){
                        out = true;
                    }
                    if(radius<-Display.getWidth()*2){
                        Core.game.componentsToRemove.add(this);
                        ship.asteroidAttack = null;
                    }
                    int c = 20;
                    for(int i = 0; i<c; i++){
                        double r = i+(time/100D);
                        while(r>=c){
                            r-=c;
                        }
                        double X = Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2;
                        double Y = Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2;
                        MenuComponentAsteroid a = new MenuComponentAsteroid(X-25, Y-25, AsteroidMaterial.STONE, false);
                        a.drop = false;
                        Core.game.add(a);
                    }
                }
            case 1:
                if((!out&&time%50==0)||(out&&time%100==0)){
                    direction*=-1;
                }
                if((!out&&time%10==0)||(out&&time%20==0)){
                    int radius = (int) (Math.sqrt(Math.pow(Display.getWidth(),2)+Math.pow(Display.getHeight(), 2))/2D)-time;
                    if(radius<=0){
                        out = true;
                    }
                    if(radius<-Display.getWidth()*2){
                        Core.game.componentsToRemove.add(this);
                        ship.asteroidAttack = null;
                    }
                    int c = 20;
                    for(int i = 0; i<c; i++){
                        double r = i+(time/25D*direction);
                        while(r>=c){
                            r-=c;
                        }
                        double X = Math.cos(Math.toRadians(r*(360D/c)))*radius+x+width/2;
                        double Y = Math.sin(Math.toRadians(r*(360D/c)))*radius+y+height/2;
                        MenuComponentAsteroid a = new MenuComponentAsteroid(X-25, Y-25, AsteroidMaterial.STONE, false);
                        a.drop = false;
                        Core.game.add(a);
                    }
                }
        }
    }
    @Override
    public void render(){}
}
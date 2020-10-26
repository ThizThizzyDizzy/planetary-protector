package planetaryprotector.friendly;
import planetaryprotector.Core;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.ZComponent;
public class ShootingStar extends GameObject implements ZComponent{
    private final int particleResolution = MenuOptionsGraphics.particles+1;
    public int speed = 10;
    public int landing = 0;
    public int landTime = 48;
    public ShootingStar(Game game, double x, double y){
        super(game, x,y, 25, 25);
    }
    @Override
    public void render(){
        if(dead)return;
        double fallProgress = landing/(double)landTime;
        double landX = x+width/2;
        double landY = y+height/2;//TODO something better
        double startX = landX-Core.helper.displayWidth()*.7;
        double startY = landY-Core.helper.displayHeight();
        double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
        double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
        GL11.glColor4d(1, 1, 1, 1);
        GL11.glPushMatrix();
        GL11.glTranslated(X, Y, 0);
        GL11.glRotated(landing, 0, 0, 1);
        drawRect(-width/2, -width/2, width/2, height/2, Item.star.getWorldTexture());
        GL11.glPopMatrix();
    }
    public void tick(){
        if(dead)return;
        landing++;
        if(landing>=landTime){
            dead = true;
            game.addItem(new DroppedItem(game, x+width/2, y+height/2, Item.star));
        }
        double fallProgress = landing/(double)landTime;
        double landX = x+width/2;
        double landY = y+height/2;
        double startX = landX-Core.helper.displayWidth()*.7;
        double startY = landY-Core.helper.displayHeight();
        double frameDistanceX = (landX-startX)/(double)landTime;
        double frameDistanceY = (landY-startY)/(double)landTime;
        double dX = frameDistanceX/particleResolution;
        double dY = frameDistanceY/particleResolution;
        double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
        double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
        for(int i = 0; i<particleResolution; i++){
            X-=dX;
            Y-=dY;
            Particle particle = new Particle(game, X, Y, ParticleEffectType.SMOKE, 1, true);
            particle.width = particle.height = 25;
            game.addParticleEffect(particle);
        }
    }
    @Override
    public double getZ(){
        return Math.max(5, 1-.05*landing+5.56);
    }
}
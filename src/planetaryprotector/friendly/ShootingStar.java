package planetaryprotector.friendly;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.MathUtil;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.Options;
import planetaryprotector.item.DroppedItem;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.ZComponent;
public class ShootingStar extends GameObject implements ZComponent{
    private final int particleResolution = Options.options.particles+1;
    public int landing = 0;
    public int landTime = 96;
    public ShootingStar(Game game, int x, int y){
        super(game, x, y, 25, 25);
    }
    @Override
    public void draw(){
        if(dead)return;
        double fallProgress = landing/(double)landTime;
        double landX = x+width/2;
        double landY = y+height/2;
        double startX = landX-DizzyEngine.screenSize.x*.7;//TODO something better
        double startY = landY-DizzyEngine.screenSize.y;
        double X = MathUtil.lerp(startX, landX, fallProgress);
        double Y = MathUtil.lerp(startY, landY, fallProgress);
        Renderer.setColor(1, 1, 1, 1);
        GL11.glPushMatrix();
        GL11.glTranslated(X, Y, 0);
        GL11.glRotated(landing, 0, 0, 1);
        Renderer.fillRect(-width/2, -width/2, width/2, height/2, Item.star.getWorldTexture());
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
        double startX = landX-DizzyEngine.screenSize.x*.7;
        double startY = landY-DizzyEngine.screenSize.y;
        double frameDistanceX = (landX-startX)/(double)landTime;
        double frameDistanceY = (landY-startY)/(double)landTime;
        double dX = frameDistanceX/particleResolution;
        double dY = frameDistanceY/particleResolution;
        double X = MathUtil.lerp(startX, landX, fallProgress);
        double Y = MathUtil.lerp(startY, landY, fallProgress);
        for(int i = 0; i<particleResolution; i++){
            X -= dX;
            Y -= dY;
            Particle particle = new Particle(game, (int)X, (int)Y, ParticleEffectType.SMOKE, 1, true);
            particle.width = particle.height = 25;
            game.addParticleEffect(particle);
        }
    }
    @Override
    public double getZ(){
        return Math.max(5, 1-.05*landing+5.56);
    }
}

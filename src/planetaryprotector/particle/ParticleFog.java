package planetaryprotector.particle;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
public class ParticleFog extends MenuComponentParticle{
    public static final double SIZE = 200;
    public ParticleFog(double x, double y, boolean air, double opacity){
        super(x, y, ParticleEffectType.FOG);
        x+=(Core.game.rand.nextDouble()-.5)*SIZE/10;
        y+=(Core.game.rand.nextDouble()-.5)*SIZE/10;
        width = height = SIZE;
        this.air = air;
        this.opacity = opacity;
        rotSpeed = Core.game.rand.nextDouble()*10-5;
    }
    @Override
    public void render(){
        removeRenderBound();
        if(dead){
            MenuGame game = (MenuGame) parent;
            game.componentsToRemove.add(this);
            return;
        }
        switch(Core.game.phase){
            case 2:
                GL11.glColor4d(.95, .95, .95, opacity);
                break;
            case 3:
                GL11.glColor4d(.9, .9-.01, .9-.01, opacity);
                break;
            case 4:
                if(Core.game.mothership!=null){
                    switch(Core.game.mothership.phase){
                        case 2:
                            GL11.glColor4d(.7, .7-.01, .7-.01, opacity);
                            break;
                        case 3:
                            GL11.glColor4d(.65, .65-.01, .65-.01, opacity);
                            break;
                        case 4:
                            GL11.glColor4d(.6, .6-.01, .6-.01, opacity);
                            break;
                        case 1:
                        default:
                            GL11.glColor4d(.75, .75-.01, .75-.01, opacity);
                            break;
                    }
                    break;
                }
            default:
                GL11.glColor4d(1, 1, 1, opacity);
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), images[0]);
        GL11.glPopMatrix();
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        rotation+=rotSpeed;
        x+=5;
        if(x>Display.getWidth()){
            dead = true;
        }
    }
}
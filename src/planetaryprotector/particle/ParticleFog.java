package planetaryprotector.particle;
import planetaryprotector.Core;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import simplelibrary.opengl.ImageStash;
public class ParticleFog extends Particle{
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
        if(dead){
            return;
        }
        double lightness = 1;
        double redTint = 0;
        switch(Core.game.phase){
            case 2:
                lightness = .95;
                break;
            case 3:
                lightness = .9;
                redTint = .01;
                break;
            case 4:
                if(Core.game.mothership!=null){
                    switch(Core.game.mothership.phase){
                        case 2:
                            lightness = .7;
                            redTint = .01;
                            break;
                        case 3:
                            lightness = .65;
                            redTint = .01;
                            break;
                        case 4:
                            lightness = .6;
                            redTint = .01;
                            break;
                        case 1:
                        default:
                            lightness = .75;
                            redTint = .01;
                            break;
                    }
                    break;
                }
            default:
                lightness = 1;
        }
        if(MenuGame.theme==MenuGame.Theme.SPOOKY){
            redTint+=.01;
            lightness-=.125;
        }
        if(MenuGame.theme==MenuGame.Theme.SNOWY){
            lightness = Math.sqrt(lightness);
        }
        GL11.glColor4d(lightness, lightness-redTint, lightness-redTint, opacity*MenuOptionsGraphics.fogIntensity);
        GL11.glPushMatrix();
        GL11.glTranslated(x+width/2, y+height/2, 0);
        GL11.glRotated(rotation, 0, 0, 1);
        drawRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ImageStash.instance.getTexture(images[0]));
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
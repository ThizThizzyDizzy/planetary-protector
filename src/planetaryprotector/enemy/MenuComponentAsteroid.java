package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.component.MenuComponentAnimation;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.particle.ParticleEffectType;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.ZComponent;
public class MenuComponentAsteroid extends MenuComponentAnimation implements ZComponent{
    public AsteroidMaterial material;
    private boolean hit;
    public boolean drop = true;
    private final boolean canParticulate;
    private final int particleResolution = MenuOptionsGraphics.particles*6;
    public MenuComponentAsteroid(double x, double y, AsteroidMaterial material, boolean canParticulate){
        super(x,y, 50, 50, material.images);
        delay /= 2;
        this.material = material;
        this.canParticulate = canParticulate;
    }
    @Override
    public void render(){
        if(frame>=12&&!hit){
            hit = true;
            if(drop){
                Core.game.damage(x+width/2,y+height/2, material);
            }else{
                Core.game.damage(x+width/2, y+height/2);
            }
            Core.game.pushParticles(x+width/2, y+height/2, width, width/2);
        }
        if(frame>=images.length-1&&x>-100){
            x = -5000;
            Core.game.componentsToRemove.add(this);
        }
        if(MenuOptionsGraphics.particulateMeteors&&canParticulate&&frame<=12){
            removeRenderBound();
            double fallProgress = (frame-1)/12d;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-Display.getWidth()*.7;
            double startY = landY-Display.getHeight();
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            GL11.glColor4d(1, 1, 1, 1);
            drawRect(X-width/2, Y-width/2, X+width/2, Y+height/2, images[0]);
            return;
        }
        super.render();
    }
    @Override
    public void tick(){
        super.tick();
        if(MenuOptionsGraphics.particulateMeteors&&canParticulate&&frame<=12){
            double fallProgress = frame/12d;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-Display.getWidth()*.7;
            double startY = landY-Display.getHeight();
            double frameDistanceX = (landX-startX)/12d;
            double frameDistanceY = (landY-startY)/12d;
            double dX = frameDistanceX/particleResolution;
            double dY = frameDistanceY/particleResolution;
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            for(int i = 0; i<particleResolution; i++){
                X-=dX;
                Y-=dY;
                MenuComponentParticle particle = new MenuComponentParticle(X, Y, ParticleEffectType.SMOKE, 1, true);
                particle.width = particle.height = 25;
                Core.game.addParticleEffect(particle);
            }
        }
    }
    @Override
    public double getZ(){
        return Math.max(5, 1-.05*frame+5.56);
    }
}
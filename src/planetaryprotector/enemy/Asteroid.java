package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.menu.component.GameObjectAnimated;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.ImageStash;
public class Asteroid extends GameObjectAnimated implements ZComponent{
    public AsteroidMaterial material;
    private boolean hit;
    public boolean drop = true;
    private final int particulate;
    public static final int PARTICULATE_NEVER = 0;
    /**
     * enables particulate meteors only if the settings allow
     */
    public static final int PARTICULATE_SETTINGS = 1;
    public static final int PARTICULATE_ALWAYS = 2;
    private final int particleResolution = MenuOptionsGraphics.particles*6;
    /**
     * when the asteroid hits the ground, previously a constant 12, thus the name
     */
    private double twelve = 24;
    /**
     * @param x
     * @param y
     * @param material the material of the asteroid
     * @param particulate 0=never, 1=only if settings allow, 2=always
     */
    public Asteroid(double x, double y, AsteroidMaterial material, int particulate){
        super(x,y, 50, 50, material.images);
        delay /= 2;
        twelve /= 2;
        this.material = material;
        this.particulate = particulate;
    }
    @Override
    public void render(){
        if(dead)return;
        if(isParticulate()&&frame<=twelve){
            double fallProgress = (frame-1)/(double)twelve;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-Display.getWidth()*.7;
            double startY = landY-Display.getHeight();
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            GL11.glColor4d(1, 1, 1, 1);
            drawRect(X-width/2, Y-width/2, X+width/2, Y+height/2, ImageStash.instance.getTexture(images[0]));
            return;
        }
        super.render();
    }
    @Override
    public void tick(){
        if(dead)return;
        super.tick();
        if(frame>=twelve&&!hit){
            hit = true;
            if(drop){
                Core.game.damage(x+width/2,y+height/2, material);
            }else{
                Core.game.damage(x+width/2, y+height/2);
            }
            Core.game.pushParticles(x+width/2, y+height/2, width*2, width/8, Particle.PushCause.ASTEROID);
        }
        if(frame>=images.length-1&&x>-100){
            dead = true;
        }
        if(isParticulate()&&frame<=twelve){
            double fallProgress = frame/(double)twelve;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-Display.getWidth()*.7;
            double startY = landY-Display.getHeight();
            double frameDistanceX = (landX-startX)/(double)twelve;
            double frameDistanceY = (landY-startY)/(double)twelve;
            double dX = frameDistanceX/particleResolution;
            double dY = frameDistanceY/particleResolution;
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            for(int i = 0; i<particleResolution; i++){
                X-=dX;
                Y-=dY;
                Particle particle = new Particle(X, Y, ParticleEffectType.SMOKE, 1, true);
                particle.width = particle.height = 25;
                Core.game.addParticleEffect(particle);
            }
        }
    }
    @Override
    public double getZ(){
        return Math.max(5, 1-.05*frame+5.56);
    }
    private boolean isParticulate(){
        switch(particulate){
            case 0:
                return false;
            case 1:
                return MenuOptionsGraphics.particulateMeteors;
            case 2:
                return true;
        }
        return true;
    }
}
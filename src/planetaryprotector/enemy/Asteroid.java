package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.ImageStash;
public class Asteroid extends GameObject implements ZComponent{
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
     * when the asteroid hits the ground
     */
    private static final int frames = 19;
    private static final int hitFrame = 12;
    private int frame = 0;//the amount this has fallen
    /**
     * @param x
     * @param y
     * @param material the material of the asteroid
     * @param particulate 0=never, 1=only if settings allow, 2=always
     */
    public Asteroid(Game game, int x, int y, AsteroidMaterial material, int particulate){
        super(game, x,y, 50, 50);
        this.material = material;
        this.particulate = particulate;
    }
    @Override
    public void draw(){
        if(dead)return;
        GL11.glColor4d(1, 1, 1, 1);
        if(isParticulate()&&frame<=hitFrame){
            double fallProgress = (frame-1)/(double)hitFrame;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-game.getWorldBoundingBox().width*.7;
            double startY = landY-game.getWorldBoundingBox().height;
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            drawRect(X-width/2, Y-width/2, X+width/2, Y+height/2, ImageStash.instance.getTexture("/textures/asteroids/stone.png"), 0, 0, 1, 1d/frames);
            if(material.color!=null){
                GL11.glColor4d(material.color.getRed()/255d,material.color.getGreen()/255d,material.color.getBlue()/255d,1);
                drawRect(X-width/2, Y-width/2, X+width/2, Y+height/2, ImageStash.instance.getTexture("/textures/asteroids/ore.png"), 0, 0, 1, 1d/frames);
            }
            return;
        }
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/asteroids/stone.png"), 0, frame/(double)frames, 1, (frame+1d)/frames);
        if(material.color!=null){
            GL11.glColor4d(material.color.getRed()/255d,material.color.getGreen()/255d,material.color.getBlue()/255d,1);
            drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/asteroids/ore.png"), 0, frame/(double)frames, 1, (frame+1d)/frames);
        }
    }
    public void tick(){
        if(dead)return;
        frame++;
        if(frame>=hitFrame&&!hit){
            hit = true;
            if(drop){
                game.damage(x+width/2,y+height/2, material);
            }else{
                game.damage(x+width/2, y+height/2);
            }
            game.pushParticles(x+width/2, y+height/2, width*2, width/8, Particle.PushCause.ASTEROID);
        }
        if(frame>=frames){
            dead = true;
        }
        if(isParticulate()&&frame<=hitFrame){
            double fallProgress = frame/(double)hitFrame;
            double landX = x+width/2;
            double landY = y+height/2;
            double startX = landX-game.getWorldBoundingBox().width*.7;
            double startY = landY-game.getWorldBoundingBox().height;
            double frameDistanceX = (landX-startX)/(double)hitFrame;
            double frameDistanceY = (landY-startY)/(double)hitFrame;
            double dX = frameDistanceX/particleResolution;
            double dY = frameDistanceY/particleResolution;
            double X = Core.getValueBetweenTwoValues(0, startX, 1, landX, fallProgress);
            double Y = Core.getValueBetweenTwoValues(0, startY, 1, landY, fallProgress);
            for(int i = 0; i<particleResolution; i++){
                X-=dX;
                Y-=dY;
                Particle particle = new Particle(game, (int)X, (int)Y, ParticleEffectType.SMOKE, 1, true);
                particle.width = particle.height = 25;
                game.addParticleEffect(particle);
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
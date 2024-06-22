package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.MathUtil;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.GameObject;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
import planetaryprotector.menu.component.ZComponent;
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
    private final int particleResolution = Options.options.particles*6;
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
        Renderer.setColor(1, 1, 1, 1);
        if(isParticulate()&&frame<=hitFrame){
            float fallProgress = (frame-1)/(float)hitFrame;
            float landX = x+width/2;
            float landY = y+height/2;
            float startX = landX-game.getWorldBoundingBox().width*.7f;
            float startY = landY-game.getWorldBoundingBox().height;
            float X = MathUtil.lerp(startX, landX, fallProgress);
            float Y = MathUtil.lerp(startY, landY, fallProgress);
            Renderer.fillRect(X-width/2, Y-width/2, X+width/2, Y+height/2, ResourceManager.getTexture("/textures/asteroids/stone.png"), 0, 0, 1, 1f/frames);
            if(material.color!=null){
                Renderer.setColor(material.color);
                Renderer.fillRect(X-width/2, Y-width/2, X+width/2, Y+height/2, ResourceManager.getTexture("/textures/asteroids/ore.png"), 0, 0, 1, 1f/frames);
            }
            return;
        }
        Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture("/textures/asteroids/stone.png"), 0, frame/(float)frames, 1, (frame+1f)/frames);
        if(material.color!=null){
            Renderer.setColor(material.color);
            Renderer.fillRect(x, y, x+width, y+height, ResourceManager.getTexture("/textures/asteroids/ore.png"), 0, frame/(float)frames, 1, (frame+1f)/frames);
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
            double X = MathUtil.lerp(startX, landX, fallProgress);
            double Y = MathUtil.lerp(startY, landY, fallProgress);
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
                return Options.options.particulateMeteors;
            case 2:
                return true;
        }
        return true;
    }
}
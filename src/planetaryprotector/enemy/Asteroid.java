package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.collision.AxisAlignedBoundingBox;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import com.thizthizzydizzy.dizzyengine.world.object.SizedWorldObject;
import org.joml.Vector3f;
import planetaryprotector.Options;
import planetaryprotector.game.Game;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
public class Asteroid extends SizedWorldObject{
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
    public final float targetX, targetY;
    private final Game game;
    private static final int SPAWN_HEIGHT = 8000;
    private int LAND_HEIGHT; // The Z level at which to raycast the collision.
    private boolean isFront;
    /**
     * @param x
     * @param y
     * @param material the material of the asteroid
     * @param particulate 0=never, 1=only if settings allow, 2=always
     */
    public Asteroid(Game game, int x, int y, AsteroidMaterial material, int particulate){
        this.game = game;
        this.targetX = x;
        this.targetY = y;
        LAND_HEIGHT = (int)game.getCityBoundingBox().max.z+100;
        setPosition(new Vector3f(x, y+SPAWN_HEIGHT, SPAWN_HEIGHT));
        setSize(new Vector3f(50, 50, 0));
        this.material = material;
        this.particulate = particulate;
    }
    @Override
    public void render(){
        if(isDead())return;
        float w = getSize().x;
        float h = getSize().y;
        float v1 = 1-(frame/(float)frames);
        float v2 = 1-((frame+1f)/frames);
        
        int stoneTex = ResourceManager.getTexture("/textures/asteroids/stone.png");
        Renderer.setColor(1, 1, 1, 1);
        
        if(isFront){
            Renderer.fillRectXZ(-w/2, -h/2, w/2, h/2, .01f, stoneTex, 0, v2, 1, v1);
        }else{
            Renderer.fillRect(-w/2, -h/2, w/2, h/2, stoneTex, 0, v1, 1, v2);
        }
        if(material.color!=null){
            Renderer.setColor(material.color);
            int oreTex = ResourceManager.getTexture("/textures/asteroids/ore.png");
            if(isFront){
                Renderer.fillRectXZ(-w/2, -h/2, w/2, h/2, .01f, oreTex, 0, v2, 1, v1);
            }else{
                Renderer.fillRect(-w/2, -h/2, w/2, h/2, oreTex, 0, v1, 1, v2);
            }
        }
        Renderer.setColor(1, 1, 1, 1);
    }
    public org.joml.Vector2f getCenter(){
        return new org.joml.Vector2f(targetX, targetY);
    }
    public void tick(){
        if(isDead())return;
        frame++;
        if(hit){
            if(frame>=frames){
                remove();
            }
            return;
        }
        Vector3f pos = getPosition();
        float lastX = pos.x;
        float lastY = pos.y;
        float lastZ = pos.z;
        pos.z -= (SPAWN_HEIGHT-LAND_HEIGHT)/(float)hitFrame;
        if(pos.z<=0)pos.z = 0;
        pos.y = targetY+pos.z; //TODO include skew factor
        if(isParticulate()){
            float offset = (pos.z-LAND_HEIGHT)/100;
            pos.x = targetX-offset;
            pos.y-=offset;
            
            float dX = (pos.x-lastX)/particleResolution;
            float dY = (pos.y-lastY)/particleResolution;
            float dZ = (pos.z-lastZ)/particleResolution;
            for(int i = 0; i<particleResolution; i++){
                float X = lastX+dX*i;
                float Y = lastY+dY*i;
                float Z = lastZ+dZ*i;
                Particle particle = new Particle(game, (int)X, (int)Y, ParticleEffectType.SMOKE);
                particle.setPosition(new Vector3f(X, Y, Z));
                particle.setSize(new Vector3f(25, 25, 25));
                game.addParticleEffect(particle);
            }
        }
        setPosition(pos);
        if(frame>=hitFrame&&!hit){
            hit = true;
            var raycast = game.damage((int)targetX, (int)targetY, drop?material:null);
            if(raycast.hitFront)isFront = true;
            setPosition(raycast.hitPosition);
            game.pushParticles((int)targetX, (int)targetY, getSize().x*2, getSize().x/8, Particle.PushCause.ASTEROID);
        }
    }
    @Override
    public AxisAlignedBoundingBox getAxisAlignedBoundingBox(){
        AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
        bbox.min.set(getSize()).mul(-.5f).add(getPosition()).add(0, 0, getSize().z/2);
        bbox.max.set(getSize()).mul(.5f).add(getPosition()).add(0, 0, getSize().z/2);
        return bbox;
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

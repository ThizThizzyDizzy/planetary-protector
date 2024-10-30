package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Vector2f;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.Core;
import planetaryprotector.structure.Structure;
public class EnemyLaser extends Enemy{
    public int initialDelay = 20*5;
    public float laserPower = 1;
    public float laserTime = 20*10;
    public float laserSize = 20;
    public float laserSizing = 1/3f;
    public EnemyLaser(Game game){
        super(game, 0, 0, 50, 50, 100);
        int[] location = getBestStrike(game);
        if(location==null){
            location = game.getCityBoundingBox().getCenter();
        }
        x = location[0];
        y = location[1];
        laserPower *= strength;
    }
    @Override
    public void draw(){
        if(laserFiring!=null){
            float xDiff = laserFiring[0]-x;
            float yDiff = laserFiring[1]-y;
            float dist = (float)Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, 0, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, laserSize/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, .5f, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (laserSize*(2/3f))/2f);
            }
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.setColor(1, 1, 0, 1);
                Renderer.fillRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), 10, (laserSize*(1/3f))/2f);
                Renderer.setColor(1, 1, 1, 1);
            }
        }
        width = height = (int)(50*((initialDelay/20D)+1));
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(x-width/2, y-height/2, x+width/2, y+height/2, ResourceManager.getTexture("/textures/enemies/ship.png"));
        Renderer.setColor(1, 1, 1, 1);
    }
    boolean increase = true;
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            increase = true;
            game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(increase&&strength<7.5){
                strength += .375;
            }
            return;
        }
        if(laserTime<=0){
            laserFiring = null;
            initialDelay++;
            if(initialDelay>=20*5){
                dead = true;
                if(increase&&strength<7.5){
                    strength += .375;
                }
            }
            return;
        }
        initialDelay--;
        if(initialDelay<=0){
            initialDelay = 0;
            fireLaser();
        }
    }
    float[] laserFiring = null;
    private void fireLaser(){
        laserFiring = null;
        double dist = Double.POSITIVE_INFINITY;
        ShieldGenerator gen = null;
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                ShieldGenerator g = (ShieldGenerator)structure;
                dist = Math.min(dist, Vector2f.distance(x, y, g.x, g.y));
                gen = g;
            }
        }
        if(gen==null)return;
        if(gen.getShieldSize(laserPower*40)<=0){
            increase = false;
            return;
        }
        laserSize += laserSizing;
        if(laserSize>=25){
            laserSizing *= -1;
        }
        if(laserSize<=15){
            laserSizing *= -1;
        }
        laserTime--;
        laserFiring = new float[]{gen.x+gen.width/2, gen.y+gen.height/2};
        gen.setShieldSize(gen.getShieldSize()-laserPower*10);
    }
}

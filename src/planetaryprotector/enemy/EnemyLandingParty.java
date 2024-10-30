package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.ShieldGenerator;
import java.util.ArrayList;
import org.joml.Vector2f;
import planetaryprotector.structure.Structure;
public class EnemyLandingParty extends Enemy{
    public int initialDelay = 20*10;
    public float laserPower = 1.25f;
    public float laserTime = 20*30;
    public float laserSize = 20;
    public float laserSizing = 1/3f;
    public EnemyLandingParty(Game game){
        super(game, 0, 0, 50, 50, 250);
        int[] location = getFurthestCorner(game);
        if(location==null){
            location = game.getCityBoundingBox().getCenter();
        }
        x = location[0];
        y = location[1];
        laserPower *= strength;
        for(int i = 0; i<strength-7; i++){
            crew.add(new EnemyAlien(game, x, y));
        }
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
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            increase = true;
            game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(increase&&strength<10){
                strength += .5;
            }
            return;
        }
        DO:
        do{
            for(EnemyAlien alien : crew){
                if(!alien.dead){
                    break DO;
                }
            }
            if(crew.isEmpty())break;
            running = true;
        }while(false);
        if(laserTime<=0||running){
            if(laserTime<=0){
                laserFiring = null;
            }
            initialDelay++;
            if(initialDelay>=20*10){
                if(!landed){
                    increase = true;
                }
                for(EnemyAlien alien : crew){
                    if(!alien.dead){
                        increase = true;
                    }
                }
                if(increase&&strength<10){
                    strength += .5;
                }
                dead = true;
            }
            return;
        }
        fireLaser();
        if(initialDelay>=20*5||landing){
            initialDelay--;
        }
        if(initialDelay<0){
            initialDelay = 0;
            land();
        }
    }
    boolean running = false;
    float[] laserFiring = null;
    boolean landing = false;
    public boolean landed = false;
    boolean increase = false;
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
        if(gen.getShieldSize()/2>dist-25&&initialDelay<20*5){
            dead = true;
            increase = true;
        }
        if(gen.getShieldSize()/2<dist-50){
            landing = true;
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
    public static int[] getFurthestCorner(Game game){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                shieldGen.add((ShieldGenerator)structure);
            }
        }
        ArrayList<int[]> possibleStrikes = new ArrayList<>();//TODO redo!
        for(ShieldGenerator gen : shieldGen){
            int dist = (int)Vector2f.distance(gen.x, gen.y, 25, 25);
            possibleStrikes.add(new int[]{25, 25, dist});
            dist = (int)Vector2f.distance(gen.x, gen.y, game.getCityBoundingBox().width-25, 25);
            possibleStrikes.add(new int[]{game.getCityBoundingBox().width-25, 25, dist});
            dist = (int)Vector2f.distance(gen.x, gen.y, 25, game.getCityBoundingBox().height-25);
            possibleStrikes.add(new int[]{25, game.getCityBoundingBox().height-25, dist});
            dist = (int)Vector2f.distance(gen.x, gen.y, game.getCityBoundingBox().width-25, game.getCityBoundingBox().height-25);
            possibleStrikes.add(new int[]{game.getCityBoundingBox().width-25, game.getCityBoundingBox().height-25, dist});
        }
        int max = Integer.MIN_VALUE;
        for(int[] strike : possibleStrikes){
            max = Math.max(strike[2], max);
        }
        for(int[] strike : possibleStrikes){
            if(strike[2]==max){
                return new int[]{strike[0], strike[1]};
            }
        }
        return null;
    }
    ArrayList<EnemyAlien> crew = new ArrayList<>();
    private void land(){
        landed = true;
        for(EnemyAlien alien : crew){
            if(game.enemies.contains(alien))break;
            if(alien.dead)continue;
            game.enemies.add(alien);
            break;
        }
    }
}

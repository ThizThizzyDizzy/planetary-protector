package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.building.ShieldGenerator;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.structure.Structure;
import simplelibrary.opengl.ImageStash;
public class EnemyLandingParty extends Enemy{
    public int initialDelay = 20*10;
    public double laserPower = 1.25;
    public double laserTime = 20*30;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    public EnemyLandingParty(Game game){
        super(game, 0, 0, 50, 50, 250);
        double[] location = getFurthestCorner(game);
        if(location==null){
            location = game.getCityBoundingBox().getCenter();
        }
        x=location[0];
        y=location[1];
        laserPower*=strength;
        for(int i = 0; i<strength-7; i++){
            crew.add(new EnemyAlien(game, x, y));
        }
    }
    @Override
    public void render(){
        if(laserFiring!=null){
            double xDiff = laserFiring[0]-x;
            double yDiff = laserFiring[1]-y;
            double dist = Math.sqrt((xDiff*xDiff)+(yDiff*yDiff));
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, 0, 0, 1);
                Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), laserSize/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, .5, 0, 1);
                Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(2/3D))/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, 1, 0, 1);
                Game.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(1/3D))/2D,10,0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        width = height = 50*((initialDelay/20D)+1);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/ship.png"));
        GL11.glColor4d(1, 1, 1, 1);
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
                strength+=.5;
            }
            return;
        }
        DO:do{
            for(EnemyAlien alien : crew){
                if(!alien.dead){
                    break DO;
                }
            }
            if(crew.isEmpty()) break;
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
                    strength+=.5;
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
    double[] laserFiring = null;
    boolean landing = false;
    public boolean landed = false;
    boolean increase = false;
    private void fireLaser(){
        laserFiring = null;
        double dist = Double.POSITIVE_INFINITY;
        ShieldGenerator gen = null;
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                ShieldGenerator g = (ShieldGenerator) structure;
                dist = Math.min(dist,Core.distance(this, g));
                gen = g;
            }
        }
        if(gen==null) return;
        if(gen.getShieldSize()/2>dist-25&&initialDelay<20*5){
            dead = true;
            increase = true;
        }
        if(gen.getShieldSize()/2<dist-50){
            landing = true;
            return;
        }
        laserSize+=laserSizing;
        if(laserSize>=25){
            laserSizing*=-1;
        }
        if(laserSize<=15){
            laserSizing*=-1;
        }
        laserTime--;
        laserFiring = new double[]{gen.x+gen.width/2,gen.y+gen.height/2};
        gen.setShieldSize(gen.getShieldSize()-laserPower*10);
    }
    public static double[] getFurthestCorner(Game game){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                shieldGen.add((ShieldGenerator) structure);
            }
        }
        ArrayList<Double[]> possibleStrikes = new ArrayList<>();//TODO redo!
        for(ShieldGenerator gen : shieldGen){
            double dist = Core.distance(gen,25d,25d);
            possibleStrikes.add(new Double[]{25d,25d,dist});
            dist = Core.distance(gen,game.getCityBoundingBox().width-25d,25d);
            possibleStrikes.add(new Double[]{game.getCityBoundingBox().width-25d,25d,dist});
            dist = Core.distance(gen,25d,game.getCityBoundingBox().height-25d);
            possibleStrikes.add(new Double[]{25d,game.getCityBoundingBox().height-25d,dist});
            dist = Core.distance(gen,game.getCityBoundingBox().width-25d,game.getCityBoundingBox().height-25d);
            possibleStrikes.add(new Double[]{game.getCityBoundingBox().width-25d,game.getCityBoundingBox().height-25d,dist});
        }
        double max = Double.NEGATIVE_INFINITY;
        for(Double[] strike : possibleStrikes){
            max = Math.max(strike[2], max);
        }
        for(Double[] strike : possibleStrikes){
            if(strike[2]==max){
                return new double[]{strike[0],strike[1]};
            }
        }
        return null;
    }
    ArrayList<EnemyAlien> crew = new ArrayList<>();
    private void land(){
        landed = true;
        for(EnemyAlien alien : crew){
            if(game.enemies.contains(alien)) break;  
            if(alien.dead) continue;
            game.enemies.add(alien);
            break;
        }
    }
}
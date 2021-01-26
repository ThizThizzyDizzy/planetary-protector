package planetaryprotector.enemy;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.ShieldGenerator;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.structure.Structure;
import simplelibrary.opengl.ImageStash;
public class EnemyLaserMeteor extends Enemy{
    public int initialDelay = 20*5;
    public double laserPower = 1.5;
    public double laserTime = 20*30;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    int[] loc = null;
    boolean incrase = false;
    public EnemyLaserMeteor(Game game){
        super(game, 0, 0, 50, 50, 150);
        int[] location = EnemyMeteorStrike.getMeteorStrike(game);
        if(location==null){
            location = EnemyMeteorStrike.getBestStrike(game);
            if(location==null){
                location = game.getCityBoundingBox().getCenter();
            }
        }
        loc = location;
        x=location[0]-100;
        y=location[1];
        laserPower*=strength;
    }
    @Override
    public void draw(){
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
        width = height = (int)(50*((initialDelay/20D)+1));
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/ship.png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
    boolean meteor = false;
    boolean increase = true;
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            increase = true;
            game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(increase&&strength<maxStrength){
                strength++;
            }
            return;
        }
        if(EnemyMeteorStrike.getMeteorStrike(game)!=null&&!meteor){
            EnemyMeteorStrike strike = new EnemyMeteorStrike(game);
            strike.x = loc[0];
            strike.y = loc[1];
            strike.initialDelay = 0;
            game.enemies.add(strike);
            meteor = true;
            increase = false;
        }
        if(laserTime<=0){
            laserFiring = null;
            initialDelay++;
            if(initialDelay>=20*5){
                dead = true;
                if(increase&&strength<maxStrength){
                    strength++;
                }
                return; 
            }
            return;
        }
        initialDelay--;
        if(initialDelay<=0){
            initialDelay = 0;
            fireLaser();
        }
    }
    double[] laserFiring = null;
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
        if(gen.getShieldSize(laserPower*40)<=0){
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
}
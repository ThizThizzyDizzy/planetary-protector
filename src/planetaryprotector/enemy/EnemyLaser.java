package planetaryprotector.enemy;
import org.lwjgl.opengl.Display;
import planetaryprotector.particle.Particle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.Building;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import simplelibrary.opengl.ImageStash;
public class EnemyLaser extends Enemy{
    public int initialDelay = 20*5;
    public double laserPower = 1;
    public double laserTime = 20*10;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    private final MenuGame game;
    public EnemyLaser(MenuGame game){
        super(0, 0, 50, 50, 100);
        double[] location = getBestStrike();
        if(location==null){
            location = new double[]{Display.getWidth()/2, Display.getHeight()/2};
        }
        x=location[0];
        y=location[1];
        laserPower*=strength;
        this.game = game;
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
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), laserSize/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, .5, 0, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(2/3D))/2D,10,0);
            }
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                GL11.glColor4d(1, 1, 0, 1);
                MenuGame.drawRegularPolygon(x+(xDiff*percent), y+(yDiff*percent), (laserSize*(1/3D))/2D,10,0);
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        width = height = 50*((initialDelay/20D)+1);
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/ship.png"));
        GL11.glColor4d(1, 1, 1, 1);
    }
    boolean increase = true;
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            increase = true;
            game.addParticleEffect(new Particle(x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(increase&&strength<7.5){
                strength+=.375;
            }
            return;
        }
        if(laserTime<=0){
            laserFiring = null;
            initialDelay++;
            if(initialDelay>=20*5){
                dead = true;
                if(increase&&strength<7.5){
                    strength+=.375;
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
    double[] laserFiring = null;
    private void fireLaser(){
        laserFiring = null;
        double dist = Double.POSITIVE_INFINITY;
        ShieldGenerator gen = null;
        for(Building building : game.buildings){
            if(building instanceof ShieldGenerator){
                ShieldGenerator g = (ShieldGenerator) building;
                dist = Math.min(dist,Core.distance(this, g));
                gen = g;
            }
        }
        if(gen==null) return;
        if(gen.getShieldSize(laserPower*40)<=0){
            increase = false;
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
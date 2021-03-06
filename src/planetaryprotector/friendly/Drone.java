package planetaryprotector.friendly;
import java.util.Random;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.particle.Particle;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.structure.Silo;
import org.lwjgl.opengl.GL11;
import planetaryprotector.GameObject;
import planetaryprotector.enemy.EnemyMothership;
import simplelibrary.opengl.ImageStash;
public class Drone extends GameObject{
    int power = 0;
    int maxPower = 20*60*5;
    double[] target = new double[]{0,0};
    Silo silo;
    double speed = 2.5;
    public double laserPower = 1/4D;
    public double laserSize = 20;
    public double laserSizing = 1/3D;
    boolean charge = false;
    boolean deaded = false;
    public Drone(Silo silo, int power){
        super(silo.game, silo.x+silo.width/2, silo.y+silo.height/2, 50, 50);
        this.silo = silo;
        this.power = power;
    }
    public void tick(){
        if(deaded) return;
        if(power<=0){
            dead = true;
            game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, 1, true));
            if(silo!=null){
                silo.drones--;
                silo.droneList.remove(this);
            }
            deaded = true;
            return;
        }
        power--;
        if(silo!=null&&(silo.damages.size()>=10||!game.structures.contains(silo))){
            silo = null;
        }
        if(silo!=null&&Core.distance(this, silo)<100&&silo.getPower()>=100&&power<=maxPower-10){
            silo.addPower(-100);
            power+=10;
        }
        power = Math.max(0,Math.min(maxPower, power));
        if(en!=null){
            target = new double[]{en.x,en.y};
            if(Core.distance(this, en)<=en.width/2+width*5){
                fireLaser();
            }
            if(en!=null){
                if(Core.distance(this, en)<=en.width/2+width){
                    target = new double[]{x,y};
                }
            }
        }else{
            target = new double[]{x,y};
            findEnemy();
        }
        if(silo!=null&&power<20*30){
            charge = true;
        }
        if(charge){
            if(silo==null||power>=20*60*2.5){
                charge = false;
            }
            target = new double[]{silo.x+silo.width/2,silo.y+silo.height/2};
        }
        if(power<20*15){
            laserFiring = null;
        }
        if(laserFiring!=null){
            power-=laserPower;
            en.health-=laserPower;
        }
        for(Drone drone : silo.droneList){
            if(drone==this)continue;
            if(drone.x==this.x&&drone.y==this.y){
                target = new double[]{game.getCityBoundingBox().randX(game.rand),game.getCityBoundingBox().randY(game.rand)};
            }else{
                double dist = Core.distance(drone, this);
                if(dist<width){
                    double xDiff = drone.x-x;
                    double yDiff = drone.y-y;
                    target = new double[]{x-xDiff, y-yDiff};
                }
            }
        }
        double xDiff = target[0]-x;
        double yDiff = target[1]-y;
        double dist = Core.distance(0, 0, xDiff, yDiff);
        xDiff/=dist;
        yDiff/=dist;
        xDiff*=speed;
        yDiff*=speed;
        x+=xDiff;
        y+=yDiff;
    }
    double[] laserFiring = null;
    Enemy en = null;
    private void findEnemy(){
        double dist = Double.POSITIVE_INFINITY;
        en = null;
        for(Enemy enemy : game.enemies){
            if(enemy instanceof EnemyMeteorStrike||enemy instanceof EnemyMothership)continue;
            double d = Core.distance(this, enemy);
            if(d<dist){
                en = enemy;
                dist = d;
            }
        }
        if(en==null){
            for(Enemy enemy : game.enemies){
                if(enemy instanceof EnemyMothership){
                    en = enemy;
                }
            }
        }
    }
    private void fireLaser(){
        laserFiring = null;
        findEnemy();
        if(en==null) return;
        laserSize+=laserSizing;
        if(laserSize>=25){
            laserSizing*=-1;
        }
        if(laserSize<=15){
            laserSizing*=-1;
        }
        laserFiring = new double[]{en.x,en.y};
    }
    @Override
    public void draw(){
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(0, 0, 0, 0, 0);
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
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/drone.png"));
        GL11.glColor4d(0, 0, 0, 1);
        drawRect(x-width/2, y+height/2, x+width/2, y+height/2+5, 0);
        GL11.glColor4d(0, 0.25, 1, 1);
        drawRectWithBounds(x-width/2,y+height/2,x-width/2+(width*(power/(double)maxPower)),y+height/2+5,x-width/2+1, y+height/2+1, x+width/2-1, y+height/2+5-1, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
}
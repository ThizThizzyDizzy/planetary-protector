package planetaryprotector.friendly;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Vector2f;
import planetaryprotector.GameObject;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.Silo;
public class Drone extends GameObject{
    int power = 0;
    int maxPower = 20*60*5;
    float[] target = new float[]{0, 0};
    Silo silo;
    double speed = 2.5;
    public float laserPower = 1/4f;
    public float laserSize = 20;
    public float laserSizing = 1/3f;
    boolean charge = false;
    boolean deaded = false;
    public Drone(Silo silo, int power){
        super(null, 0, 0, 50, 50);
//        super(silo.game, silo.x+silo.width/2, silo.y+silo.height/2, 50, 50);
        this.silo = silo;
        this.power = power;
    }
    public void tick(){
        if(deaded)return;
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
//        if(silo!=null&&Vector2f.distance(x, y, silo.x, silo.y)<100&&silo.getPower()>=100&&power<=maxPower-10){
//            silo.addPower(-100);
//            power += 10;
//        }
        power = Math.max(0, Math.min(maxPower, power));
        if(en!=null){
            target = new float[]{en.x, en.y};
            if(Vector2f.distance(x, y, en.x, en.y)<=en.width/2+width*5){
                fireLaser();
            }
            if(en!=null){
                if(Vector2f.distance(x, y, en.x, en.y)<=en.width/2+width){
                    target = new float[]{x, y};
                }
            }
        }else{
            target = new float[]{x, y};
            findEnemy();
        }
        if(silo!=null&&power<20*30){
            charge = true;
        }
        if(charge){
            if(silo==null||power>=20*60*2.5){
                charge = false;
            }
//            target = new float[]{silo.x+silo.width/2, silo.y+silo.height/2};
        }
        if(power<20*15){
            laserFiring = null;
        }
        if(laserFiring!=null){
            power -= laserPower;
            en.health -= laserPower;
        }
        for(Drone drone : silo.droneList){
            if(drone==this)continue;
            if(drone.x==this.x&&drone.y==this.y){
//                target = new float[]{game.getCityBoundingBox().randX(game.rand), game.getCityBoundingBox().randY(game.rand)};
            }else{
                double dist = Vector2f.distance(drone.x, drone.y, x, y);
                if(dist<width){
                    float xDiff = drone.x-x;
                    float yDiff = drone.y-y;
                    target = new float[]{x-xDiff, y-yDiff};
                }
            }
        }
        float xDiff = target[0]-x;
        float yDiff = target[1]-y;
        float dist = Vector2f.distance(0, 0, xDiff, yDiff);
        xDiff /= dist;
        yDiff /= dist;
        xDiff *= speed;
        yDiff *= speed;
        x += xDiff;
        y += yDiff;
    }
    float[] laserFiring = null;
    Enemy en = null;
    private void findEnemy(){
        double dist = Double.POSITIVE_INFINITY;
        en = null;
        for(Enemy enemy : game.enemies){
            if(enemy instanceof EnemyMeteorStrike||enemy instanceof EnemyMothership)
                continue;
            double d = Vector2f.distance(x, y, enemy.x, enemy.y);
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
        if(en==null)return;
        laserSize += laserSizing;
        if(laserSize>=25){
            laserSizing *= -1;
        }
        if(laserSize<=15){
            laserSizing *= -1;
        }
        laserFiring = new float[]{en.x, en.y};
    }
    @Override
    public void draw(){
        Renderer.setColor(1, 1, 1, 1);
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
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(x-width/2, y-height/2, x+width/2, y+height/2, ResourceManager.getTexture("/textures/drone.png"));
        Renderer.setColor(0, 0, 0, 1);
        Renderer.fillRect(x-width/2, y+height/2, x+width/2, y+height/2+5, 0);
        Renderer.setColor(0, 0.25f, 1, 1);
        Renderer.bound(x-width/2+1, y+height/2+1, x+width/2-1, y+height/2+5-1);
        Renderer.fillRect(x-width/2, y+height/2, x-width/2+(width*(power/(float)maxPower)), y+height/2+5, 0);
        Renderer.setColor(1, 1, 1, 1);
    }
}

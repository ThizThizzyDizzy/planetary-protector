package planetaryprotector.friendly;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Options;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.particle.Particle;
import planetaryprotector.enemy.EnemyLandingParty;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.structure.Silo;
public class Missile extends Drone{
    Enemy target;
    double speed = 0;
    double accel = .0981;
    int power = 2500;
    int time = 0;
    public Missile(Silo silo, Enemy target){
        super(silo, 0);
        this.silo = silo;
        this.target = target;
    }
    boolean caboom = false;
    @Override
    public void tick(){
        if(target==null)return;
        speed += accel;
        y -= speed;
        time++;
        if(time==20*5&&speed>0){
            accel *= -3;
            x = target.x;
        }
        if(Options.options.particles>0&&!caboom){
            if(Options.options.particles==1&&time%2==0||Options.options.particles==2){
                game.addParticleEffect(new Particle(game, x, y+(accel<0?0:height), ParticleEffectType.SMOKE));
            }
        }
        if(y>target.y&&accel<0&&!caboom){
            caboom = true;
            y = target.y;
            target.health -= power+speed;
            dead = true;
            game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, Math.min(9, power/1000), target instanceof EnemyAlien?false:(target instanceof EnemyLandingParty?((EnemyLandingParty)target).landed:false)));
        }
    }
    @Override
    public void draw(){
        int texture = ResourceManager.getTexture("/textures/missile.png");
        if(speed<0){
            Renderer.fillRect(x, y+height, x+width, y, texture);
        }else{
            Renderer.fillRect(x, y, x+width, y+height, texture);
        }
    }
}

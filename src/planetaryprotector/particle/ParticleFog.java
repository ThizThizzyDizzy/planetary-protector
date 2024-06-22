package planetaryprotector.particle;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Matrix4f;
import planetaryprotector.Options;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.game.Game;
public class ParticleFog extends Particle{
    public static final int SIZE = 200;
    public ParticleFog(Game game, int x, int y, boolean air, float opacity){
        super(game, x, y, ParticleEffectType.FOG);
        x+=(game.rand.nextDouble()-.5)*SIZE/10;
        y+=(game.rand.nextDouble()-.5)*SIZE/10;
        width = height = SIZE;
        this.air = air;
        this.opacity = opacity;
        rotSpeed = game.rand.nextDouble()*10-5;
    }
    @Override
    public void draw(){
        if(dead){
            return;
        }
        float lightness = 1;
        float redTint = 0;
        switch(game.phase){
            case 2:
                lightness = .95f;
                break;
            case 3:
                lightness = .9f;
                redTint = .01f;
                break;
            case 4:
                int mothershipPhase = 0;
                for(Enemy e : game.enemies){
                    if(e instanceof EnemyMothership){
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership) e).phase);
                    }
                }
                if(mothershipPhase>=0){
                    switch(mothershipPhase){
                        case 2:
                            lightness = .7f;
                            redTint = .01f;
                            break;
                        case 3:
                            lightness = .65f;
                            redTint = .01f;
                            break;
                        case 4:
                            lightness = .6f;
                            redTint = .01f;
                            break;
                        case 1:
                        default:
                            lightness = .75f;
                            redTint = .01f;
                            break;
                    }
                    break;
                }
            default:
                lightness = 1;
        }
        if(Game.theme==Game.Theme.SPOOKY){
            redTint+=.01;
            lightness-=.125;
        }
        if(Game.theme==Game.Theme.SNOWY){
            lightness = (float)Math.sqrt(lightness);
        }
        Renderer.setColor(lightness, lightness-redTint, lightness-redTint, opacity*Options.options.fogIntensity);
        Renderer.pushModel(new Matrix4f().translate(x+width/2, y+height/2, 0).rotate(rotation, 0, 0, 1));
        Renderer.fillRect(-2*(width/2), -2*(height/2), 2*(width/2), 2*(height/2), ResourceManager.getTexture(images[0]));
        Renderer.popModel();
        Renderer.setColor(1,1,1,1);
    }
    @Override
    public void tick(){
        rotation+=rotSpeed;
        x+=5;
        if(x>game.getCityBoundingBox().getRight()+game.getXGamePadding()){
            dead = true;
        }
    }
}
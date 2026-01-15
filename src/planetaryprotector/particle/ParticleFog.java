package planetaryprotector.particle;
import com.thizthizzydizzy.dizzyengine.DizzyEngine;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Vector3f;
import planetaryprotector.Options;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.game.Game;
public class ParticleFog extends Particle{
    public static final int SIZE = 500;
    public ParticleFog(Game game, int x, int y, float z, float opacity){
        super(game, x, y, ParticleEffectType.FOG);
        setPosition(getPosition().add((float)(game.rand.nextDouble()-.5)*SIZE/20, (float)(game.rand.nextDouble()-.5)*SIZE/20, z+(float)(game.rand.nextDouble())*SIZE/20));
        setSize(new Vector3f(SIZE));
        this.opacity = opacity;
        rotSpeed = game.rand.nextDouble()*10-5;
    }
    @Override
    public void preRender(){
        super.preRender();
        var game = DizzyEngine.getLayer(Game.class);
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
                        mothershipPhase = Math.max(mothershipPhase, ((EnemyMothership)e).phase);
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
            redTint += .01;
            lightness -= .125;
        }
        if(Game.theme==Game.Theme.SNOWY){
            lightness = (float)Math.sqrt(lightness);
        }
        Renderer.setColor(lightness, lightness-redTint, lightness-redTint, Options.options.fogIntensity);
    }
    @Override
    public void tick(){
        var game = DizzyEngine.getLayer(Game.class);
        rotation += rotSpeed;
        setPosition(getPosition().add(5, 0, 0));
        if(getPosition().x>game.getCityBoundingBox().max.x+game.getXGamePadding()+SIZE){
            remove();
        }
    }
}

package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import org.joml.Vector2f;
import planetaryprotector.friendly.Worker;
import planetaryprotector.game.Game;
public class EnemyAlien extends Enemy{
    public static final int speed = Worker.workerSpeed*2/3;
    public EnemyAlien(Game game, int x, int y){
        super(game, x, y, 10, 10, 5);
    }
    @Override
    public void draw(){
        Renderer.setColor(1, 1, 1, 1);
        int x = (int)this.x;
        int y = (int)this.y;
        Renderer.fillRect(x-width/2, y-height/2, x+width/2, y+height/2, ResourceManager.getTexture("/textures/enemies/alien.png"));
    }
    @Override
    public void tick(){
//        if(health<=0){
//            dead = true;
//        }
//        if(dead){
//            return;
//        }
//        Structure s = null;
//        double dist = Double.POSITIVE_INFINITY;
//        for(Structure structure : game.structures){
//            if(structure.type.isDecoration())continue;
//            if(structure.type==StructureType.WRECK||structure.type==StructureType.EMPTY_PLOT||(structure instanceof Skyscraper&&((Skyscraper)structure).falling)||structure.shield!=null)
//                continue;
//            double d = Vector2f.distance(x, y, structure.x, structure.y);
//            if(d<dist){
//                dist = d;
//                s = structure;
//            }
//        }
//        if(s==null){
//            return;
//        }
//        boolean there = Vector2f.distance(x, y, s.x, s.y)<s.width;
//        if(!there)move(new int[]{s.x+s.width/2, s.y+s.height/2});
//        if(there){
//            timer--;
//            if(timer<0){
//                timer += 4;//0;
//                s.onHit(x-s.x, y-s.y);
//                for(int i = 0; i<Options.options.particles; i++){
//                    game.addParticleEffect(new Particle(game, x-25, y-25, ParticleEffectType.SMOKE, 0));
//                }
//                dead = true;
//            }
//        }
    }
    int timer = 20*5;
    private void move(int[] location){
        int[] loc = new int[]{location[0]-width/2, location[1]-height/2};
        int xDiff = loc[0]-x;
        int yDiff = loc[1]-y;
        double dist = Vector2f.distance(0, 0, xDiff, yDiff);
        if(dist<=speed){
            x = loc[0];
            y = loc[1];
            return;
        }
        xDiff /= dist;
        yDiff /= dist;
        x += xDiff*speed;
        y += yDiff*speed;
    }
}

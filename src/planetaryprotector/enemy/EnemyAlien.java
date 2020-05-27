package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.structure.building.Skyscraper;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import org.lwjgl.opengl.GL11;
import planetaryprotector.structure.Structure;
import simplelibrary.opengl.ImageStash;
public class EnemyAlien extends Enemy{
    public static final double speed = Worker.workerSpeed*(2/3D);
    public EnemyAlien(Game game, double x,double y){
        super(game, x, y, 10, 10, 5);
    }
    @Override
    public void render(){
        GL11.glColor4d(1, 1, 1, 1);
        int x = (int)this.x;
        int y = (int)this.y;
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/enemies/alien.png"));
    }
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            return;
        }
        Building b = null;
        double dist = Double.POSITIVE_INFINITY;
        for(Structure structure : game.structures){
            if(structure instanceof Building){
                Building building = (Building) structure;
                if(building.type==BuildingType.WRECK||building.type==BuildingType.EMPTY||(building instanceof Skyscraper&&((Skyscraper)building).falling)||building.shield!=null)continue;
                double d = Core.distance(this, building);
                if(d<dist){
                    dist = d;
                    b = building;
                }
            }
        }
        if(b==null){
            return;
        }
        boolean there = Core.distance(this, b)<b.width;
        if(!there)move(new double[]{b.x+b.width/2,b.y+b.height/2});
        if(there){
            timer--;
            if(timer<0){
                timer+=4;//0;
                b.onHit(x-b.x, y-b.y);
                for(int i = 0; i<MenuOptionsGraphics.particles; i++){
                    game.addParticleEffect(new Particle(game, x-25, y-25, ParticleEffectType.SMOKE, 0));
                }
                dead = true;
            }
        }
    }
    int timer = 20*5;
    private void move(double[] location){
        double[] loc = new double[]{location[0]-width/2, location[1]-height/2};
        double xDiff = loc[0]-x;
        double yDiff = loc[1]-y;
        double dist = Core.distance(0, 0, xDiff, yDiff);
        if(dist<=speed){
            x = loc[0];
            y = loc[1];
            return;
        }
        xDiff/=dist;
        yDiff/=dist;
        x+=xDiff*speed;
        y+=yDiff*speed;
    }
}
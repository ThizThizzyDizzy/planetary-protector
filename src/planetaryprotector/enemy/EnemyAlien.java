package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.friendly.MenuComponentWorker;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.building.MenuComponentBuilding;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.MenuComponentSkyscraper;
import planetaryprotector.building.MenuComponentBuildingDamage;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import org.lwjgl.opengl.GL11;
import simplelibrary.opengl.ImageStash;
public class EnemyAlien extends MenuComponentEnemy{
    public static final double speed = MenuComponentWorker.workerSpeed*(2/3D);
    private final MenuGame game;
    public EnemyAlien(double x,double y){
        super(x, y, 10, 10, 5);
        game = Core.game;
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 1, 1, 1);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, ImageStash.instance.getTexture("/textures/him.png"));//"/textures/enemies/alien.png"));
    }
    @Override
    public void tick(){
        if(health<=0){
            dead = true;
        }
        if(dead){
            game.componentsToRemove.add(this);
            return;
        }
        MenuComponentBuilding b = null;
        double dist = Double.POSITIVE_INFINITY;
        for(MenuComponentBuilding building : game.buildings){
            if(building.type==BuildingType.WRECK||building.type==BuildingType.EMPTY||(building instanceof MenuComponentSkyscraper&&((MenuComponentSkyscraper)building).falling))continue;
            double d = game.distance(this, building);
            if(d<dist){
                dist = d;
                b = building;
            }
        }
        if(b==null){
            return;
        }
        boolean there = true;
        if(x<b.x){
            x+=speed;
            there = false;
        }
        if(x>b.x+b.width){
            x-=speed;
            there = false;
        }
        if(y<b.y){
            y+=speed;
            there = false;
        }
        if(y>b.y+b.width){
            y-=speed;
            there = false;
        }
        if(there){
            timer--;
            if(timer<0){
                timer+=4;//0;
                b.damages.add(new MenuComponentBuildingDamage(x-b.x, y-b.y, 50, 50));
                for(int i = 0; i<MenuOptionsGraphics.particles; i++){
                    game.addParticleEffect(new MenuComponentParticle(x-25, y-25, ParticleEffectType.SMOKE, 0));
                }
                if(b.damages.size()>10){
                    game.addParticleEffect(new MenuComponentParticle(x, y, ParticleEffectType.EXPLOSION, 1));
                    dead = true;
                }
            }
        }
    }
    int timer = 20*5;
}
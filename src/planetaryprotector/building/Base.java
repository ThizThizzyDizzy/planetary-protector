    package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import static planetaryprotector.game.Game.rand;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.ingame.MenuExpedition;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Base extends Building implements BuildingDamagable{
    public int door = 0;
    public int deathTick = -1;
    public Base(Game game, double x, double y){
        super(game, x, y, 100, 100, BuildingType.BASE);
    }
    @Override
    public void update(){
        if(game==null)return;
        if(!game.paused){
            boolean open = false;
            for(Worker worker : game.workers){
                if(Core.distance(worker, x+width/2, y+height-12.5)<=25){
                    open = true;
                    break;
                }
            }
            if(game.workerCooldown<=16){
                open = true;
            }
            door = Math.min(16, Math.max(0, door+(open?1:-1)));
        }
        if(damages.size()>=10&&deathTick<451){
            deathTick++;
        }
        if(deathTick>-1){
            if(deathTick%10==0&&deathTick<100){
                game.addParticleEffect(new Particle(game, x+rand.nextInt(100), y+rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            }
            if(deathTick==100){
                game.addParticleEffect(new Particle(game, x+50, y+51, ParticleEffectType.EXPLOSION, 10));
                game.replaceBuilding(this, new Wreck(game, x, y, 0));
            }
        }
    }
    @Override
    public void draw(){
        drawRect(x, y-25, x+width, y+height, type.getTexture());
        if(game==null){
            drawRect(x,y-25,x+width,y+height, getTexture("door/0"));
            return;
        }
        drawRect(x,y-25,x+width,y+height, getTexture("door/"+door));        
        renderDamages();
    }
    @Override
    public void drawForeground(){
        super.drawForeground();
        Game.theme.applyTextColor();
        if(game.finishedExpeditions.size()>0){
            drawText(x, y+height-height/8, x+width, y+height, game.finishedExpeditions.size()+"");
        }
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public int getMaxLevel(){
        return -1;
    }
    @Override
    public Config save(Config cfg) {
       return cfg;
    }
    public static Base loadSpecific(Config cfg, Game game, double x, double y) {
        Base base = new Base(game, x, y);
        return base;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return 2.5;
    }
    @Override
    protected double getIgnitionChance(){
        return .1;
    }
    @Override
    protected double getRandX(Random rand){
        return rand.nextDouble()*width;
    }
    @Override
    protected double getRandY(Random rand){
        return rand.nextDouble()*(height+25)-25;
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data) {
        data.add("Door: "+door);
        data.add("Death Tick: "+deathTick);
    }
    @Override
    public int getBuildingHeight(){
        return 25;
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
    public double getWorkerX(){
        return x+width/2;
    }
    public double getWorkerY(){
        return y+height-getBuildingHeight()/2;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Assign All Workers", (e) -> {
            game.assignAllWorkers();
        }, () -> {
            return true;
        }));
        if(game.phase>=2){
            actions.add(new Action("Damage Report", (e) -> {
                game.damageReport();
            }, () -> {
                return true;
            }));
        }
        if(game.phase>=3){
            actions.add(new Action("Expeditions", (e) -> {
                menu.openOverlay(new MenuExpedition(menu));
            }, () -> {
                return game.workers.size()>1;
            }));
        }
    }
}
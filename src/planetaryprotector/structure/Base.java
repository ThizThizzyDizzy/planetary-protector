package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.ingame.MenuExpedition;
public class Base extends Structure{
    public int door = 0;
    public int deathTick = -1;
    public Base(Game game, int x, int y){
        super(StructureType.BASE, game, x, y, 100, 100);
    }
    @Override
    public void tick(){
        super.tick();
        if(game==null)return;
        if(!game.paused){
            boolean open = false;
            for(Worker worker : game.workers){
                if(worker.getCenter().distance(x+width/2, y+height-12.5f)<=25){
                    open = true;
                    break;
                }
            }
            if(game.workerCooldown<=16){
                open = true;
            }
            door = Math.min(16, Math.max(0, door+(open?1:-1)));
        }
        if(deathTick>-1){
            if(deathTick%10==0&&deathTick<100){
                game.addParticleEffect(new Particle(game, x+game.rand.nextInt(100), y+game.rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
            }
            if(deathTick==100){
                game.addParticleEffect(new Particle(game, x+50, y+51, ParticleEffectType.EXPLOSION, 10));
                game.replaceStructure(this, new Wreck(game, x, y, 0));
            }
        }
    }
    @Override
    public void destroy(){
        if(deathTick<451){
            deathTick++;
        }
    }
    @Override
    public void renderBackground(){
        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        super.renderBackground();
    }
    @Override
    public void render(){
        Renderer.fillRect(x, y-25, x+width, y+height, type.getTexture());
        if(game==null){
            Renderer.fillRect(x,y-25,x+width,y+height, type.getTexture("door/0"));
            return;
        }
        Renderer.fillRect(x,y-25,x+width,y+height, type.getTexture("door/"+door));        
        renderDamages();
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        if(game.finishedExpeditions.size()>0){
            Renderer.drawText(x, y+height-height/8, x+width, y+height, game.finishedExpeditions.size()+"");
        }
        Renderer.setColor(1, 1, 1, 1);
    }
    public static Base loadSpecific(Object cfg, Game game, int x, int y) {
        Base base = new Base(game, x, y);
        return base;
    }
    @Override
    public void getDebugInfo(ArrayList<String> data) {
        super.getDebugInfo(data);
        data.add("Door: "+door);
        data.add("Death Tick: "+deathTick);
    }
    public int getWorkerX(){
        return x+width/2;
    }
    public int getWorkerY(){
        return y+height-getStructureHeight()/2;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Assign All Workers", game::assignAllWorkers, () -> {
            return true;
        }));
        if(game.phase>=2){
            actions.add(new Action("Damage Report", game::damageReport, () -> {
                return true;
            }));
        }
        if(game.phase>=3){
            actions.add(new Action("Expeditions", () -> {
                menu.openOverlay(new MenuExpedition(menu));
            }, () -> {
                return game.workers.size()>1;
            }));
        }
    }
}
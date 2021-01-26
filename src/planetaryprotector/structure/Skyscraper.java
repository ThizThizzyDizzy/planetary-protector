package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.menu.options.MenuOptionsGraphics;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import planetaryprotector.structure.task.TaskSkyscraperAddFloor;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.game.Action;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Skyscraper extends Structure implements StructureDemolishable{
    public static final int floorHeight = 10;
    public int floorCount = 0;
    public boolean falling;
    public boolean right = true;
    public int fallen;
    public static final int maxHeight = 100;
    private boolean falled = false;
    public double pop = 0;
    public int fallSpeed = 3;
    public Skyscraper(Game game, int x, int y, int floors){
        super(StructureType.SKYSCRAPER, game, x, y, 100, 100);
        this.floorCount = floors;
    }
    @Override
    public void tick(){
        super.tick();
        pop*=1.000001;
        if(pop>getMaxPop()){
            pop = getMaxPop();
        }
        if(falling){
            for(Iterator<Particle> it = fires.iterator(); it.hasNext();){
                Particle fire = it.next();
                fire.offsetSubParticles(2);
                if(fire.y-50>-fallen){
                    it.remove();
                    fire.x+=x;
                    fire.y+=y;
                    fire.fading = true;
                    game.addParticleEffect(fire);
                }
            }
            for(int i = 0; i<MenuOptionsGraphics.particles*4+1; i++){
                game.addParticleEffect(new Particle(game, game.rand.nextInt((int)width)+x-25, game.rand.nextInt((int)height)+y-fallen-25,ParticleEffectType.SMOKE, 1, false));
            }
            right = !right;
            x += right?1:-1;
            y+=fallSpeed;
            fallen+=fallSpeed;
            for(Worker worker : game.workers){
                if(isClickWithinBounds(worker.x+(worker.width/2), worker.y+(worker.height/2), x, y-fallen, x+width, y+height-fallen)){
                    worker.dead = true;
                }
            }
            for(Enemy alien : game.enemies){
                if(alien instanceof EnemyAlien&&isClickWithinBounds(alien.x+(alien.width/2), alien.y+(alien.height/2), x, y-fallen, x+width, y+height-fallen)){
                    alien.dead = true;
                }
            }
        }
        if(fallen>=floorHeight*floorCount){
            damages.clear();
            clearFires();
            y -= fallen;
            fallen = 0;
            falling = false;
            falled = true;
            if(!right){
                x++;
            }
            game.replaceStructure(this, new Wreck(game, x, y, floorCount*floorHeight));
        }
    }
    @Override
    public void destroy(){
        falling = true;
    }
    @Override
    public void renderBackground(){
        drawRect(x, y-fallen, x+width, y+height-fallen, StructureType.EMPTY_PLOT.getTexture());
    }
    @Override
    public void drawOverlay(){
        drawRect(x, y-getStructureHeight()-fallen, x+width, y+height-fallen, 0);
    }
    @Override
    public void render(){
        boolean seeThrough = game.hideSkyscrapers;
        GL11.glColor4d(1, 1, 1, seeThrough?.05:1);
        double fallenPercent = fallen/(floorHeight*(floorCount+0D));
        if(falled){
            drawRect(x, y, x+width, y+height, StructureType.WRECK.getTexture());
            return;
        }
        for(int i = 0; i<floorCount; i++){
            drawRectWithBounds(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*i)+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, type.getTexture());
            if(i==floorCount-1){
                GL11.glColor4d(1, 1, 1, fallenPercent*(seeThrough?.05:1));
                drawRectWithBounds(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*(i+1))+height, x, y-(floorHeight*floorCount), x+width, y+height-fallen, StructureType.WRECK.getTexture());
                GL11.glColor4d(1, 1, 1, 1);
            }
        }
        renderDamages();
    }
    @Override
    protected void drawBar(double percent, double r, double g, double b){
        if(falling)return;
        super.drawBar(percent, r, g, b);
    }
    @Override
    public boolean onDamage(int x, int y){
        pop*=0.95;
        return super.onDamage(x, y-fallen);
    }
    @Override
    public Config save(Config cfg) {
        super.save(cfg);
        cfg.set("floors", floorCount);
        cfg.set("falling", falling);
        cfg.set("fallen", fallen);
        cfg.set("falled", falled);
        cfg.set("population", pop);
        return cfg;
    }
    public static Skyscraper loadSpecific(Config cfg, Game game, int x, int y) {
        Skyscraper sky = new Skyscraper(game, x, y, cfg.get("floors", 10));
        sky.falling = cfg.get("falling", false);
        sky.fallen = cfg.get("fallen", 0);
        sky.falled = cfg.get("falled", false);
        try{
            sky.pop = cfg.get("population", 0d);
        }catch(ClassCastException ex){
            sky.pop = cfg.get("population", 0);
        }
        return sky;
    }
    public int getMaxPop(){
        int pop = 0;
        pop += game.popPerFloor*floorCount;
        pop -= game.popPerFloor*damages.size()*floorCount/5;
        pop = Math.max(0, pop);
        return pop;
    }
    public int addPop(int amount){
        while(pop<getMaxPop()&&amount>0){
            amount--;
            pop++;
        }
        return amount;
    }
    @Override
    public String getName(){
        return floorCount+" Floor Skyscraper";
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Floor Count: "+floorCount);
        data.add("Falling: "+falling);
        data.add("Right: "+right);
        data.add("Fallen: "+fallen);
        data.add("Falled: "+falled);
        data.add("Pop: "+pop);
    }
    @Override
    public int getStructureHeight(){
        return floorHeight*floorCount-fallen;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Add Floor", new TaskSkyscraperAddFloor(this)));
        actions.add(new Action("Add 10 Floors", new TaskSkyscraperAddFloor(this, 10)));
    }
    @Override
    public void drawDamage(StructureDamage damage){
        drawRectWithBounds(damage.x-x+(right?1:0), damage.y-y+fallen, damage.x+damage.size-x+(right?1:0), damage.y+damage.size-y+fallen, 0, -((floorCount-1)*floorHeight), width, height-fallen, type.getDamageTexture());
    }
}
package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.particle.Particle;
import planetaryprotector.friendly.Worker;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import java.util.Iterator;
import planetaryprotector.Options;
import planetaryprotector.structure.task.TaskSkyscraperAddFloor;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.game.Action;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
public class Skyscraper extends Structure implements StructureDemolishable{
    public static final int floorHeight = 10;
    public int floorCount = 0;
    public boolean falling;
    public boolean right = true;
    //public int fallen; // Y value should not change
    public static final int maxHeight = 100;
    private boolean falled = false;
    private int distanceFallen = 0;
    public double pop = 0;
    public int fallSpeed = 3;
    private ArrayList<SkyscraperDecal> decals = new ArrayList<>();
    public Skyscraper(Game game, int x, int y, int floors){
        super(StructureType.SKYSCRAPER, game, x, y, 100, 100);
        this.floorCount = floors;
    }
    @Override
    public void tick(){
        super.tick();
        pop *= 1.000001;
        if(pop>getMaxPop()){
            pop = getMaxPop();
        }
        if(falling){
            for(Iterator<Particle> it = fires.iterator(); it.hasNext();){
                Particle fire = it.next();
                fire.y+=fallSpeed;
                fire.offsetSubParticles(fallSpeed);
                if(fire.y-50>0){
                    it.remove();
                    fire.x += x;
                    fire.y += y;
                    fire.fading = true;
                    game.addParticleEffect(fire);
                }
            }
            for(int i = 0; i<Options.options.particles*4+1; i++){
                game.addParticleEffect(new Particle(game, game.rand.nextInt((int)width)+x-25, game.rand.nextInt((int)height)+y-25, ParticleEffectType.SMOKE, 1, false));
            }
            right = !right;
            x += right?1:-1;
            distanceFallen += fallSpeed;
            for(Worker worker : game.workers){
                if(getBoundingBox(false).intersects(worker.getBoundingBox(false)))worker.dead = true;
            }
            for(Enemy alien : game.enemies){
                if(alien instanceof EnemyAlien&&getBoundingBox(false).intersects(alien.getBoundingBox(false)))alien.dead = true;
            }
        }
        if(distanceFallen>=floorHeight*floorCount){
            damages.clear();
            clearFires();
            distanceFallen = 0;
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
        Renderer.setColor(1, 1, 1, 1);
        Renderer.fillRect(x, y, x+width, y+height, StructureType.WRECK.getTexture());
        double fallenPercent = distanceFallen/(floorHeight*(floorCount+0D));
        Renderer.setColor(1, 1, 1, 1-distanceFallen);
        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        Renderer.setColor(1, 1, 1, 1);
    }
    @Override
    public void drawOverlay(){
        Renderer.fillRect(x, y-getStructureHeight(), x+width, y+height, 0);
    }
    @Override
    public void render(){
        Renderer.setColor(1, 1, 1, game.hideSkyscrapers?.2f:1);
        if(falled){
            Renderer.fillRect(x, y, x+width, y+height, StructureType.WRECK.getTexture());
            return;
        }
        if(falling){
            Renderer.bound(x, y-(floorHeight*floorCount)+distanceFallen, x+width, y+height);
            Renderer.translate(0, distanceFallen);
        }
        for(int i = 0; i<floorCount; i++){
            boolean l = true, r = true, t = true;
            FOR:
            for(SkyscraperDecal d : decals){
                switch(d.type){
                    case TOP:
                        t = false;
                        break;
                    case LEFT:
                        int floorMin = floorCount-(d.y/10-5);
                        int floorMax = floorCount-(d.y/10-9);
                        if(i>=floorMin&&i<=floorMax)l = false;
                        break;
                    case RIGHT:
                        floorMin = floorCount-(d.y/10-5);
                        floorMax = floorCount-(d.y/10-9);
                        if(i>=floorMin&&i<=floorMax)r = false;
                        break;
                    case WINDOW:
                    case DUST:
                        break FOR;
                }
            }
            if(l&&r)Renderer.fillRect(x, y-(floorHeight*(i+1))+height, x+width, y-(floorHeight*i)+height, type.getTexture(), 0, 10/11f, 1, 1);//front face only
            else if(l){
                Renderer.fillRect(x, y-(floorHeight*(i+1))+height, x+width/2, y-(floorHeight*i)+height, type.getTexture(), 0, 10/11f, .5f, 1);//front face only
            }else if(r){
                Renderer.fillRect(x+width/2, y-(floorHeight*(i+1))+height, x+width, y-(floorHeight*i)+height, type.getTexture(), 0.5f, 10/11f, 1, 1);//front face only
            }
            if(i==floorCount-1){
                if(t)Renderer.fillRect(x, y-(floorHeight*(i+1)), x+width, y-(floorHeight*(i+1))+height, type.getTexture(), 0, 0, 1, 10/11f);//top face
            }
        }
        for(SkyscraperDecal decal : decals){
            decal.render(this);
        }
        Renderer.setColor(1, 1, 1, 1);
        renderDamages();
        if(falling){
            Renderer.unTranslate();
            Renderer.unBound();
        }
    }
    @Override
    protected void drawBar(float percent, float r, float g, float b){
        if(falling)return;
        super.drawBar(percent, r, g, b);
    }
    @Override
    public boolean onDamage(int x, int y){
        pop *= 0.95;
        return super.onDamage(x, y);
    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.skyscraper = new GameState.Structure.Skyscraper();
        state.skyscraper.floors = floorCount;
        state.skyscraper.falling = falling;
        state.skyscraper.fallen = distanceFallen;
        state.skyscraper.falled = falled;
        state.skyscraper.population = pop;
        for(SkyscraperDecal decal : decals)state.skyscraper.decals.add(decal.save());
        return state;
    }
    public static Skyscraper loadSpecific(GameState.Structure state, Game game, int x, int y){
        Skyscraper sky = new Skyscraper(game, x, y, state.skyscraper.floors);
        sky.falling = state.skyscraper.falling;
        sky.distanceFallen = state.skyscraper.fallen;
        sky.falled = state.skyscraper.falled;
        sky.pop = state.skyscraper.population;
        for(var decal : state.skyscraper.decals){
            sky.decals.add(SkyscraperDecal.load(decal));
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
        data.add("Fallen: "+distanceFallen);
        data.add("Falled: "+falled);
        data.add("Pop: "+pop);
    }
    @Override
    public int getStructureHeight(){
        return floorHeight*floorCount-distanceFallen;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Add Floor", new TaskSkyscraperAddFloor(this)));
        actions.add(new Action("Add 10 Floors", new TaskSkyscraperAddFloor(this, 10)));
    }
    private void tryAdd(SkyscraperDecal decal){
        for(SkyscraperDecal d : decals)if(!decal.isValid(this)||decal.conflictsWith(d))return;
        decals.add(decal);
    }
    public void generateApocolypseDecals(){
        while(game.rand.nextInt(6)>0){
            SkyscraperDecal.Type type = new SkyscraperDecal.Type[]{SkyscraperDecal.Type.LEFT, SkyscraperDecal.Type.RIGHT, SkyscraperDecal.Type.TOP}[game.rand.nextInt(3)];
            int x = 0, y = 0;
            if(type==SkyscraperDecal.Type.RIGHT)x = 50;
            if(type==SkyscraperDecal.Type.LEFT||type==SkyscraperDecal.Type.RIGHT)y = 100+game.rand.nextInt(floorCount)*floorHeight;
            tryAdd(new SkyscraperDecal(x, y, type, game.rand.nextInt(type.variants)));
        }
        for(int x = 0; x<12; x++){
            for(int y = 0; y<floorCount; y++){
                int X = 2+x*8;
                int Y = 100+y*floorHeight;
                if(game.rand.nextBoolean())tryAdd(new SkyscraperDecal(X, Y, SkyscraperDecal.Type.WINDOW, game.rand.nextInt(SkyscraperDecal.Type.WINDOW.variants)));
            }
        }
        int dusts = game.rand.nextInt((10+floorCount)*10);
        for(int i = 0; i<dusts; i++){
            tryAdd(new SkyscraperDecal(game.rand.nextInt(150)-50, game.rand.nextInt(100+floorCount*floorHeight+50)-50, SkyscraperDecal.Type.DUST, game.rand.nextInt(SkyscraperDecal.Type.DUST.variants)));
        }
    }
}

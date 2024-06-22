package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.ResourceManager;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import org.joml.Matrix4f;
import planetaryprotector.GameObject;
import planetaryprotector.Options;
import planetaryprotector.anim.Animation;
import planetaryprotector.game.Action;
import planetaryprotector.game.BoundingBox;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.Particle;
import planetaryprotector.particle.ParticleEffectType;
import planetaryprotector.research.Research;
import planetaryprotector.structure.task.Task;
import planetaryprotector.structure.task.TaskAnimated;
public abstract class Structure extends GameObject{
    public ArrayList<StructureDamage> damages = new ArrayList<>();
    public Task task;
    //FIRE
    public double fire = 0;
    public int fireDamage = 0;
    public double fireIncreaseRate = 0;
    public final ArrayList<Particle> fires = new ArrayList<>();
    //Levels
    public int level = 0;
    protected final ArrayList<Upgrade> upgrades = new ArrayList<>();
    private static final int barHeight = 5;
    private float barOffset = 0;
    //structure stuff
    public float mouseover = 0;
    public ShieldGenerator shield = null;//the shield generator that is currently projecting a shield on this building
    public final StructureType type;
    public Structure(StructureType type, Game game, int x, int y, int width, int height){
        this(type, game, x, y, width, height, 1, new ArrayList<>());
    }
    public Structure(StructureType type, Game game, int x, int y, int width, int height, int level, ArrayList<Upgrade> upgrades){
        super(game, x, y, width, height);
        this.type = type;
        this.level = level;
        this.upgrades.addAll(upgrades);
    }
    public void tick(){
        fire+=fireIncreaseRate;
        if(fire-fireDamage*type.getFireDestroyThreshold()*.1>=type.getFireDestroyThreshold()*.1){
            int X = getRandX(game.rand);
            int Y = getRandY(game.rand);
            damages.add(new StructureDamage(this, X-25, Y-25));
            fires.add(new Particle(game, X-25, Y-25, ParticleEffectType.FIRE));
            ignite();
            fireDamage++;
        }
        for(Particle particle : fires){
            particle.tick();
        }
        if(damages.size()>=type.getDamageDestroyThreshold()){
            destroy();
        }
    }
    public void destroy(){
        game.addParticleEffect(new Particle(game, x+game.rand.nextInt(100), y+game.rand.nextInt(100), ParticleEffectType.EXPLOSION, 1));
        game.replaceStructure(this, new Wreck(game, x, y, type.getTotalCost(level, Item.ironIngot)));
    }
    public void drawOverlay(){
        Renderer.fillRect(x, y-getStructureHeight(), x+width, y+height, 0);
    }
    public void render(){
        if(!type.isBackgroundStructure()){
            Renderer.fillRect(x, y-getStructureHeight(), x+width, y+height, type.getTexture());
            for(Upgrade upgrade : type.upgrades){
                int count = getUpgrades(upgrade);
                if(count==0)continue;
                Renderer.fillRect(x, y-getStructureHeight(), x+width, y+height, upgrade.getTexture(type, count));
            }
            renderDamages();
        }
    }
    public void renderBackground(){
        if(type.isBackgroundStructure()){
            Renderer.fillRect(x, y, x+width, y+height, type.getTexture());
            for(Upgrade upgrade : type.upgrades){
                int count = getUpgrades(upgrade);
                if(count==0)continue;
                Renderer.fillRect(x, y-getStructureHeight(), x+width, y+height, upgrade.getTexture(type, count));
            }
        }
        renderDamages();
    }
    @Override
    public void draw(){
        render();
        Renderer.setColor(1, 0, 0, (game.damageReportTimer/game.damageReportTime)*damages.size()/10f);
        drawOverlay();
        Renderer.setColor(0, 1, 1, mouseover);
        drawOverlay();
        Renderer.setColor(1, 1, 1, 1);
        if(shield!=null){
            Renderer.setColor(.75f, .875f, 1, shield.getProjectedShieldStrength()/10f);
            drawOverlay();
            Renderer.setColor(1, 1, 1, 1);
        }
        renderForeground();
        Renderer.pushModel(new Matrix4f().translate(x, y, 0));
        for(Particle fire : fires){
            fire.draw();
        }
        Renderer.popModel();
    }
    public void renderForeground(){
        barOffset = barHeight-getStructureHeight();
        if((this instanceof PowerUser)&&((PowerUser)this).isPowerActive()){
            drawBar((float)(((PowerUser)this).getDisplayPower()/((PowerUser)this).getDisplayMaxPower()), 0, .25f, 1);
        }
        if(game.observatory&&(this instanceof StarlightUser)&&((StarlightUser)this).isStarlightActive()){
            drawBar((float)(((StarlightUser)this).getDisplayStarlight()/((StarlightUser)this).getDisplayMaxStarlight()), .625f, .875f, 1);
        }
        barOffset = height;
        if(type.isDamagable()){
            switch(Options.options.health){
                case 0://none
                    break;
                case 1://when damaged
                    if(damages.isEmpty()&&fires.isEmpty())break;
                case 2:
                    float percent = getHealthPercent();
                    drawBar(percent, 1-percent, percent, 0);
            }
        }
    }
    public void renderDamages(){
        Renderer.pushModel(new Matrix4f().translate(x, y, 0));
        for(StructureDamage damage : damages){
            damage.render();
        }
        Renderer.popModel();
    }
    protected void drawBar(float percent, float r, float g, float b){
        if(Double.isNaN(percent))return;
        Renderer.setColor(0, 0, 0, 1);
        Renderer.fillRect(x,y+barOffset-barHeight,x+width, y+barOffset, 0);
        if(percent>0){
            Renderer.setColor(r, g, b, 1);
            Renderer.fillRect(x+1,y+barOffset-barHeight+1,x+width*percent-1, y+barOffset-1, 0);
        }
        Renderer.setColor(1, 1, 1, 1);
        barOffset += barHeight;
    }
    public float getHealthPercent(){
        return 1-(damages.size()/10f);
    }
    public void upgrade(){
        level++;
        game.refreshNetworks();
    }
    public static Structure load(GameState.Structure state, Game game){//TODO clean this up!
        ArrayList<Upgrade> upgrades = new ArrayList<>();
        for(var upgrade : state.upgrades)upgrades.add(Upgrade.valueOf(upgrade));
        StructureType type = StructureType.getByName(state.type);
        Structure s = type.load(state, game, state.x, state.y, state.level, upgrades);
        for(var damage : state.damages){
            s.damages.add(StructureDamage.load(s, damage));
        }
        s.fire = state.fire;
        s.fireDamage = state.fireDamage;
        s.fireIncreaseRate = state.fireIncreaseRate;
        for(var fire : state.fires){
            s.fires.add(new Particle(game, fire.x, fire.y, ParticleEffectType.FIRE));
        }
        return s;
    }
    public String getName(){
        if(type.getMaxLevel()>1)return "Level "+level+" "+type.getDisplayName();
        return type.getDisplayName();
    }
    public boolean canUpgrade(){
        return level<type.getMaxLevel();
    }
    public GameState.Structure save(){
        GameState.Structure state = new GameState.Structure();
        state.type = type.name;
        state.level = level;
        for(var upgrade : upgrades)state.upgrades.add(upgrade.name());
        for(var damage : damages)state.damages.add(damage.save());
        state.x = x;
        state.y = y;
        state.fire = fire;
        state.fireDamage = fireDamage;
        state.fireIncreaseRate = fireIncreaseRate;
        for(var fire : fires){
            GameState.Structure.Fire f = new GameState.Structure.Fire();
            f.x = fire.x;
            f.y = fire.y;
            state.fires.add(f);
        }
        if(this instanceof PowerConsumer){
            state.power = ((PowerConsumer)this).getPower();
        }
        if(this instanceof StarlightConsumer){
            state.starlight = ((StarlightConsumer)this).getStarlight();
        }
        state.shield = shield==null?-1:shield.getIndex();
        return state;
    }
    protected void ignite(){
        ignite(x+getRandX(game.rand),y+getRandY(game.rand));
    }
    protected void ignite(int x, int y){
        x-=this.x;
        y-=this.y;
        if(this instanceof Base){
            y-=25;
        }
        fires.add(new Particle(game, x-25, y-25, ParticleEffectType.FIRE));
    }
    public final boolean onHit(int x, int y){
        if(shield!=null){
            if(shield.shieldHit())return true;
        }
        return damage(x,y);
    }
    public boolean damage(int x, int y){
        if(type.isDamagable()){
            if(game.rand.nextDouble()<type.getIgnitionChance()){
                fireIncreaseRate += 0.0002;
                ignite(x, y);
            }
            return onDamage(x, y);
        }
        return false;
    }
    /**
     * damage the building at a certain location
     * @param x x-value of the damage
     * @param y y-value of the damage
     * @return true if the damage was stopped, or false if the damage should hit the ground
     */
    public boolean onDamage(int x, int y){
        damages.add(new StructureDamage(this, x-25, y-25));
        return true;
    }
    public void clearFires(){
        for(Particle fire : fires){
            fire.x+=x;
            fire.y+=y;
            fire.fading = true;
            game.addParticleEffect(fire);
        }
        fires.clear();
    }
    public int getStructureHeight(){
        return type.getStructureHeight();
    }
    /**
     * Called after all structures are loaded- this is in case any structure needs to load data regarding another structure (For example, shield projector target)
     * @param game the game that is being loaded
     * @param state the config to load from
     */
    public void postLoad(Game game, GameState.Structure state){
        if(state.shield!=-1)shield = (ShieldGenerator) game.structures.get(state.shield);
    }
    public void getDebugInfo(ArrayList<String> data){
        data.add(type.name);
        data.add("Damage: "+damages.size());
        data.add("Level "+level);
        data.add("Upgrades: "+upgrades.size());
        for(int i = 0; i<upgrades.size(); i++){
            data.add(" "+upgrades.get(i).name());
        }
        data.add("Fire: "+fire);
        data.add("Fire damage: "+fireDamage);
        data.add("Fire increase: "+fireIncreaseRate);
        data.add("Fires: "+fires.size());
        if(this instanceof PowerConsumer){
            data.add("Power Consumer");
            data.add(" Demand: "+((PowerConsumer)this).getDemand());
            data.add(" Power: "+((PowerConsumer)this).getPower()+"/"+((PowerConsumer)this).getMaxPower());
        }
        if(this instanceof PowerProducer){
            data.add("Power Producer");
            data.add(" Supply: "+((PowerProducer)this).getProduction());
            data.add(" Renewable: "+(((PowerProducer)this).isRenewable()?"TRUE":"FALSE"));
        }
        if(this instanceof StarlightConsumer){
            data.add("Starlight Consumer");
            data.add(" Demand: "+((StarlightConsumer)this).getStarlightDemand());
            data.add(" Starlight: "+((StarlightConsumer)this).getStarlight()+"/"+((StarlightConsumer)this).getMaxStarlight());
        }
        if(this instanceof StarlightProducer){
            data.add("Starlight Producer");
            data.add(" Supply: "+((StarlightProducer)this).getStarlightProduction());
            data.add(" Renewable: "+(((StarlightProducer)this).isStarlightRenewable()?"TRUE":"FALSE"));
        }
    }
    public int getIndex(){
        return game.structures.indexOf(this);
    }
    @Override
    public int getRandY(Random rand){
        return (int)Math.round(rand.nextDouble()*(height+getStructureHeight())-getStructureHeight());
    }
    public ArrayList<Upgrade> getBoughtUpgrades(){
        return upgrades;
    }
    public int getUpgrades(){
        return getBoughtUpgrades().size();
    }
    public int getUpgrades(Upgrade upgrade){
        int total = 0;
        for(Upgrade u : upgrades){
            if(u==upgrade)total++;
        }
        return total;
    }
    public boolean hasUpgrade(Upgrade upgrade){
        for(Upgrade u : upgrades){
            if(u==upgrade)return true;
        }
        return false;
    }
    public ArrayList<Integer> getUpgradeLevels(){
        ArrayList<Integer> lvls = new ArrayList<>();
        for(Upgrade upgrade : type.upgrades){
            for(int i : upgrade.availability){
                if(!lvls.contains(i))lvls.add(i);
            }
        }
        Collections.sort(lvls);
        return lvls;
    }
    public int getNextUpgradeLevel(){
        ArrayList<Upgrade> currentUpgrades = new ArrayList<>(upgrades);
        int I = 0;
        L:for(int l : getUpgradeLevels()){
            I++;
            if(currentUpgrades.isEmpty())return l;
            Upgrade u = currentUpgrades.remove(0);
            for(int i : u.availability)if(i==l)continue L;
            throw new IllegalStateException("Upgrade "+u.toString()+" should not be on level "+l+" in position "+I+"!");
        }
        return -1;
    }
    public ArrayList<Upgrade> getAvailableUpgrades(){
        int next = getNextUpgradeLevel();
        if(next==-1||level<next)return new ArrayList<>();
        ArrayList<Upgrade> available = new ArrayList<>();
        for(Upgrade u : type.upgrades){
            boolean avbl = false;
            for(int i : u.availability)if(i==next)avbl = true;
            if(!avbl)continue;
            if(getUpgrades(u)>=u.max)continue;
            if(!u.research.isCompleted())continue;
            available.add(u);
        }
        return available;
    }
    public boolean canBuyUpgrade(Upgrade upgrade){
        return getAvailableUpgrades().contains(upgrade);
    }
    public boolean buyUpgrade(Upgrade upgrade){
        if(!canBuyUpgrade(upgrade))return false;
        upgrades.add(upgrade);
        game.refreshNetworks();
        return true;
    }
    public int getVariants(){
        return 1;
    }
    public int getVariant(){
        return ((int)x)%getVariants();
    }//TODO random, not linear. Also use X and Y
    public void getActions(MenuGame menu, ArrayList<Action> actions){}
    public void setTask(Task task){
        if(game.selectedStructure==this)game.actionUpdateRequired = 2;
        this.task = task;
    }
    public static enum Upgrade{//TODO UN-ENUM
        SUPERCHARGE(Research.SUPERCHARGE, "Supercharge", StructureType.COAL_GENERATOR, 1200, 5, 4,8,12,16,20),
        ECOLOGICAL(Research.ECOLOGICAL, "Ecological", StructureType.COAL_GENERATOR, 1200, 5, 4,8,12,16,20),
        STARLIGHT_INFUSED_FUEL(Research.STARLIGHT_INFUSED_FUEL, "Starlight Infused Fuel", StructureType.COAL_GENERATOR, 3000, 1, 12, 20),
        STONE_GRINDING(Research.STONE_GRINDING, "Stone Grinding", StructureType.MINE, 1200, 2, 8, 12),//12,20
        POWER_TOOLS(Research.POWER_TOOLS, "Power Tools", StructureType.MINE, 600, 1, 4,8,12,16,20),
        PHOTOVOLTAIC_SENSITIVITY(Research.PHOTOVOLTAIC_SENSITIVITY, "Photovoltaic Sensitivity", StructureType.SOLAR_GENERATOR, 600, 5, 4,8,12,16,20),
        STARLIGHT_GENERATION(Research.STARLIGHT_GENERATION, "Starlight Generation", StructureType.SOLAR_GENERATOR, 3000, 1, 16,20),
        SHIELD_PROJECTOR(Research.SHIELD_PROJECTOR, "Shield Projector", StructureType.SHIELD_GENERATOR, 200, 1, 4);//20
        static{
            SUPERCHARGE.costs = new ItemStack[]{new ItemStack(Item.coal, 10), new ItemStack(Item.ironIngot, 50)};
            ECOLOGICAL.costs = new ItemStack[]{new ItemStack(Item.coal, 20), new ItemStack(Item.ironIngot, 80)};
            STARLIGHT_INFUSED_FUEL.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 100), new ItemStack(Item.coal, 50)};
            STONE_GRINDING.costs = new ItemStack[]{new ItemStack(Item.stone, 40), new ItemStack(Item.ironIngot, 40)};
            POWER_TOOLS.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 50)};
            PHOTOVOLTAIC_SENSITIVITY.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 10)};
            STARLIGHT_GENERATION.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 30)};
            SHIELD_PROJECTOR.costs = new ItemStack[]{new ItemStack(Item.ironIngot, 120)};
        }
        public final int time;
        public final int max;
        public final int[] availability;
        public ItemStack[] costs;
        public final Research research;
        private final String name;
        private Upgrade(Research research, String name, StructureType type, int time, int max, int... availability){
            type.upgrades.add(this);
            this.time = time;
            this.max = max;
            this.availability = availability;
            this.research = research;
            this.name = name;
            if(research!=null){
                research.upgrade = this;
            }
        }
        @Override
        public String toString(){
            return name;
        }
        public int getTexture(StructureType type, int count){
            return ResourceManager.getTexture(getTextureS(type, count));
        }
        public String getTextureS(StructureType type, int count){
            return type.getTextureS(name().toLowerCase().replace("_", " ")+"/"+Game.theme.tex()+"/"+count);
        }
        public Animation getAnimation(StructureType type, int count){
            return TaskAnimated.getAnimation(getAnimationS(type, count));
        }
        public String getAnimationS(StructureType type, int count){
            return "/textures/tasks/upgrade/"+type.name+"/"+name().toLowerCase().replace("_", " ")+"/"+Game.theme.tex()+"/"+count;
        }
        /**
         * @return A String interpretation of the upgrade slots
         */
        public String getConfiguration(){
            char a = 'X';
            char b = 'X';
            char c = 'X';
            char d = 'X';
            char e = 'X';
            for(int i : availability){
                if(i==4)a = 'O';
                if(i==8)b = 'O';
                if(i==12)c = 'O';
                if(i==16)d = 'O';
                if(i==20)e = 'O';
            }
            return max+" ~ |"+a+"|"+b+"|"+c+"|"+d+"|"+e+"|";
        }
    }
    @Override
    public BoundingBox getBoundingBox(boolean includeHeight){
        if(includeHeight)return new BoundingBox(x, y, width, height+getStructureHeight());
        return super.getBoundingBox(includeHeight);
    }
}
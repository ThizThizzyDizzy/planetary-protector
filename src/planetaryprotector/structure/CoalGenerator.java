package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import planetaryprotector.game.Action;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
public class CoalGenerator extends Structure implements PowerProducer, StarlightConsumer, StructureDemolishable{
    public int coal = 0;
    public float burning = 0;//how much time is left on current coal burning
    private static final int BURN_TIME = 500;
    public double power;
    public boolean autoFuel = false;
    private double starlight = 0;
    private static final double STARLIGHT_THRESHOLD = 0.01;
    private int frame = 0;
    static final int frames = 8;
    private int speed = 0;//0-100
    private static final int acceleration = 2;
    private double delay = 0;
    private int spinTimer = 0;
    public CoalGenerator(Game game, int x, int y) {
        super(StructureType.COAL_GENERATOR, game, x, y, 100, 100);
    }
    public CoalGenerator(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.COAL_GENERATOR, game, x, y, 100, 100, level, upgrades);
    }
    @Override
    public void tick(){
        super.tick();
        if(burning>0){
            power+=4+Math.pow(level,1.4)+level*Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE))*(starlight>STARLIGHT_THRESHOLD?2:0);
            if(starlight>STARLIGHT_THRESHOLD)starlight-=STARLIGHT_THRESHOLD;
            burning-=Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE));
            speed+=acceleration;
        }else{
            speed-=acceleration;
        }
        speed = Math.max(0, Math.min(100, speed));
        if(speed>0){
            delay = 100d/frames/speed;
            spinTimer++;
            while(spinTimer>=delay){
                spinTimer-=delay;
                frame++;
                if(frame>=frames)frame = 0;
            }
        }
        if(coal==0&&autoFuel&&burning<=0){
            if(game.hasResources(new ItemStack(Item.coal))){
                game.removeResources(new ItemStack(Item.coal));
                coal++;
            }
        }
        if(power<=0&&burning<=0&&coal>0){
            coal--;
            burning = getBurnTime();
        }
    }
    @Override
    public void render(){
        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        Renderer.fillRect(x, y, x+width, y+height, type.getTexture(), getVariant()/(float)getVariants(), frame/(float)frames, (getVariant()+1)/(float)getVariants(), (frame+1)/(float)frames);
        for(Upgrade upgrade : type.upgrades){
            int count = getUpgrades(upgrade);
            if(count==0)continue;
            Renderer.fillRect(x, y, x+width, y+height, upgrade.getTexture(type, count), getVariant()/(float)getVariants(), frame/(float)frames, (getVariant()+1)/(float)getVariants(), (frame+1)/(float)frames);
        }
        renderDamages();
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Renderer.setColor(1,.1f,0,1);
        Renderer.fillRect(x,y+18,x+width*(burning/getBurnTime()), y+20, 0);
        Game.theme.applyTextColor();
        if(coal>0)Renderer.drawCenteredText(x, y+18, x+width, y+36, coal+" Coal");//TODO coal fill bar //TODO max coal
        Renderer.drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);//TODO level markings
        Renderer.setColor(1, 1, 1, 1);
    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.coalGenerator = new GameState.Structure.CoalGenerator();
        state.coalGenerator.autoFuel = autoFuel;
        return state;
    }
    public static CoalGenerator loadSpecific(GameState.Structure state, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        CoalGenerator generator = new CoalGenerator(game, x, y, level, upgrades);
        generator.power = state.power;
        generator.autoFuel = state.coalGenerator.autoFuel;
        return generator;
    }
    @Override
    public double getProduction(){
        return power;
    }
    @Override
    public void producePower(double power){
        this.power-=power;
    }
    @Override
    public boolean isRenewable(){
        return false;
    }
    @Override
    public double getMaxStarlight(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL)?0.1:0;
    }
    @Override
    public double getStarlightDemand(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL)?Math.min(getMaxStarlight()-starlight,STARLIGHT_THRESHOLD*1.5):0;
    }
    @Override
    public double getStarlight(){
        return starlight;
    }
    @Override
    public void addStarlight(double starlight){
        this.starlight+=starlight;
    }
    private float getBurnTime(){
        return (float)(BURN_TIME*Math.pow(1.38, getUpgrades(Upgrade.ECOLOGICAL)));
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    public boolean isStarlightActive(){
        return hasUpgrade(Upgrade.STARLIGHT_INFUSED_FUEL);
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Coal: "+coal);
        data.add("Burning: "+burning);
        data.add("Auto-fuel: "+(autoFuel?"Enabled":"Disabled"));
        data.add("Power Production: "+(burning<=0?0:4+Math.pow(level,1.4)+level*Math.pow(1.38, getUpgrades(Upgrade.SUPERCHARGE))*(starlight>STARLIGHT_THRESHOLD?2:0)));
    }
    @Override
    public int getVariants(){
        return 2;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Add Coal", () -> {
            game.removeResources(new ItemStack(Item.coal));
            coal++;
        }, () -> {
            return game.hasResources(new ItemStack(Item.coal));
        }));
        actions.add(new Action((autoFuel?"Disable":"Enable")+" Auto-fueling", () -> {
            autoFuel = !autoFuel;
        }, () -> {
            return true;
        }));
    }
    @Override
    public double getDisplayPower(){
        return 0;
    }
    @Override
    public double getDisplayMaxPower(){
        return 0;
    }
    @Override
    public double getDisplayStarlight(){
        return getStarlight();
    }
    @Override
    public double getDisplayMaxStarlight(){
        return getMaxStarlight();
    }
}
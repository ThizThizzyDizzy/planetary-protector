package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.item.Item;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
public class Mine extends Structure implements PowerConsumer, StructureDemolishable{
    private int timer = 0;
    private int delay = 20;
    private double power;
    private boolean powerTools = true;//used for every-other-tick double items
    public Mine(Game game, int x, int y){
        super(StructureType.MINE, game, x, y, 100, 100);
    }
    public Mine(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.MINE, game, x, y, 100, 100, level, upgrades);
        delay = 20-level;
    }
    private boolean canDeployItem(){
        int items = 0;
//        for(DroppedItem item : game.droppedItems){
//            if(getBoundingBox(false).contains(item.x+item.width/2, item.y+item.height/2))
//                items++;
//        }
        return items<25*level;
    }
    public void deployItem(){
//        if(!canDeployItem())return;
//        int itemX = x+game.rand.nextInt(79)+11;
//        int itemY = y+game.rand.nextInt(79)+11;
//        itemX -= 5;
//        itemY -= 5;
//        game.addItem(new DroppedItem(game, itemX, itemY, randomItem()));
    }
    @Override
    public void tick(){
        super.tick();
        if(timer<=0){
            deployItem();
            if(hasUpgrade(Upgrade.POWER_TOOLS)&&power>=5&&canDeployItem()){
                if(powerTools){
                    deployItem();//1.5x items with power tools
                    powerTools = false;
                }else{
                    powerTools = true;
                }
                power -= 5;
            }
            timer = delay;
        }
        timer--;
    }
//    @Override
//    public void renderForeground(){
//        super.renderForeground();
//        Game.theme.applyTextColor();
//        if(power>0)Renderer.drawCenteredText(x, y, x+width, y+20, (int)power+"");
//        Renderer.drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
//        Renderer.setColor(1, 1, 1, 1);
//    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.mine = new GameState.Structure.Mine();
        state.mine.timer = timer;
        state.mine.delay = delay;
        state.mine.powerTools = powerTools;
        return state;
    }
    public static Mine loadSpecific(GameState.Structure state, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        Mine mine = new Mine(game, x, y, level, upgrades);
        mine.timer = state.mine.timer;
        mine.delay = state.mine.delay;
        mine.powerTools = state.mine.powerTools;
        return mine;
    }
    @Override
    public void upgrade(){
        super.upgrade();
        delay = 20-level;
    }
    private Item randomItem(){
        double stoneThreshold = 0.5-getUpgrades(Upgrade.STONE_GRINDING)*.2;
        if(game.rand.nextDouble()<stoneThreshold)return Item.stone;
        if(game.rand.nextDouble()>.6)return Item.ironOre;
        return Item.coal;
    }
    @Override
    public double getMaxPower(){
        return hasUpgrade(Upgrade.POWER_TOOLS)?500:0;
    }
    @Override
    public double getDemand(){
        return hasUpgrade(Upgrade.POWER_TOOLS)?10:0;
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public void addPower(double power){
        this.power += power;
    }
    @Override
    public boolean isPowerActive(){
        return hasUpgrade(Upgrade.POWER_TOOLS);
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Timer: "+timer);
        data.add("Delay: "+delay);
        data.add("Power: "+power);
    }
    @Override
    public double getDisplayPower(){
        return getPower();
    }
    @Override
    public double getDisplayMaxPower(){
        return getMaxPower();
    }
}

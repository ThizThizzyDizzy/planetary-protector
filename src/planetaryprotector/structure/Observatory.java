package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.menu.MenuGame;
public class Observatory extends Structure implements PowerConsumer, StarlightProducer, StructureDemolishable{
    public double starlight = 0;
    private static final double powerPerStarlight = 2000;
    private static final double starlightSpeed = 0.01;
    private static final double maxStarlight = 1000;
    private static final double size = 20;
    private static final double scanCost = 0.05;
    private int scanning = 0;//0 = stopped, 1 = running, but ran out, 2 = running
    private double collecting;
    private ArrayList<String> scan = new ArrayList<>();
    private int textHeight = 15;
    private float yOff;
    private double power;
    private double summonThreshold = 500;
    private int shootTimer = -1;
    public Observatory(Game game, int x, int y){
        super(StructureType.OBSERVATORY, game, x, y, 100, 100);
    }
    @Override
    public void tick(){
        super.tick();
        if(starlight<maxStarlight){
            collecting = Math.min(1, maxStarlight-starlight);
        }
        double star = 1-game.getSunlight();
        collecting = Math.min(collecting, star*starlightSpeed);
        collecting = Math.min(collecting, power/powerPerStarlight);
        starlight += collecting;
        power-=collecting*powerPerStarlight;
        scan.clear();
        if(scanning>0){
            if(starlight<scanCost){
                scanning = 1;
            }
            if(scanning==1){
                if(starlight>scanCost*20){
                    scanning = 2;
                }
            }
            if(scanning==2){
                starlight-=scanCost;
                scan();
            }
        }
        if(shootTimer>=0)shootTimer++;
        if(shootTimer>=20)shootTimer = -1;
    }
    @Override
    public void render(){
        super.render();
        Renderer.setColor(1, 1, 1, 1);
        float laserSize = (float)Math.max((size/starlightSpeed)*collecting,shootTimer>10?20-shootTimer:shootTimer);
        if(laserSize>0){
            float yDiff = -y-100;
            float dist = Math.abs(yDiff);
            Renderer.setColor(.8f, .8f, 1, 1);//big outer one
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.fillRegularPolygon(x+width/2, y+(yDiff*percent)+height/4, 10, laserSize/2f);
            }
            Renderer.setColor(.5f, .5f, 1, 1);
            Renderer.fillRect(x, y+60, x+width, y+70, 0);
            Renderer.setColor(.9f, .9f, 1, 1);//small inner one
            for(int i = 0; i<dist; i++){
                float percent = i/dist;
                Renderer.fillRegularPolygon(x+width/2, y+(yDiff*percent)+height/4, 10, (laserSize*(1/3f))/2f);
            }
//            Renderer.fillRect(x+1, y+61, x+(width*(1-game.getSunlight()))-1, y+69, 0);
            Renderer.setColor(1, 1, 1, 1);
        }
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        if(scanning==1){
            Renderer.drawCenteredText(x, y, x+width, y+15, "Charging");
        }
        if(scanning==2){
            Renderer.drawCenteredText(x, y, x+width, y+15, "Scanning");
        }
        if(!scan.isEmpty()){
            yOff = height-scan.size()*textHeight;
            for(String str : scan){
                text(str);
            }
        }
        if(starlight>0){
            Renderer.drawCenteredText(x, y+47, x+width, y+58, "Starlight: "+Math.round(starlight*100)/100d);
        }
        if(collecting>0){
            Renderer.drawCenteredText(x, y+30, x+width, y+45, "+"+Math.round(collecting*100)/100d);
        }
        Renderer.setColor(1, 1, 1, 1);
    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.observatory = new GameState.Structure.Observatory();
        state.observatory.starlight = starlight;
        state.observatory.scanning = scanning;
        state.observatory.collecting = collecting;
        return state;
    }
    public static Observatory loadSpecific(GameState.Structure state, Game game, int x, int y) {
        Observatory observatory = new Observatory(game, x, y);
        observatory.power = state.power;
        observatory.starlight = state.observatory.starlight;
        observatory.scanning = state.observatory.scanning;
        observatory.collecting = state.observatory.collecting;
        return observatory;
    }
    public void toggleScan(){
        if(scanning==0){
            scanning = 2;
        }else{
            scanning = 0;
        }
    }
    private void scan(){
        if(game.meteorShower){
            scan.add("Shower ends:");
        }else{
            scan.add("Next shower:");
        }
        int ticks = Math.abs(game.meteorShowerTimer);
        int seconds = 0,minutes = 0,hours = 0;
        while(ticks>=20){
            seconds++;
            ticks-=20;
        }
        while(seconds>=60){
            minutes++;
            seconds-=60;
        }
        while(minutes>=60){
            hours++;
            minutes-=60;
        }
        if(hours>0){
            scan.add(hours+" hrs");
        }else if(minutes>0){
            scan.add(minutes+" min");
        }else if(seconds>0){
            scan.add(seconds+" sec");
        }else if(ticks>0){
            scan.add("!!!");
        }
    }
    private void text(String str){
        Renderer.drawText(x, y+yOff, x+width, y+yOff+textHeight, str);
        yOff+=textHeight;
    }
    public boolean canAddStar(){
        return starlight+100<=maxStarlight;
    }
    public boolean addStar(){
        starlight += 100;
        if(starlight>maxStarlight){
            starlight-=100;
            return false;
        }
        return true;
    }
    @Override
    public double getMaxPower(){
        return 1000;
    }
    @Override
    public double getDemand(){
        return 10;
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
    public double getStarlightProduction(){
        return Math.min(.2, starlight);
    }
    @Override
    public void produceStarlight(double starlight){
        this.starlight-=starlight;
    }
    @Override
    public boolean isStarlightRenewable(){
        return true;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    public boolean isStarlightActive(){
        return true;
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Starlight: "+starlight);
        data.add("Scanning: "+scanning);
        data.add("Collecting: "+collecting);
        data.add("power: "+power);
    }
    public boolean canSummonStar(){
        return starlight>=summonThreshold&&game.secretWaiting==-1;
    }
    public void summonStar(){
        if(!canSummonStar())return;
        starlight-=summonThreshold;
        game.secretWaiting = 0;
        shootTimer = 0;
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Add Star", () -> {
            if(game.hasResources(new ItemStack(Item.star))){
                if(addStar()){
                    game.removeResources(new ItemStack(Item.star));
                }
            }
        }, () -> {
            return game.hasResources(new ItemStack(Item.star))&&canAddStar();
        }));
        actions.add(new Action("Toggle Scan", this::toggleScan, () -> {
            return true;
        }));
        actions.add(new Action("Summon Shooting Star", this::summonStar, () -> {
            return canSummonStar();
        }));
    }
    @Override
    public double getDisplayPower(){
        return getPower();
    }
    @Override
    public double getDisplayMaxPower(){
        return getMaxPower();
    }
    @Override
    public double getDisplayStarlight(){
        return 1-game.getSunlight();
    }
    @Override
    public double getDisplayMaxStarlight(){
        return 1;
    }
}
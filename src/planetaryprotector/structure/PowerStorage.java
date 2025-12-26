package planetaryprotector.structure;
import java.util.ArrayList;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.game.GameState;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.ingame.MenuPowerStorageConfiguration;
public class PowerStorage extends Structure implements StructurePowerStorage, StructureDemolishable{
    private double power;
    public boolean charge = true;
    public boolean discharge = true;
    public int daylightThreshold = 40;
    public boolean automaticControl = true;
    public boolean daylightControl = true;
    public boolean daylightRecharge = true;
    public boolean daylightDischarge = false;
    public boolean nightRecharge = true;
    public boolean nightDischarge = true;
    public boolean neutralRecharge = false;
    public boolean neutralDischarge = false;
    public boolean meteorOverride = true;
    public boolean meteorRecharge = false;
    public boolean meteorDischarge = true;
    private int lastDaylight = -1;
    private boolean lastMeteor = false;
    public int rechargeRate = 100;
    public int dischargeRate = 100;
    public PowerStorage(Game game, int x, int y){
        super(StructureType.POWER_STORAGE, game, x, y, 100, 100);
    }
    public PowerStorage(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.POWER_STORAGE, game, x, y, 100, 100, level, upgrades);
    }
    @Override
    public void tick(){
        super.tick();
        if(automaticControl){
            if(daylightControl){
                int daylight = (int)Math.round(game.getSunlight()*100);
                if(lastDaylight!=daylight){
                    if(daylight>=daylightThreshold&&lastDaylight<daylightThreshold){
                        charge = daylightRecharge;
                        discharge = daylightDischarge;
                    }
                    if(lastDaylight>=daylightThreshold&&daylight<daylightThreshold){
                        charge = nightRecharge;
                        discharge = nightDischarge;
                    }
                }
                lastDaylight = daylight;
            }
            if(meteorOverride){
                if(!lastMeteor&&game.meteorShower){
                    charge = meteorRecharge;
                    discharge = meteorDischarge;
                }
                if(lastMeteor&&!game.meteorShower){
                    if(daylightControl){
                        if(Math.round(game.getSunlight()*100)>=daylightThreshold){
                            charge = daylightRecharge;
                            discharge = daylightDischarge;
                        }else{
                            charge = nightRecharge;
                            discharge = nightDischarge;
                        }
                    }else{
                        charge = neutralRecharge;
                        discharge = neutralDischarge;
                    }
                }
                lastMeteor = game.meteorShower;
            }
        }
    }
//    @Override
//    public void renderBackground(){
//        Renderer.fillRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
//        super.renderBackground();
//    }
//    @Override
//    public void renderForeground(){
//        super.renderForeground();
//        Game.theme.applyTextColor();
//        Renderer.drawCenteredText(x, y, x+width, y+20, (int)power+"");
//        Renderer.drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
//        Renderer.setColor(1, 1, 1, 1);
//    }
    @Override
    public GameState.Structure save(){
        var state = super.save();
        state.powerStorage = new GameState.Structure.PowerStorage();
        state.powerStorage.charge = charge;
        state.powerStorage.discharge = discharge;
        state.powerStorage.daylightThreshold = daylightThreshold;
        state.powerStorage.automaticControl = automaticControl;
        state.powerStorage.daylightControl = daylightControl;
        state.powerStorage.daylightRecharge = daylightRecharge;
        state.powerStorage.daylightDischarge = daylightDischarge;
        state.powerStorage.nightRecharge = nightRecharge;
        state.powerStorage.nightDischarge = nightDischarge;
        state.powerStorage.neutralRecharge = neutralRecharge;
        state.powerStorage.neutralDischarge = neutralDischarge;
        state.powerStorage.meteorOverride = meteorOverride;
        state.powerStorage.meteorRecharge = meteorRecharge;
        state.powerStorage.meteorDischarge = meteorDischarge;
        state.powerStorage.lastDaylight = lastDaylight;
        state.powerStorage.lastMeteor = lastMeteor;
        return state;
    }
    public static PowerStorage loadSpecific(GameState.Structure state, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        PowerStorage powerStorage = new PowerStorage(game, x, y, level, upgrades);
        powerStorage.power = state.power;
        powerStorage.charge = state.powerStorage.charge;
        powerStorage.discharge = state.powerStorage.discharge;
        powerStorage.daylightThreshold = state.powerStorage.daylightThreshold;
        powerStorage.automaticControl = state.powerStorage.automaticControl;
        powerStorage.daylightControl = state.powerStorage.daylightControl;
        powerStorage.daylightRecharge = state.powerStorage.daylightRecharge;
        powerStorage.daylightDischarge = state.powerStorage.daylightDischarge;
        powerStorage.nightRecharge = state.powerStorage.nightRecharge;
        powerStorage.nightDischarge = state.powerStorage.nightDischarge;
        powerStorage.neutralRecharge = state.powerStorage.neutralRecharge;
        powerStorage.neutralDischarge = state.powerStorage.neutralDischarge;
        powerStorage.meteorOverride = state.powerStorage.meteorOverride;
        powerStorage.meteorRecharge = state.powerStorage.meteorRecharge;
        powerStorage.meteorDischarge = state.powerStorage.meteorDischarge;
        powerStorage.lastDaylight = state.powerStorage.lastDaylight;
        powerStorage.lastMeteor = state.powerStorage.lastMeteor;
        return powerStorage;
    }
    @Override
    public double getProduction(){
        if(!discharge)return 0;
        return Math.min(getMaxProduction()*dischargeRate/100, power);
    }
    public double getMaxProduction(){
        return 32+32*level;
    }
    @Override
    public void producePower(double power){
        this.power -= power;
    }
    @Override
    public boolean isRenewable(){
        return false;
    }
    @Override
    public double getMaxPower(){
        return Math.round((7000+3000*Math.pow(level, 1.5))/1000)*1000;
    }
    @Override
    public double getDemand(){
        if(!charge)return 0;
        return Math.min(getMaxDemand()*rechargeRate/100, getMaxPower()-getPower());
    }
    public double getMaxDemand(){
        return 4+4*level;
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
        return true;
    }
    @Override
    public void getDebugInfo(ArrayList<String> data){
        super.getDebugInfo(data);
        data.add("Power: "+power);
        data.add("Charge: "+charge);
        data.add("Discharge: "+discharge);
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action((charge?"Disable":"Enable")+" Charging", () -> {
            charge = !charge;
        }, () -> {
            return true;
        }));
        actions.add(new Action((discharge?"Disable":"Enable")+" Discharging", () -> {
            discharge = !discharge;
        }, () -> {
            return true;
        }));
        actions.add(new Action("Power Storage Configuration", () -> {
            menu.openOverlay(new MenuPowerStorageConfiguration(menu, this));
        }, () -> {
            return true;
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
}

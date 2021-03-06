package planetaryprotector.structure;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.game.Action;
import planetaryprotector.game.Game;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.menu.ingame.MenuPowerStorageConfiguration;
import simplelibrary.config2.Config;
import static simplelibrary.opengl.Renderer2D.drawCenteredText;
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
    public PowerStorage(Game game, int x, int y) {
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
                int daylight = (int) Math.round(game.getSunlight()*100);
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
    @Override
    public void renderBackground(){
        drawRect(x, y, x+width, y+height, StructureType.EMPTY_PLOT.getTexture());
        super.renderBackground();
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        drawCenteredText(x, y, x+width, y+20, (int)power+"");
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public Config save(Config cfg) {
        super.save(cfg);
        cfg.set("power", power);
        cfg.set("charge", charge);
        cfg.set("discharge", discharge);
        cfg.set("daylightThreshold", daylightThreshold);
        cfg.set("automaticControl", automaticControl);
        cfg.set("daylightControl", daylightControl);
        cfg.set("daylightRecharge", daylightRecharge);
        cfg.set("daylightDischarge", daylightDischarge);
        cfg.set("nightRecharge", nightRecharge);
        cfg.set("nightDischarge", nightDischarge);
        cfg.set("neutralRecharge", neutralRecharge);
        cfg.set("neutralDischarge", neutralDischarge);
        cfg.set("meteorOverride", meteorOverride);
        cfg.set("meteorRecharge", meteorRecharge);
        cfg.set("meteorDischarge", meteorDischarge);
        cfg.set("lastDaylight", lastDaylight);
        cfg.set("lastMeteor", lastMeteor);
        return cfg;
    }
    public static PowerStorage loadSpecific(Config cfg, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        PowerStorage powerStorage = new PowerStorage(game, x, y, level, upgrades);
        powerStorage.power = cfg.get("power", powerStorage.power);
        powerStorage.charge = cfg.get("charge", powerStorage.charge);
        powerStorage.discharge = cfg.get("discharge", powerStorage.discharge);
        powerStorage.daylightThreshold = cfg.get("daylightThreshold", powerStorage.daylightThreshold);
        powerStorage.automaticControl = cfg.get("automaticControl", powerStorage.automaticControl);
        powerStorage.daylightControl = cfg.get("daylightControl", powerStorage.daylightControl);
        powerStorage.daylightRecharge = cfg.get("daylightRecharge", powerStorage.daylightRecharge);
        powerStorage.daylightDischarge = cfg.get("daylightDischarge", powerStorage.daylightDischarge);
        powerStorage.nightRecharge = cfg.get("nightRecharge", powerStorage.nightRecharge);
        powerStorage.nightDischarge = cfg.get("nightDischarge", powerStorage.nightDischarge);
        powerStorage.neutralRecharge = cfg.get("neutralRecharge", powerStorage.neutralRecharge);
        powerStorage.neutralDischarge = cfg.get("neutralDischarge", powerStorage.neutralDischarge);
        powerStorage.meteorOverride = cfg.get("meteorOverride", powerStorage.meteorOverride);
        powerStorage.meteorRecharge = cfg.get("meteorRecharge", powerStorage.meteorRecharge);
        powerStorage.meteorDischarge = cfg.get("meteorDischarge", powerStorage.meteorDischarge);
        powerStorage.lastDaylight = cfg.get("lastDaylight", powerStorage.lastDaylight);
        powerStorage.lastMeteor = cfg.get("lastMeteor", powerStorage.lastMeteor);
        return powerStorage;
    }
    @Override
    public double getProduction(){
        if(!discharge)return 0;
        return Math.min(getMaxProduction()*dischargeRate/100,power);
    }
    public double getMaxProduction(){
        return 32+32*level;
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
    public double getMaxPower(){
        return Math.round((7000+3000*Math.pow(level, 1.5))/1000)*1000;
    }
    @Override
    public double getDemand(){
        if(!charge)return 0;
        return Math.min(getMaxDemand()*rechargeRate/100,getMaxPower()-getPower());
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
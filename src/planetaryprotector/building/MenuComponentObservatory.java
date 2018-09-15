package planetaryprotector.building;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class MenuComponentObservatory extends BuildingPowerConsumer{
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
    private double yOff;
    public MenuComponentObservatory(double x, double y){
        super(x, y, 100, 100, BuildingType.OBSERVATORY, 1000);
    }
    @Override
    public boolean onDamage(double x, double y){
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        return true;
    }
    @Override
    public void update(){
        super.update();
        if(starlight<maxStarlight){
            collecting = Math.min(1, maxStarlight-starlight);
        }
        double star = 1-Core.game.getSunlight();
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
    }
    @Override
    public void render(){
        super.render();
        GL11.glColor4d(1, 1, 1, 1);
        drawCenteredText(x, y, x+width, y+15, "Power: "+Math.round(power));
        if(scanning==1){
            drawCenteredText(x, y+15, x+width, y+30, "Charging");
        }
        if(scanning==2){
            drawCenteredText(x, y+15, x+width, y+30, "Scanning");
        }
        if(!scan.isEmpty()){
            yOff = height-scan.size()*textHeight;
            for(String str : scan){
                text(str);
            }
        }
        double laserSize = (size/starlightSpeed)*collecting;
        if(starlight>0){
            drawCenteredText(x, y+47, x+width, y+58, "Starlight: "+Math.round(starlight*100)/100d);
        }
        if(collecting>0){
            drawCenteredText(x, y+30, x+width, y+45, "+"+Math.round(collecting*100)/100d);
            double yDiff = -y-100;
            double dist = Math.abs(yDiff);
            GL11.glColor4d(.8, .8, 1, 1);//big outer one
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                MenuGame.drawRegularPolygon(x+width/2, y+(yDiff*percent)+height/4, laserSize/2D,10,0);
            }
            GL11.glColor4d(.5, .5, 1, 1);
            drawRect(x, y+60, x+width, y+70, 0);
            GL11.glColor4d(.9, .9, 1, 1);//small inner one
            for(int i = 0; i<dist; i++){
                double percent = i/dist;
                MenuGame.drawRegularPolygon(x+width/2, y+(yDiff*percent)+height/4, (laserSize*(1/3D))/2D,10,0);
            }
            drawRect(x+1, y+61, x+(width*(1-Core.game.getSunlight()))-1, y+69, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    @Override
    public boolean canUpgrade(){
        return false;
    }
    @Override
    public MenuComponentBuilding getUpgraded() {
        return null;
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        for(int i = 0; i<damages.size(); i++){
            MenuComponentBuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("x", x);
        cfg.set("y", y);
        cfg.set("power", power);
        cfg.set("max power", maxPower);
        cfg.set("starlight", starlight);
        cfg.set("scanning", scanning);
        cfg.set("collecting", collecting);
        return cfg;
    }
    public static MenuComponentObservatory loadSpecific(Config cfg) {
        MenuComponentObservatory observatory = new MenuComponentObservatory(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            observatory.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        observatory.power = cfg.get("power", observatory.power);
        observatory.maxPower = cfg.get("max power", observatory.maxPower);
        observatory.starlight = cfg.get("starlight", observatory.starlight);
        observatory.scanning = cfg.get("scanning", observatory.scanning);
        observatory.collecting = cfg.get("collecting", observatory.collecting);
        return observatory;
    }
    @Override
    protected double getIgnitionChance(){
        return .4;
    }
    public void toggleScan(){
        if(scanning==0){
            scanning = 2;
        }else{
            scanning = 0;
        }
    }
    private void scan(){
        MenuGame game = Core.game;
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
        drawText(x, y+yOff, x+width, y+yOff+textHeight, str);
        yOff+=textHeight;
    }
    public void addStar(){
        starlight += 100;
        if(starlight>maxStarlight){
            starlight = maxStarlight;
        }
    }
    @Override
    public String getName(){
        return "Observatory";
    }
}
package planetaryprotector.building;
import java.util.ArrayList;
import planetaryprotector.enemy.AsteroidMaterial;
import planetaryprotector.Core;
import planetaryprotector.enemy.Enemy;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.event.BuildingChangeEventListener;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.Particle;
import simplelibrary.config2.Config;
public class ShieldGenerator extends Building implements BuildingPowerConsumer, BuildingDamagable, BuildingDemolishable, BuildingChangeEventListener{
    public double shieldSize = 0;
    public double maxShieldSize = 500;
    public double shieldStrength = 1;
    public double oldPower;
    public double blastRecharge = 0;
    public boolean canBlast = false;//replace with special upgrade
    public final Shield shield;
    public boolean shieldOutline = false;
    private double power = 0;
    public Building projectorTarget;
    public ShieldGenerator(double x, double y){
        super(x, y, 100, 100, BuildingType.SHIELD_GENERATOR);
        shield = new Shield(this);
    }
    public ShieldGenerator(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.SHIELD_GENERATOR, level, upgrades);
        canBlast = level>=10;
        shieldStrength = getStats(level);
        maxShieldSize += (level)*250;
        shield = new Shield(this);
    }
    public void blast(){
        if(!canBlast||blastRecharge!=0) return;
        blastRecharge = -50;
    }
    @Override
    public void update(){
        super.update();
        if(blastRecharge>0) blastRecharge--;
        if(blastRecharge<0){//<editor-fold defaultstate="collapsed" desc="Shield blast">
            blastRecharge++;
            double size = (Display.getWidth()*1.8)-(((-blastRecharge)%10)*(Display.getWidth()/5));
            shieldSize = size;
            Core.game.pushParticles(x+width/2, y+height/2, size, size/50, Particle.PushCause.SHEILD_BLAST);
            if(size==0){
                for(Enemy enemy : Core.game.enemies){
                    enemy.dead = true;
                }
                Core.game.meteorShower = false;
                for(AsteroidMaterial mat : AsteroidMaterial.values()){
                    mat.timer+=100;
                }
                if(Core.game.mothership!=null){
                    Core.game.mothership.shieldBlast();
                }
            }
            if(blastRecharge==0){
                blastRecharge = 20*60*10;
                shieldSize = 0;
                power = 0;
            }
        }//</editor-fold>
        double surface = 2*Math.PI*Math.pow(shieldSize/2,2);
        if(projectorTarget!=null){
            surface+=projectorTarget.getBuildingHeight()*4+10_000;
        }
        double powerDrawFactor = 50_000D;
        power -= surface/powerDrawFactor;
        double maxSurface = power/100*powerDrawFactor;
        double maxSize = Math.sqrt(maxSurface/2/Math.PI)*2;
        if(shieldSize>maxSize){
            shieldSize--;
        }
        shieldSize+=(maxSize-shieldSize)/100;
        oldPower = power;
        if(power<=0){
            shieldSize = 0;
            power = 0;
        }
        shieldSize = Math.max(0, Math.min(maxShieldSize, shieldSize));
    }
    @Override
    public void drawForeground(){
        MenuGame.theme.applyTextColor();
        drawCenteredText(x, y, x+width, y+20, (int)power+":"+(int)shieldSize);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public int getMaxLevel(){
        return 20;
    }
    /**
     * @return the shieldSize
     */
    public double getShieldSize(){
        return shieldSize;
    }
    public double getShieldSize(double damage){
        double difference = -damage;
        double size = shieldSize;
        size += difference/shieldStrength;
        if(size<0){
            return 0;
        }
        return size;
    }
    /**
     * @param shieldSize the shieldSize to set
     */
    public void setShieldSize(double shieldSize){
        double difference = shieldSize-this.shieldSize;
        this.shieldSize += difference/shieldStrength;
        if(this.shieldSize<0){
            this.shieldSize = 0;
        }
    }
    @Override
    public Config save(Config cfg) {
        cfg.set("blastRecharge", blastRecharge);
        cfg.set("size", shieldSize);
        cfg.set("maxSize", maxShieldSize);
        cfg.set("strength", shieldStrength);
        cfg.set("oldPower", oldPower);
        cfg.set("shieldOutline", shieldOutline);
        cfg.set("target", projectorTarget==null?-1:projectorTarget.getIndex());
        return cfg;
    }
    public static ShieldGenerator loadSpecific(Config cfg, double x, double y, int level, ArrayList<Upgrade> upgrades){
        ShieldGenerator generator = new ShieldGenerator(x, y, level, upgrades);
        generator.power = cfg.get("power", 0d);
        generator.blastRecharge = cfg.get("blastRecharge", 0d);
        generator.shieldSize = cfg.get("size", 0d);
        generator.maxShieldSize = cfg.get("maxSize", 500d);
        generator.shieldStrength = cfg.get("strength", 1d);
        generator.oldPower = cfg.get("oldPower", 0d);
        generator.shieldOutline = cfg.get("shieldOutline", false);
        return generator;
    }
    @Override
    protected double getFireDestroyThreshold(){
        return .6;
    }
    @Override
    protected double getIgnitionChance(){
        return .8;
    }
    private double getStats(int level){
        return Math.max(level, (49/400d)*Math.pow(level, 2)+1);
    }
    @Override
    public void upgrade(){
        super.upgrade();
        canBlast = getLevel()>=10;
        shieldStrength = getStats(getLevel());
        maxShieldSize += (getLevel())*250;
    }
    @Override
    public double getMaxPower(){
        return Integer.MAX_VALUE;
    }
    @Override
    public double getDemand(){
        return 100;
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public void addPower(double power){
        this.power+=power;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
    @Override
    protected void getBuildingDebugInfo(ArrayList<String> data) {
        data.add("Shield size: "+shieldSize+"/"+maxShieldSize);
        data.add("Shield Strength: "+shieldStrength);
        data.add("oldPower: "+oldPower);
        data.add("Blast Recharge: "+blastRecharge);
        data.add("Can Blast: "+canBlast);
        data.add("Shield outline: "+shieldOutline);
        data.add("Power: "+power);
        data.add("Projector target: "+(projectorTarget==null?"NONE":projectorTarget.toString()));
    }
    public void setProjectorTarget(Building building){
        if(projectorTarget!=null){
            projectorTarget.shield = null;
        }
        projectorTarget = building;
        if(projectorTarget!=null){
            projectorTarget.shield = this;
        }
    }
    @Override
    public void onBuildingChange(Building from, Building to){
        if(projectorTarget==from)setProjectorTarget(to);
    }
    @Override
    public void postLoad(MenuGame game, Config config) {
        super.postLoad(game, config);
        int index = config.get("target", -1);
        if(index!=-1){
            projectorTarget = game.buildings.get(index);
        }
    }
    /**
     * Damage the shield
     * @return true if the shield successfully absorbed the hit
     */
    public boolean shieldHit(){
        setShieldSize(getShieldSize() - 100);
        if(getShieldSize()>0){
            return true;
        }
        setShieldSize(0);
        return false;
    }
    @Override
    public boolean isBackgroundStructure(){
        return false;
    }
    public double getProjectedShieldStrength(){
        return Math.min(1, shieldSize/100);
    }
}
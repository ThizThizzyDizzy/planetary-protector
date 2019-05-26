package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.friendly.Drone;
import planetaryprotector.enemy.MenuComponentEnemy;
import planetaryprotector.friendly.Missile;
import planetaryprotector.particle.Particle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class Silo extends Building implements BuildingPowerConsumer, BuildingDamagable, BuildingDemolishable{
    public int drones = 0;
    int missiles = 0;
    public final ArrayList<Drone> droneList = new ArrayList<>();
    public boolean outline;
    public static final ItemStack missileCost = new ItemStack(Item.ironIngot, 75);
    public static final ItemStack droneCost = new ItemStack(Item.ironIngot, 100);
    private int missilePowerCost = 2500;
    private int dronePowerCost = 5000;
    private int power = 0;
    public Silo(double x, double y){
        this(x, y, 1, new ArrayList<>());
    }
    public Silo(Silo silo, int level, ArrayList<Upgrade> upgrades){
        this(silo.x, silo.y, level, upgrades);
        drones = silo.drones;
        missiles = silo.missiles;
        power = silo.power;
    }
    public Silo(double x, double y, int level, ArrayList<Upgrade> upgrades){
        super(x, y, 100, 100, BuildingType.SILO, level, upgrades);
    }
    @Override
    public void renderBackground(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
        renderDamages();
        drawMouseover();
        drawCenteredText(x, y, x+width, y+20, ""+power);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+getLevel());
    }
    public void draw(){
        if(drones>0){
            drawCenteredText(x, y+height-60, x+width, y+height-40, drones+" Drones");
        }
        if(missiles>0){
            drawCenteredText(x, y+height-40, x+width, y+height-20, missiles+" Missiles");
        }
        if(outline){
            drawRect(x+width/2-250, y+height/2-250, x+width/2+250, y+height/2+250, ImageStash.instance.getTexture("/textures/buildings/power outline.png"));
        }
    }
    @Override
    public void update(){
        super.update();
        for(Drone drone : droneList){
            drone.tick();
        }
        if(droneList.size()<drones){
            if(power>=20*600*5){
                power-=20*600*5;
                droneList.add(Core.game.addDrone(new Drone(this, 20*60*5)));
            }
        }
//        if(power<maxPower){
//            for(MenuComponentBuilding building : Core.game.buildings){
//                if(building instanceof MenuComponentGenerator&&Core.game.distance(building, this)<=250){
//                    MenuComponentGenerator gen = (MenuComponentGenerator) building;
//                    if(gen.power<gen.transferAmount){
//                        continue;
//                    }
//                    if(power+gen.transferAmount>maxPower){
//                        continue;
//                    }
//                    gen.power -= gen.transferAmount;
//                    power += gen.transferAmount;
//                }
//            }
//        }else{
//            power = maxPower;
//        }
    }
    public boolean canBuildMissile(){
        switch(getLevel()){
            case 1:
                if(missiles>=5){
                    return false;
                }
            case 2:
                if(missiles>=10){
                    return false;
                }
            case 3:
                if(missiles>=25){
                    return false;
                }
        }
        return Core.game.hasResources(missileCost)&&power>=missilePowerCost;
    }
    public boolean canBuildDrone(){
        switch(getLevel()){
            case 2:
                if(drones>=1){
                    return false;
                }
            case 3:
                if(drones>=5){
                    return false;
                }
        }
        return Core.game.hasResources(droneCost)&&power>=dronePowerCost&&getLevel()>=2;
    }
    public boolean canLaunchMissile(){
        return missiles>0;
    }
    public void buildMissile(){
        if(!canBuildMissile()) return;
        Core.game.removeResources(new ItemStack(missileCost));
        power-=missilePowerCost;
        missiles++;
    }
    public void buildDrone(){
        if(!canBuildDrone()) return;
        Core.game.removeResources(new ItemStack(droneCost));
        power-=dronePowerCost;
        drones++;
    }
    public void launchMissile(){
        if(!canLaunchMissile()) return;
        MenuComponentEnemy target = null;
        if(Core.game.mothership!=null) target = Core.game.mothership;
        if(target==null){
            DO:do{
                for(MenuComponentEnemy enemy : Core.game.enemies){
                    if(enemy instanceof EnemyMeteorStrike||enemy instanceof EnemyAlien) continue;
                    if(target!=null) break DO;
                    target = enemy;
                }
                for(MenuComponentEnemy enemy : Core.game.enemies){
                    if(enemy instanceof EnemyMeteorStrike) continue;
                    if(target!=null) break DO;
                    target = enemy;
                }
            }while(false);
        }
        if(target==null)return;
        missiles--;
        droneList.add(Core.game.addDrone(new Missile(this, target)));
    }
    @Override
    public boolean onDamage(double x, double y){
        if(damages.size()>=5&&MenuGame.rand.nextBoolean()){
            explosionInHangar();
        }
        return super.onDamage(x, y);
    }
    @Override
    public int getMaxLevel(){
        return 3;
    }
    @Override
    public Config save(Config cfg){
        cfg.set("energy", power);
        cfg.set("drones", drones);
        cfg.set("missiles", missiles);
        cfg.set("x", x);
        cfg.set("y", y);
        return cfg;
    }
    public static Silo loadSpecific(Config config, double x, double y, int level, ArrayList<Upgrade> upgrades) {
        Silo silo = new Silo(x, y, level, upgrades);
        silo.power = config.get("energy", 0);
        silo.drones = config.get("drones", 0);
        silo.missiles = config.get("missiles", 0);
        return silo;
    }
    public void explosionInHangar(){
        for(int i = 0; i<missiles; i++){
            damages.add(new BuildingDamage(this, MenuGame.rand.nextInt((int) width), MenuGame.rand.nextInt((int) height)));
        }
        Core.game.addParticleEffect(new Particle(x, y, ParticleEffectType.EXPLOSION, missiles/3+1));
    }
    public ItemStack[] missileCost(){
        return new ItemStack[]{new ItemStack(missileCost)};
    }
    public ItemStack[] droneCost(){
        return new ItemStack[]{new ItemStack(droneCost)};
    }
    @Override
    protected double getFireDestroyThreshold(){
        return .25;
    }
    @Override
    protected double getIgnitionChance(){
        return .2;
    }
    @Override
    public String getName(){
        return "Level "+getLevel()+" Silo";
    }
    private static int getMaxPower(int level){
        return level==1?100000:(level==2?250000:1000000);
    }
    @Override
    public double getMaxPower(){
        return getMaxPower(getLevel());
    }
    @Override
    public double getDemand(){
        return 100;
    }
    @Override
    public void addPower(double power){
        this.power+=power;
    }
    @Override
    public double getPower(){
        return power;
    }
    @Override
    public boolean isPowerActive(){
        return true;
    }
}
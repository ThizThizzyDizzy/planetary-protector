package planetaryprotector.building;
import planetaryprotector.Core;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.friendly.MenuComponentDrone;
import planetaryprotector.enemy.MenuComponentEnemy;
import planetaryprotector.friendly.MenuComponentMissile;
import planetaryprotector.particle.MenuComponentParticle;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import simplelibrary.config2.Config;
import simplelibrary.opengl.ImageStash;
import static simplelibrary.opengl.Renderer2D.drawRect;
public class MenuComponentSilo extends MenuComponentBuilding{
    public int drones = 0;
    int missiles = 0;
    public int power = 0;
    int maxPower = 10000;
    public ArrayList<MenuComponentDrone> droneList = new ArrayList<>();
    public boolean outline;
    public static final ItemStack missileCost = new ItemStack(Item.ironIngot, 75);
    public static final ItemStack droneCost = new ItemStack(Item.ironIngot, 100);
    private int missilePowerCost = 2500;
    private int dronePowerCost = 5000;
    public MenuComponentSilo(double x, double y){
        this(x, y, 1);
    }
    public MenuComponentSilo(MenuComponentSilo silo, int level){
        this(silo.x, silo.y, level);
        drones = silo.drones;
        missiles = silo.missiles;
        power = silo.power;
    }
    public MenuComponentSilo(double x, double y, int level){
        super(x, y, 100, 100, BuildingType.SILO);
        this.level = level;
        maxPower = level==1?100000:(level==2?250000:1000000);
    }
    @Override
    public void renderBackground(){
        removeRenderBound();
        drawRect(x, y, x+width, y+height, ImageStash.instance.getTexture("/textures/buildings/"+type.texture+".png"));
        renderDamages();
        drawCenteredText(x, y, x+width, y+20, ""+power);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
    }
    public void render(){
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
        for(MenuComponentDrone drone : droneList){
            drone.tick();
        }
        if(droneList.size()<drones){
            if(power>=20*600*5){
                power-=20*600*5;
                droneList.add(Core.game.addDrone(new MenuComponentDrone(this, 20*60*5)));
            }
        }
        if(power<maxPower){
            for(MenuComponentBuilding building : Core.game.buildings){
                if(building instanceof MenuComponentGenerator&&Core.game.distance(building, this)<=250){
                    MenuComponentGenerator gen = (MenuComponentGenerator) building;
                    if(gen.power<gen.transferAmount){
                        continue;
                    }
                    if(power+gen.transferAmount>maxPower){
                        continue;
                    }
                    gen.power -= gen.transferAmount;
                    power += gen.transferAmount;
                }
            }
        }else{
            power = maxPower;
        }
    }
    public boolean canBuildMissile(){
        switch(level){
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
        switch(level){
            case 2:
                if(drones>=1){
                    return false;
                }
            case 3:
                if(drones>=5){
                    return false;
                }
        }
        return Core.game.hasResources(droneCost)&&power>=dronePowerCost&&level>=2;
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
        droneList.add(Core.game.addDrone(new MenuComponentMissile(this, target)));
    }
    @Override
    public boolean onDamage(double x, double y){
        if(MenuGame.rand.nextBoolean()){
            explosionInHangar();
        }
        damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        return false;
    }
    @Override
    public MenuComponentBuilding getUpgraded(){
        return new MenuComponentSilo(this, level+1);
    }
    @Override
    public boolean canUpgrade(){
        return level<3;
    }
    @Override
    public Config save(Config cfg){
        cfg.set("type", type.name());
        cfg.set("count", damages.size());
        for(int i = 0; i<damages.size(); i++){
            MenuComponentBuildingDamage damage = damages.get(i);
            cfg.set(i+" x", damage.x);
            cfg.set(i+" y", damage.y);
        }
        cfg.set("level", level);
        cfg.set("energy", power);
        cfg.set("drones", drones);
        cfg.set("missiles", missiles);
        cfg.set("x", x);
        cfg.set("y", y);
        return cfg;
    }
    public static MenuComponentSilo loadSpecific(Config config) {
        MenuComponentSilo silo = new MenuComponentSilo(config.get("x", 0d), config.get("y",0d), config.get("level",1));
        for(int i = 0; i<config.get("count", 0); i++){
            silo.damages.add(new MenuComponentBuildingDamage(config.get(i+" x", 0d), config.get(i+" y", 0d), 50, 50));
        }
        silo.power = config.get("energy", 0);
        silo.drones = config.get("drones", 0);
        silo.missiles = config.get("missiles", 0);
        return silo;
    }
    public void explosionInHangar(){
        for(int i = 0; i<missiles; i++){
            damages.add(add(new MenuComponentBuildingDamage(MenuGame.rand.nextInt((int) width), MenuGame.rand.nextInt((int) height), 50, 50)));
        }
        Core.game.addParticleEffect(new MenuComponentParticle(x, y, ParticleEffectType.EXPLOSION, missiles/3+1));
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
}
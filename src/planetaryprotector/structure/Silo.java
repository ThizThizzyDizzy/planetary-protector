package planetaryprotector.structure;
import planetaryprotector.enemy.EnemyAlien;
import planetaryprotector.enemy.EnemyMeteorStrike;
import planetaryprotector.item.Item;
import planetaryprotector.item.ItemStack;
import planetaryprotector.friendly.Drone;
import planetaryprotector.enemy.Enemy;
import planetaryprotector.friendly.Missile;
import planetaryprotector.particle.Particle;
import planetaryprotector.game.Game;
import planetaryprotector.particle.ParticleEffectType;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import planetaryprotector.enemy.EnemyMothership;
import planetaryprotector.game.Action;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class Silo extends Structure implements PowerConsumer, StructureDemolishable{
    public int drones = 0;
    int missiles = 0;
    public final ArrayList<Drone> droneList = new ArrayList<>();
    public static final ItemStack missileCost = new ItemStack(Item.ironIngot, 75);
    public static final ItemStack droneCost = new ItemStack(Item.ironIngot, 100);
    private int missilePowerCost = 2500;
    private int dronePowerCost = 5000;
    private int power = 0;
    public Silo(Game game, int x, int y){
        this(game, x, y, 1, new ArrayList<>());
    }
    public Silo(Game game, Silo silo, int level, ArrayList<Upgrade> upgrades){
        this(game, silo.x, silo.y, level, upgrades);
        drones = silo.drones;
        missiles = silo.missiles;
        power = silo.power;
    }
    public Silo(Game game, int x, int y, int level, ArrayList<Upgrade> upgrades){
        super(StructureType.SILO, game, x, y, 100, 100, level, upgrades);
    }
    @Override
    public void renderForeground(){
        super.renderForeground();
        Game.theme.applyTextColor();
        if(drones>0){
            drawCenteredText(x, y+height-60, x+width, y+height-40, drones+" Drones");
        }
        if(missiles>0){
            drawCenteredText(x, y+height-40, x+width, y+height-20, missiles+" Missiles");
        }
        drawCenteredText(x, y, x+width, y+20, ""+power);
        drawCenteredText(x, y+height-20, x+width, y+height, "Level "+level);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        super.tick();
        for(Drone drone : droneList){
            drone.tick();
        }
        if(droneList.size()<drones){
            if(power>=20*600*5){
                power-=20*600*5;
                droneList.add(game.addDrone(new Drone(this, 20*60*5)));
            }
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
        return game.hasResources(missileCost)&&power>=missilePowerCost;
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
        return game.hasResources(droneCost)&&power>=dronePowerCost&&level>=2;
    }
    public boolean canLaunchMissile(){
        return missiles>0;
    }
    public void buildMissile(){
        if(!canBuildMissile()) return;
        game.removeResources(new ItemStack(missileCost));
        power-=missilePowerCost;
        missiles++;
    }
    public void buildDrone(){
        if(!canBuildDrone()) return;
        game.removeResources(new ItemStack(droneCost));
        power-=dronePowerCost;
        drones++;
    }
    public void launchMissile(){
        if(!canLaunchMissile()) return;
        Enemy target = null;
        if(target==null){
            DO:do{
                for(Enemy enemy : game.enemies){
                    if(enemy instanceof EnemyMothership){
                        target = enemy;
                        break DO;
                    }
                }
                for(Enemy enemy : game.enemies){
                    if(enemy instanceof EnemyMeteorStrike||enemy instanceof EnemyAlien) continue;
                    target = enemy;
                    break DO;
                }
                for(Enemy enemy : game.enemies){
                    if(enemy instanceof EnemyMeteorStrike) continue;
                    target = enemy;
                    break DO;
                }
            }while(false);
        }
        if(target==null)return;
        missiles--;
        droneList.add(game.addDrone(new Missile(this, target)));
    }
    @Override
    public boolean onDamage(int x, int y){
        if(damages.size()>=5&&game.rand.nextBoolean()){
            explosionInHangar();
        }
        return super.onDamage(x, y);
    }
    @Override
    public Config save(Config cfg){
        super.save(cfg);
        cfg.set("energy", power);
        cfg.set("drones", drones);
        cfg.set("missiles", missiles);
        cfg.set("x", x);
        cfg.set("y", y);
        return cfg;
    }
    public static Silo loadSpecific(Config config, Game game, int x, int y, int level, ArrayList<Upgrade> upgrades) {
        Silo silo = new Silo(game, x, y, level, upgrades);
        silo.power = config.get("energy", 0);
        silo.drones = config.get("drones", 0);
        silo.missiles = config.get("missiles", 0);
        return silo;
    }
    public void explosionInHangar(){
        for(int i = 0; i<missiles; i++){
            damages.add(new StructureDamage(this, game.rand.nextInt((int) width), game.rand.nextInt((int) height)));
        }
        game.addParticleEffect(new Particle(game, x, y, ParticleEffectType.EXPLOSION, missiles/3+1));
    }
    public ItemStack[] missileCost(){
        return new ItemStack[]{new ItemStack(missileCost)};
    }
    public ItemStack[] droneCost(){
        return new ItemStack[]{new ItemStack(droneCost)};
    }
    private static int getMaxPower(int level){
        return level==1?100000:(level==2?250000:1000000);
    }
    @Override
    public double getMaxPower(){
        return getMaxPower(level);
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
    @Override
    public void getDebugInfo(ArrayList<String> data) {
        super.getDebugInfo(data);
        data.add("Drones: "+drones);
        data.add("Missiles: "+missiles);
        data.add("Power: "+power);
    }
    @Override
    public void getActions(MenuGame menu, ArrayList<Action> actions){
        actions.add(new Action("Build Missile", (e) -> {
            buildMissile();
        },() -> {
            return canBuildMissile();
        }, missileCost()));
        if(level>1){
            actions.add(new Action("Build Drone", (e) -> {
                buildDrone();
            }, () -> {
                return canBuildDrone();
            }, droneCost()));
        }
        actions.add(new Action("Fire Missiles", (e) -> {
            launchMissile();
        }, () -> {
            return canLaunchMissile();
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
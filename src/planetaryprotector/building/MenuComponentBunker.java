package planetaryprotector.building;
import planetaryprotector.menu.MenuGame;
import simplelibrary.config2.Config;
public class MenuComponentBunker extends MenuComponentBuilding{
    public MenuComponentBunker(double x, double y){
        super(x, y, 100, 100, BuildingType.BUNKER);
    }
    @Override
    public boolean onDamage(double x, double y){
        if(MenuGame.rand.nextDouble()<.25){
            damages.add(add(new MenuComponentBuildingDamage(x-25, y-25, 50, 50)));
        }
        return true;
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
        return cfg;
    }
    public static MenuComponentBunker loadSpecific(Config cfg) {
        MenuComponentBunker bunker = new MenuComponentBunker(cfg.get("x", 0d), cfg.get("y",0d));
        for(int i = 0; i<cfg.get("count", 0); i++){
            bunker.damages.add(new MenuComponentBuildingDamage(cfg.get(i+" x", 0d), cfg.get(i+" y", 0d), 50, 50));
        }
        return bunker;
    }
    @Override
    protected double getIgnitionChance(){
        return 0;
    }
}
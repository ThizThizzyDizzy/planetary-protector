package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import java.util.ArrayList;
import java.util.Iterator;
import planetaryprotector.menu.component.ZComponent;
import simplelibrary.opengl.gui.components.MenuComponent;
public abstract class MenuComponentEnemy extends MenuComponent{
    public static double strength = 1;
    public static final double maxStrength = 15;
    public boolean dead = false;
    public int health;
    public MenuComponentEnemy(double x, double y, double width, double height, int health){
        super(x, y, width, height);
        this.health = health;
    }
    @Override
    public abstract void render();
    @Override
    public abstract void tick();
    public static MenuComponentEnemy randomEnemy(MenuGame game){
        if(canMeteorStrike()){
            return new EnemyMeteorStrike(game);
        }
        if(strength>=2.5){
            if(MenuGame.rand.nextDouble()<=.5){
                if(strength>=10){
                    return new EnemyLaserMeteor(game);
                }
                return new EnemyLaser(game);
            }
        }
        if(strength>=7.5){
            if(MenuGame.rand.nextDouble()<=.25){
                return new EnemyLandingParty(game);
            }
        }
        return new EnemyMeteorStrike(game);
    }
    private static boolean canMeteorStrike(){
        return EnemyMeteorStrike.getMeteorStrike()!=null;
    }
    public static double[] getBestStrike(){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Building building : Core.game.buildings){
            if(building.type==BuildingType.SHIELD_GENERATOR){
                shieldGen.add((ShieldGenerator) building);
            }
        }
        ArrayList<Double[]> possibleStrikes = new ArrayList<>();
        for(Building building : Core.game.buildings){
            if(building.type==BuildingType.WRECK){
                if(((Wreck)building).ingots<=1){
                    continue;
                }
            }
            double[] hitBox = new double[]{building.x, building.y, building.x+building.width, building.y+building.height};
            if(building instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) building;
                hitBox = new double[]{sky.x, sky.y-(sky.floorCount*sky.floorHeight), sky.x+sky.width, sky.y+sky.height};
            }
            double[][] corners = new double[][]{new double[]{hitBox[0]+1,hitBox[1]+1}, new double[]{hitBox[2]-1,hitBox[1]+1}, new double[]{hitBox[0]+1,hitBox[3]-1}, new double[]{hitBox[2]-1,hitBox[3]-1}};
            FOR:for(double[] d : corners){
                double X = d[0];
                double Y = d[1];
                if(Y<0)continue;
                for (Iterator<MenuComponentEnemy> it = Core.game.enemies.iterator(); it.hasNext();) {
                    MenuComponentEnemy enemy = it.next();
                    if(enemy.x>X-50&&enemy.x<X+50&&enemy.y>Y-50&&enemy.y<Y+50) continue FOR;
                }
                if(shieldGen.isEmpty()){
                    possibleStrikes.add(new Double[]{X,Y,0d});
                }
                for(ShieldGenerator gen : shieldGen){
                    double dist = Core.distance(gen,X,Y);
                    possibleStrikes.add(new Double[]{X,Y,dist});
                }
            }
        }
        double max = Double.NEGATIVE_INFINITY;
        for(Double[] strike : possibleStrikes){
            max = Math.max(strike[2], max);
        }
        for(Double[] strike : possibleStrikes){
            if(strike[2]==max){
                return new double[]{strike[0],strike[1]};
            }
        }
        return null;
    }
}
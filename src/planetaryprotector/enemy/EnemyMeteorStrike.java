package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
import planetaryprotector.building.ShieldGenerator;
import planetaryprotector.building.Building;
import planetaryprotector.building.Wreck;
import planetaryprotector.building.BuildingType;
import planetaryprotector.building.Skyscraper;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
public class EnemyMeteorStrike extends MenuComponentEnemy{
    public int initialDelay = 20*10;
    public int meteorDelay = 0;
    public int meteors = 0;
    private final MenuGame game;
    public EnemyMeteorStrike(MenuGame game){
        super(0, 0, 50, 50,5);
        double[] location = getMeteorStrike();
        if(location==null){
            location = MenuComponentEnemy.getBestStrike();
            if(location==null){
                location = new double[]{game.base.x+game.base.width/2,game.base.y+game.base.height/2};
            }
            if(MenuComponentEnemy.strength<2.5){
                MenuComponentEnemy.strength+=.25;
            }
        }
        x=location[0];
        y=location[1];
        this.game = game;
        meteors = (int)(MenuComponentEnemy.strength+1);
    }
    @Override
    public void render(){
        removeRenderBound();
        GL11.glColor4d(1, 1, 0, .25);
        drawRect(x-width/2, y-Display.getHeight(), x+width/2, y+height/2, 0);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(meteors<=0||dead){
            game.componentsToRemove.add(this);
            return;
        }
        initialDelay--;
        if(initialDelay<=0){
            meteorDelay--;
            if(meteorDelay<=0&&meteors>0){
                meteorDelay+=Math.max(5, 20-MenuComponentEnemy.strength);
                meteors--;
                game.addAsteroid(new Asteroid(x-width/2, y-height/2, AsteroidMaterial.STONE, 1));
            }
        }
    }
    public static double[] getMeteorStrike(){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Building building : Core.game.buildings){
            if(building.type==BuildingType.SHIELD_GENERATOR){
                shieldGen.add((ShieldGenerator) building);
            }
        }
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
                for(MenuComponentEnemy enemy: Core.game.enemies){
                    if(enemy.x>X-50&&enemy.x<X+50&&enemy.y>Y-50&&enemy.y<Y+50) continue FOR;
                }
                if(shieldGen.isEmpty()){
                    return new double[]{X,Y};
                }
                for(ShieldGenerator gen : shieldGen){
                    double shieldRadius = gen.shieldSize/2;
                    double dist = Core.distance(gen,X,Y);
                    if(dist>shieldRadius){
                        return new double[]{X,Y};
                    }
                }
            }
        }
        return null;
    }
}
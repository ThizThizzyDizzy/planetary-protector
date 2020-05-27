package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.structure.building.ShieldGenerator;
import planetaryprotector.structure.building.Building;
import planetaryprotector.structure.building.Wreck;
import planetaryprotector.structure.building.BuildingType;
import planetaryprotector.structure.building.Skyscraper;
import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import planetaryprotector.structure.Structure;
public class EnemyMeteorStrike extends Enemy{
    public int initialDelay = 20*10;
    public int meteorDelay = 0;
    public int meteors = 0;
    public EnemyMeteorStrike(Game game){
        super(game, 0, 0, 50, 50,5);
        double[] location = getMeteorStrike(game);
        if(location==null){
            location = Enemy.getBestStrike(game);
            if(location==null){
                location = new double[]{Display.getWidth()/2, Display.getHeight()/2};
            }
            if(Enemy.strength<2.5){
                Enemy.strength+=.25;
            }
        }
        x=location[0];
        y=location[1];
        meteors = (int)(Enemy.strength+1);
    }
    @Override
    public void render(){
        GL11.glColor4d(1, 1, 0, .25);
        drawRect(x-width/2, y-Display.getHeight(), x+width/2, y+height/2, 0);
        drawRect(x-width/2, y-height/2, x+width/2, y+height/2, 0);
        GL11.glColor4d(1, 1, 1, 1);
    }
    @Override
    public void tick(){
        if(meteors<=0||dead){
            dead = true;
            return;
        }
        initialDelay--;
        if(initialDelay<=0){
            meteorDelay--;
            if(meteorDelay<=0&&meteors>0){
                meteorDelay+=Math.max(5, 20-Enemy.strength);
                meteors--;
                game.addAsteroid(new Asteroid(game, x-width/2, y-height/2, AsteroidMaterial.STONE, 1));
            }
        }
    }
    public static double[] getMeteorStrike(Game game){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                shieldGen.add((ShieldGenerator) structure);
            }
        }
        for(Structure structure : game.structures){
            if(structure instanceof Wreck){
                if(((Wreck)structure).ingots<=1){
                    continue;
                }
            }
            double[] hitBox = new double[]{structure.x, structure.y, structure.x+structure.width, structure.y+structure.height};
            if(structure instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) structure;
                hitBox = new double[]{sky.x, sky.y-(sky.floorCount*sky.floorHeight), sky.x+sky.width, sky.y+sky.height};
            }
            double[][] corners = new double[][]{new double[]{hitBox[0]+1,hitBox[1]+1}, new double[]{hitBox[2]-1,hitBox[1]+1}, new double[]{hitBox[0]+1,hitBox[3]-1}, new double[]{hitBox[2]-1,hitBox[3]-1}};
            FOR:for(double[] d : corners){
                double X = d[0];
                double Y = d[1];
                if(Y<0)continue;
                for(Enemy enemy: game.enemies){
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
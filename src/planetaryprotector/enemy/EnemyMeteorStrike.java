package planetaryprotector.enemy;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.structure.ShieldGenerator;
import planetaryprotector.structure.Wreck;
import planetaryprotector.structure.Skyscraper;
import java.util.ArrayList;
import org.joml.Vector2f;
import planetaryprotector.structure.Structure;
public class EnemyMeteorStrike extends Enemy{
    public int initialDelay = 20*10;
    public int meteorDelay = 0;
    public int meteors = 0;
    public EnemyMeteorStrike(Game game){
        super(game, 0, 0, 50, 50,5);
        int[] location = getMeteorStrike(game);
        if(location==null){
            location = Enemy.getBestStrike(game);
            if(location==null){
                location = game.getCityBoundingBox().getCenter();
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
    public void draw(){
        Renderer.setColor(1, 1, 0, .25f);
        Renderer.fillRect(x-width/2, y-game.getCityBoundingBox().height, x+width/2, y+height/2, 0);//TODO redo
        Renderer.fillRect(x-width/2, y-height/2, x+width/2, y+height/2, 0);
        Renderer.setColor(1, 1, 1, 1);
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
    public static int[] getMeteorStrike(Game game){
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
            int[] hitBox = new int[]{structure.x, structure.y, structure.x+structure.width, structure.y+structure.height};
            if(structure instanceof Skyscraper){
                Skyscraper sky = (Skyscraper) structure;
                hitBox = new int[]{sky.x, sky.y-(sky.floorCount*sky.floorHeight), sky.x+sky.width, sky.y+sky.height};
            }
            int[][] corners = new int[][]{new int[]{hitBox[0]+1,hitBox[1]+1}, new int[]{hitBox[2]-1,hitBox[1]+1}, new int[]{hitBox[0]+1,hitBox[3]-1}, new int[]{hitBox[2]-1,hitBox[3]-1}};
            FOR:for(int[] d : corners){
                int X = d[0];
                int Y = d[1];
                if(Y<0)continue;
                for(Enemy enemy: game.enemies){
                    if(enemy.x>X-50&&enemy.x<X+50&&enemy.y>Y-50&&enemy.y<Y+50) continue FOR;
                }
                if(shieldGen.isEmpty()){
                    return new int[]{X,Y};
                }
                for(ShieldGenerator gen : shieldGen){
                    double shieldRadius = gen.shieldSize/2;
                    double dist = Vector2f.distance(gen.x,gen.y,X,Y);
                    if(dist>shieldRadius){
                        return new int[]{X,Y};
                    }
                }
            }
        }
        return null;
    }
}
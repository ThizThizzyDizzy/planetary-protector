package planetaryprotector.enemy;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.structure.building.ShieldGenerator;
import planetaryprotector.structure.building.Wreck;
import planetaryprotector.structure.building.Skyscraper;
import java.util.ArrayList;
import java.util.Iterator;
import planetaryprotector.GameObject;
import planetaryprotector.structure.Structure;
public abstract class Enemy extends GameObject{
    public static double strength = 1;
    public static final double maxStrength = 15;
    public int health;
    public Enemy(Game game, double x, double y, double width, double height, int health){
        super(game, x, y, width, height);
        this.health = health;
    }
    @Override
    public abstract void render();
    public abstract void tick();
    public static Enemy randomEnemy(Game game){
        if(canMeteorStrike(game)){
            return new EnemyMeteorStrike(game);
        }
        if(strength>=2.5){
            if(game.rand.nextDouble()<=.5){
                if(strength>=10){
                    return new EnemyLaserMeteor(game);
                }
                return new EnemyLaser(game);
            }
        }
        if(strength>=7.5){
            if(game.rand.nextDouble()<=.25){
                return new EnemyLandingParty(game);
            }
        }
        return new EnemyMeteorStrike(game);
    }
    private static boolean canMeteorStrike(Game game){
        return EnemyMeteorStrike.getMeteorStrike(game)!=null;
    }
    public static double[] getBestStrike(Game game){
        ArrayList<ShieldGenerator> shieldGen = new ArrayList<>();
        for(Structure structure : game.structures){
            if(structure instanceof ShieldGenerator){
                shieldGen.add((ShieldGenerator) structure);
            }
        }
        ArrayList<Double[]> possibleStrikes = new ArrayList<>();
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
                for (Iterator<Enemy> it = game.enemies.iterator(); it.hasNext();) {
                    Enemy enemy = it.next();
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
    public void shieldBlast(){
        dead = true;
    }
}
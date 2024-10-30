package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.joml.Vector2f;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
import planetaryprotector.structure.Structure;
public class StarlightNetwork{
    public ArrayList<StarlightConsumer> demand = new ArrayList<>();
    public ArrayList<StarlightProducer> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static StarlightNetwork detect(ArrayList<Structure> structures, Structure structure){
        if(structure instanceof StarlightUser){
            if(!((StarlightUser)structure).isStarlightActive())return null;
            StarlightNetwork network = new StarlightNetwork();
            if(structure instanceof StarlightProducer){
                network.supply.add((StarlightProducer)structure);
            }
            if(structure instanceof StarlightConsumer){
                network.demand.add((StarlightConsumer)structure);
            }
            network.detect(structures);
            return network;
        }
        return null;
    }
    public void tick(){
        if(demand.size()+supply.size()==1)return;
        double totalDemand = 0;
        for(StarlightConsumer consumer : demand){
            totalDemand += consumer.getStarlightDemand();
        }
        double totalSupply = 0;
        for(StarlightProducer producer : supply){
            totalSupply += producer.getStarlightProduction();
            if(totalSupply>=totalDemand){
                totalSupply = totalDemand;
                break;
            }
        }
        if(totalSupply>=totalDemand){
            double supplySoFar = 0;
            for(StarlightProducer producer : supply){
                double production = producer.getStarlightProduction();
                if(supplySoFar+production>=totalDemand)
                    production = totalDemand-supplySoFar;
                producer.produceStarlight(production);
                supplySoFar += production;
                if(supplySoFar>=totalDemand)break;
            }
            for(StarlightConsumer consumer : demand){
                consumer.addStarlight(consumer.getStarlightDemand());
            }
        }else{
            for(StarlightProducer producer : supply){
                producer.produceStarlight(producer.getStarlightProduction());
            }
            distributeStarlight(totalSupply);
        }
    }
    private void detect(ArrayList<Structure> structures){
        boolean foundNew = true;
        while(foundNew){
            foundNew = false;
            ArrayList<StarlightUser> network = new ArrayList<>();
            network.addAll(demand);
            network.addAll(supply);
            for(StarlightUser user : network){
                for(Structure other : structures){
                    if(other instanceof StarlightUser&&!network.contains(other)){
                        if(!((StarlightUser)other).isStarlightActive())continue;
                        var struc = (Structure)user;
                        if(Vector2f.distance(struc.x, struc.y, other.x, other.y)>POWER_TRANSFER_RADIUS)
                            continue;
                        if(other instanceof StarlightConsumer){
                            demand.add((StarlightConsumer)other);
                            foundNew = true;
                        }
                        if(other instanceof StarlightProducer){
                            supply.add((StarlightProducer)other);
                            foundNew = true;
                        }
                    }
                }
            }
        }
        Collections.sort(supply, (StarlightProducer o1, StarlightProducer o2) -> {//starlight storage CAN send starlight to itself, this will give the illusion of discharge rate decreasing as its charge decreases, and never hitting zero.
            boolean r1 = o1.isStarlightRenewable();
            boolean r2 = o2.isStarlightRenewable();
            boolean s1 = o1 instanceof StructureStarlightStorage;
            boolean s2 = o2 instanceof StructureStarlightStorage;
            int i1 = 0;
            int i2 = 0;
            if(r1)i1 = 1;
            if(r2)i2 = 1;
            if(s1)i1 = -1;
            if(s2)i2 = -1;
            return i2-i1;
        });
    }
    private void distributeStarlight(double starlight){
        HashMap<StarlightConsumer, Double> demands = new HashMap<>();
        for(StarlightConsumer consumer : demand){
            demands.put(consumer, consumer.getStarlightDemand());
        }
        while(starlight>0.01){
            if(demands.isEmpty())return;
            double avg = starlight/demands.size();
            double min = avg;
            for(StarlightConsumer c : demands.keySet()){
                min = Math.min(min, demands.get(c));
            }
            for(StarlightConsumer c : new ArrayList<>(demands.keySet())){
                c.addStarlight(min);
                starlight -= min;
                demands.put(c, demands.get(c)-min);
                if(demands.get(c)<=.01)demands.remove(c);//satisfied demand!
            }
        }
    }
    public void draw(){
        if(demand.size()+supply.size()==1)return;
        for(StarlightConsumer consumer : demand){
            Structure s = (Structure)consumer;
            if(Core.debugMode){
                Renderer.setColor(.8f, 0, 0, 1);
                Renderer.fillHollowRegularPolygon(s.x+s.width/2, s.y+s.height/2, 10, 40, 50);
            }
            drawConnectors(s);
            Renderer.setColor(0, .5f, 1, 1);
            Renderer.fillHollowRegularPolygon(s.x+s.width/2, s.y+s.height/2, 50, POWER_TRANSFER_RADIUS-5, POWER_TRANSFER_RADIUS);
            Renderer.setColor(0, .5f, 1, 1);
        }
        for(StarlightProducer producer : supply){
            Structure s = (Structure)producer;
            if(Core.debugMode){
                Renderer.setColor(0, .3f, .9f, 1);
                Renderer.fillHollowRegularPolygon(s.x+s.width/2, s.y+s.height/2, 10, 25, 35);
            }
            drawConnectors(s);
            Renderer.setColor(0, .5f, 1, 1);
            Renderer.fillHollowRegularPolygon(s.x+s.width/2, s.y+s.height/2, 50, POWER_TRANSFER_RADIUS-5, POWER_TRANSFER_RADIUS);
            Renderer.setColor(0, .5f, 1, 1);
        }
    }
    private void drawConnectors(Structure s){
        for(StarlightConsumer consumer : demand){
            Structure other = (Structure)consumer;
            if(other==s)continue;
            if(Vector2f.distance(s.x, s.y, other.x, other.y)<=POWER_TRANSFER_RADIUS){
                Game.drawConnector(s.x+s.width/2, s.y+s.height/2, other.x+other.width/2, other.y+other.height/2, 10, .2f, .9f, .8f, 0, .45f, .4f);
            }
        }
        for(StarlightProducer producer : supply){
            Structure other = (Structure)producer;
            if(other==s)continue;
            if(Vector2f.distance(s.x, s.y, other.x, other.y)<=POWER_TRANSFER_RADIUS){
                Game.drawConnector(s.x+s.width/2, s.y+s.height/2, other.x+other.width/2, other.y+other.height/2, 10, .2f, .9f, .8f, 0, .45f, .4f);
            }
        }
    }
}

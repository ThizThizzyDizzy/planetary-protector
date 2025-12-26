package planetaryprotector.structure;
import com.thizthizzydizzy.dizzyengine.graphics.Renderer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.joml.Vector2f;
import planetaryprotector.Core;
import planetaryprotector.game.Game;
public class PowerNetwork{
    public ArrayList<PowerConsumer> demand = new ArrayList<>();
    public ArrayList<PowerProducer> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static PowerNetwork detect(List<Structure> structures, Structure structure){
        if(structure instanceof PowerUser){
            if(!((PowerUser)structure).isPowerActive())return null;
            PowerNetwork network = new PowerNetwork();
            if(structure instanceof PowerProducer){
                network.supply.add((PowerProducer)structure);
            }
            if(structure instanceof PowerConsumer){
                network.demand.add((PowerConsumer)structure);
            }
            network.detect(structures);
            return network;
        }
        return null;
    }
    public void tick(){
        if(demand.size()+supply.size()==1)return;
        double totalDemand = 0;
        for(PowerConsumer consumer : demand){
            totalDemand += consumer.getDemand();
        }
        double totalSupply = 0;
        for(PowerProducer producer : supply){
            totalSupply += producer.getProduction();
            if(totalSupply>=totalDemand){
                totalSupply = totalDemand;
                break;
            }
        }
        if(totalSupply>=totalDemand){
            double supplySoFar = 0;
            for(PowerProducer producer : supply){
                double production = producer.getProduction();
                if(supplySoFar+production>=totalDemand)
                    production = totalDemand-supplySoFar;
                producer.producePower(production);
                supplySoFar += production;
                if(supplySoFar>=totalDemand)break;
            }
            for(PowerConsumer consumer : demand){
                consumer.addPower(consumer.getDemand());
            }
        }else{
            for(PowerProducer producer : supply){
                producer.producePower(producer.getProduction());
            }
            distributePower(totalSupply);
        }
    }
    private void detect(List<Structure> structures){
        boolean foundNew = true;
        while(foundNew){
            foundNew = false;
            ArrayList<PowerUser> network = new ArrayList<>();
            network.addAll(demand);
            network.addAll(supply);
            for(PowerUser user : network){
                for(Structure other : structures){
                    if(other instanceof PowerUser&&!network.contains(other)){
                        if(!((PowerUser)other).isPowerActive())continue;
                        var struc = (Structure)user;
                        if(Vector2f.distance(struc.getPosition().x, struc.getPosition().y, other.getPosition().x, other.getPosition().y)>POWER_TRANSFER_RADIUS)
                            continue;
                        if(other instanceof PowerConsumer){
                            demand.add((PowerConsumer)other);
                            foundNew = true;
                        }
                        if(other instanceof PowerProducer){
                            supply.add((PowerProducer)other);
                            foundNew = true;
                        }
                    }
                }
            }
        }
        Collections.sort(supply, (PowerProducer o1, PowerProducer o2) -> {//power storage CAN send power to itself, this will give the illusion of discharge rate decreasing as its charge decreases, and never hitting zero.
            boolean r1 = o1.isRenewable();
            boolean r2 = o2.isRenewable();
            boolean s1 = o1 instanceof StructurePowerStorage;
            boolean s2 = o2 instanceof StructurePowerStorage;
            int i1 = 0;
            int i2 = 0;
            if(r1)i1 = 1;
            if(r2)i2 = 1;
            if(s1)i1 = -1;
            if(s2)i2 = -1;
            return i2-i1;
        });
    }
    private void distributePower(double power){
        HashMap<PowerConsumer, Double> demands = new HashMap<>();
        for(PowerConsumer consumer : demand){
            demands.put(consumer, consumer.getDemand());
        }
        while(power>0.01){
            if(demands.isEmpty())return;
            double avg = power/demands.size();
            double min = avg;
            for(PowerConsumer c : demands.keySet()){
                min = Math.min(min, demands.get(c));
            }
            for(PowerConsumer c : new ArrayList<>(demands.keySet())){
                c.addPower(min);
                power -= min;
                demands.put(c, demands.get(c)-min);
                if(demands.get(c)<=.01)demands.remove(c);//satisfied demand!
            }
        }
    }
    public void draw(){
        if(demand.size()+supply.size()==1)return;
        for(PowerConsumer consumer : demand){
            Structure s = (Structure)consumer;
            if(Core.debugMode){
                Renderer.setColor(.8f, 0, 0, 1);
                Renderer.fillHollowRegularPolygon(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, 10, 40, 50);
            }
            drawConnectors(s);
            Renderer.setColor(1, 0, 0, 1);
            Renderer.fillHollowRegularPolygon(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, 50, POWER_TRANSFER_RADIUS-5, POWER_TRANSFER_RADIUS);
            Renderer.setColor(1, 1, 1, 1);
        }
        for(PowerProducer producer : supply){
            Structure s = (Structure)producer;
            if(Core.debugMode){
                Renderer.setColor(0, .3f, .9f, 1);
                if(((PowerProducer)s).isRenewable())
                    Renderer.setColor(0, .5f, 1, 1);
                Renderer.fillHollowRegularPolygon(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, 10, 25, 35);
            }
            drawConnectors(s);
            Renderer.setColor(1, 0, 0, 1);
            Renderer.fillHollowRegularPolygon(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, 50, POWER_TRANSFER_RADIUS-5, POWER_TRANSFER_RADIUS);
            Renderer.setColor(1, 1, 1, 1);
        }
    }
    private void drawConnectors(Structure s){
        for(PowerConsumer consumer : demand){
            Structure other = (Structure)consumer;
            if(other==s)continue;
            if(Vector2f.distance(s.getPosition().x, s.getPosition().y, other.getPosition().x, other.getPosition().y)<=POWER_TRANSFER_RADIUS){
                Game.drawConnector(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, other.getPosition().x+other.getSize().x/2, other.getPosition().y+other.getSize().y/2, 10, 1, 1, 0, .25f, .25f, 0);
            }
        }
        for(PowerProducer producer : supply){
            Structure other = (Structure)producer;
            if(other==s)continue;
            if(Vector2f.distance(s.getPosition().x, s.getPosition().y, other.getPosition().x, other.getPosition().y)<=POWER_TRANSFER_RADIUS){
                Game.drawConnector(s.getPosition().x+s.getSize().x/2, s.getPosition().y+s.getSize().y/2, other.getPosition().x+other.getSize().x/2, other.getPosition().y+other.getSize().y/2, 10, 1, 1, 0, .25f, .25f, 0);
            }
        }
    }
}

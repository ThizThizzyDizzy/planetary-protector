package planetaryprotector.building;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
public class PowerNetwork{
    public ArrayList<Building> demand = new ArrayList<>();
    public ArrayList<Building> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static PowerNetwork detect(ArrayList<Building> buildings, Building building){
        if(building instanceof PowerNetworkSection){
            if(!((PowerNetworkSection)building).isPowerActive())return null;
            PowerNetwork network = new PowerNetwork();
            if(building instanceof BuildingPowerProducer){
                network.supply.add((Building)building);
            }
            if(building instanceof BuildingPowerConsumer){
                network.demand.add((Building)building);
            }
            network.detect(buildings);
            return network;
        }
        return null;
    }
    public void tick(){
        if(demand.size()+supply.size()==1)return;
        double totalDemand = 0;
        for(Building building : demand){
            totalDemand += ((BuildingPowerConsumer)building).getDemand();
        }
        double totalSupply = 0;
        for(Building building : supply){
            totalSupply += ((BuildingPowerProducer)building).getProduction();
            if(totalSupply>=totalDemand){
                totalSupply = totalDemand;
                break;
            }
        }
        if(totalSupply>=totalDemand){
            double supplySoFar = 0;
            for(Building building : supply){
                double production = ((BuildingPowerProducer)building).getProduction();
                if(supplySoFar+production>=totalDemand)production = totalDemand-supplySoFar;
                ((BuildingPowerProducer)building).producePower(production);
                supplySoFar+=production;
                if(supplySoFar>=totalDemand)break;
            }
            for(Building building : demand){
                ((BuildingPowerConsumer)building).addPower(((BuildingPowerConsumer)building).getDemand());
            }
        }else{
            for(Building building : supply){
                ((BuildingPowerProducer)building).producePower(((BuildingPowerProducer)building).getProduction());
            }
            distributePower(totalSupply);
        }
    }
    private void detect(ArrayList<Building> buildings){
        boolean foundNew = true;
        while(foundNew){
            foundNew = false;
            ArrayList<Building> network = new ArrayList<>();
            network.addAll(demand);
            network.addAll(supply);
            for(Building building : network){
                synchronized(buildings){
                    for(Building other : buildings){
                        if(other instanceof PowerNetworkSection&&!network.contains(other)){
                            if(!((PowerNetworkSection)other).isPowerActive())continue;
                            if(Core.distance(building, other)>POWER_TRANSFER_RADIUS)continue;
                            if(other instanceof BuildingPowerConsumer){
                                demand.add(other);
                                foundNew = true;
                            }
                            if(other instanceof BuildingPowerProducer){
                                supply.add(other);
                                foundNew = true;
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(supply, new Comparator<Building>(){//power storage CAN send power to itself, this will give the illusion of discharge rate decreasing as its charge decreases, and never hitting zero.
            @Override
            public int compare(Building o1, Building o2){
                boolean r1 = ((BuildingPowerProducer)o1).isRenewable();
                boolean r2 = ((BuildingPowerProducer)o2).isRenewable();
                boolean s1 = o1 instanceof BuildingPowerStorage;
                boolean s2 = o2 instanceof BuildingPowerStorage;
                int i1 = 0;
                int i2 = 0;
                if(r1)i1 = 1;
                if(r2)i2 = 1;
                if(s1)i1 = -1;
                if(s2)i2 = -1;
                return i2-i1;
            }
        });
    }
    private void distributePower(double power){
        HashMap<BuildingPowerConsumer, Double> demands = new HashMap<>();
        for(Building b : demand){
            demands.put((BuildingPowerConsumer)b,((BuildingPowerConsumer)b).getDemand());
        }
        while(power>0.01){
            if(demands.isEmpty())return;
            double avg = power/demands.size();
            double min = avg;
            for(BuildingPowerConsumer c : demands.keySet()){
                min = Math.min(min,demands.get(c));
            }
            ArrayList<BuildingPowerConsumer> them = new ArrayList<>(demands.keySet());
            for(BuildingPowerConsumer c : them){
                c.addPower(min);
                power-=min;
                demands.put(c,demands.get(c)-min);
                if(demands.get(c)<=.01)demands.remove(c);//satisfied demand!
            }
        }
    }
    public void draw(){
        if(demand.size()+supply.size()==1)return;
        for(Building b : demand){
            if(Core.debugMode){
                GL11.glColor4d(.8, 0, 0, 1);
                MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, 50, 40, 25, 0);
            }
            drawConnectors(b);
            GL11.glColor4d(1, 0, 0, 1);
            MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, POWER_TRANSFER_RADIUS, POWER_TRANSFER_RADIUS-5, 50, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
        for(Building b : supply){
            if(Core.debugMode){
                GL11.glColor4d(0, .3, .9, 1);
                if(((BuildingPowerProducer)b).isRenewable())GL11.glColor4d(0, .5, 1, 1);
                MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, 35, 25, 25, 0);
            }
            drawConnectors(b);
            GL11.glColor4d(1, 0, 0, 1);
            MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, POWER_TRANSFER_RADIUS, POWER_TRANSFER_RADIUS-5, 50, 0);
            GL11.glColor4d(1, 1, 1, 1);
        }
    }
    private void drawConnectors(Building b){
        for(Building other : demand){
            if(other==b)continue;
            if(Core.distance(b, other)<=POWER_TRANSFER_RADIUS){
                MenuGame.drawConnector(b.x+b.width/2, b.y+b.height/2, other.x+other.width/2, other.y+other.height/2, 10, 1, 1, 0, .25, .25, 0);
            }
        }
        for(Building other : supply){
            if(other==b)continue;
            if(Core.distance(b, other)<=POWER_TRANSFER_RADIUS){
                MenuGame.drawConnector(b.x+b.width/2, b.y+b.height/2, other.x+other.width/2, other.y+other.height/2, 10, 1, 1, 0, .25, .25, 0);
            }
        }
    }
}

package planetaryprotector.building;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import planetaryprotector.Core;
import planetaryprotector.menu.MenuGame;
public class StarlightNetwork{
    public ArrayList<Building> demand = new ArrayList<>();
    public ArrayList<Building> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static StarlightNetwork detect(ArrayList<Building> buildings, Building building){
        if(building instanceof StarlightNetworkSection){
            if(!((StarlightNetworkSection)building).isStarlightActive())return null;
            StarlightNetwork network = new StarlightNetwork();
            if(building instanceof BuildingStarlightProducer){
                network.supply.add((Building)building);
            }
            if(building instanceof BuildingStarlightConsumer){
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
            totalDemand += ((BuildingStarlightConsumer)building).getStarlightDemand();
        }
        double totalSupply = 0;
        for(Building building : supply){
            totalSupply += ((BuildingStarlightProducer)building).getStarlightProduction();
            if(totalSupply>=totalDemand){
                totalSupply = totalDemand;
                break;
            }
        }
        if(totalSupply>=totalDemand){
            double supplySoFar = 0;
            for(Building building : supply){
                double production = ((BuildingStarlightProducer)building).getStarlightProduction();
                if(supplySoFar+production>=totalDemand)production = totalDemand-supplySoFar;
                ((BuildingStarlightProducer)building).produceStarlight(production);
                supplySoFar+=production;
                if(supplySoFar>=totalDemand)break;
            }
            for(Building building : demand){
                ((BuildingStarlightConsumer)building).addStarlight(((BuildingStarlightConsumer)building).getStarlightDemand());
            }
        }else{
            for(Building building : supply){
                ((BuildingStarlightProducer)building).produceStarlight(((BuildingStarlightProducer)building).getStarlightProduction());
            }
            distributeStarlight(totalSupply);
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
                        if(other instanceof StarlightNetworkSection&&!network.contains(other)){
                            if(!((StarlightNetworkSection)other).isStarlightActive())continue;
                            if(Core.distance(building, other)>POWER_TRANSFER_RADIUS)continue;
                            if(other instanceof BuildingStarlightConsumer){
                                demand.add(other);
                                foundNew = true;
                            }
                            if(other instanceof BuildingStarlightProducer){
                                supply.add(other);
                                foundNew = true;
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(supply, (Building o1, Building o2) -> {//starlight storage CAN send starlight to itself, this will give the illusion of discharge rate decreasing as its charge decreases, and never hitting zero.
            boolean r1 = ((BuildingStarlightProducer)o1).isStarlightRenewable();
            boolean r2 = ((BuildingStarlightProducer)o2).isStarlightRenewable();
            boolean s1 = o1 instanceof BuildingStarlightStorage;
            boolean s2 = o2 instanceof BuildingStarlightStorage;
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
        HashMap<BuildingStarlightConsumer, Double> demands = new HashMap<>();
        for(Building b : demand){
            demands.put((BuildingStarlightConsumer)b,((BuildingStarlightConsumer)b).getStarlightDemand());
        }
        while(starlight>0.01){
            double avg = starlight/demands.size();
            double min = avg;
            for(BuildingStarlightConsumer c : demands.keySet()){
                min = Math.min(min,demands.get(c));
            }
            for(BuildingStarlightConsumer c : demands.keySet()){
                c.addStarlight(min);
                starlight-=min;
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
                MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, 50, 40, 10, 0);
            }
            drawConnectors(b);
            GL11.glColor4d(0, .5, 1, 1);
            MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, POWER_TRANSFER_RADIUS, POWER_TRANSFER_RADIUS-5, 50, 0);
            GL11.glColor4d(0, .5, 1, 1);
        }
        for(Building b : supply){
            if(Core.debugMode){
                GL11.glColor4d(0, .3, .9, 1);
                MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, 35, 25, 10, 0);
            }
            drawConnectors(b);
            GL11.glColor4d(0, .5, 1, 1);
            MenuGame.drawTorus(b.x+b.width/2, b.y+b.height/2, POWER_TRANSFER_RADIUS, POWER_TRANSFER_RADIUS-5, 50, 0);
            GL11.glColor4d(0, .5, 1, 1);
        }
    }
    private void drawConnectors(Building b){
        for(Building other : demand){
            if(other==b)continue;
            if(Core.distance(b, other)<=POWER_TRANSFER_RADIUS){
                MenuGame.drawConnector(b.x+b.width/2, b.y+b.height/2, other.x+other.width/2, other.y+other.height/2, 10, .2, .9, .8, 0, .45, .4);
            }
        }
        for(Building other : supply){
            if(other==b)continue;
            if(Core.distance(b, other)<=POWER_TRANSFER_RADIUS){
                MenuGame.drawConnector(b.x+b.width/2, b.y+b.height/2, other.x+other.width/2, other.y+other.height/2, 10, .2, .9, .8, 0, .45, .4);
            }
        }
    }
}

package planetaryprotector.building;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import planetaryprotector.Core;
public class StarlightNetwork{
    public ArrayList<Building> demand = new ArrayList<>();
    public ArrayList<Building> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static StarlightNetwork detect(ArrayList<Building> buildings, Building building){
        if(building instanceof StarlightNetworkSection){
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
            for(Building building : demand){
                ((BuildingStarlightConsumer)building).addStarlight(((BuildingStarlightConsumer)building).getStarlightDemand());
            }
            double supp = 0;
            for(Building building : supply){
                double production = ((BuildingStarlightProducer)building).getStarlightProduction();
                supp+=production;
                if(supp>=totalDemand)production = totalDemand-supp;
                ((BuildingStarlightProducer)building).produceStarlight(production);
                if(supp>=totalDemand)break;
            }
        }else{
            distributeStarlight(totalSupply);
            for(Building building : supply){
                ((BuildingStarlightProducer)building).produceStarlight(((BuildingStarlightProducer)building).getStarlightProduction());
            }
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
                for(Building other : buildings){
                    if(other instanceof StarlightNetworkSection&&!network.contains(other)){
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
        Collections.sort(supply, new Comparator<Building>(){//starlight storage CAN send starlight to itself, this will give the illusion of discharge rate decreasing as its charge decreases, and never hitting zero.
            @Override
            public int compare(Building o1, Building o2){
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
            }
        });
    }
    private void distributeStarlight(double starlight){
        HashMap<BuildingStarlightConsumer, Double> demands = new HashMap<>();
        for(Building b : demand){
            demands.put((BuildingStarlightConsumer)b,((BuildingStarlightConsumer)b).getStarlightDemand());
        }
        while(starlight>0){
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
}

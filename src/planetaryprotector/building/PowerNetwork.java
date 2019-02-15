package planetaryprotector.building;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import planetaryprotector.Core;
public class PowerNetwork{
    public ArrayList<Building> demand = new ArrayList<>();
    public ArrayList<Building> supply = new ArrayList<>();
    private static final int POWER_TRANSFER_RADIUS = 250;
    public static PowerNetwork detect(ArrayList<Building> buildings, Building building){
        if(building instanceof PowerNetworkSection){
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
            for(Building building : demand){
                ((BuildingPowerConsumer)building).addPower(((BuildingPowerConsumer)building).getDemand());
            }
            double supp = 0;
            for(Building building : supply){
                double production = ((BuildingPowerProducer)building).getProduction();
                supp+=production;
                if(supp>=totalDemand)production = totalDemand-supp;
                ((BuildingPowerProducer)building).producePower(production);
                if(supp>=totalDemand)break;
            }
        }else{
            distributePower(totalSupply);
            for(Building building : supply){
                ((BuildingPowerProducer)building).producePower(((BuildingPowerProducer)building).getProduction());
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
                    if(other instanceof PowerNetworkSection&&!network.contains(other)){
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
        while(power>0){
            double avg = power/demands.size();
            double min = avg;
            for(BuildingPowerConsumer c : demands.keySet()){
                min = Math.min(min,demands.get(c));
            }
            for(BuildingPowerConsumer c : demands.keySet()){
                c.addPower(min);
                power-=min;
                demands.put(c,demands.get(c)-min);
                if(demands.get(c)<=.01)demands.remove(c);//satisfied demand!
            }
        }
    }
}

package planetaryprotector.event;
import planetaryprotector.building.Building;
public interface BuildingChangeEventListener{
    public void onBuildingChange(Building from, Building to);
}
package planetaryprotector.building;
public abstract class BuildingPowerConsumer extends MenuComponentBuilding{
    public double power = 0;
    public double maxPower = Integer.MAX_VALUE;
    public BuildingPowerConsumer(double x, double y, double width, double height, BuildingType type){
        super(x, y, width, height, type);
    }
    public BuildingPowerConsumer(double x, double y, double width, double height, BuildingType type, double maxPower){
        super(x, y, width, height, type);
        this.maxPower = maxPower;
    }
}
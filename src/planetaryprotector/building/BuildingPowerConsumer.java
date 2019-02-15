package planetaryprotector.building;
public interface BuildingPowerConsumer extends PowerNetworkSection{
    public abstract double getMaxPower();
    public abstract double getDemand();
    public abstract double getPower();
    public abstract void addPower(double power);
}
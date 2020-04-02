package planetaryprotector.building;
public interface BuildingPowerConsumer extends BuildingPowerUser{
    public double getMaxPower();
    public double getDemand();
    public double getPower();
    public void addPower(double power);
}
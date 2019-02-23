package planetaryprotector.building;
public interface BuildingStarlightConsumer extends StarlightNetworkSection{
    public double getMaxStarlight();
    public double getStarlightDemand();
    public double getStarlight();
    public void addStarlight(double starlight);
}
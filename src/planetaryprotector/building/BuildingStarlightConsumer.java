package planetaryprotector.building;
public interface BuildingStarlightConsumer extends StarlightNetworkSection{
    public abstract double getMaxStarlight();
    public abstract double getStarlightDemand();
    public abstract double getStarlight();
    public abstract void addStarlight(double starlight);
}
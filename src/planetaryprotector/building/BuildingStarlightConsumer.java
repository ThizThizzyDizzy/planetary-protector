package planetaryprotector.building;
public interface BuildingStarlightConsumer extends BuildingStarlightUser{
    public double getMaxStarlight();
    public double getStarlightDemand();
    public double getStarlight();
    public void addStarlight(double starlight);
}
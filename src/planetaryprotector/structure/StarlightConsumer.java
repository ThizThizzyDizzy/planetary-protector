package planetaryprotector.structure;
public interface StarlightConsumer extends StarlightUser{
    public double getMaxStarlight();
    public double getStarlightDemand();
    public double getStarlight();
    public void addStarlight(double starlight);
}
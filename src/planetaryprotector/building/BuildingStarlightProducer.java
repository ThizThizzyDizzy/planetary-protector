package planetaryprotector.building;
public interface BuildingStarlightProducer extends StarlightNetworkSection{
    public abstract double getStarlightProduction();
    /**
     * Called when starlight is used. use this to burn coal, etc.
     * @param starlight how much starlight was produced.
     */
    public abstract void produceStarlight(double starlight);
    @Deprecated
    public boolean isStarlightRenewable();
}
package planetaryprotector.structure.building;
public interface StarlightProducer extends StarlightUser{
    public double getStarlightProduction();
    /**
     * Called when starlight is used. use this to burn coal, etc.
     * @param starlight how much starlight was produced.
     */
    public void produceStarlight(double starlight);
    @Deprecated
    public boolean isStarlightRenewable();
}
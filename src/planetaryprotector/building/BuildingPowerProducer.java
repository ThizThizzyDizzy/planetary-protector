package planetaryprotector.building;
public interface BuildingPowerProducer extends PowerNetworkSection{
    public double getProduction();
    /**
     * Called when power is used. use this to burn coal, etc.
     * @param power how much power was produced.
     */
    public void producePower(double power);
    public boolean isRenewable();
}
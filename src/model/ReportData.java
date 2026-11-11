package model;

public class ReportData {
    private String name;
    private int orderCount;
    private double totalRevenue;

    public ReportData(String name, int orderCount, double totalRevenue) {
        this.name = name;
        this.orderCount = orderCount;
        this.totalRevenue = totalRevenue;
    }
    public String getName() { return name; }
    public int getOrderCount() { return orderCount; }
    public double getTotalRevenue() { return totalRevenue; }
    
    
}
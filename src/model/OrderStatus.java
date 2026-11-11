package model;
public enum OrderStatus {
PENDING("pending"),
DELIVERING("delivering"),
DELIVERED("delivered");
private final String status;
OrderStatus(String status) {
this.status = status;
    }
public String getStatus() {
return status;
    }
@Override
public String toString() {
return status;
    }
}
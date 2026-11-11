package model;

import admin.Admin;
import admin.RestAdmin;
import application.DataInitializer;
import restaurant.PremiumRestaurant;
import restaurant.Restaurant;
import java.util.*;
import java.util.Map.Entry;

public class DeliveryDataBase {
    private Admin systemAdministrator;
    private ArrayList<RestAdmin> restAdmins;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Customer> customers;
    private ArrayList<Rider> riders;
    private ArrayList<Order> orders;

    private HashMap<String, ArrayList<Order>> customerOrdersMap;
    private Hashtable<String, ArrayList<Restaurant>> customerRestaurantsMap;
    private HashMap<String, Double> customerPaymentsMap;

    public DeliveryDataBase() {
        this.restAdmins = new ArrayList<>();
        this.restaurants = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.riders = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.customerOrdersMap = new HashMap<>();
        this.customerRestaurantsMap = new Hashtable<>();
        this.customerPaymentsMap = new HashMap<>();
        this.systemAdministrator = new Admin("System Admin", "admin", "12345");
        DataInitializer.initData(this);
    }

    // --- Basic Getters ---
    public Admin getSystemAdministrator() { return systemAdministrator; }
    public ArrayList<RestAdmin> getRestAdmins() { 
        restAdmins.sort(RestAdmin::compareTo);
        return restAdmins; 
    }
    public ArrayList<Restaurant> getRestaurants() { return restaurants; }
    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Rider> getRiders() { return riders; }
    public ArrayList<Order> getOrders() { 
        Collections.sort(orders, new OrderFinalPriceComparator());
        return orders; 
    }
    public HashMap<String, Double> getCustomerPaymentsMap() { return customerPaymentsMap; }

    // --- Basic search methods ---
    public Customer findCustomerByCode(String code) throws ValidationException {
        for (Customer c : customers) {
            if (c.getCode().equals(code)) return c;
        }
        throw new ValidationException("Customer not found");
    }

    public Admin findAdminByUsername(String username) throws ValidationException {
        if (systemAdministrator.getUsername().equals(username)) return systemAdministrator;
        for (RestAdmin admin : restAdmins) {
            if (admin.getUsername().equals(username)) return admin;
        }
        throw new ValidationException("Admin not found");
    }
    
    public RestAdmin findRestAdminByCode(String code) throws ValidationException {
        for (RestAdmin admin : restAdmins) {
            if (admin.getCode().equals(code)) return admin;
        }
        throw new ValidationException("Restaurant administrator not found");
    }

    public Restaurant findRestaurantByCode(String code) throws ValidationException {
        for (Restaurant r : restaurants) {
            if (r.getCode().equals(code)) return r;
        }
        throw new ValidationException("Restaurant not found");
    }

    public Rider findRiderById(String id) throws ValidationException {
        for (Rider r : riders) {
            if (r.getId().equals(id)) return r;
        }
        throw new ValidationException("Rider not found");
    }

    public Order findOrderByCode(String code) throws ValidationException {
        for (Order o : orders) {
            if (o.getCode().equals(code)) return o;
        }
        throw new ValidationException("Order with code"+ code+"not found");
    }

    // --- Add methods ---
    public void addCustomer(Customer customer) throws ValidationException {
        if (customer == null ) throw new ValidationException("Customer cannot be null.");
        		
        try {
        	findCustomerByCodeQuietly(customer.getCode());
        	throw new ValidationException("Customer " + customer.getCode() + " already exists.");
        }catch(ValidationException e) {
        	customers.add(customer);
        }  
    }
    
    private Customer findCustomerByCodeQuietly(String code) throws ValidationException {
        for (Customer c : customers) if (c.getCode().equals(code)) return c;
        throw new ValidationException("Customer not found");
    }

    public void addRestAdmin(RestAdmin admin) throws ValidationException {
        if (admin == null) throw new ValidationException("Customer cannot be null.");
        try {
        	findAdminByUsername(admin.getUsername());
        	throw new ValidationException("RestAdmin " + admin.getCode() + " already exists.");
        }catch(ValidationException e) {
        	restAdmins.add(admin);	
        }
     }

    public void addRestaurant(Restaurant restaurant) throws ValidationException {
        if (restaurant == null) throw new ValidationException("Restaurant cannot be null.");
        
        try {
            findRestaurantByCode(restaurant.getCode());
            throw new ValidationException("Restaurant " + restaurant.getCode() + " already exists.");
        } catch (ValidationException e) {
            restaurants.add(restaurant);
        }
    }

    public void addRider(Rider rider) throws ValidationException {
        if (rider == null)throw new ValidationException("Restaurant cannot be null.");
        try{
        	findRiderById(rider.getId());
        	throw new ValidationException("Rider " + rider.getId() + " already exists.");
        }catch (ValidationException e) {
        	riders.add(rider);
        }
         
    }

    public boolean addOrder(Order order) {
        orders.add(order);
        addOrderToCustomerMap(order.getCustomerCode(), order);
        addRestaurantToCustomerMap(order.getCustomerCode(), order.getRestaurant());
        addPaymentToCustomerMap(order.getCustomerCode(), order.getFinalPrice());
        return true;
    }

    // --- Assignment functions ---
    public void addOrderToCustomerMap(String customerCode, Order order) {
        customerOrdersMap.putIfAbsent(customerCode, new ArrayList<>());
        ArrayList<Order> customerOrders = customerOrdersMap.get(customerCode);
        if (!customerOrders.contains(order)) customerOrders.add(order);
    }

    private void addRestaurantToCustomerMap(String customerCode, Restaurant restaurant) {
        customerRestaurantsMap.putIfAbsent(customerCode, new ArrayList<>());
        ArrayList<Restaurant> visitedRestaurants = customerRestaurantsMap.get(customerCode);
        if (!visitedRestaurants.contains(restaurant)) visitedRestaurants.add(restaurant);
    }

    private void addPaymentToCustomerMap(String customerCode, double price) {
        double currentTotal = customerPaymentsMap.getOrDefault(customerCode, 0.0);
        customerPaymentsMap.put(customerCode, currentTotal + price);
    }

    public ArrayList<Order> getActiveOrdersForRider(String riderId) {
        ArrayList<Order> activeOrders = new ArrayList<>();
        for (Order o : orders) {
            if (riderId.equals(o.getRiderCode()) && o.getStatus()==OrderStatus.DELIVERING) {
                activeOrders.add(o);
            }
        }
        return activeOrders;
    }

    public ArrayList<Restaurant> getPremiumRestaurantsForCustomer(Customer customer) {
        ArrayList<Restaurant> premiumList = new ArrayList<>();
        if (customer == null) return premiumList;
        ArrayList<Restaurant> visited = customerRestaurantsMap.get(customer.getCode());
        if (visited != null) {
            for (Restaurant r : visited) {
                if (r instanceof PremiumRestaurant) premiumList.add(r);
            }
        }
        return premiumList;
    }

    public Customer getCustomerWithMostOrders() {
        Customer topCustomer = null;
        int maxOrders = -1;
        for (String customerCode : customerOrdersMap.keySet()) {
            int orderCount = customerOrdersMap.get(customerCode).size();
            if (orderCount > maxOrders) {
                maxOrders = orderCount;
                try { topCustomer = findCustomerByCode(customerCode); } catch (ValidationException e) {}
            }
        }
        return topCustomer;
    }

    public Rider getRiderWithMostDeliveries() {
        Rider topRider = null;
        int maxDeliveries = -1;
        for (Rider rider : riders) {
            int deliveryCount = 0;
            for (Order o : rider.getAssignedOrders()) {
                if (o.getStatus()==OrderStatus.DELIVERED) deliveryCount++;
            }
            if (deliveryCount > maxDeliveries) {
                maxDeliveries = deliveryCount;
                topRider = rider;
            }
        }
        return topRider;
    }

    public ArrayList<Restaurant> getOpenRestaurantsByType(String type) {
        ArrayList<Restaurant> openByType = new ArrayList<>();
        for (Restaurant r : restaurants) {
            if (r.isOpen() && r.getType().equalsIgnoreCase(type)) openByType.add(r);
        }
        Collections.sort(openByType, new RestaurantRatingComparator());
        return openByType;
    }

    public void assignRiderToOrder(String riderId, String orderCode) throws ValidationException {
        Rider rider = findRiderById(riderId);
        Order order = findOrderByCode(orderCode);
        
        
        if (!rider.isAvailable()) throw new ValidationException("Rider is not available.");
        if (order.getRiderCode() != null) throw new ValidationException("Order already assigned.");
        
        order.setRiderCode(riderId);
        rider.addAssignedOrder(order); 
        order.setStatus(OrderStatus.DELIVERING);
    }

    public void assignRestaurantToRestAdmin(String adminCode, String restaurantCode) throws ValidationException {
        RestAdmin admin = findRestAdminByCode(adminCode);
        Restaurant restaurant = findRestaurantByCode(restaurantCode);

        admin.addManagedRestaurant(restaurant); 
    }
    
    public ArrayList<Restaurant> getRestaurantsByCustomer(String customerCode) {
        return customerRestaurantsMap.getOrDefault(customerCode, new ArrayList<>());
    }
    
    public ArrayList<Order> getOrdersByCustomerCode(String customerCode) {
        return customerOrdersMap.getOrDefault(customerCode, new ArrayList<>());
    }
    
    
}
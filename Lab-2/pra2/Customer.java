public class Customer {
    private static long customerCounter = 100;

    private String name;
    private String email;
    private String mobile;
    private final String customerId;

    public Customer(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        customerId = generateCustomerId();
    }

    private static String generateCustomerId() {
        customerCounter++;
        return String.format("CUST%03d", customerCounter);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCustomerId() {
        return customerId;
    }
}

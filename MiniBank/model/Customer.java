package model;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serial;
import java.io.Serializable;
import model.annotation.MaxLength;

public class Customer implements Cloneable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static long customerCounter = 100;

    @MaxLength(40)
    private String name;
    @MaxLength(254)
    private String email;
    @MaxLength(10)
    private String mobile;
    private final String customerId;
    private Address address;

    public Customer(String name) {
        this(name, "", "", null);
    }

    public Customer(String name, String email, String mobile) {
        this(name, email, mobile, null);
    }

    public Customer(String name, String email, String mobile, Address address) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.address = address;
        customerId = generateCustomerId();
    }

    private static synchronized String generateCustomerId() {
        customerCounter++;
        return String.format("CUST%03d", customerCounter);
    }

    @Serial
    private void readObject(ObjectInputStream input) throws IOException, ClassNotFoundException {
        input.defaultReadObject();
        if (customerId == null || !customerId.matches("CUST\\d{3,}")) {
            throw new InvalidObjectException("Invalid serialized customer ID.");
        }
        try {
            long loadedId = Long.parseLong(customerId.substring(4));
            synchronized (Customer.class) {
                customerCounter = Math.max(customerCounter, loadedId);
            }
        } catch (NumberFormatException exception) {
            InvalidObjectException invalid = new InvalidObjectException(
                    "Serialized customer ID is out of range.");
            invalid.initCause(exception);
            throw invalid;
        }
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

    public Address getAddress() {
        return address;
    }

    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new AssertionError(exception);
        }
    }

    public static class Address implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private final String line;
        private final String city;
        private final String pincode;

        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }
    }
}

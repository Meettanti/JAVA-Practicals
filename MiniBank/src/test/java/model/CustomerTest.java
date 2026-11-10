package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class CustomerTest {
    @Test
    void cloneIsDistinctAndRetainsCustomerDataAndImmutableAddress() {
        Customer.Address address = new Customer.Address("12 Lake Road", "Anand", "388001");
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210", address);

        Customer clonedCustomer = customer.clone();

        assertNotSame(customer, clonedCustomer);
        assertEquals(customer.getCustomerId(), clonedCustomer.getCustomerId());
        assertEquals(customer.getName(), clonedCustomer.getName());
        assertEquals(customer.getEmail(), clonedCustomer.getEmail());
        assertEquals(customer.getMobile(), clonedCustomer.getMobile());
        assertSame(address, clonedCustomer.getAddress());
    }

    @Test
    void accountKeepsItsCustomerAndLegacyNameConstructorStillWorks() {
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210");
        SavingsAccount customerAccount = new SavingsAccount(customer, 4000, 0);
        SavingsAccount legacyAccount = new SavingsAccount("Asha", 1000, 0);

        assertSame(customer, customerAccount.getCustomer());
        assertEquals("Riya", customerAccount.getOwnerName());
        assertEquals("Asha", legacyAccount.getCustomer().getName());
        assertEquals("Asha", legacyAccount.getOwnerName());
    }
}

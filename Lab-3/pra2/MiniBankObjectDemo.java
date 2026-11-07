public class MiniBankObjectDemo {
    public static void main(String[] args) {
        Account firstAccount = new Account("Riya", 4000);
        Account secondAccount = new Account(firstAccount);

        System.out.println(firstAccount);
        System.out.println("Accounts equal: " + firstAccount.equals(secondAccount));
        System.out.println("Equal hash codes: "
                + (firstAccount.hashCode() == secondAccount.hashCode()));

        Object accountObject = firstAccount;
        System.out.println("Object is an Account: " + (accountObject instanceof Account));

        Customer.Address address = new Customer.Address("12 Lake Road", "Anand", "388001");
        Customer customer = new Customer("Riya", "riya@example.com", "9876543210", address);
        Customer clonedCustomer = customer.clone();

        System.out.println("Customer ID: " + customer.getCustomerId());
        System.out.println("Address: " + customer.getAddress().getLine() + ", "
                + customer.getAddress().getCity() + " " + customer.getAddress().getPincode());
        System.out.println("Clone is a separate object: " + (customer != clonedCustomer));
        System.out.println("Cloned customer ID: " + clonedCustomer.getCustomerId());
    }
}

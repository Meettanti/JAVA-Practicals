public class NotificationDemo {
    public static void main(String[] args) {
        UrgentNotifier emailSender =
                message -> System.out.println("Email: " + message);
        Notifier smsSender =
                message -> System.out.println("SMS: " + message);
        Notifier[] senders = {emailSender, smsSender};
        String message = "Your account summary is ready.";

        for (Notifier sender : senders) {
            sender.send(message);
            if (sender instanceof Urgent) {
                sender.send(message);
            }
        }
    }
}

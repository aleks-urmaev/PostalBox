package alex.example;

import java.util.Random;

class PostalBox {
    private final UserNotificationApi notificationApi;

    private Random random = new Random();

    Order[] orders;


    public PostalBox(UserNotificationApi notificationApi, int slotsAmount) {
        this.notificationApi = notificationApi;
        this.orders = new Order[slotsAmount];
    }

    public int placeOrder(Integer orderId) throws IllegalArgumentException {
        for (int i = 0; i < orders.length; i++) {
            for (Order order : orders) {
                if (order != null && order.orderId == orderId) {
                    throw new IllegalArgumentException("Order is already in the slot orderId:" + orderId);
                }
            }

            if (orders[i] == null) {
                int receiveCode = random.nextInt(100000, 999999);
                boolean notificationDelivered = notificationApi.sendNotification(orderId, receiveCode);
                if (!notificationDelivered) {
                    throw new IllegalArgumentException("Can't deliver the notification");
                }
                orders[i] = new Order(orderId, receiveCode);
                return i;
            }
        }
        throw new IllegalArgumentException("There is no free slots in postal box");
    }

    public OrderDelivered getOrder(int receiveCode) throws IllegalArgumentException {
        for (int i = 0; i < orders.length; i++) {
            Order current = orders[i];
            if (current != null && current.code == receiveCode) {
                return new OrderDelivered(orders[i].orderId, i);
            }
        }
        throw new IllegalArgumentException("Can't find the order by code:" + receiveCode);
    }

    static class Order {
        int orderId;
        int code;

        public Order(int orderId, int code) {
            this.orderId = orderId;
            this.code = code;
        }
    }

    static class OrderDelivered {
        int orderId;
        int slotNumber;

        public OrderDelivered(int orderId, int slotNumber) {
            this.orderId = orderId;
            this.slotNumber = slotNumber;
        }
    }
}

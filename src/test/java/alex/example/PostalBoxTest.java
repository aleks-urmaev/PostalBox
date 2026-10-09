package alex.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PostalBoxTest {
    private PostalBox sut = new PostalBox(new UserNotificationStub(), 3);

    @BeforeEach
    void init() {
        sut.orders = new PostalBox.Order[3];
    }

    @Test
    void testPlaceOrderNoErrors() {
        assertDoesNotThrow(() -> sut.placeOrder(1));
    }

    @Test
    void testPlaceOrderNoSlots() {
        sut.placeOrder(1);
        sut.placeOrder(2);
        sut.placeOrder(3);

        assertThrows(IllegalArgumentException.class, () -> sut.placeOrder(4));
    }

    @Test
    void testPlaceOrderAlreadyPut() {
        sut.placeOrder(1);
        assertThrows(IllegalArgumentException.class, () -> sut.placeOrder(1));
    }

    @Test
    void testPlaceOrderNotificationError() {
        assertThrows(IllegalArgumentException.class, () -> sut.placeOrder(11));
        assertThrows(IllegalArgumentException.class, () -> sut.placeOrder(12));
        assertThrows(IllegalArgumentException.class, () -> sut.placeOrder(13));
        assertDoesNotThrow(() -> sut.placeOrder(1));
    }

    @Test
    void testGetOrderNoOrder() {
        assertThrows(IllegalArgumentException.class, () -> sut.getOrder(1));
    }

    private static class UserNotificationStub implements UserNotificationApi {
        @Override
        public boolean sendNotification(int orderId, int receiveCode) {
            if (orderId < 10) {
                return true;
            } else {
                return false;
            }
        }
    }
}
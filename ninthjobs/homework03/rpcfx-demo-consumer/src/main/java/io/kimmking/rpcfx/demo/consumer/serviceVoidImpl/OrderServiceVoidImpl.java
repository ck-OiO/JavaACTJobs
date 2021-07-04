package io.kimmking.rpcfx.demo.consumer.serviceVoidImpl;

import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceVoidImpl implements OrderService {
    @Override
    public Order findOrderById(int id) {
        return null;
    }
}

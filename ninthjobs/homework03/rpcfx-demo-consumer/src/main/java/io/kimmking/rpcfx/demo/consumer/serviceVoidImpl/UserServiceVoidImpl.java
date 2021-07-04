package io.kimmking.rpcfx.demo.consumer.serviceVoidImpl;

import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceVoidImpl implements UserService {
    @Override
    public User findById(int id) {
        return null;
    }
}

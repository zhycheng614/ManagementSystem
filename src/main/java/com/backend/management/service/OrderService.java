package com.backend.management.service;

import com.backend.management.exception.MaintenanceAlreadyExistException;
import com.backend.management.exception.OrderNotExistException;
import com.backend.management.model.Order;
import com.backend.management.model.User;
import com.backend.management.repository.OrderRepository;
import com.backend.management.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    public List<Order> listByTenant(String username){
        return orderRepository.findByUser(new User.Builder().setUsername(username).build());
    }
    public Order findByIdAndTenant(Long orderId, String username) throws OrderNotExistException {
        Order order = orderRepository.findByIdAndUser(orderId,new User.Builder().setUsername(username).build());
        if(order == null){
            throw new OrderNotExistException("Order DNE");
        }
        return order;
    }
    public void add(Order order){
        if(orderRepository.existsById(order.getTenantNumber())){
            throw new MaintenanceAlreadyExistException("Maintenance already exist");
        }
        order.setStatus("submitted");
        order.setSubmittedDate(LocalDate.now());
        orderRepository.save(order);
    }
    @Transactional
    public void delete(Long orderId, String username)throws OrderNotExistException{
        Order order = orderRepository.findByIdAndUser(orderId,new User.Builder().setUsername(username).build());
        if(order == null){
            throw new OrderNotExistException("Order DNE");
        }
        orderRepository.deleteById(orderId);
    }

//  provider p0 feature
    public List<Order> listOfUnclaimed(){
        return orderRepository.findAllByStatus("submitted");

    }
    @Transactional
    public void claimTask(Long id, String provider_username, String note) {
        Order task = orderRepository.getById(id);
        task.setStatus(note);
        task.setProvider(provider_username);
        task.setProcessedDate(LocalDate.now());
    }
    @Transactional
    public void completeTask(Long id) {
        Order task = orderRepository.getById(id);
        task.setStatus("complete");
        task.setProviderNote("it's fixed!");
        task.setCompleteDate(LocalDate.now());
    }


    public List<Order> listByProvider(String username){
        return orderRepository.findAllByProvider(username);
    }

    public List<Order> listByManager(String username){
        return orderRepository.findAllByManager(username);
    }

    public List<Order> listCompleteByProvider(String username){
        return orderRepository.findAllCompleteByProvider(username,"complete");
    }
    // manager p0
    public List<Order> listOfAll(){
        return orderRepository.findAll();
    }
}

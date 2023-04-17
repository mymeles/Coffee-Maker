package edu.ncsu.csc.CoffeeMaker.models;

import java.util.List;

public class Orders {
    Long        id;
    List<Order> orders;

    public Orders () {
        orders = null;
    }

    public Orders ( final List<Order> orders ) {
        setOrders( orders );
    }

    public Long getId () {
        return id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public List<Order> getOrders () {
        return orders;
    }

    public void setOrders ( final List<Order> orders ) {
        this.orders = orders;
    }

    public List<Order> getOrdersByUser ( final String username ) {
        final List<Order> temp = orders;

        for ( int i = 0; i < temp.size(); ++i ) {
            if ( !temp.get( i ).getOwnerName().equals( username ) ) {
                temp.remove( i );
            }
        }
        return temp;
    }

    public boolean addOrders ( final List<Order> o ) {
        for ( int i = 0; i < o.size(); ++i ) {
            orders.add( o.get( i ) );
        }
        return true;

    }

    public boolean addOrder ( final Order o ) {
        return orders.add( o );
    }

    @Override
    public String toString () {
        return "";
    }

    @Override
    public int hashCode () {
        return 0;
    }

    @Override
    public boolean equals ( final Object obj ) {
        return false;
    }
}

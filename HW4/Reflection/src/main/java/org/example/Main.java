package org.example;


public class Main {
    public static void main(String[] args) {
        Person person = new Person(1, "John", 25);
        Vehicle vehicle = new Vehicle("Toyota", 2010, 10000.0, 1, person);
        Car car = new Car("Toyota", 2010, 10000.0, 1, person, 5, false, true);

        DeepCopy deepCopy = new DeepCopy();
        Person personCopy = (Person) deepCopy.clone(person);
        Vehicle vehicleCopy = (Vehicle) deepCopy.clone(vehicle);
        Car carCopy = (Car) deepCopy.clone(car);

    }
}
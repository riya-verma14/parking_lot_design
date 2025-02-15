import java.util.ArrayList;
import java.util.List;
import java.time.Duration;
import java.time.LocalDateTime;

abstract class Payment {
    public abstract double calculateCost(double hours);
}

class CarPayment extends Payment {
    @Override
    public double calculateCost(double hours) {
        return hours * 2;
    }
}

class BikePayment extends Payment {
    @Override
    public double calculateCost(double hours) {
        return hours * 1;
    }
}

class HandicappedPayment extends Payment {
    @Override
    public double calculateCost(double hours) {
        return 0;
    }
}

abstract class Vehicle {
    protected Payment payment;
    private LocalDateTime parkedTime;

    public abstract String getType();

    public double calculateCost(double hours) {
        return payment.calculateCost(hours);
    }

    public void setParkedTime() {
        parkedTime = LocalDateTime.now();
    }

    public LocalDateTime getParkedTime() {
        return parkedTime;
    }
}

class Car extends Vehicle {
    public Car() {
        payment = new CarPayment();
    }

    @Override
    public String getType() {
        return "Car";
    }
}

class Bike extends Vehicle {
    public Bike() {
        payment = new BikePayment();
    }

    @Override
    public String getType() {
        return "Bike";
    }
}

class HandicappedVehicle extends Vehicle {
    public HandicappedVehicle() {
        payment = new HandicappedPayment();
    }

    @Override
    public String getType() {
        return "Handicapped";
    }
}

class ParkingLot {
    private List<List<List<Vehicle>>> spots;
    private int floors;
    private int rows;
    private int spotsPerRow;

    public ParkingLot(int floors, int rows, int spotsPerRow) {
        this.floors = floors;
        this.rows = rows;
        this.spotsPerRow = spotsPerRow;
        spots = new ArrayList<>();
        for (int i = 0; i < floors; i++) {
            List<List<Vehicle>> floor = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                List<Vehicle> row = new ArrayList<>();
                for (int k = 0; k < spotsPerRow; k++) {
                    row.add(null);
                }
                floor.add(row);
            }
            spots.add(floor);
        }
    }

    public boolean park(Vehicle v, int floor, int row, int spot) {
        if (spots.get(floor).get(row).get(spot) == null) {
            spots.get(floor).get(row).set(spot, v);
            System.out.println(v.getType() + " parked successfully at floor " + floor + ", row " + row + ", spot " + spot + ".");
            return true;
        } else {
            System.out.println("Spot already occupied.");
            return false;
        }
    }

    public boolean leave(Vehicle v) {
        for (int i = 0; i < floors; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < spotsPerRow; k++) {
                    if (spots.get(i).get(j).get(k) == v) {
                        double hours = calculateHoursParked(v);
                        double cost = v.calculateCost(hours);
                        spots.get(i).get(j).set(k, null);
                        System.out.println(v.getType() + " left successfully. Total cost: " + cost);
                        return true;
                    }
                }
            }
        }
        System.out.println(v.getType() + " not found.");
        return false;
    }

    public int availableSpots(int floor) {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < spotsPerRow; j++) {
                if (spots.get(floor).get(i).get(j) == null) {
                    count++;
                }
            }
        }
        return count;
    }

    public int handicappedSpots(int floor) {
        int count = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < spotsPerRow; j++) {
                if (spots.get(floor).get(i).get(j) instanceof HandicappedVehicle) {
                    count++;
                }
            }
        }
        return count;
    }

    public double calculateHoursParked(Vehicle v) {
        for (int i = 0; i < floors; i++) {
            for (int j = 0; j < rows; j++) {
                for (int k = 0; k < spotsPerRow; k++) {
                    if (spots.get(i).get(j).get(k) == v) {
                        LocalDateTime now = LocalDateTime.now();
                        LocalDateTime parkedTime = v.getParkedTime();
                        Duration duration = Duration.between(parkedTime, now);
                        return duration.toHours();
                    }
                }
            }
        }
        return 0;
    }
}

public class parking_lot_design {
    public static void main(String[] args) {
        ParkingLot lot = new ParkingLot(3, 10, 20);

        Car car1 = new Car();
        Car car2 = new Car();
        Bike bike1 = new Bike();
        Bike bike2 = new Bike();
        HandicappedVehicle hv1 = new HandicappedVehicle();

        car1.setParkedTime();
        lot.park(car1, 0, 0, 0);
        car2.setParkedTime();
        lot.park(car2, 0, 0, 1);
        bike1.setParkedTime();
        lot.park(bike1, 0, 0, 2);
        hv1.setParkedTime();
        lot.park(hv1, 2, 9, 19);

        System.out.println("Available spots on floor 0: " + lot.availableSpots(0));
        System.out.println("Handicapped spots on floor 2: " + lot.handicappedSpots(2));

        lot.leave(car1);
        lot.leave(bike2);

        System.out.println("Available spots on floor 0: " + lot.availableSpots(0));
    }
}
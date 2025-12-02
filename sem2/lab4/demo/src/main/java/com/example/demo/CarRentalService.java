package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarRentalService {

    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public CarRentalService(CarRepository carRepository, RentalRepository rentalRepository) {
        this.carRepository = carRepository;
        this.rentalRepository = rentalRepository;
        initializeCars();
    }

    private void initializeCars() {
        if (carRepository.count() == 0) {
            carRepository.save(new Car("Toyota", "Camry", 2022, "Silver", 2500));
            carRepository.save(new Car("BMW", "X5", 2023, "Black", 5000));
            carRepository.save(new Car("Honda", "Civic", 2021, "White", 2000));
        }
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getAvailableCars() {
        return carRepository.findByIsAvailableTrue();
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getUserRentals(String userLogin) {
        if (userLogin == null) {
            return List.of();
        }
        return rentalRepository.findByUserLogin(userLogin);
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findByIsActiveTrue();
    }

    public boolean rentCar(Long carId, String customerName, String customerPhone,
                           String customerEmail, String userLogin, int rentalDays) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent() && optionalCar.get().getIsAvailable()) {
            Car car = optionalCar.get();
            car.setIsAvailable(false);
            carRepository.save(car);

            Rental rental = new Rental(car, customerName, customerPhone, customerEmail, userLogin, rentalDays);
            rentalRepository.save(rental);
            return true;
        }
        return false;
    }

    public void addCar(Car car) {
        carRepository.save(car);
    }

    public boolean updateCar(Long carId, Car updatedCar) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setBrand(updatedCar.getBrand());
            car.setModel(updatedCar.getModel());
            car.setYear(updatedCar.getYear());
            car.setColor(updatedCar.getColor());
            car.setPricePerDay(updatedCar.getPricePerDay());
            carRepository.save(car);
            return true;
        }
        return false;
    }

    public boolean deleteCar(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent() && optionalCar.get().getIsAvailable()) {
            carRepository.deleteById(carId);
            return true;
        }
        return false;
    }

    public boolean returnCar(Long rentalId) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.setActive(false);
            rental.getCar().setIsAvailable(true);
            rentalRepository.save(rental);
            return true;
        }
        return false;
    }

    public boolean deleteRental(Long rentalId) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            rental.getCar().setIsAvailable(true);
            rentalRepository.delete(rental);
            return true;
        }
        return false;
    }

    // НОВЫЙ МЕТОД: Отмена заказа клиентом
    public boolean cancelRental(Long rentalId, String userLogin) {
        Optional<Rental> optionalRental = rentalRepository.findById(rentalId);
        if (optionalRental.isPresent()) {
            Rental rental = optionalRental.get();
            // Проверяем, что отменяет владелец заказа и заказ еще активен
            if (rental.getUserLogin().equals(userLogin) && rental.isActive()) {
                rental.setActive(false);
                rental.getCar().setIsAvailable(true);
                rentalRepository.save(rental);
                return true;
            }
        }
        return false;
    }

    public Optional<Car> findCarById(Long carId) {
        return carRepository.findById(carId);
    }

    public Optional<Rental> findRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId);
    }
}
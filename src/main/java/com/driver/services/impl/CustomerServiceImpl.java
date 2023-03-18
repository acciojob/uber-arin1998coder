package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Autowired
	CabRepository cabRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		Customer customer = customerRepository2.findById(customerId).get();
		customerRepository2.delete(customer);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query

		//get all the drivers
		List<Driver> driverList = driverRepository2.findAll();
		Cab cab=null;
		Driver driver=null;
		//find the first availbale cab and driver
		for(Driver driver1:driverList){
			if(driver1.getCab().getAvailable()) {
				driver = driver1;
				driver.getCab().setAvailable(false);
				break;
			}
		}
		if(driver==null) throw new Exception("No cab available!");

		TripBooking tripBooking = new TripBooking();
		//find the customer using the customer ID
		Customer customer = customerRepository2.findById(customerId).get();
		//set the trip booking attributes
		int ratePKm =driver.getCab().getPerKmRate();
		int bill = ratePKm * distanceInKm;

		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setBill(bill);
		tripBooking.setDistanceInKm(distanceInKm);

		tripBooking.setDriver(driver);
		tripBooking.setCustomer(customer);

		driver.getTripBookingList().add(tripBooking);
		customer.getTripBookingList().add(tripBooking);

		driverRepository2.save(driver);

		return tripBooking;

//		this.tripBookingId = tripBookingId;
//		this.fromLocation = fromLocation;
//		this.toLocation = toLocation;
//		this.distanceInKm = distanceInKm;
//		this.status = status;
//		this.bill = bill;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		//find the cab of the driver
		int cabId = tripBooking.getDriver().getCab().getId();
		Cab cab = cabRepository2.findById(cabId).get();
		//set the availability of the driver's cab as true
		cab.setAvailable(true);
		tripBooking.setStatus(TripStatus.CANCELED);
		driverRepository2.save(tripBooking.getDriver());
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		//find the cab of the driver
		int cabId = tripBooking.getDriver().getCab().getId();
		Cab cab = cabRepository2.findById(cabId).get();
		//set the availability of the driver's cab as true
		cab.setAvailable(true);
		tripBooking.setStatus(TripStatus.COMPLETED);
		driverRepository2.save(tripBooking.getDriver());

	}
}

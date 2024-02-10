package com.imaginnovate.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imaginnovate.model.Employee;
import com.imaginnovate.model.TaxDetails;
import com.imaginnovate.repository.EmployeeRepository;
import com.imaginnovate.service.EmployeeService;

import java.util.Date;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void storeEmployeeDetails(Employee employee) {
        if (!isValidEmployee(employee)) {
            throw new IllegalArgumentException("Invalid employee data");
        }

        employeeRepository.save(employee);
    }

    public TaxDetails calculateTaxDeduction(String employeeId) {
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }

        double yearlySalary = calculateYearlySalary(employee);
        double taxAmount = calculateTaxAmount(yearlySalary);
        double cessAmount = calculateCessAmount(yearlySalary);

        return new TaxDetails(employeeId, employee.getFirstName(), employee.getLastName(),
                yearlySalary, taxAmount, cessAmount);
    }

    private boolean isValidEmployee(Employee employee) {
        return employee.getEmployeeId() != null && !employee.getEmployeeId().isEmpty()
                && employee.getFirstName() != null && !employee.getFirstName().isEmpty()
                && employee.getLastName() != null && !employee.getLastName().isEmpty()
                && employee.getEmail() != null && !employee.getEmail().isEmpty()
                && employee.getPhoneNumbers() != null && !employee.getPhoneNumbers().isEmpty()
                && employee.getDoj() != null
                && employee.getSalary() > 0;
    }

    private double calculateYearlySalary(Employee employee) {
        long monthsOfWork = calculateMonths(employee.getDoj(), new Date());
        double totalSalary = employee.getSalary() * monthsOfWork;
        return totalSalary;
    }

    private long calculateMonths(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff / (30 * 24 * 60 * 60 * 1000); // Assuming each month has 30 days
    }

    private double calculateTaxAmount(double yearlySalary) {
        double taxAmount = 0;
        if (yearlySalary > 1000000) {
            taxAmount = 0.2 * (yearlySalary - 1000000);
            taxAmount += 0.1 * 500000; // 10% tax on 500000
            taxAmount += 0.05 * 250000; // 5% tax on 250000
        } else if (yearlySalary > 500000) {
            taxAmount = 0.1 * (yearlySalary - 500000);
            taxAmount += 0.05 * 250000; // 5% tax on 250000
        } else if (yearlySalary > 250000) {
            taxAmount = 0.05 * (yearlySalary - 250000);
        }
        return taxAmount;
    }

    private double calculateCessAmount(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return 0.02 * (yearlySalary - 2500000);
        }
        return 0;
    }
}

package com.imaginnovate.service;

import com.imaginnovate.model.Employee;
import com.imaginnovate.model.TaxDetails;

public interface EmployeeService {

	void storeEmployeeDetails(Employee employee);

	TaxDetails calculateTaxDeduction(String employeeId);

}

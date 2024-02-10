package com.imaginnovate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imaginnovate.model.Employee;
import com.imaginnovate.model.TaxDetails;
import com.imaginnovate.service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@PostMapping("/store")
	public ResponseEntity<String> storeEmployeeDetails(@RequestBody Employee employee) {
		try {
			employeeService.storeEmployeeDetails(employee);
			return ResponseEntity.ok("Employee details stored successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@GetMapping("/tax-deduction/{employeeId}")
	public ResponseEntity<TaxDetails> getTaxDeduction(@PathVariable String employeeId) {
		try {
			TaxDetails taxDetails = employeeService.calculateTaxDeduction(employeeId);
			return ResponseEntity.ok(taxDetails);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}
	}
}
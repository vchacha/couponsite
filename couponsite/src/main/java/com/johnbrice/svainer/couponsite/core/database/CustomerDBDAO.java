package com.johnbrice.svainer.couponsite.core.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.CustomerDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

/**
 * Implements CRUD operations for {@code CustomerDO} 
 * All methods creates connection to database, in the end of method connection is closed. 
 * This class work with Customer table and Customer_Coupon table in database
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *  
 */

public class CustomerDBDAO implements CustomerDAO {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Connection connection;

	public CustomerDBDAO() {
		this.connection = ConnectionManagerPool.getInstance().returnConnection();
	}

	/** 
	 * this method inserts Customer to Customer table in database
	 * builds query to insert to database
	 * creates connection to database
	 * @param  customerDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to insert customer
	 * @return integer
	 * 		   number of insert records
	 *   */
	@Override
	public int createCustomer(CustomerDO customerDO) {
		try {
			Statement statement = connection.createStatement();
			String insertQuery = buildCustomerInsertQuery(customerDO);
			int numberOfInsertRecords = statement.executeUpdate(insertQuery);
			statement.close();
			return numberOfInsertRecords;
		} catch (SQLException e) {
			throw new DAOException(
					"Wasn't able to inset customer: " + "\n" + customerDO.toString() + "\n" + e.getMessage());
		}
	}
	
	/** 
	 * this method removes Customer from Customer table in database
	 * builds query to insert to database, searches Customer by ID
	 * creates connection to database
	 * @param  customerDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to remove customer
	 * @return integer
	 * 		   number of remove records
	 *   */
	@Override
	public int removeCustomer(CustomerDO customerDO) {
		try {
			Statement statement = connection.createStatement();
			int numberOfRemoveRecords = statement.executeUpdate("DELETE FROM Customer WHERE customer_id = " + customerDO.getCustomerId());
			statement.close();
			return numberOfRemoveRecords;
			} catch (SQLException e) {
			throw new DAOException(
					"wasn't able to remove customer ..." + customerDO.toString() + "\n" + e.getMessage());
		}
	}

	/** 
	 * this method updates all Customer parameters in Customer table in database except customerId
	 * builds query to insert to database, searches Customer by ID
	 * creates connection to database
	 * @param  customerDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to update customer
	 * @return integer
	 * 		   number of update records
	 *   */
	@Override
	public int updateCustomer(CustomerDO customerDO) {
		try {
			Statement statement = connection.createStatement();
			String updateQuery = updateCustomerInsertQuery(customerDO.getCustomerId(), customerDO.getCustomerName(),
					customerDO.getPassword(), customerDO.getEmail());
			int numberOfUpdateRecords = statement.executeUpdate(updateQuery);
			statement.close();
			return numberOfUpdateRecords;
		} catch (SQLException e) {
			String errorMessage = "Wasn't able to update customer ..." + customerDO.toString() + "\n" + e.getMessage();
			System.err.println(errorMessage);
			throw new DAOException(errorMessage);
		}
	}

	/** 
	 * this method gets Customer parameters from Customer table in database
	 * builds query to insert to database, searches Customer by ID
	 * creates connection to database
	 * @param  long
	 * 		   customerId
	 * @throws DAOException
	 * 		   if wasn't able to find customer
	 * @return Customer
	  */
	@Override
	public CustomerDO getCustomer(long customerId) {
		Statement statement = null;
		CustomerDO customerDO = null;
		try {
			statement = connection.createStatement();
			String customerQuery = "SELECT * FROM customer where customer_id = " + customerId;
			ResultSet resultSet = statement.executeQuery(customerQuery);
			while (resultSet.next()) {
				customerDO = new CustomerDO(Integer.parseInt(resultSet.getString("CUSTOMER_ID")),
						resultSet.getString("CUSTOMER_NAME"), resultSet.getString("PASSWORD"),
						resultSet.getString("EMAIL"));
			}

		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find customerID ..." + customerId + "\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return customerDO;
	}

	/** 
	 * this method gets Collection of Customers from Customer table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to find any customer
	 * @return Collection of Customers
	  */
	@Override
	public Collection<CustomerDO> getAllCustomers() {
		Statement statement = null;
		Collection<CustomerDO> customers = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String customerQuery = "SELECT customer_Id, customer_Name, password, email FROM customer";
			ResultSet resultSet = statement.executeQuery(customerQuery);
			while (resultSet.next()) {
				CustomerDO tempCustomerDO = new CustomerDO(Integer.parseInt(resultSet.getString("CUSTOMER_ID")),resultSet.getString("CUSTOMER_NAME"), resultSet.getString("PASSWORD"), resultSet.getString("EMAIL"));
				customers.add(tempCustomerDO);
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find any customer list ...\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return customers;
	}
	/** 
	 * this method inserts Coupon Id and customer Id (that wants to purchase this coupon) to table Customer_Coupon in database
	 * builds query to insert to database
	 * creates connection to database
	 * @param  couponDO
	 * 
	 * @param  long
	 * 		   customerID
	 * @throws DAOException
	 * 		   if wasn't able to purchase coupon
	 * @return integer
	 * 		   number of purchase coupon
	 *   */
	@Override
	public int purchaseCoupon(CouponDO couponDO, long customerID) {
		Statement statement = null;
		try {
			statement = connection.createStatement();

			String query = ("INSERT INTO Customer_Coupon VALUES(" + customerID + "," + couponDO.getCouponId() + ")");
			int numberOfPurchaseCoupon = statement.executeUpdate(query); 
			if (numberOfPurchaseCoupon > 0){
				int tempAmount = couponDO.getAmount() - 1;
				String queryAmount = ("UPDATE Coupon SET Amount = " + tempAmount + " WHERE Coupon_id = " + couponDO.getCouponId());
				statement.executeUpdate(queryAmount);
			}
			statement.close();
		return numberOfPurchaseCoupon;
		} catch (SQLException e) {
			throw new DAOException(
					"Wasn't able to purchase coupon: " + "\n" + couponDO.toString() + "\n" + e.getMessage());
			}
	}
	
	/** 
	 * this method returns Collection of Coupons that was purchased by customer
	 * searches them by customerId in table Customer_Coupon
	 * but all Coupon parameters data are at Coupon table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 
	 * @param  long customerID
	 * @throws DAOException
	 * 		   if wasn't able to find coupon list
	 * @return Collection of Coupons
	 * */
	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByCustomer(long customerId) {
		Statement statement = null;
		List<String> couponIds = new ArrayList<>();
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();
			String couponIdsQuery = ("SELECT distinct coupon_id FROM customer_coupon where customer_Id = " + customerId + ";");
			ResultSet resultSetForCouponIds = statement.executeQuery(couponIdsQuery);
			while (resultSetForCouponIds.next()) {
				couponIds.add(resultSetForCouponIds.getString("Coupon_ID"));
				}
			String couponQuery = ("SELECT * FROM Coupon WHERE COUPON_ID IN(" + String.join(",", couponIds) + ")");
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				CouponDO tempCouponDO = new CouponDO(Long.parseLong(resultSet.getString("Coupon_ID")),
						Long.parseLong(resultSet.getString("Company_ID")), resultSet.getString("Title"),
						convertStringToDate(resultSet.getString("Start_Date")),
						convertStringToDate(resultSet.getString("End_Date")),
						Integer.parseInt(resultSet.getString("Amount")), Type.valueOf(resultSet.getString("Type")),
						resultSet.getString("Message"), Double.parseDouble(resultSet.getString("Price")),
						resultSet.getString("Image"));
				coupons.add(tempCouponDO);
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find any coupon list ...\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return coupons;
	}

	/** 
	 * this method returns Collection of Coupons with some Type and that was purchased by customer
	 * searches them by customerId in table Customer_Coupon
	 * but all Coupon parameters data are at Coupon table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 
	 * @param  long
	 * 		   customerID
	 * @param  Type
	 * 		   type of Coupon
	 * @throws DAOException
	 * 		   if wasn't able to find coupon list
	 * @return Collection of Coupons
	 **/
	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByType(long customerId, Type type) {
		Statement statement = null;
		List<String> couponIds = new ArrayList<>();
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();
			String couponIdsQuery = ("SELECT distinct coupon_id FROM customer_coupon where customer_Id = " + customerId + ";");
			ResultSet resultSetForCouponIds = statement.executeQuery(couponIdsQuery);
			while (resultSetForCouponIds.next()) {
				couponIds.add(resultSetForCouponIds.getString("Coupon_ID"));
				}
			String couponQuery = ("SELECT * FROM Coupon WHERE Type = " + '"' + type + '"' + "and COUPON_ID IN(" + String.join(",", couponIds) + ")");
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				CouponDO tempCouponDO = new CouponDO(Long.parseLong(resultSet.getString("Coupon_ID")),
						Long.parseLong(resultSet.getString("Company_ID")), resultSet.getString("Title"),
						convertStringToDate(resultSet.getString("Start_Date")),
						convertStringToDate(resultSet.getString("End_Date")),
						Integer.parseInt(resultSet.getString("Amount")), Type.valueOf(resultSet.getString("Type")),
						resultSet.getString("Message"), Double.parseDouble(resultSet.getString("Price")),
						resultSet.getString("Image"));
				coupons.add(tempCouponDO);
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find any coupon list ...\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return coupons;
	}
	
	/** 
	 * this method returns Collection of Coupons with some Price and that was purchased by customer
	 * searches them by customerId in table Customer_Coupon
	 * but all Coupon parameters data are at Coupon table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 
	 * @param  long
	 * 		   customerID
	 * @param  double
	 * 		   price
	 * @throws DAOException
	 * 		   if wasn't able to find coupon list
	 * @return Collection of Coupons
	 *   */
	@Override
	public Collection<CouponDO> getAllPurchaseCouponsByPrice(long customerId, double price) {
		Statement statement = null;
		List<String> couponIds = new ArrayList<>();
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();
			String couponIdsQuery = ("SELECT distinct coupon_id FROM customer_coupon where customer_Id = " + customerId + ";");
			ResultSet resultSetForCouponIds = statement.executeQuery(couponIdsQuery);
			while (resultSetForCouponIds.next()) {
				couponIds.add(resultSetForCouponIds.getString("Coupon_ID"));
				}
			String couponQuery = ("SELECT * FROM Coupon WHERE Price = " + price + "and COUPON_ID IN(" + String.join(",", couponIds) + ")");
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				CouponDO tempCouponDO = new CouponDO(Long.parseLong(resultSet.getString("Coupon_ID")),
						Long.parseLong(resultSet.getString("Company_ID")), resultSet.getString("Title"),
						convertStringToDate(resultSet.getString("Start_Date")),
						convertStringToDate(resultSet.getString("End_Date")),
						Integer.parseInt(resultSet.getString("Amount")), Type.valueOf(resultSet.getString("Type")),
						resultSet.getString("Message"), Double.parseDouble(resultSet.getString("Price")),
						resultSet.getString("Image"));
				coupons.add(tempCouponDO);
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find any coupon list ...\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return coupons;
	}	
	/** 
	 * this method return true then find in Customer table in database Customer with CustomerId and Password, else false
	 * builds query to insert to database, searches Customer by CustomerId and verify Password
	 * creates connection to database
	 * 		   
	 * @param  long
	 * 		   customerId
	 * @param  String
	 * 		   password 
	 * @throws DAOException
	 * 		   Company id or/and password are not exist in database
	 * @return boolean
	  */	
	@Override
	public boolean login(long customerId, String password) {
		Statement statement = null;
		String tempCustomerName = null;
		String tempPassword = null;
		try {
			statement = connection.createStatement();

			String loginQuery = "SELECT Customer_Id, password from customer where Customer_Id = '" + customerId
					+ "' and password = '" + password + "'";
			ResultSet resultSet = statement.executeQuery(loginQuery);
			while (resultSet.next()) {

				tempCustomerName = resultSet.getString("Customer_Id");
				tempPassword = resultSet.getString("Password");
			}
		} catch (SQLException e) {
			throw new DAOException(
					"Customer id or/and password are not valid! Please try again... \n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (tempCustomerName != null & tempPassword != null) {
			return true;
		} else {
			return false;
		}
	}

	private String buildCustomerInsertQuery(CustomerDO customerDO) {
		StringBuilder sb = new StringBuilder("INSERT INTO Customer VALUES(");
		sb.append(String.valueOf(customerDO.getCustomerId()));
		sb.append(",");
		sb.append(surroundWithCommas(customerDO.getCustomerName()));
		sb.append(",");
		sb.append(surroundWithCommas(customerDO.getPassword()));
		sb.append(",");
		sb.append(surroundWithCommas(customerDO.getEmail()));
		sb.append(")");
		return sb.toString();
	}

	private String surroundWithCommas(String string) {
		return "'" + string + "'";
	}

	private String updateCustomerInsertQuery(long customerId, String customerName, String password, String email) {
		StringBuilder sb = new StringBuilder("Update Customer SET Customer_Name = ");
		sb.append(surroundWithCommas(customerName));
		sb.append(", Password = ");
		sb.append(surroundWithCommas(password));
		sb.append(", Email = ");
		sb.append(surroundWithCommas(email));
		sb.append("WHERE Customer_Id = " + customerId);
		return sb.toString();
	}

	private Date convertStringToDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			throw new DAOException("cannot parse date: " + date + " from coupon table: " + "\n" + e.getMessage()); 
		}		
	}

	
	
}

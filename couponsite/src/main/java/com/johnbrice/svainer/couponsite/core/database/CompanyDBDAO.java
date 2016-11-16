package com.johnbrice.svainer.couponsite.core.database;

import java.sql.Connection;
import java.util.Date;

import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.model.CompanyDO;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Implements CRUD operations for {@code CompanyDO} 
 * All methods creates connection to database, in the end of method connection is closed. 
 * This class work with Company table and Customer_Coupon table in database
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *  
 */

public class CompanyDBDAO implements CompanyDAO {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Connection connection;

	public CompanyDBDAO() {
		this.connection = ConnectionManagerPool.getInstance().returnConnection();
	}

	/** 
	 * this method inserts Company to Company table in database
	 * builds query to insert to database
	 * creates connection to database
	 * @param  companyDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to insert company
	 * @return integer
	 * 		   number of insert records
	 *   */
	@Override
	public int createCompany(CompanyDO companyDO) {
		try {
			Statement statement = connection.createStatement();
			String insertQuery = buildCompanyInsertQuery(companyDO);
			int numberOfInsertRecords = statement.executeUpdate(insertQuery);
			statement.close();
			return numberOfInsertRecords;
		} catch (SQLException e) {
			throw new DAOException(
					"Wasn't able to inset company: " + "\n" + companyDO.toString() + "\n" + e.getMessage());
		}
	}

	/** 
	 * this method removes Company from Company table in database
	 * builds query to insert to database, searches Company by ID
	 * creates connection to database
	 * @param  companyDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to remove company
	 * @return integer
	 * 		   number of remove records
	 *   */
	@Override
	public int removeCompany(CompanyDO companyDO) {
		try {
			Statement statement = connection.createStatement();
			int numberOfRemoveRecords = statement.executeUpdate("DELETE FROM Company WHERE company_id = " + companyDO.getCompanyId());
			
			//TODO couponDAO.removeCouponsByCompany(long companyId);
			
			statement.close();
			return numberOfRemoveRecords;
			} catch (SQLException e) {
			throw new DAOException(
					"wasn't able to remove company ..." + companyDO.toString() + "\n" + e.getMessage());
		}
	}

	/** 
	 * this method updates all Company parameters in Company table in database except companyId
	 * builds query to insert to database, searches Company by ID
	 * creates connection to database
	 * @param  companyDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to update company
	 * @return integer
	 * 		   number of update records
	 *   */
	@Override
	public int updateCompany(CompanyDO companyDO) {
		try {
			Statement statement = connection.createStatement();
			String updateQuery = updateCompanyInsertQuery(companyDO.getCompanyId(), companyDO.getCompanyName(),
					companyDO.getPassword(), companyDO.getEmail());
			int numberOfUpdateComapnies = statement.executeUpdate(updateQuery);
			statement.close();
			return numberOfUpdateComapnies;
		} catch (SQLException e) {
			String errorMessage = "Wasn't able to update company ..." + companyDO.toString() + "\n" + e.getMessage();
			throw new DAOException(errorMessage);
		}
	}
	
	/** 
	 * this method gets Company parameters from Company table in database
	 * builds query to insert to database, searches Company by ID
	 * creates connection to database
	 * @param  long
	 * 		   companyId
	 * @throws DAOException
	 * 		   if wasn't able to find company
	 * @return Company
	  */
	@Override
	public CompanyDO getCompany(long companyId) {
		Statement statement = null;
		CompanyDO companyDO = null;
		try {
			statement = connection.createStatement();
			String companyQuery = "SELECT * FROM company where company_id = " + companyId;
			ResultSet resultSet = statement.executeQuery(companyQuery);
			while (resultSet.next()) {
				companyDO = new CompanyDO(Integer.parseInt(resultSet.getString("COMPANY_ID")),
						resultSet.getString("COMPANY_NAME"), resultSet.getString("PASSWORD"),
						resultSet.getString("EMAIL"));
			}

		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find companyID ..." + companyId + "\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return companyDO;
	}

	/** 
	 * this method returns Collection of Companies from Company table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to find any company
	 * @return Collection of Companies
	  */
	@Override
	public Collection<CompanyDO> getAllCompanies() {
		Statement statement = null;
		Collection<CompanyDO> companies = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String companyQuery = "SELECT company_Id, company_Name, password, email FROM company";
			ResultSet resultSet = statement.executeQuery(companyQuery);
			while (resultSet.next()) {
				CompanyDO tempCompanyDO = new CompanyDO(Integer.parseInt(resultSet.getString("COMPANY_ID")),
						resultSet.getString("COMPANY_NAME"), resultSet.getString("PASSWORD"),
						resultSet.getString("EMAIL"));
				companies.add(tempCompanyDO);
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find any company list ...\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return companies;
	}

	/** 
	 * this method gets from Coupon table in database Collection of Coupons that belong to specific Company
	 * builds query to insert to database, searches Coupons by CompanyID
	 * creates connection to database
	 * 		   
	 * @param  long
	 * 		   companyId
	 * @throws DAOException
	 * 		   if wasn't able to find any coupon list
	 * @return Collection of Coupons
	  */	
	@Override
	public Collection<CouponDO> getAllCouponsByCompany(long companyId) {
		Statement statement = null;
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String couponQuery = "SELECT * FROM Coupon where Company_Id = " + companyId;
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				CouponDO tempCouponDO = new CouponDO(Long.parseLong(resultSet.getString("Company_ID")), Long.parseLong(resultSet.getString("Coupon_ID")),
						resultSet.getString("Title"), convertToDate(resultSet.getString("Start_Date")), convertToDate(resultSet.getString("End_Date")),
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
	 * this method gets from Coupon table in database Collection of Coupons with specific Type and that belong to specific Company
	 * builds query to insert to database, searches Coupons by CompanyID and by Type
	 * creates connection to database
	 * 		   
	 * @param  long
	 * 		   companyId
	 * @param  Type
	 * 		   type of Coupon 
	 * @throws DAOException
	 * 		   if wasn't able to find any coupon list
	 * @return Collection of Coupons
	  */	
	
	@Override
	public Collection<CouponDO> getAllCouponsByCompanyAndType(long companyId, Type type) {
		Statement statement = null;
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String couponQuery = "SELECT * FROM Coupon where Company_Id = " + '"' + companyId + '"'+ "and Type = " + '"' + type + '"';
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				CouponDO tempCouponDO = new CouponDO(Long.parseLong(resultSet.getString("Company_ID")), Long.parseLong(resultSet.getString("Coupon_ID")),
						resultSet.getString("Title"), convertToDate(resultSet.getString("Start_Date")), convertToDate(resultSet.getString("End_Date")),
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
	 * this method return true then find in Company table in database Company with CompanyId and Password, else false
	 * builds query to insert to database, searches Company by CompanyID and verify Password
	 * creates connection to database
	 * 		   
	 * @param  long
	 * 		   companyId
	 * @param  String
	 * 		   password 
	 * @throws DAOException
	 * 		   Company id or/and password are not exist in database
	 * @return boolean
	  */	
	@Override
	public boolean login(long companyId, String password) {
		Statement statement = null;
		String tempCompanyName = null;
		String tempPassword = null;
		try {
			statement = connection.createStatement();

			String loginQuery = "SELECT Company_Id, password from company where Company_Id = '" + companyId
					+ "' and password = '" + password + "'";
			ResultSet resultSet = statement.executeQuery(loginQuery);
			while (resultSet.next()) {

				tempCompanyName = resultSet.getString("Company_Id");
				tempPassword = resultSet.getString("Password");
			}
		} catch (SQLException e) {
			throw new DAOException(
					"Company id or/and password are not valid! Please try again... \n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		if (tempCompanyName != null & tempPassword != null) {
			return true;
		} else {
			return false;
		}
	}

	private String buildCompanyInsertQuery(CompanyDO companyDO) {
		StringBuilder sb = new StringBuilder("INSERT INTO Company VALUES(");
		sb.append(String.valueOf(companyDO.getCompanyId()));
		sb.append(",");
		sb.append(surroundWithCommas(companyDO.getCompanyName()));
		sb.append(",");
		sb.append(surroundWithCommas(companyDO.getPassword()));
		sb.append(",");
		sb.append(surroundWithCommas(companyDO.getEmail()));
		sb.append(")");
		return sb.toString();
	}

	private String surroundWithCommas(String string) {
		return "'" + string + "'";
	}

	private String updateCompanyInsertQuery(long companyId, String companyName, String companyPassword, String companyEmail) {
		StringBuilder sb = new StringBuilder("Update Company SET Company_Name = ");
		sb.append(surroundWithCommas(companyName));
		sb.append(", Password = ");
		sb.append(surroundWithCommas(companyPassword));
		sb.append(", Email = ");
		sb.append(surroundWithCommas(companyEmail));
		sb.append("WHERE Company_Id = " + companyId);
		return sb.toString();
	}

	
	private Date convertToDate(String date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		try {
			return simpleDateFormat.parse(date);
		} catch (ParseException e) {
			throw new DAOException("cannot parse date: " + date + " from coupon table: " + "\n" + e.getMessage()); 
		}		
	}

	
}

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
import java.util.stream.Collectors;

import com.johnbrice.svainer.couponsite.core.logic.exception.DAOException;
import com.johnbrice.svainer.couponsite.core.model.CouponDO;
import com.johnbrice.svainer.couponsite.core.model.PurchasedCouponDO;
import com.johnbrice.svainer.couponsite.core.model.Type;


/**
 * Implements CRUD operations for {@code CouponDO} 
 * All methods creates connection to database, in the end of methods connection is closed. 
 * This class work with Coupon table and Customer_Coupon table in database
 * 
 * @author Svetlana Vainer
 * @author Alissa Boubyr
 *  
 */

public class CouponDBDAO implements CouponDAO {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private Connection connection;

	public CouponDBDAO() {
		this.connection = ConnectionManagerPool.getInstance().returnConnection();
	}

	/** 
	 * this method inserts Coupon to Coupon table in database
	 * builds query to insert to database
	 * creates connection to database
	 * @param  couponDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to insert coupon
	 * @return integer
	 * 		   number of insert records
	 *   */
	@Override
	public int createCoupon(CouponDO couponDO) {
		try {
			Statement statement = connection.createStatement();
			String insertQuery = buildCouponInsertQuery(couponDO);
			int numberOfInsertRecords = statement.executeUpdate(insertQuery);
			statement.close();
			return numberOfInsertRecords;
		} catch (SQLException e) {
			throw new DAOException(
					"Wasn't able to create coupon: " + "\n" + couponDO.toString() + "\n" + e.getMessage());
		}
	}

	/** 
	 * this method removes Coupon from Coupon table in database, also removes all purchase of this Coupon
	 * builds query to insert to database, searches Coupon by ID
	 * creates connection to database
	 * @param  couponDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to remove coupon
	 *   */
	@Override
	public void removeCoupon(CouponDO couponDO) {
		try {
			Statement statement = connection.createStatement();
			List<PurchasedCouponDO> purchasedCouponDOs = fetchPurchasedCouponsByCouponId(couponDO.getCouponId());
			if (!purchasedCouponDOs.isEmpty()) {
				String query = deletePurchasedCouponsByCouponId(purchasedCouponDOs);
				statement.executeUpdate(query);				
			}
			statement.executeUpdate("DELETE FROM Coupon WHERE coupon_id = " + couponDO.getCouponId());
			statement.close();
		} catch (SQLException e) {
			throw new DAOException(
					"wasn't able to remove coupon ..." + couponDO.toString() + "\n" + e.getMessage());
		}
	}

	/** 
	 * this method updates all Coupon parameters in Coupon table in database except couponId and companyId
	 * builds query to insert to database, searches Coupon by ID
	 * creates connection to database
	 * @param  couponDO
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to update coupon
	 * @return integer
	 * 		   number of update records
	 *   */
	@Override
	public int updateCoupon(CouponDO couponDO) {
		try {
			Statement statement = connection.createStatement();
			String updateQuery = updateCouponInsertQuery(couponDO.getCouponId(), couponDO.getCompanyId(),
					couponDO.getTitle(), couponDO.getStartDate(), couponDO.getEndDate(), couponDO.getAmount(),
					couponDO.getType(), couponDO.getMessage(), couponDO.getPrice(), couponDO.getImage());
			int numberOfUpdateRecords = statement.executeUpdate(updateQuery);
			statement.close();
			return numberOfUpdateRecords;
		} catch (SQLException e) {
			String errorMessage = "Wasn't able to update coupon ..." + couponDO.toString() + "\n" + e.getMessage();
			throw new DAOException(errorMessage);
		}
	}
	
	/** 
	 * this method gets Coupon parameters from Coupon table in database
	 * builds query to insert to database, searches Coupon by ID
	 * creates connection to database
	 * @param  long
	 * 		   companyId
	 * @param  long
	 * 		   couponId 
	 * @throws DAOException
	 * 		   if wasn't able to find coupon
	 * @return Coupon
	  */
	@Override
	public CouponDO getCoupon(long companyId, long couponId) {
		Statement statement = null;
		CouponDO couponDO = null;
		try {
			statement = connection.createStatement();
			String couponQuery = "SELECT * FROM coupon where company_Id = " + companyId + " and coupon_Id = "
					+ couponId;
			ResultSet resultSet = statement.executeQuery(couponQuery);
			while (resultSet.next()) {
				couponDO = new CouponDO(Long.parseLong(resultSet.getString("Coupon_ID")),
						Long.parseLong(resultSet.getString("Company_ID")), resultSet.getString("Title"),
						convertStringToDate(resultSet.getString("Start_Date")),
						convertStringToDate(resultSet.getString("End_Date")),
						Integer.parseInt(resultSet.getString("Amount")), Type.valueOf(resultSet.getString("Type")),
						resultSet.getString("Message"), Double.parseDouble(resultSet.getString("Price")),
						resultSet.getString("Image"));
			}
		} catch (SQLException e) {
			throw new DAOException("Wasn't able to find couponID ..." + couponId + "\n" + e.getMessage());
		} finally {
			try {
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return couponDO;
	}
	
	/** 
	 * this method gets Collection of Coupons from Coupon table in database
	 * builds query to insert to database
	 * creates connection to database
	 * 		   
	 * @throws DAOException
	 * 		   if wasn't able to find any company
	 * @return Collection of Coupons
	  */
	@Override
	public Collection<CouponDO> getAllCoupons() {
		Statement statement = null;
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String couponQuery = "SELECT * FROM Coupon";
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
	 * this method gets from Coupon table in database Collection of Coupons with specific Type
	 * builds query to insert to database, searches Coupons by by Type
	 * creates connection to database
	 * 		  
	 * @param  Type
	 * 		   type of Coupon 
	 * @throws DAOException
	 * 		   if wasn't able to find any coupon with type
	 * @return Collection of Coupons
	  */	
	@Override
	public Collection<CouponDO> getAllCouponsByType(Type type) {
		Statement statement = null;
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();

			String couponQuery = "SELECT * FROM Coupon where Type = " + "'" + type + "'";
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
			throw new DAOException("Wasn't able to find any coupon with type: " + type + "\n" + e.getMessage());
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
	 * this method gets from Coupon table in database Collection of Coupons that purchased
	 * purchase Coupons are at Customer_Coupon table in database
	 * builds query to insert to database, searches Coupons by ID and they must be purchased
	 * creates connection to database
	 * 
	 * @throws DAOException
	 * 		   if wasn't able to find any coupon list
	 * @return Collection of Coupons
	  */	
	@Override
	public Collection<CouponDO> getAllPurchaseCoupons() {
		Statement statement = null;
		List<String> couponIds = new ArrayList<>();
		Collection<CouponDO> coupons = new ArrayList<>();
		try {
			statement = connection.createStatement();
			String couponIdsQuery = ("SELECT distinct coupon_id FROM customer_coupon;");
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

	private String buildCouponInsertQuery(CouponDO couponDO) {
		StringBuilder sb = new StringBuilder("INSERT INTO Coupon VALUES(");
		sb.append(String.valueOf(couponDO.getCouponId()));
		sb.append(",");
		sb.append(surroundWithCommas(String.valueOf(couponDO.getCompanyId())));
		sb.append(",");
		sb.append(surroundWithCommas(couponDO.getTitle()));
		sb.append(",");
		sb.append(surroundWithCommas(convertDateToSimpleString(couponDO.getStartDate())));
		sb.append(",");
		sb.append(surroundWithCommas(convertDateToSimpleString(couponDO.getEndDate())));
		sb.append(",");
		sb.append(surroundWithCommas(String.valueOf(couponDO.getAmount())));
		sb.append(",");
		sb.append(surroundWithCommas(String.valueOf(couponDO.getType())));
		sb.append(",");
		sb.append(surroundWithCommas(couponDO.getMessage()));
		sb.append(",");
		sb.append(surroundWithCommas(String.valueOf(couponDO.getPrice())));
		sb.append(",");
		sb.append(surroundWithCommas(couponDO.getImage()));
		sb.append(")");
		return sb.toString();
	}

	private String surroundWithCommas(String string) {
		return "'" + string + "'";
	}

	private String updateCouponInsertQuery(long couponId, long companyId, String title, Date startDate, Date endDate,
			int amount, Type type, String message, double price, String image) {
		StringBuilder sb = new StringBuilder("Update Coupon SET Company_Id = ");
		sb.append(surroundWithCommas(String.valueOf(companyId)));
		sb.append(", Title = ");
		sb.append(surroundWithCommas(title));
		sb.append(", Start_Date = ");
		sb.append(surroundWithCommas(convertDateToSimpleString(startDate)));
		sb.append(", End_Date = ");
		sb.append(surroundWithCommas(convertDateToSimpleString(endDate)));
		sb.append(", Amount = ");
		sb.append(surroundWithCommas(String.valueOf(amount)));
		sb.append(", Type = ");
		sb.append(surroundWithCommas(String.valueOf(type)));
		sb.append(", Message = ");
		sb.append(surroundWithCommas(message));
		sb.append(", Price = ");
		sb.append(surroundWithCommas(String.valueOf(price)));
		sb.append(", Image = ");
		sb.append(surroundWithCommas(image));
		sb.append("WHERE Coupon_Id = " + couponId);
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

	private String convertDateToSimpleString(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
		return simpleDateFormat.format(date);
	}
	private String deletePurchasedCouponsByCouponId(List<PurchasedCouponDO> fetchPurchasedCouponsByCouponId) {
		StringBuilder sb = new StringBuilder("DELETE FROM Customer_Coupon WHERE ");
		sb.append(String.join(" OR ",
				fetchPurchasedCouponsByCouponId.stream()
				.map(i -> "(coupon_id = " + i.getCouponId() + " and CUSTOMER_ID = " + i.getCustomerId() + ")")
				.collect(Collectors.toList())));
		return sb.toString();
	}
	
	private List<PurchasedCouponDO> fetchPurchasedCouponsByCouponId(long couponId) {
		List<PurchasedCouponDO> purchasedCouponDOs = new ArrayList<>();
		Statement statement = null;
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * from Customer_Coupon WHERE coupon_id = " + couponId);
			while (resultSet.next()) {
				purchasedCouponDOs.add(new PurchasedCouponDO(Long.valueOf(resultSet.getString("CUSTOMER_ID")),
						Long.valueOf(resultSet.getString("COUPON_ID"))));
			}
		} catch (SQLException e) {
			throw new DAOException("wasn't able to fetch purchased coupons records..." + e.getMessage());
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new DAOException("get error in attempt to close statement..." + e.getMessage());
			}
		}
		return purchasedCouponDOs;
	}
}

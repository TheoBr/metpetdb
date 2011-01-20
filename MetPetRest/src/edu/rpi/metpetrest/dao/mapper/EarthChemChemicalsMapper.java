package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.rpi.metpetrest.model.Item;
import edu.rpi.metpetrest.model.Method;

public class EarthChemChemicalsMapper implements ResultSetExtractor<Method> {

	NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);

	public EarthChemChemicalsMapper()
	{
	nf.setMinimumFractionDigits(2);
	}
	
	@Override
	public Method extractData(ResultSet rs) throws SQLException,
			DataAccessException 
		{
	
		Method method = null;
		if (rs.next())
		{
			String value = null;
			
			if (rs.getBigDecimal(6) != null)
				value =  nf.format(rs.getBigDecimal(6));
			
		method = new Method(rs.getString(3), rs.getString(4));
		
		Item currItem = new Item("chemical" , rs.getString(1), "-1", rs.getString(2), rs.getString(5),  value );
		
		if (currItem.getName() != null && !currItem.getName().equals(""))
			method.addItem(currItem);
		
		}
		
		while (rs.next())
		{
			String value = null;
			
			if (rs.getBigDecimal(6) != null)
				value =  nf.format(rs.getBigDecimal(6));

			Item currItem = new Item("chemical" , rs.getString(1), "-1", rs.getString(2), rs.getString(5),  value);
			
		
			if (currItem.getName() != null && !currItem.getName().equals(""))						
			method.addItem(currItem);
		}
		
		return method;
	}

}

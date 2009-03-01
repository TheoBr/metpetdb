package edu.rpi.metpetdb.server;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;

import edu.rpi.metpetdb.server.dao.GeometryTypeDbUnit;

public class PostGisDataTypeFactory extends DefaultDataTypeFactory {
	public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException
   {
       if (sqlTypeName.equals("geometry"))
       {
           return new GeometryTypeDbUnit();
       }

       return super.createDataType(sqlType, sqlTypeName);
   }
}

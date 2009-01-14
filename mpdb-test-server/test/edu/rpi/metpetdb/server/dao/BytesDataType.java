/*
 *
 * The DbUnit Database Testing Framework
 * Copyright (C)2002-2004, DbUnit.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package edu.rpi.metpetdb.server.dao;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbunit.dataset.ITable;
import org.dbunit.dataset.datatype.AbstractDataType;
import org.dbunit.dataset.datatype.TypeCastException;
import org.dbunit.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Manuel Laflamme
 * @author Last changed by: $Author$
 * @version $Revision$ $Date$
 * @since 1.0 (Mar 20, 2002)
 */
public class BytesDataType extends AbstractDataType
{

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(BytesDataType.class);

    private static final int MAX_URI_LENGTH = 256;

    public BytesDataType(String name, int sqlType)
    {
        super(name, sqlType, byte[].class, false);
    }

    private byte[] toByteArray(InputStream in, int length) throws IOException
    {
        if (logger.isDebugEnabled())
		{
			logger.debug("toByteArray(in={}, length={}) - start", in, Integer.toString(length));
		}

        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        in = new BufferedInputStream(in);
        int i = in.read();
        while (i != -1)
        {
            out.write(i);
            i = in.read();
        }
        return out.toByteArray();
    }

    ////////////////////////////////////////////////////////////////////////////
    // DataType class

    public Object typeCast(Object value) throws TypeCastException
    {
        logger.debug("typeCast(value={}) - start", value);

        if (value == null || value == ITable.NO_VALUE)
        {
            return null;
        }

        if (value instanceof byte[])
        {
            return value;
        }

        if (value instanceof String)
        {
        	final byte[] valueByte = new byte[25];
        	final String valueString = (String) value;
        	int byteCount = 0;
        	for(int i = 0;i<((String)value).length();++i) {
        		if (valueString.charAt(i) == '\\') {
        			final String bytePart = valueString.substring(i+1, i+4);
        			valueByte[byteCount] = (byte) Integer.parseInt(bytePart);
        			i = i+3;
        		} else {
        			try {
						valueByte[byteCount] =  valueString.substring(i,i+1).getBytes("UTF-8")[0];
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
        		++byteCount;
        	}
        	return valueByte; 
        }

        if (value instanceof Blob)
        {
            try
            {
                Blob blobValue = (Blob)value;
                return blobValue.getBytes(1, (int)blobValue.length());
            }
            catch (SQLException e)
            {
                throw new TypeCastException(value, this, e);
            }
        }

        if (value instanceof URL)
        {
            try
            {
                return toByteArray(((URL)value).openStream(), 0);
            }
            catch (IOException e)
            {
                throw new TypeCastException(value, this, e);
            }
        }

        if (value instanceof File)
        {
            try
            {
                File file = (File)value;
                return toByteArray(new FileInputStream(file), (int)file.length());
            }
            catch (IOException e)
            {
                throw new TypeCastException(value, this, e);
            }
        }

        throw new TypeCastException(value, this);
    }

    public int compare(Object o1, Object o2) throws TypeCastException
    {
        logger.debug("compare(o1={}, o2={}) - start", o1, o2);

        try
        {
            // New since dbunit 2.4 for performance improvement. Most of the times
            // the "typeCase" can be avoided like this.
            if(areObjectsEqual(o1, o2))
            {
                return 0;
            }
            
            byte[] value1 = (byte[])typeCast(o1);
            byte[] value2 = (byte[])typeCast(o2);

            if (value1 == null && value2 == null)
            {
                return 0;
            }

            if (value1 == null && value2 != null)
            {
                return -1;
            }

            if (value1 != null && value2 == null)
            {
                return 1;
            }

            return compare(value1, value2);
        }
        catch (ClassCastException e)
        {
            throw new TypeCastException(e);
        }
    }

    private boolean areObjectsEqual(Object o1, Object o2) {
		// TODO Auto-generated method stub
		return true;
	}

	public int compare(byte[] v1, byte[] v2) throws TypeCastException
    {
        if (logger.isDebugEnabled())
		{
			logger.debug("compare(v1={}, v2={}) - start", v1, v2);
		}

        int len1 = v1.length;
        int len2 = v2.length;
        int n = Math.min(len1, len2);
        int i = 0;
        int j = 0;

        if (i == j)
        {
            int k = i;
            int lim = n + i;
            while (k < lim)
            {
                byte c1 = v1[k];
                byte c2 = v2[k];
                if (c1 != c2)
                {
                    return c1 - c2;
                }
                k++;
            }
        }
        else
        {
            while (n-- != 0)
            {
                byte c1 = v1[i++];
                byte c2 = v2[j++];
                if (c1 != c2)
                {
                    return c1 - c2;
                }
            }
        }
        return len1 - len2;
    }

    public Object getSqlValue(int column, ResultSet resultSet)
            throws SQLException, TypeCastException
    {
    	if(logger.isDebugEnabled())
    		logger.debug("getSqlValue(column={}, resultSet={}) - start", new Integer(column), resultSet);

        byte[] value = resultSet.getBytes(column);
        if (value == null || resultSet.wasNull())
        {
            return null;
        }
        return value;
    }

    public void setSqlValue(Object value, int column, PreparedStatement statement)
            throws SQLException, TypeCastException
    {
    	if (logger.isDebugEnabled()) 
    	{
    		logger.debug("setSqlValue(value={}, column={}, statement={}) - start",
        		new Object[]{value, new Integer(column), statement} );
    	}

        super.setSqlValue(value, column, statement);
    }

}
package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.MaterialData;

public class MaterialDataMapper implements RowMapper<MaterialData> {

	@Override
	public MaterialData mapRow(ResultSet rs, int arg1) throws SQLException {

		MaterialData materialData = new MaterialData(rs.getString(9),
				rs.getString(10));

		return materialData;
	}

}

package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import edu.rpi.metpetrest.model.MineralData;

public class MineralDataMapper implements RowMapper<MineralData> {

	@Override
	public MineralData mapRow(ResultSet rs, int arg1) throws SQLException {
		MineralData mineralData = new MineralData();

		mineralData.setName(rs.getString(12));

		return mineralData;
	}

}

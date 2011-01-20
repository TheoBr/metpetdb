package edu.rpi.metpetrest.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.PGgeometry;
import org.postgis.Point;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import edu.rpi.metpetrest.model.EarthChemData;
import edu.rpi.metpetrest.model.EarthChemSample;
import edu.rpi.metpetrest.model.MaterialData;
import edu.rpi.metpetrest.model.PublicationData;

public class EarthChemSampleMapper implements
		ResultSetExtractor<EarthChemSample> {

	@Override
	public EarthChemSample extractData(ResultSet rs) throws SQLException,
			DataAccessException {

		EarthChemSample data = new EarthChemSample();

		if (rs.next()) {
			data.setSampleId(rs.getString(1));
			data.setSampleNumber(rs.getString(2));
			data.setIgsn(rs.getString(3));

			PGgeometry geometry = (PGgeometry) rs.getObject(4);

			data.setLocation((Point) geometry.getGeometry());

			EarthChemPublicationDataMapper pubDataMapper = new EarthChemPublicationDataMapper();
			PublicationData pubData = pubDataMapper.mapRow(rs, 0);

			MaterialDataMapper materialDataMapper = new MaterialDataMapper();
			MaterialData materialData = materialDataMapper.mapRow(rs, 0);

			EarthChemData earthChemData = new EarthChemData(pubData,
					materialData);

			data.setEarthChemData(earthChemData);

		}

		return data;
	}

}

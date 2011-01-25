package edu.rpi.metpetrest.dao;

import java.util.List;

import javax.sql.DataSource;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.jdbc.core.JdbcTemplate;

import edu.rpi.metpetrest.dao.mapper.EarthChemChemicalsMapper;
import edu.rpi.metpetrest.dao.mapper.EarthChemMineralsMapper;
import edu.rpi.metpetrest.dao.mapper.EarthChemSampleMapper;
import edu.rpi.metpetrest.dao.mapper.IdMapper;
import edu.rpi.metpetrest.dao.mapper.SampleDataMapper;
import edu.rpi.metpetrest.dao.mapper.UserSampleDataMapper;
import edu.rpi.metpetrest.model.EarthChemSample;
import edu.rpi.metpetrest.model.Method;
import edu.rpi.metpetrest.model.SampleData;
import edu.rpi.metpetrest.model.UserSampleData;

public class SampleDAOImpl extends JdbcTemplate {

	private static final String samplesQuery = "select distinct(samples.number) from samples where samples.user_id =  (select user_id from users where email = \'PUBLICATION\' and name = \'PUBLICATION\')";

	private static final String chemicalAnalysisQuery = "SELECT chemical_analyses.chemical_analysis_id FROM chemical_analyses WHERE chemical_analyses.public_data = \'Y\' AND chemical_analyses.user_id = (select user_id from users where email = \'PUBLICATION\' and name = \'PUBLICATION\');";

	private static final String sampleQuery = "SELECT samples.sample_id FROM samples WHERE samples.public_data = 'Y' AND samples.user_id = (select user_id from users where email = 'PUBLICATION' and name = 'PUBLICATION');";

	private static final String subsampleQuery = "SELECT subsamples.subsample_id FROM subsamples WHERE subsamples.public_data = 'Y' AND subsamples.user_id = (select user_id from users where email = 'PUBLICATION' and name = 'PUBLICATION');";

	private static final String sampleQuery2 = "SELECT sr1.sample_id, georeference.title, georeference.first_author, regions.name FROM samples sr1 LEFT OUTER JOIN  sample_regions  ON (sr1.sample_id = sample_regions.sample_id) LEFT OUTER JOIN regions ON (regions.region_id = sample_regions.region_id ) ,samples sr2 LEFT OUTER JOIN sample_reference ON (sr2.sample_id = sample_reference.sample_id) LEFT OUTER JOIN georeference ON (sample_reference.reference_id = georeference.reference_id) WHERE sr1.sample_id = sr2.sample_id AND sr1.public_data = 'Y' AND sr1.user_id = (select user_id from users where email = 'PUBLICATION' and name = 'PUBLICATION') order by sr1.sample_id";

	private static final String sampleQueryForPublication = "SELECT  samples.sample_id, georeference.title, georeference.first_author, '' as foo FROM georeference, samples, sample_reference WHERE georeference.reference_id = sample_reference.reference_id AND sample_reference.sample_id = samples.sample_id AND georeference.reference_number = ? AND samples.public_data = 'Y'";

	private static final String sampleQueryForUser = "SELECT  samples.sample_id, samples.user_id, users.name from samples, users where samples.user_id = ? and samples.user_id = users.user_id and samples.public_data = 'Y'";

	private static final String earthChemSampleQuery = "select samples.sample_id, samples.number, samples.sesar_number, samples.location, samples.location_error, georeference.title, georeference.first_author, georeference.second_authors, georeference.journal_name, metamorphic_grades.name, rock_type.rock_type, chemical_analyses.large_rock, minerals.name FROM samples LEFT OUTER JOIN subsamples ON (samples.sample_id = subsamples.sample_id) LEFT OUTER JOIN chemical_analyses ON (subsamples.subsample_id = chemical_analyses.subsample_id) LEFT OUTER JOIN sample_metamorphic_grades ON (samples.sample_id = sample_metamorphic_grades.sample_id) LEFT OUTER JOIN metamorphic_grades ON ( sample_metamorphic_grades.metamorphic_grade_id = metamorphic_grades.metamorphic_grade_id) LEFT OUTER JOIN sample_reference ON (samples.sample_id = sample_reference.sample_id) LEFT OUTER JOIN georeference ON (sample_reference.reference_id = georeference.reference_id) LEFT OUTER JOIN sample_minerals ON ( samples.sample_id = sample_minerals.sample_id) LEFT OUTER JOIN minerals ON (sample_minerals.mineral_id = minerals.mineral_id) LEFT OUTER JOIN rock_type ON (samples.rock_type_id = rock_type.rock_type_id) WHERE samples.number = ?";

	private static final String earthChemMineralsQuery = "select minerals.name FROM samples LEFT OUTER JOIN sample_minerals ON ( samples.sample_id = sample_minerals.sample_id) LEFT OUTER JOIN minerals ON (sample_minerals.mineral_id = minerals.mineral_id) WHERE samples.number = ?";

	private static final String earthChemChemicalsQuery = "select elements.name, 'elements' as type, chemical_analyses.analyst, chemical_analyses.analysis_method, chemical_analysis_elements.measurement_unit, chemical_analysis_elements.amount FROM samples LEFT OUTER JOIN subsamples ON (samples.sample_id = subsamples.sample_id) LEFT OUTER JOIN chemical_analyses ON (subsamples.subsample_id = chemical_analyses.subsample_id) LEFT OUTER JOIN chemical_analysis_elements ON (chemical_analyses.chemical_analysis_id = chemical_analysis_elements.chemical_analysis_id) LEFT OUTER JOIN elements ON (chemical_analysis_elements.element_id = elements.element_id) WHERE samples.number = ? AND chemical_analyses.large_rock = 'Y'"
			+ " UNION "
			+ "select oxides.species, 'oxides' as type, chemical_analyses.analyst, chemical_analyses.analysis_method, chemical_analysis_oxides.measurement_unit, chemical_analysis_oxides.amount FROM samples LEFT OUTER JOIN subsamples ON (samples.sample_id = subsamples.sample_id) LEFT OUTER JOIN chemical_analyses ON (subsamples.subsample_id = chemical_analyses.subsample_id) LEFT OUTER JOIN chemical_analysis_oxides ON (chemical_analyses.chemical_analysis_id = chemical_analysis_oxides.chemical_analysis_id)  LEFT OUTER JOIN oxides ON (chemical_analysis_oxides.oxide_id = oxides.oxide_id) WHERE samples.number = ? AND chemical_analyses.large_rock = 'Y'";

	private DataSource dataSource = null;

	private final Ehcache cache;
	
	public SampleDAOImpl(CacheManager cacheManager) {


		this.cache = cacheManager.getEhcache("earthChemSamples");
		
	}
	
	
    public EarthChemSample readEarthChemSample(String sampleNumber) 
    {
    	Element element = cache.get(sampleNumber);
    	
    	if (element != null)
    		return (EarthChemSample)element.getValue();
    	else
    		return null;
    	
    }

    
	public EarthChemSample getEarthChemSample(String sampleNumber) {
		
		EarthChemSample sampleFound = null;
		
		if ((sampleFound = readEarthChemSample(sampleNumber)) != null)
			return sampleFound;
		else
			{
			sampleFound = this.query(earthChemSampleQuery, new Object[] { sampleNumber },new EarthChemSampleMapper());
			
			List<String> minerals = this.getEarthChemMinerals(sampleNumber);
			
			Method method = this.getEarthChemChemicals(sampleNumber);

			sampleFound.getEarthChemData().getCitation().getSampleType()
					.addMinerals(minerals);

			if (method != null && method.getItems() != null
					&& method.getItems().size() > 0)
				sampleFound.getEarthChemData().getCitation().getSampleType()
						.addMethod(method);// addChemicals(chemicals);

			
			cache.put(new Element(sampleFound.getSampleNumber(), sampleFound));
			return sampleFound;
			}
	}

	public List<String> getEarthChemMinerals(String sampleNumber) {	
		return this.query(earthChemMineralsQuery,
				new Object[] { sampleNumber }, new EarthChemMineralsMapper());
	}

	public Method getEarthChemChemicals(String sampleNumber) {
		return this.query(earthChemChemicalsQuery, new Object[] { sampleNumber,
				sampleNumber }, new EarthChemChemicalsMapper());
	}

	public List<UserSampleData> getSamplesForUser(String userId) {
		return this.query(sampleQueryForUser, new Object[] { userId },
				new UserSampleDataMapper());
	}

	public List<SampleData> getSamplesForPublication(String referenceId) {
		return this.query(sampleQueryForPublication,
				new Object[] { referenceId }, new SampleDataMapper());
	}

	public List<String> getSamples() {
		return this.queryForList(samplesQuery, String.class);
	}

	public List<SampleData> findPublicSampleDataOwnedByPublication() {
		return this.query(sampleQuery2, new SampleDataMapper());
	}

	public List<Long> findPublicChemicalAnalysisIdsOwnedByPublication() {
		return this.query(chemicalAnalysisQuery, new IdMapper());
	}

	public List<Long> findPublicSampleIdsOwnedByPublication() {
		return this.query(sampleQuery, new IdMapper());
	}

	public List<Long> findPublicSubsampleIdsOwnedByPublication() {
		return this.query(subsampleQuery, new IdMapper());
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}

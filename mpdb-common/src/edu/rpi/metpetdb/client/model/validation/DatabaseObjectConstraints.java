package edu.rpi.metpetdb.client.model.validation;

import java.util.Collection;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.Image;
import edu.rpi.metpetdb.client.model.Project;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.SampleComment;
import edu.rpi.metpetdb.client.model.StartSessionRequest;
import edu.rpi.metpetdb.client.model.Subsample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.XrayImage;
import edu.rpi.metpetdb.client.model.interfaces.MObject;
import edu.rpi.metpetdb.client.model.properties.SearchSampleProperty;
import edu.rpi.metpetdb.client.model.validation.primitive.BooleanConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.FloatConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.IntegerConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.ShortConstraint;
import edu.rpi.metpetdb.client.model.validation.primitive.StringConstraint;

/**
 * Constraints for objects in the database
 * 
 */
public class DatabaseObjectConstraints implements IsSerializable {
	public static void validate(final MObject o, final PropertyConstraint[] pc)
			throws ValidationException {
		if (pc == null || o == null)
			return;
		for (int k = 0; k < pc.length; k++) {
			pc[k].validateEntity(o);
		}
	}

	// ------ SearchMineral ------
	public ValueInCollectionConstraint SearchSample_minerals;
	public ObjectConstraint SearchSample_elements;
	public ObjectConstraint SearchSample_oxides;
	public ValueInCollectionConstraint SearchSample_possibleRockTypes;

	// ------ SampleMineral ------
	public PropertyConstraint[] SampleMineral__all;
	public ValueInCollectionConstraint SampleMineral_Sample_minerals_mineral;
	public FloatConstraint SampleMineral_Sample_minerals_amount;

	// ------ Region ------
	public PropertyConstraint[] Region__all;
	public StringConstraint Region_name;

	// ------ MetamorphicGrade ------
	public PropertyConstraint[] MetamorphicGrade__all;
	public ValueInCollectionConstraint MetamorphicGrade_Sample_metamorphicGrades_metamorphicGrade;

	// ------ Reference ------
	public PropertyConstraint[] Reference__all;
	public StringConstraint Reference_name;

	// ------ Image Type ------
	public PropertyConstraint[] ImageType__all;
	public StringConstraint ImageType_imageType;

	// ------ Subsample Type ------
	public PropertyConstraint[] SubsampleType__all;
	public StringConstraint SubsampleType_subsampleType;

	// ------ Rock Type ------
	public PropertyConstraint[] RockType__all;
	public StringConstraint RockType_rockType;

	// ------ SampleComment ------
	public PropertyConstraint[] SampleComment__all;
	public StringConstraint SampleComment_text;
	public void validate(final SampleComment sc) throws ValidationException {
		validate(sc, SampleComment__all);
	}

	// ------ ImageComment ------
	public PropertyConstraint[] ImageComment__all;
	public StringConstraint ImageComment_text;

	// ------ ChemicalAnalysisOxide ------
	public PropertyConstraint[] ChemicalAnalysisOxide__all;
	public ValueInCollectionConstraint ChemicalAnalysisOxide_ChemicalAnalysis_oxides_oxide;
	public FloatConstraint ChemicalAnalysisOxide_ChemicalAnalysis_oxides_amount;
	public FloatConstraint ChemicalAnalysisOxide_ChemicalAnalysis_oxides_precision;
	public StringConstraint ChemicalAnalysisOxide_ChemicalAnalysis_oxides_precisionUnit;

	// ------ ChemicalAnalysisElement ------
	public PropertyConstraint[] ChemicalAnalysisElement__all;
	public ValueInCollectionConstraint ChemicalAnalysisElement_ChemicalAnalysis_elements_element;
	public FloatConstraint ChemicalAnalysisElement_ChemicalAnalysis_elements_amount;
	public FloatConstraint ChemicalAnalysisElement_ChemicalAnalysis_elements_precision;
	public StringConstraint ChemicalAnalysisElement_ChemicalAnalysis_elements_precisionUnit;

	// ------ XrayImage ------
	public PropertyConstraint[] XrayImage__all;
	public IntegerConstraint XrayImage_current;
	public IntegerConstraint XrayImage_voltage;
	public IntegerConstraint XrayImage_dwelltime;
	public StringConstraint XrayImage_element;

	public void validate(final XrayImage xi) throws ValidationException {
		validate(xi, XrayImage__all);
	}

	// ------ Image ------
	public PropertyConstraint[] Image__all;
	public ValueInCollectionConstraint Image_imageType;
	public StringConstraint Image_collector;
	public IntegerConstraint Image_scale;
	public ObjectConstraint Image_comments;

	public void validate(final Image i) throws ValidationException {
		validate(i, Image__all);
	}

	// ------ Sample ------
	public PropertyConstraint[] Sample__all;
	public Sample_sesarNumber Sample_sesarNumber;
	public StringConstraint Sample_alias;
	public StringConstraint Sample_country;
	public StringConstraint Sample_description;
	public StringConstraint Sample_collector;
	public StringConstraint Sample_locationText;
	public ValueInCollectionConstraint Sample_rockType;
	public IntegerConstraint Sample_subsampleCount;
	public GeometryConstraint Sample_location;
	public FloatConstraint Sample_latitudeError;
	public FloatConstraint Sample_longitudeError;
	public ShortConstraint Sample_datePrecision;
	public BooleanConstraint Sample_publicData;
	public TimestampConstraint Sample_collectionDate;
	public ObjectConstraint Sample_minerals;
	public ObjectConstraint Sample_regions;
	public ObjectConstraint Sample_comments;
	public ObjectConstraint Sample_owner;
	public ValueInCollectionConstraint Sample_metamorphicGrades;
	public ObjectConstraint Sample_references;

	public void validate(final Sample s) throws ValidationException {
		validate(s, Sample__all);
	}

	// ------ Subsample ------
	public PropertyConstraint[] Subsample__all;
	public StringConstraint Subsample_name;
	public ValueInCollectionConstraint Subsample_subsampleType;
	public ObjectConstraint Subsample_images;
	public StringConstraint Subsample_sampleName;
	public IntegerConstraint Subsample_imageCount;
	public IntegerConstraint Subsample_analysisCount;
	public BooleanConstraint Subsample_publicData;
	public ObjectConstraint Subsample_owner;

	public void validate(Subsample u) throws ValidationException {
		validate(u, Subsample__all);
	}

	// ------ Project ------
	public PropertyConstraint[] Project__all;
	public StringConstraint Project_name;

	public void validate(Project u) throws ValidationException {
		validate(u, Project__all);
	}

	// ------ ChemicalAnalysis ------
	public PropertyConstraint[] ChemicalAnalysis__all;
	public StringConstraint ChemicalAnalysis_spotId;
	public StringConstraint ChemicalAnalysis_analysisMethod;
	public StringConstraint ChemicalAnalysis_location;
	public StringConstraint ChemicalAnalysis_analyst;
	public StringConstraint ChemicalAnalysis_description;
	public TimestampConstraint ChemicalAnalysis_analysisDate;
	public ShortConstraint ChemicalAnalysis_datePrecision;
	public IntegerConstraint ChemicalAnalysis_pointX;
	public IntegerConstraint ChemicalAnalysis_pointY;
	public BooleanConstraint ChemicalAnalysis_largeRock;
	public ObjectConstraint ChemicalAnalysis_image;
	public FloatConstraint ChemicalAnalysis_total;
	public ObjectConstraint ChemicalAnalysis_reference;
	public ValueInCollectionConstraint ChemicalAnalysis_mineral;
	public ObjectConstraint ChemicalAnalysis_elements;
	public ObjectConstraint ChemicalAnalysis_oxides;
	public StringConstraint ChemicalAnalysis_sampleName;
	public StringConstraint ChemicalAnalysis_subsampleName;
	public BooleanConstraint ChemicalAnalysis_publicData;
	public ObjectConstraint ChemicalAnalysis_owner;

	public void validate(ChemicalAnalysis u) throws ValidationException {
		validate(u, ChemicalAnalysis__all);
	}

	// ------ UserWithPassword ------
	public UserConstraint UserWithPassword_user;
	public StringConstraint UserWithPassword_oldPassword;
	public StringConstraint UserWithPassword_newPassword;
	public UserWithPassword_vrfPassword UserWithPassword_vrfPassword;

	// ------ StartSessionRequest ------
	public PropertyConstraint[] StartSessionRequest__all;
	public RestrictedCharacterStringConstraint StartSessionRequest_emailAddress;
	public StringConstraint StartSessionRequest_password;

	public void validate(StartSessionRequest u) throws ValidationException {
		validate(u, StartSessionRequest__all);
	}

	// ------ User ------
	public PropertyConstraint[] User__all;
	public RestrictedCharacterStringConstraint User_emailAddress;
	public StringConstraint User_name;
	public StringConstraint User_address;
	public StringConstraint User_city;
	public StringConstraint User_province;
	public StringConstraint User_country;
	public StringConstraint User_postalCode;
	public StringConstraint User_institution;
	public StringConstraint User_referenceEmail;

	public void validate(final User u) throws ValidationException {
		validate(u, User__all);
	}
	/**
	 * Called just before this instance is first used.
	 * <p>
	 * This method should only be invoked by the server, and only after it has
	 * finished filling in all of the constraint instances. It can be used to
	 * backfill missing data that we cannot acquire from our database, or to
	 * perform other work based on the newly created constraint instances.
	 * </p>
	 */
	public void finishInitialization(DatabaseObjectConstraints oc) {
		// TODO email address pattern
		User_emailAddress.pattern = "A-Z0-9a-z_\\.@";
		// Sample_collectionEnded.collectionBegan = Sample_collectionBegan;

		// UserWithPassword is not available in the database like this,
		// so we need to manually configure the values that we normally
		// acquire automatically.
		//
		UserWithPassword_user.required = true;
		UserWithPassword_oldPassword.required = true;
		UserWithPassword_newPassword.required = true;
		UserWithPassword_vrfPassword.required = true;

		UserWithPassword_oldPassword.minLength = 1;
		UserWithPassword_newPassword.minLength = 8;
		UserWithPassword_vrfPassword.minLength = 8;

		UserWithPassword_oldPassword.maxLength = 100;
		UserWithPassword_newPassword.maxLength = 100;
		UserWithPassword_vrfPassword.maxLength = 100;
		UserWithPassword_vrfPassword.newPassword = UserWithPassword_newPassword;

		// StartSessionRequest is not available in the database.
		//
		StartSessionRequest_emailAddress.required = true;
		StartSessionRequest_emailAddress.minLength = User_emailAddress.minLength;
		StartSessionRequest_emailAddress.maxLength = User_emailAddress.maxLength;
		StartSessionRequest_emailAddress.pattern = User_emailAddress.pattern;

		StartSessionRequest_password.required = true;
		StartSessionRequest_password.minLength = UserWithPassword_oldPassword.minLength;
		StartSessionRequest_password.maxLength = UserWithPassword_oldPassword.maxLength;

		// Fill in fake constraints for searching
		SearchSample_minerals.setValues((Collection<? extends MObject>)Sample_minerals.getValues());
		SearchSample_minerals.property = SearchSampleProperty.minerals;
		SearchSample_minerals.propertyName = "Minerals";
		SearchSample_minerals.required = false;
		SearchSample_minerals.entityName = "SearchSample";
		
		SearchSample_elements.setConstraints(ChemicalAnalysisElement__all);
		SearchSample_elements.property = SearchSampleProperty.elements;
		SearchSample_elements.propertyName = "Elements";
		SearchSample_elements.required = false;
		SearchSample_elements.entityName = "SearchSample";
		
		SearchSample_oxides.setConstraints(ChemicalAnalysisOxide__all);
		SearchSample_oxides.property = SearchSampleProperty.oxides;
		SearchSample_oxides.propertyName = "Oxides";
		SearchSample_oxides.required = false;
		SearchSample_oxides.entityName = "SearchSample";
		
		SearchSample_possibleRockTypes.setValues((Collection<? extends MObject>)Sample_rockType.getValues());
		SearchSample_possibleRockTypes.property = SearchSampleProperty.possibleRockTypes;
		SearchSample_possibleRockTypes.propertyName = "possibleRockTypes";
		SearchSample_possibleRockTypes.required = false;
		SearchSample_possibleRockTypes.entityName = "SearchSample";

//		
//		SearchSample_region.setConstraints(Sample_regions.getConstraints());
//		SearchSample_region.entityName = "SearchSample";
//		SearchSample_region.property = SearchSampleProperty.region;
//		SearchSample_region.propertyName = "Region";
//		SearchSample_region.required = false;
//		
//		SearchSample_references.setConstraints(Sample_references.getConstraints());
//		SearchSample_references.entityName = "SearchSample";
//		SearchSample_references.property = SearchSampleProperty.references;
//		SearchSample_references.propertyName = "Reference";
//		SearchSample_references.required = false;
//		
//		SearchSample_metamorphicGrades.setConstraints(Sample_metamorphicGrades.getConstraints());
//		SearchSample_references.entityName = "SearchSample";
//		SearchSample_references.property = SearchSampleProperty.metamorphicGrades;
//		SearchSample_references.propertyName = "MetamorphicGrades";
//		SearchSample_references.required = false;
	}
}

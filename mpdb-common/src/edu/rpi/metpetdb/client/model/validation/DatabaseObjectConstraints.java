package edu.rpi.metpetdb.client.model.validation;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
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
	public static void validate(final MObjectDTO o,
			final PropertyConstraint[] pc) throws ValidationException {
		if (pc == null || o == null)
			return;
		for (int k = 0; k < pc.length; k++) {
			pc[k].validateEntity(o);
		}
	}

	// ------ SampleMineral ------
	public PropertyConstraint[] SampleMineral__all;
	public ValueInCollectionConstraint SampleMineral_Sample_minerals_mineral;
	public FloatConstraint SampleMineral_Sample_minerals_amount;

	// ------ Region ------
	public PropertyConstraint[] Region__all;
	public StringConstraint Region_name;

	// ------ MetamorphicGrade ------
	public PropertyConstraint[] MetamorphicGrade__all;
	public StringConstraint MetamorphicGrade_name;

	// ------ Reference ------
	public PropertyConstraint[] Reference__all;
	public StringConstraint Reference_name;

	// ------ SampleComment ------
	public PropertyConstraint[] SampleComment__all;
	public StringConstraint SampleComment_text;

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
	public StringConstraint XrayImage_lines;
	public BooleanConstraint XrayImage_radiation;
	public ValueInCollectionConstraint XrayImage_element;

	// ------ Image ------
	public PropertyConstraint[] Image__all;
	public ImageTypeConstraint Image_imageType;
	public IntegerConstraint Image_lut;
	public IntegerConstraint Image_contrast;
	public IntegerConstraint Image_brightness;
	public IntegerConstraint Image_pixelsize;

	// ------ Sample ------
	public PropertyConstraint[] Sample__all;
	public Sample_sesarNumber Sample_sesarNumber;
	public StringConstraint Sample_alias;
	public StringConstraint Sample_country;
	public StringConstraint Sample_description;
	public StringConstraint Sample_collector;
	public StringConstraint Sample_locationText;
	public RockTypeConstraint Sample_rockType;
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
	public ObjectConstraint Sample_metamorphicGrades;
	public ObjectConstraint Sample_references;

	public void validate(final SampleDTO s) throws ValidationException {
		validate(s, Sample__all);
	}

	// ------ Subsample ------
	public PropertyConstraint[] Subsample__all;
	public StringConstraint Subsample_name;
	public SubsampleTypeConstraint Subsample_type;
	public ObjectConstraint Subsample_images;

	public void validate(SubsampleDTO u) throws ValidationException {
		validate(u, Subsample__all);
	}

	// ------ Project ------
	public PropertyConstraint[] Project__all;
	public StringConstraint Project_name;

	public void validate(ProjectDTO u) throws ValidationException {
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
	public ObjectConstraint ChemicalAnalysis_reference;
	public ValueInCollectionConstraint ChemicalAnalysis_mineral;
	public ObjectConstraint ChemicalAnalysis_elements;
	public ObjectConstraint ChemicalAnalysis_oxides;

	public void validate(ChemicalAnalysisDTO u) throws ValidationException {
		validate(u, ChemicalAnalysis__all);
	}

	// ------ UserWithPassword ------
	public UserConstraint UserWithPassword_user;
	public StringConstraint UserWithPassword_oldPassword;
	public StringConstraint UserWithPassword_newPassword;
	public UserWithPassword_vrfPassword UserWithPassword_vrfPassword;

	// ------ StartSessionRequest ------
	public PropertyConstraint[] StartSessionRequest__all;
	public RestrictedCharacterStringConstraint StartSessionRequest_username;
	public StringConstraint StartSessionRequest_password;

	public void validate(StartSessionRequestDTO u) throws ValidationException {
		validate(u, StartSessionRequest__all);
	}

	// ------ User ------
	public PropertyConstraint[] User__all;
	public RestrictedCharacterStringConstraint User_username;
	public StringConstraint User_emailAddress;

	public void validate(final UserDTO u) throws ValidationException {
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
		User_username.pattern = "A-Z0-9a-z_\\.@";
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
		StartSessionRequest_username.required = true;
		StartSessionRequest_username.minLength = User_username.minLength;
		StartSessionRequest_username.maxLength = User_username.maxLength;
		StartSessionRequest_username.pattern = User_username.pattern;

		StartSessionRequest_password.required = true;
		StartSessionRequest_password.minLength = UserWithPassword_oldPassword.minLength;
		StartSessionRequest_password.maxLength = UserWithPassword_oldPassword.maxLength;
	}
}

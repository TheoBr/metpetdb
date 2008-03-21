package edu.rpi.metpetdb.client.model.validation;

import com.google.gwt.user.client.rpc.IsSerializable;

import edu.rpi.metpetdb.client.error.ValidationException;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisDTO;
import edu.rpi.metpetdb.client.model.ImageDTO;
import edu.rpi.metpetdb.client.model.MObjectDTO;
import edu.rpi.metpetdb.client.model.ProjectDTO;
import edu.rpi.metpetdb.client.model.SampleDTO;
import edu.rpi.metpetdb.client.model.SampleMineralDTO;
import edu.rpi.metpetdb.client.model.StartSessionRequestDTO;
import edu.rpi.metpetdb.client.model.SubsampleDTO;
import edu.rpi.metpetdb.client.model.UserDTO;
import edu.rpi.metpetdb.client.model.XrayImageDTO;

/**
 * Constraints for objects in the database
 * 
 */
public class DatabaseObjectConstraints implements IsSerializable {
	private static void validate(final MObjectDTO o,
			final PropertyConstraint[] pc) throws ValidationException {
		if (pc == null)
			return;
		for (int k = 0; k < pc.length; k++) {
			pc[k].validateEntity(o);
		}
	}

	// ------ Project ------
	public PropertyConstraint[] Project__all;
	public StringConstraint Project_name;
	public UserConstraint Project_owner;

	public void validate(final ProjectDTO s) throws ValidationException {
		validate(s, Project__all);
	}

	// ------ Sample ------
	public PropertyConstraint[] Sample__all;
	public Sample_sesarNumber Sample_sesarNumber;
	public StringConstraint Sample_alias;
	public MineralConstraint Sample_minerals;
	public GeometryConstraint Sample_location;
	public TimestampConstraint Sample_collectionDate;
	public BooleanConstraint Sample_publicData;
	public RockTypeConstraint Sample_rockType;
	public UserConstraint Sample_owner;
	public StringConstraint Sample_country;
	public StringConstraint Sample_description;
	public StringConstraint Sample_collector;
	public StringConstraint Sample_locationText;
	public FloatConstraint Sample_latitudeError;
	public FloatConstraint Sample_longitudeError;
	public MultiValuedStringConstraint Sample_regions;
	public MultiValuedStringConstraint Sample_metamorphicGrades;
	public MultiValuedStringConstraint Sample_references;

	public void validate(final SampleDTO s) throws ValidationException {
		validate(s, Sample__all);
	}

	// ------ Subsample ------
	public PropertyConstraint[] Subsample__all;
	public StringConstraint Subsample_name;
	public SubsampleTypeConstraint Subsample_type;
	public ImageConstraint Subsample_images;

	public void validate(final SubsampleDTO s) throws ValidationException {
		validate(s, Subsample__all);
	}

	// ------ SampleMineral ------
	public PropertyConstraint[] SampleMineral__all;
	public FloatConstraint SampleMineral_amount;

	public void validate(final SampleMineralDTO m) throws ValidationException {
		validate(m, SampleMineral__all);
	}

	// ------- Image ------
	public PropertyConstraint[] Image_all;
	public ImageTypeConstraint Image_imageType;
	public IntegerConstraint Image_lut;
	public IntegerConstraint Image_contrast;
	public IntegerConstraint Image_brightness;
	public IntegerConstraint Image_pixelsize;

	public void validate(final ImageDTO i) throws ValidationException {
		validate(i, Image_all);
	}

	// ------- XrayImage ------
	public PropertyConstraint[] XrayImage_all;
	public BooleanConstraint XrayImage_radiation;
	public StringConstraint XrayImage_lines;
	public IntegerConstraint XrayImage_dwelltime;
	public IntegerConstraint XrayImage_current;
	public IntegerConstraint XrayImage_voltage;
	public CollectionConstraint XrayImage_element;

	public void validate(final XrayImageDTO i) throws ValidationException {
		validate(i, XrayImage_all);
	}

	// ------ ChemicalAnalysis ------
	public PropertyConstraint[] ChemicalAnalysis_all;
	public ImageConstraint ChemicalAnalysis_image;
	public StringConstraint ChemicalAnalysis_spotId;
	public IntegerConstraint ChemicalAnalysis_pointX;
	public IntegerConstraint ChemicalAnalysis_pointY;
	public StringConstraint ChemicalAnalysis_analysisMethod;
	public StringConstraint ChemicalAnalysis_location;
	public StringConstraint ChemicalAnalysis_analyst;
	public TimestampConstraint ChemicalAnalysis_analysisDate;
	public StringConstraint ChemicalAnalysis_description;
	public MineralConstraint ChemicalAnalysis_mineral;
	public BooleanConstraint ChemicalAnalysis_largeRock;

	public void validate(final ChemicalAnalysisDTO ma)
			throws ValidationException {
		validate(ma, ChemicalAnalysis_all);
	}

	// ------ User ------
	public PropertyConstraint[] User__all;
	public RestrictedCharacterStringConstraint User_username;
	public StringConstraint User_emailAddress;

	public void validate(final UserDTO u) throws ValidationException {
		validate(u, User__all);
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

	/**
	 * Called just before this instance is first used.
	 * <p>
	 * This method should only be invoked by the server, and only after it has
	 * finished filling in all of the constraint instances. It can be used to
	 * backfill missing data that we cannot acquire from our database, or to
	 * perform other work based on the newly created constraint instances.
	 * </p>
	 */
	public void finishInitialization() {
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

		// TODO get these from the database, see DataStore.java
		Sample_regions.maxLength = 30;
		Sample_regions.minLength = 0;
		Sample_metamorphicGrades.maxLength = 50;
		Sample_metamorphicGrades.minLength = 0;
		Sample_references.maxLength = 30;
		Sample_references.minLength = 0;
		XrayImage_element.required = true;
	}
}

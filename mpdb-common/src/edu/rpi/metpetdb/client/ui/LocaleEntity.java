package edu.rpi.metpetdb.client.ui;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/** Entity property description message text constants, to support i18n. */
public interface LocaleEntity extends ConstantsWithLookup {
	/* Project */
	String Project();
	String Project_name();

	/* Sample */
	String Sample();
	String Sample_sesarNumber();
	String Sample_alias();
	String Sample_owner();
	String[] Sample_location();
	String Sample_latitude();
	String Sample_longitude();
	String Sample_collectionDate();
	String Sample_rockType();
	String Sample_publicData();
	String Sample_minerals();
	String Sample_country();
	String Sample_description();
	String Sample_collector();
	String Sample_locationText();
	String Sample_latitudeError();
	String Sample_longitudeError();
	String Sample_regions();
	String Sample_metamorphicGrades();
	String Sample_references();
	String Sample_subsampleCount();
	String Sample_editDescription();
	String Sample_editHeader();
	String Sample_viewDescription();
	String Sample_viewHeader();
	
	String SampleMineral_amount();

	/* User */
	String User();
	String User_username();
	String User_password();
	String User_emailAddress();

	/* UserWithPassword */
	String UserWithPassword();
	String UserWithPassword_oldPassword();
	String UserWithPassword_newPassword();
	String UserWithPassword_vrfPassword();

	/* StartSessionRequest */
	String StartSessionRequest();
	String StartSessionRequest_username();
	String StartSessionRequest_password();

	/* ImageBrowser */
	String ImageBrowser();
	String ImageBrowser_width();
	String ImageBrowser_height();

	/* Subsample */
	String Subsample();
	String Subsample_name();
	String Subsample_type();
	String Subsample_images();
	String Subsample_imageCount();
	String Subsample_analysisCount();
	String Subsample_sampleName();
	
	
	/* Mineral Analysis */
	String MineralAnalysis_sample();
	String MineralAnalysis_subsample();
	String MineralAnalysis_spotId();
	String MineralAnalysis_pointX();
	String MineralAnalysis_pointY();
	String MineralAnalysis_method();
	String MineralAnalysis_location();
	String MineralAnalysis_analyst();
	String MineralAnalysis_analysisDate();
	String MineralAnalysis_reference();
	String MineralAnalysis_comment();
	String MineralAnalysis_mineral();
	String MineralAnalysis_image();
	String MineralAnalysis_largeRock();
	
	/* Image */
	String Image_imageType();
	String Image_lut();
	String Image_contrast();
	String Image_brightness();
	String Image_pixelsize();
	
	/* XrayImage */
	String XrayImage_current();
	String XrayImage_voltage();
	String XrayImage_dwelltime();
	String XrayImage_lines();
	String XrayImage_radiation();
	String XrayImage_element();
}
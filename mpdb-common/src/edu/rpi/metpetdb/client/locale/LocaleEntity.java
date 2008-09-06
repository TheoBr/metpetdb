package edu.rpi.metpetdb.client.locale;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/** Entity property description message text constants, to support i18n. */
public interface LocaleEntity extends ConstantsWithLookup {
	/* Project */
	String Project();

	String Project_name();

	String Project_Owner();

	String Project_MemberCount();

	String Project_LastSampleAddded();

	String Project_Actions();

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

	String Sample_datePrecision();

	String Sample_comments();

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
	String ChemicalAnalysis();

	String ChemicalAnalysis_sample();

	String ChemicalAnalysis_subsample();

	String ChemicalAnalysis_spotId();

	String ChemicalAnalysis_pointX();

	String ChemicalAnalysis_pointY();

	String ChemicalAnalysis_method();

	String ChemicalAnalysis_location();

	String ChemicalAnalysis_analyst();

	String ChemicalAnalysis_analysisDate();

	String ChemicalAnalysis_reference();

	String ChemicalAnalysis_comment();

	String ChemicalAnalysis_mineral();

	String ChemicalAnalysis_image();

	String ChemicalAnalysis_largeRock();

	String ChemicalAnalysis_description();

	String ChemicalAnalysis_sampleName();

	String ChemicalAnalysis_subsampleName();

	String ChemicalAnalysis_elements();

	String ChemicalAnalysis_oxides();

	String ChemicalAnalysis_analysisMethod();

	String ChemicalAnalysis_datePrecision();

	String ChemicalAnalysis_precisionUnit();

	String ChemicalAnalysis_precision();

	String ChemicalAnalysis_total();

	/* Image */
	String Image_imageType();

	String Image_lut();

	String Image_contrast();

	String Image_brightness();

	String Image_pixelsize();

	String Image_filename();

	String Image_format();

	String Image_reference();

	String Image_collector();
	
	String Image_scale();
	
	String Image_comments();

	/* XrayImage */
	String XrayImage_current();

	String XrayImage_voltage();

	String XrayImage_dwelltime();

	String XrayImage_lines();

	String XrayImage_radiation();

	String XrayImage_element();

	String XrayImage_lut();

	/* ImageOnGrid */
	String ImageOnGrid();

	String ImageOnGrid_xpos();

	String ImageOnGrid_ypos();

	/* Collection Names */
	String Collection_Mineral();

	String Collection_Oxide();

	String Collection_Element();

	/* Token Space */
	String TokenSpace_Sample_Details();

	String TokenSpace_User_Details();

	String TokenSpace_Project_Details();

	String TokenSpace_Subsample_Details();

	String TokenSpace_ImageBroswer_Details();

	String TokenSpace_ChemicalAnalysis_Details();

	String TokenSpace_ImageListViewer();

	String TokenSpace_Register();

	String TokenSpace_Introduction();

	String TokenSpace_Bulk_Upload();

	String TokenSpace_Search();

	String TokenSpace_Permission_Denied();

	String TokenSpace_Edit_Profile();

	String TokenSpace_All_Samples();

	String TokenSpace_All_Public_Samples();

	String TokenSpace_All_Projects();

	String TokenSpace_Project_Samples();

	String TokenSpace_Samples_For_User();

	String TokenSpace_Enter_Sample();

	String TokenSpace_New_Project();

	String TokenSpace_Enter_Subsample();

	String TokenSpace_Enter_ChemicalAnalysis();

	String TokenSpace_Create_Image_Map();

	String TokenSpace_Edit_Subsample();

	/* Left Side */

	String LeftSide_UserInfo();

	String LeftSide_MySamples();

	String LeftSide_MySubsamples();

	String LeftSide_MyProjects();

	String LeftSide_MySearch();

	String LeftSide_LeftSideLayer();

}

package edu.rpi.metpetdb.client.locale;

import com.google.gwt.i18n.client.ConstantsWithLookup;

/** Entity property description message text constants, to support i18n. */
public interface LocaleEntity extends ConstantsWithLookup {
	/* Project */
	String Project();
	String Project_name();
	String Project_description();
	String Project_Owner();
	String Project_MemberCount();
	String Project_LastSampleAddded();
	String Project_ViewDetails();
	//String Project_ViewMembers();
	/* Sample */
	String Sample();
	String Sample_sesarNumber();
	String Sample_number();
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
	String Sample_regions();
	String Sample_metamorphicGrades();
	String Sample_metamorphicRegions();
	String Sample_references();
	String Sample_geoReferences();
	String Sample_subsampleCount();
	String Sample_analysisCount();
	String Sample_imageCount();
	String Sample_images();
	String Sample_editDescription();
	String Sample_editHeader();
	String Sample_viewDescription();
	String Sample_viewHeader();
	String Sample_datePrecision();
	String Sample_comments();
	String SampleMineral_amount();
	String Sample_locationError();
	String Sample_aliases();
	/* Reference */
	String GeoReference_title();
	String GeoReference_firstAuthor();
	String GeoReference_secondAuthors();
	String GeoReference_journalName();
	String GeoReference_fullText();
	/* User */
	String User();
	String User_name();
	String User_emailAddress();
	String User_address();
	String User_city();
	String User_province();
	String User_country();
	String User_postalCode();
	String User_institution();
	String User_referenceEmail();
	/* UserWithPassword */
	String UserWithPassword();
	String UserWithPassword_oldPassword();
	String UserWithPassword_newPassword();
	String UserWithPassword_vrfPassword();
	/* StartSessionRequest */
	String StartSessionRequest();
	String StartSessionRequest_emailAddress();
	String StartSessionRequest_password();
	/* ImageBrowser */
	String ImageBrowser();
	String ImageBrowser_width();
	String ImageBrowser_height();
	/* Subsample */
	String Subsample();
	String Subsample_name();
	String Subsample_subsampleType();
	String Subsample_images();
	String Subsample_imageCount();
	String Subsample_analysisCount();
	String Subsample_sampleName();
	String Subsample_publicData();
	String Subsample_owner();
	/* Mineral Analysis */
	String ChemicalAnalysis();
	String ChemicalAnalysis_sample();
	String ChemicalAnalysis_subsample();
	String ChemicalAnalysis_spotId();
	String ChemicalAnalysis_referenceX();
	String ChemicalAnalysis_referenceY();
	String ChemicalAnalysis_referenceNames();
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
	String ChemicalAnalysis_publicData();
	String ChemicalAnalysis_stageX();
	String ChemicalAnalysis_stageY();
	String ChemicalAnalysis_owner();
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
	String TokenSpace_Project_Invite();
	String TokenSpace_Project_View_Invites();
	String TokenSpace_Subsample_Details();
	String TokenSpace_ImageBroswer_Details();
	String TokenSpace_ChemicalAnalysis_Details();
	String TokenSpace_ImageListViewer();
	String TokenSpace_Register();
	String TokenSpace_Home();
	String TokenSpace_App();
	String TokenSpace_Bulk_Upload();
	String TokenSpace_Search();
	String TokenSpace_Permission_Denied();
	String TokenSpace_Edit_Profile();
	String TokenSpace_All_Samples();
	String TokenSpace_All_Public_Samples();
	String TokenSpace_All_Projects();
	String TokenSpace_Project_Samples();
	String TokenSpace_Project_Members();
	String TokenSpace_Project_Description();
	String TokenSpace_Samples_For_User();
	String TokenSpace_Enter_Sample();
	String TokenSpace_New_Project();
	String TokenSpace_Enter_Subsample();
	String TokenSpace_Enter_ChemicalAnalysis();
	String TokenSpace_Create_Image_Map();
	String TokenSpace_Edit_Subsample();
	String TokenSpace_My_Invites();
	/* Left Side */
	String LeftSide_UserInfo();
	String LeftSide_MySamples();
	String LeftSide_MySubsamples();
	String LeftSide_MyProjects();
	String LeftSide_MySearch();
	String LeftSide_LeftSideLayer();
	/* Search */
	String Search_Tab_RockType();
	String Search_Tab_Region();
	String Search_Tab_Minerals();
	String Search_Tab_Chemistry();
	String Search_Tab_Other();
	String SearchSample_tabs();
	String SearchSample_possibleRockTypes();
	String SearchSample_Location();
	String SearchSample_elements();
	String SearchSample_oxides();
	String SearchSample_minerals();
	String SearchSample_getPublic();
	/* Sample Comment */
	String SampleComment_text();
	
	/*Role Changes */
	String RoleChange_requestReason();
	String RoleChange_requestDate();
	String RoleChange_role();
	String RoleChange_sponsor();
	String RoleChange_user();
	String RoleChange_grant();
}

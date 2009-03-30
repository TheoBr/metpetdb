package edu.rpi.metpetdb.client.ui.excel;

import com.google.gwt.user.client.ui.Hidden;

import edu.rpi.metpetdb.client.locale.LocaleHandler;

public class ExcelUtil {
	final public static String[] columnHeaders = {
		LocaleHandler.lc_entity.Sample_number(),
		LocaleHandler.lc_entity.Sample_publicData(),
		LocaleHandler.lc_entity.Sample_subsampleCount(),
		LocaleHandler.lc_entity.Sample_imageCount(),
		LocaleHandler.lc_entity.Sample_analysisCount(),
		LocaleHandler.lc_entity.Sample_owner(),
		LocaleHandler.lc_entity.Sample_regions(),
		LocaleHandler.lc_entity.Sample_country(),
		LocaleHandler.lc_entity.Sample_rockType(),
		LocaleHandler.lc_entity.Sample_metamorphicGrades(),
		LocaleHandler.lc_entity.Sample_minerals(),
		LocaleHandler.lc_entity.Sample_references(),
		LocaleHandler.lc_entity.Sample_latitude(),
		LocaleHandler.lc_entity.Sample_longitude(),
		LocaleHandler.lc_entity.Sample_sesarNumber(),
		LocaleHandler.lc_entity.Sample_collector(),
		LocaleHandler.lc_entity.Sample_collectionDate(),
		LocaleHandler.lc_entity.Sample_locationText()
	};
	final public static String columnHeaderParameter = "column";
}

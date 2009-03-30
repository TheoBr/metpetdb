package edu.rpi.metpetdb.client.ui.objects.list;

import java.util.ArrayList;

import org.postgis.Point;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.locale.LocaleEntity;
import edu.rpi.metpetdb.client.locale.LocaleHandler;
import edu.rpi.metpetdb.client.model.Sample;
import edu.rpi.metpetdb.client.model.User;
import edu.rpi.metpetdb.client.model.properties.SampleProperty;
import edu.rpi.metpetdb.client.ui.TokenSpace;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.MLink;
import edu.rpi.metpetdb.client.ui.widgets.paging.CollapsedColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.Column;
import edu.rpi.metpetdb.client.ui.widgets.paging.MultipleStringColumn;
import edu.rpi.metpetdb.client.ui.widgets.paging.StringColumn;

public abstract class SampleList extends List<Sample> {

	private static final LocaleEntity enttxt = LocaleHandler.lc_entity;

	private static ArrayList<Column<Sample, ?>> columns;
	static {
		columns = new ArrayList<Column<Sample, ?>>();
		columns.add(new Column<Sample, MCheckBox>("Check") {
			@Override
			public MCheckBox getCellValue(Sample rowValue) {
				return new MCheckBox(rowValue);
			}
		});

		columns.add(new Column<Sample, MLink>(enttxt.Sample_number(),
				SampleProperty.number) {
			public MLink getCellValue(Sample rowValue) {
				return new MLink((String) rowValue.mGet(SampleProperty.number),
						TokenSpace.detailsOf(rowValue));
			}
		});

		columns.add(new Column<Sample, Image>(enttxt.Sample_publicData(),
				SampleProperty.publicData) {
			public Image getCellValue(Sample rowValue) {
				if ((Boolean) rowValue.mGet(SampleProperty.publicData)) {
					return new Image("images/checkmark.jpg") {
						public String toString() {
							return "True";
						}
					};
				}
				return new Image("images/xmark.jpg") {
					public String toString() {
						return "False";
					}
				};
			}
		});
		columns.add(new StringColumn<Sample>(enttxt.Sample_subsampleCount(),
				SampleProperty.subsampleCount));
		columns.add(new StringColumn<Sample>(enttxt.Sample_imageCount(),
				SampleProperty.imageCount));
		columns.add(new StringColumn<Sample>(enttxt.Sample_analysisCount(),
				SampleProperty.analysisCount));
		columns.add(new Column<Sample, MLink>(enttxt.Sample_owner(),
				SampleProperty.owner) {
			public MLink getCellValue(final Sample rowValue) {
				return new MLink(((User) rowValue.mGet(SampleProperty.owner))
						.getName(), TokenSpace.detailsOf((User) rowValue
						.mGet(SampleProperty.owner)));
			}
		});

		columns.add(new MultipleStringColumn<Sample>(enttxt.Sample_regions(),
				SampleProperty.regions));

		columns.add(new StringColumn<Sample>(enttxt.Sample_country(),
				SampleProperty.country));
		columns.add(new StringColumn<Sample>(enttxt.Sample_rockType(),
				SampleProperty.rockType));

		columns.add(new MultipleStringColumn<Sample>(enttxt
				.Sample_metamorphicGrades(), SampleProperty.metamorphicGrades));

		columns.add(new CollapsedColumn<Sample>(enttxt.Sample_minerals(),
				SampleProperty.minerals));

		columns.add(new MultipleStringColumn<Sample>(
				enttxt.Sample_references(), SampleProperty.references));

		columns.add(new StringColumn<Sample>("Location",
				SampleProperty.location) {
			@Override
			public String getCellValue(Sample rowValue) {
				final Point location = (Point) rowValue
						.mGet(SampleProperty.location);
				return "("
						+ ListExUtil.formatDouble(location.x,
								ListExUtil.latlngDigits)
						+ ","
						+ ListExUtil.formatDouble(location.y,
								ListExUtil.latlngDigits) + ")";
			}
		});

		columns.add(new Column<Sample, MLink>(enttxt.Sample_sesarNumber(),
				SampleProperty.sesarNumber) {
			@Override
			public MLink getCellValue(final Sample rowValue) {
				return new MLink((String) rowValue
						.mGet(SampleProperty.sesarNumber), TokenSpace
						.detailsOf(rowValue));
			}
		});

		columns.add(new StringColumn<Sample>(enttxt.Sample_collector(),
				SampleProperty.collector));

		columns.add(new StringColumn<Sample>(enttxt.Sample_collectionDate(),
				SampleProperty.collectionDate) {
			@Override
			public String getCellValue(Sample rowValue) {
				return Sample.dateToString(rowValue.getCollectionDate(),
						rowValue.getDatePrecision());
			}
		});

		columns.add(new StringColumn<Sample>(enttxt.Sample_locationText(),
				SampleProperty.locationText));

	}

	public SampleList() {
		super(columns);
	}

	@Override
	public String getDefaultSortParameter() {
		return "number";
	}

	@Override
	protected Widget getNoResultsWidget() {
		return new Label("No Sample Results");
	}

}

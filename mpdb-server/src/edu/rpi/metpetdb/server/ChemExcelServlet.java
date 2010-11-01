package edu.rpi.metpetdb.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Query;
import org.hibernate.Session;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.MObject;
import edu.rpi.metpetdb.client.model.SampleMineral;
import edu.rpi.metpetdb.client.model.properties.Property;

public class ChemExcelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String chemParameter = "ChemicalAnalyses";

	@Override
	protected void doGet(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException {
		response.setContentType("attachment/tab-separated-values");
		response
				.addHeader("Content-Disposition", "inline;filename=results.tsv");

		Session session = DataStore.open();
		try {
			session.beginTransaction();

			List<ChemicalAnalysis> chemicalAnalyses = new LinkedList<ChemicalAnalysis>();
			Query q;

			// If there is a GET string, fetch by ids
			if (request.getParameter(chemParameter) != null) {
				String ids[] = request.getParameterValues(chemParameter);
				q = session.getNamedQuery("ChemicalAnalysis.byId");
				for (int i = 0; i < ids.length; i++) {
					q.setParameter("id", Integer.parseInt(ids[i]));
					chemicalAnalyses.add((ChemicalAnalysis) q.uniqueResult());
				}
			}

			String headers[] = request.getParameterValues("column");

			// output the column headers
			if (request.getParameter("column") != null) {
				for (int i = 0; i < headers.length; i++) {
					response.getWriter().write(headers[i] + "\t");
				}
			}
			response.getWriter().write("\n");

			List<String> chemHeaders = Arrays.asList(headers);
			chemHeaders = chemHeaders.subList(7, 150);

			for (ChemicalAnalysis s : chemicalAnalyses) {

				// if ((s.getElements() == null || s.getElements().size() == 0)
				// && (s.getOxides() == null || s.getOxides().size() == 0))
				// writeChemicalAnalysisColumns(response, s);
				// else
				// {

				writeChemicalAnalysisColumns(response, s);

				for (String chemHeader : chemHeaders) {
					for (ChemicalAnalysisOxide cao : s.getOxides()) {
						if (chemHeader.equals(cao.getOxide().getSpecies())) {
							response.getWriter().write(
									cao.getAmount().toString());

						}
					}
					for (ChemicalAnalysisElement cae : s.getElements()) {
						if (chemHeader.equals(cae.getElement().getSymbol())) {
							response.getWriter().write(
									cae.getAmount().toString());

						}
					}

					response.getWriter().write("\t");

				}

				// }
				/*
				 * for (ChemicalAnalysisElement e :s.getElements()) {
				 * writeChemicalAnalysisColumns(response, s);
				 * 
				 * 
				 * for (String headerCol : headers)
				 * 
				 * if (e.getMeasurementUnit().equals("WT%"))
				 * response.getWriter().write(e.getAmount().toString() + "\t");
				 * else response.getWriter().write((e.getAmount() * 10000) +
				 * "\t");
				 * 
				 * response.getWriter().write(e.getName() + "\t");
				 * response.getWriter().write(e.getElement().getSymbol() +
				 * "\t");
				 * response.getWriter().write(e.getElement().getAtomicNumber() +
				 * "\t");
				 * response.getWriter().write(e.getElement().getWeight().toString
				 * () + "\t"); }
				 */

				// TODO: order by id
				/*
				 * for (ChemicalAnalysisOxide e : s.getOxides()) {
				 * writeChemicalAnalysisColumns(response, s);
				 * 
				 * 
				 * response.getWriter().write(e.getAmount().toString() + "\t");
				 * response.getWriter().write(e.getPrecisionUnit() + "\t");
				 * response.getWriter().write(e.getMeasurementUnit() + "\t");
				 * response.getWriter().write(e.getMinAmount().toString() +
				 * "\t"); response.getWriter().write(e.getMaxAmount().toString()
				 * + "\t"); response.getWriter().write(e.getName() + "\t"); //
				 * response.getWriter().write(e.getOxide().getSymbol() + "\t");
				 * // response.getWriter().write(e.getOxide().getAtomicNumber()
				 * + "\t");
				 * response.getWriter().write(e.getOxide().getWeight().toString
				 * () + "\t"); } }
				 */

				/*
				 * response.getWriter().write(boolToString(s.isPublicData()) +
				 * "\t"); response.getWriter().write(s.getSubsampleCount() +
				 * "\t"); response.getWriter().write(s.getImageCount() + "\t");
				 * response.getWriter().write(s.getAnalysisCount() + "\t");
				 * response.getWriter().write(s.getOwner().getName() + "\t");
				 * response
				 * .getWriter().write(setToString(s.getRegions(),RegionProperty
				 * .name) + "\t");
				 * response.getWriter().write(nullToEmptyString(s.getCountry())
				 * + "\t");
				 * response.getWriter().write(nullToEmptyString(s.getRockType())
				 * + "\t");
				 * response.getWriter().write(setToString(s.getMetamorphicGrades
				 * (),MetamorphicGradeProperty.name) + "\t");
				 * response.getWriter(
				 * ).write(setSampleMineralsToString(s.getMinerals()) + "\t");
				 * response.getWriter().write(setToString(s.getReferences(),
				 * ReferenceProperty.name) + "\t");
				 * response.getWriter().write(formatlatlng
				 * (((Point)s.getLocation()).y) +"\t");
				 * response.getWriter().write
				 * (formatlatlng(((Point)s.getLocation()).x) +"\t");
				 * response.getWriter
				 * ().write(nullToEmptyString(s.getSesarNumber()) + "\t");
				 * response
				 * .getWriter().write(nullToEmptyString(s.getCollector()) +
				 * "\t");response.getWriter().write(Sample.dateToString(s.
				 * getCollectionDate(), s.getDatePrecision()) + "\t");
				 * response.getWriter
				 * ().write(nullToEmptyString(s.getLocationText()) + "\t");
				 */
				if (s.getTotal() != null)
					response.getWriter().write(
							nullToEmptyString(s.getTotal().toString()));

				// TODO: the rest of the chemical analyses fields ...
				response.getWriter().write("\n");
			}
		} catch (final IOException ioe) {
			throw new IllegalStateException(ioe.getMessage());
		} finally {
			session.close();
		}

		return;
	}

	private void writeChemicalAnalysisColumns(
			final HttpServletResponse response, ChemicalAnalysis s)
			throws IOException {
		response.getWriter().write(nullToEmptyString(s.getSampleName()) + "\t");
		response.getWriter().write(
				nullToEmptyString(s.getSubsampleName()) + "\t");
		response.getWriter().write(s.getSpotId() + "\t");

		response.getWriter().write(
				nullToEmptyString(s.getAnalysisMaterial()) + "\t");
		response.getWriter().write(
				nullToEmptyString(s.getAnalysisMethod()) + "\t");
		response.getWriter().write(
				nullToEmptyString(s.getSubsample().getSubsampleType()) + "\t");
		response.getWriter().write(nullToEmptyString(s.getAnalyst()) + "\t");
	}

	private String boolToString(final Boolean b) {
		return (b) ? "yes" : "no";
	}

	private String nullToEmptyString(final Object o) {
		return (o == null) ? "" : o.toString();
	}

	private String setToString(final Set<?> values, final Property property) {
		String text = "";
		for (Object o : values) {
			text += ((MObject) o).mGet(property) + ", ";
		}
		if (!text.equals("")) {
			text = text.substring(0, text.length() - 2);
		}
		return text;
	}

	private String setSampleMineralsToString(final Set<SampleMineral> minerals) {
		String text = "";
		for (SampleMineral sm : minerals) {
			text += sm.toString() + ", ";
		}
		if (!text.equals("")) {
			text = text.substring(0, text.length() - 2);
		}
		return text;
	}

	private static String formatlatlng(final double loc) {
		final int latlngDigits = 5;
		final String locStr = String.valueOf(loc);
		final int decPos = locStr.toString().indexOf(".");
		if (locStr.length() <= decPos + latlngDigits && decPos >= 0)
			return locStr;
		return locStr.substring(0, decPos + latlngDigits);
	}
}

package edu.rpi.metpetdb.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

import javax.persistence.Column;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit38.AbstractTransactionalJUnit38SpringContextTests;
import org.springframework.transaction.annotation.Transactional;

import com.google.gwt.validation.client.InvalidConstraint;
import com.google.gwt.validation.server.ServerValidator;

import edu.rpi.metpetdb.server.dao.MetamorphicGradeDAO;
import edu.rpi.metpetdb.server.dao.MineralDAO;
import edu.rpi.metpetdb.server.dao.ReferenceDAO;
import edu.rpi.metpetdb.server.dao.SampleDAO;
import edu.rpi.metpetdb.server.dao.UserDAO;
import edu.rpi.metpetdb.server.model.Georeference;
import edu.rpi.metpetdb.server.model.Reference;
import edu.rpi.metpetdb.server.model.Sample;
import edu.rpi.metpetdb.server.model.User;
import edu.rpi.metpetdb.server.service.ReferenceUploadService;

@ContextConfiguration
public class POITest extends AbstractTransactionalJUnit38SpringContextTests {
	/*
	 * public void testPOI() throws Exception { File forig = new File(
	 * "/Users/scball/Downloads/ExampleUploadSheet (1).xls"); File ferr = new
	 * File(forig.getAbsolutePath() + "Errors at " + System.currentTimeMillis()
	 * + " for " + forig.getName());
	 * 
	 * Workbook wb = new WorkbookFactory().create(new FileInputStream(forig));
	 * 
	 * CreationHelper factory = wb.getCreationHelper();
	 * 
	 * for (int i = 0; i < wb.getNumberOfSheets(); i++) { Sheet currentSheet =
	 * wb.getSheetAt(i);
	 * 
	 * doIt2(currentSheet); } }
	 */

	private User createUser() {
		User user = new User(0L, 1, "foo@foo.com", "Foo Bar", "123 Foo St.",
				"Albany", "NY", "US", "11111", "Baz", "bar@bar.com",
				Boolean.TRUE, "ASFDS", new String("foo").getBytes());

		UserDAO userDAO = (UserDAO) applicationContext.getBean("userDAO");

		userDAO.saveUser(user);

		Long persistedUserId = user.getUser_id();

		User persistedUser = userDAO.loadUser(persistedUserId);

		return persistedUser;
	}

	@Transactional
	public void testReferenceUpload() throws FileNotFoundException,
			IOException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		File archiveDir = new File(
				"/Users/scball/Downloads/BibliograhicArchive");

		for (File currFile : archiveDir.listFiles()) {

			Georeference geoRef = doReferenceParse(currFile);
			ReferenceDAO refDAO = (ReferenceDAO) applicationContext
					.getBean("referenceDAO");

			Reference ref = new Reference(null, geoRef.getReferenceNumber());

			geoRef.setReference(ref);

			refDAO.saveGeoreference(ref);

		}

	}

	private Georeference doReferenceParse(File currFile) {
		FileInputStream fis = null;
		ByteArrayOutputStream baos = null;

		try {

			fis = new FileInputStream(currFile);
			baos = new ByteArrayOutputStream();
			byte[] chunk = new byte[2048];

			int ready = 0;
			while ((ready = fis.read(chunk)) > 0) {
				baos.write(chunk, 0, chunk.length);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {

			try {
				baos.flush();
				baos.close();
				fis.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return new Georeference(new String(baos.toByteArray()));
	}

	public void XXXtestPOI2() throws Exception {

		File forig = new File(
				"/Users/scball/Downloads/ExampleUploadSheet (1).xls");
		File ferr = new File(forig.getAbsolutePath() + "Errors at "
				+ System.currentTimeMillis() + " for " + forig.getName());

		Workbook wb = new WorkbookFactory().create(new FileInputStream(forig));

		CreationHelper factory = wb.getCreationHelper();

		doIt2(wb, wb.getSheetAt(0));

		wb.write(new FileOutputStream(ferr));

	}

	// Detect the type of spreadsheet

	/*
	 * for (Row row : sheet) {
	 * 
	 * // Load the validation rules
	 * 
	 * for (Cell cell : row) {
	 * 
	 * CellStyle style = wb.createCellStyle();
	 * 
	 * try {
	 * 
	 * style.setFillBackgroundColor(IndexedColors.WHITE .getIndex());
	 * cell.removeCellComment();
	 * 
	 * } catch (Exception e) { style.setFillBackgroundColor(IndexedColors.RED
	 * .getIndex());
	 * 
	 * ClientAnchor anchor = factory.createClientAnchor(); Drawing drawing =
	 * sheet.createDrawingPatriarch();
	 * 
	 * Comment comment = drawing.createCellComment(anchor); RichTextString str =
	 * factory .createRichTextString("Hello, World!"); comment.setString(str);
	 * comment.setAuthor("sball"); // assign the comment to the cell
	 * cell.setCellComment(comment);
	 * 
	 * }
	 * 
	 * cell.setCellStyle(style);
	 * 
	 * } }
	 * 
	 * }
	 * 
	 * wb.write(new FileOutputStream(ferr));
	 * 
	 * }
	 */
	/*
	 * public RowType analyzeHeaderRow(Row row) { for (Cell cell : row) {
	 * switch(cell.getCellType()) { case Cell.CELL_TYPE_STRING:
	 * 
	 * break;
	 * 
	 * default:
	 * 
	 * 
	 * 
	 * } } }
	 */

	/*
	 * public void doIt(Sheet sheet) {
	 * 
	 * Row headerRow = sheet.getRow(0);
	 * 
	 * SampleHeaderColumnMap sampleHeaderMap = new SampleHeaderColumnMap( new
	 * MineralDAOImpl()); ColumnMapper mapper = new ColumnMapper();
	 * mapper.add(sampleHeaderMap);
	 * 
	 * // Build the header column map ColumnMap columnMap =
	 * mapper.detectColumnMap(headerRow);
	 * 
	 * if (columnMap != null) System.out.println("score: " +
	 * columnMap.matchScore());
	 * 
	 * System.out.println("Available Header Maps"); for (ColumnMap currentMap :
	 * mapper.listMaps()) { System.out.println(currentMap.getMapName());
	 * 
	 * // columnMap = mapper.get(currentMap.getMapName());
	 * 
	 * }
	 * 
	 * // RowValidator rowValidator = columnMap.buildRowValidator();
	 * 
	 * RowPersistor rowPersistor = new SampleRowPersistor();
	 * 
	 * // TODO: Determine a row persistor
	 * 
	 * for (int i = (sheet.getFirstRowNum() + 1); i <= sheet.getLastRowNum();
	 * i++) { System.out.println(sheet.getSheetName() + " " + i); //
	 * processRow(rowValidator, rowPersistor, sheet.getRow(i));
	 * 
	 * } // Run the validations
	 * 
	 * // If there aren't any row validations, then write to the db // else
	 * write the row with cell comments to a new excel spreadsheet }
	 */
	public void doIt2(Workbook wb, Sheet sheet) {

		Row headerRow = sheet.getRow(0);

		// TODO: Determine a row persistor

		// for (int i = (sheet.getFirstRowNum() + 1); i <=
		// sheet.getLastRowNum(); i++) {

		int i = 1;

		MineralDAO mineralDAO = (MineralDAO) this.applicationContext
				.getBean("mineralDAO");

		SampleDAO sampleDAO = (SampleDAO) this.applicationContext
				.getBean("sampleDAO");

		MetamorphicGradeDAO metamorphicGradeDAO = (MetamorphicGradeDAO) this.applicationContext
				.getBean("metamorphicGradeDAO");

		System.out.println(sheet.getSheetName() + " " + i);

		Sample currentSample = new Sample(headerRow, sheet.getRow(i),
				mineralDAO.loadMinerals());
		assertEquals(currentSample.getNumber(), "NM001");
		assertEquals(currentSample.getRockType().getTypeName(), "quartzite");

		assertEquals(currentSample.getCountry(), "USA");
		assertEquals(currentSample.getCollector(), "Benjamin Hallett");
		assertEquals(currentSample.getDescription(), "Ortega Quartzite");
		// assertEquals(currentSample.getCollectionDate().getDay(), 1);
		assertEquals(currentSample.getCollectionDate().getMonth(), 7);
		assertEquals(currentSample.getCollectionDate().getYear() + 1900, 2006);
		assertEquals(currentSample.getLocationText(), "RPI");
		assertEquals(currentSample.getLocationError(), 5.0f);
		assertEquals(currentSample.getVersion(), 0);
		assertEquals(currentSample.getSesarNumber(), null);

		assertNotNull(currentSample.getLocation());

		assertEquals(currentSample.getRegions().size(), 3);
		assertEquals(currentSample.getMetamorphicGrades().size(), 1);

		assertEquals(currentSample.getMinerals().size(), 5);

		// currentSample.setOwner(createUser());

		metamorphicGradeDAO.loadExistingGrades(currentSample);

		// currentSample.getMetamorphicGrades().add(metamorphicDAO.loadMetamorpicGrade);

		ServerValidator<Sample> sampleValidator = new ServerValidator<Sample>();
		Set<InvalidConstraint<Sample>> sampleConstraints = sampleValidator
				.validate(currentSample);

		if (sampleConstraints.size() > 0) {
			// write to the spreadsheet
			flagError(wb, sheet, sheet.getRow(i), sampleConstraints);

		}

		for (InvalidConstraint<Sample> currentConstraint : sampleConstraints) {
			System.out.println(currentConstraint.getItemName() + ":"
					+ currentConstraint.getMessage());
		}
		// sampleDAO.saveSample(currentSample);

		// }
		// Run the validations

		// If there aren't any row validations, then write to the db
		// else write the row with cell comments to a new excel spreadsheet
	}

	public void XXXtestReflection() {

		for (Method currentMethod : Sample.class.getMethods()) {

			// if (currentMethod.getName().startsWith("get")) {
			Annotation[] annotations = currentMethod.getDeclaredAnnotations();

			for (Annotation annotation : annotations) {
				if (annotation instanceof javax.persistence.Column) {
					Column myAnnotation = (Column) annotation;
					System.out.println("name: " + myAnnotation.name());
					System.out.println("insertable: "
							+ myAnnotation.insertable());
					System.out.println("length: " + myAnnotation.length());
					System.out
							.println("precision: " + myAnnotation.precision());
					System.out.println("null allowed: "
							+ myAnnotation.nullable());
					System.out.println("return type"
							+ currentMethod.getReturnType());

				} else if (annotation instanceof Foo) {
					System.out.println("========="
							+ ((Foo) annotation).expression());
				}
			}
		}
		// }
	}

	/*
	 * public void processRow( RowPersistor rowPersistor, Row row) {
	 * RowProcessor rowProcessor = new StandardRowProcessor(null, rowPersistor);
	 * rowProcessor.processRow(row); }
	 */

	public void flagError(Workbook wb, Sheet sheet, Row row,
			Set<InvalidConstraint<Sample>> violatedConstraints) {
		CreationHelper factory = wb.getCreationHelper();
		CellStyle style = wb.createCellStyle();

		// try {

		style.setFillBackgroundColor(IndexedColors.RED.getIndex());
		style.setFillForegroundColor(IndexedColors.RED.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// cell.removeCellComment();

		// } catch (Exception e) {
		// style.setFillBackgroundColor(IndexedColors.RED
		// * .getIndex());
		// *
		ClientAnchor anchor = factory.createClientAnchor();
		Drawing drawing = sheet.createDrawingPatriarch();

		Comment comment = drawing.createCellComment(anchor);
		comment.setVisible(true);

		StringBuffer sb = new StringBuffer();
		for (InvalidConstraint<?> constraints : violatedConstraints) {
			sb.append(constraints.getItemName() + ":"
					+ constraints.getMessage() + "\n");
		}

		RichTextString str = factory.createRichTextString(sb.toString());

		comment.setString(str);
		comment.setAuthor("sball"); // assign the comment to the cell

		Cell cell = row.createCell(row.getLastCellNum() + 1);

		cell.setCellComment(comment);
		cell.setCellValue(sb.toString());

	}

	public static void main(String args[])

	{
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"edu/rpi/metpetdb/server/POITest-context.xml");
		
		ReferenceUploadService svc = (ReferenceUploadService)applicationContext.getBean("referenceUploadService");
		
		svc.upload(svc.loadReferenceFile(new File("/Users/scball/Downloads/BibFiles8-13-10/2006-030965.txt")));
	}
	
}

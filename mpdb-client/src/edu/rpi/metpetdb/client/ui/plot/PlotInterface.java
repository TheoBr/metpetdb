package edu.rpi.metpetdb.client.ui.plot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosureEvent;
import com.google.gwt.user.client.ui.DisclosureHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import edu.rpi.metpetdb.client.model.ChemicalAnalysis;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisElement;
import edu.rpi.metpetdb.client.model.ChemicalAnalysisOxide;
import edu.rpi.metpetdb.client.model.Element;
import edu.rpi.metpetdb.client.model.Oxide;
import edu.rpi.metpetdb.client.ui.MpDb;
import edu.rpi.metpetdb.client.ui.commands.ServerOp;
import edu.rpi.metpetdb.client.ui.objects.list.ChemicalAnalysisList;
import edu.rpi.metpetdb.client.ui.plot.charts.MLinePlot;
import edu.rpi.metpetdb.client.ui.plot.charts.MPlot;
import edu.rpi.metpetdb.client.ui.plot.charts.ScatterPlot;
import edu.rpi.metpetdb.client.ui.plot.charts.TernaryPlot;
import edu.rpi.metpetdb.client.ui.plot.charts.TetrahedralPlot;
import edu.rpi.metpetdb.client.ui.widgets.MCheckBox;
import edu.rpi.metpetdb.client.ui.widgets.NumericKeyboardListener;
import edu.rpi.metpetdb.client.ui.widgets.panels.MTwoColPanel;

public class PlotInterface implements ClickListener, ChangeListener{
	
	private ChemicalAnalysisList chemList;
	private MPlot currentGraph;
	
	private FlowPanel axisContainer = new FlowPanel();

	private List<Element> plottableElements = new ArrayList<Element>();
	private List<Oxide> plottableOxides = new ArrayList<Oxide>();
	
	private List<FlowPanel> axisFormulaContainers = new ArrayList<FlowPanel>();
	
	private RadioButton mono = new RadioButton("tetra","Mono");
	private RadioButton stereo = new RadioButton("tetra","Stereoscopic");

	private MCheckBox moles = new MCheckBox("Convert to moles");
	
	private class FormulaCell extends FlowPanel{
		public TextBox value;
		public HTML label;
		
		public FormulaCell(){
			value = new TextBox();
			value.addKeyboardListener(new NumericKeyboardListener(false,true));
			value.setWidth("40px");
			label = new HTML();
			add(value);
			add(label);
		}
		
		public FormulaCell(double val, String html){
			this();
			value.setText(String.valueOf(val));
			label.setHTML(html);
		}
	}
	
	private Widget createAxisContainer(){
/*		axisContainer.clear();
		final ListBox plotTypes = new ListBox();
		
		plotTypes.addItem("Custom...");
		
		plotTypes.addChangeListener(new ChangeListener(){
			public void onChange(final Widget sender){
				
			}
		});
		axisContainer.add(plotTypes); */
		
		new ServerOp<List<ChemicalAnalysis>>() {
			public void begin() {
				final List<Integer> ids = new ArrayList<Integer>();
				for (Object id : chemList.getSelectedValues()){
					ids.add((Integer) id);
				}
				MpDb.chemicalAnalysis_svc.details(ids, this);
			}

			public void onSuccess(final List<ChemicalAnalysis> result) {
				if (axisFormulaContainers.size() >= currentGraph.getAxisCount()){
					for (int i = axisFormulaContainers.size()-1; i >= currentGraph.getAxisCount(); i--){
						axisContainer.remove(axisFormulaContainers.get(i).getParent());
						axisFormulaContainers.remove(i);
					}
				} else {
					for (int i = axisFormulaContainers.size()+1; i <= currentGraph.getAxisCount(); i++){					
						final FlowPanel individualAxisContainer = new FlowPanel();
						final Label axisNum = new Label(String.valueOf(i));
						individualAxisContainer.add(axisNum);
						
						final FlowPanel formulaContainer = new FlowPanel();
						axisFormulaContainers.add(formulaContainer);
						final HorizontalPanel numeratorContainer = new HorizontalPanel();
						
						formulaContainer.add(numeratorContainer);
						
						FormulaCell numeratorConstant = new FormulaCell(0,"");
						numeratorContainer.add(numeratorConstant);
						individualAxisContainer.add(formulaContainer);
						
						final ClickListener boxListener = new ClickListener(){
							public void onClick(final Widget sender){
								CheckBox temp = (CheckBox) sender;
								if (temp.isChecked()){
									FormulaCell numeratorCell = new FormulaCell(0,temp.getHTML());
									numeratorContainer.add(new Label("+"));
									numeratorContainer.add(numeratorCell);
								} else {
									Set<Widget> toRemoveNum = new HashSet<Widget>();
									for (int i = 0; i < numeratorContainer.getWidgetCount(); i++){
										if (numeratorContainer.getWidget(i) instanceof FormulaCell){
											if (((FormulaCell) numeratorContainer.getWidget(i)).label.getHTML().equals(temp.getHTML())){
												toRemoveNum.add(numeratorContainer.getWidget(i));
												toRemoveNum.add(numeratorContainer.getWidget(i-1));
												break;
											}
										}
									}
									for (Widget w: toRemoveNum)
										numeratorContainer.remove(w);
								}
							}
						};
						
						final DisclosurePanel editSpecies = new DisclosurePanel();
						editSpecies.setHeader(new Label("Edit species..."));
						editSpecies.addEventHandler(new DisclosureHandler(){

							public void onClose(DisclosureEvent event) {

							}

							public void onOpen(DisclosureEvent event) {
								editSpecies.clear();
								new ServerOp<List<ChemicalAnalysis>>() {
									public void begin() {
										final List<Integer> ids = new ArrayList<Integer>();
										for (Object id : chemList.getSelectedValues()){
											ids.add((Integer) id);
										}
										MpDb.chemicalAnalysis_svc.details(ids, this);
									}

									public void onSuccess(final List<ChemicalAnalysis> result2) {
										editSpecies.clear();
										final FlowPanel speciesContainer = new FlowPanel();
										Set<String> displayNames = getPlottableSpecies(result2);
										if (displayNames.size() == 0){
											speciesContainer.add(new Label("No elements or oxides present in selected analyses"));
										}
										for (String name : displayNames){
											final CheckBox box = new CheckBox();
											box.setHTML(name);
											box.addClickListener(boxListener);
											speciesContainer.add(box);
											
										}
										editSpecies.add(speciesContainer);
									}
								}.begin();
							}
							
						});
						editSpecies.setOpen(true);
						individualAxisContainer.add(editSpecies);
						
						axisContainer.add(individualAxisContainer);
					}
				}
			}
		}.begin();
		
		return axisContainer;
	}
	
	private Set<String> getPlottableSpecies(List<ChemicalAnalysis> analyses){
		Set<String> displayNames = new HashSet<String>();
		plottableElements.clear();
		plottableOxides.clear();
		for (ChemicalAnalysis ca : analyses){
			for (ChemicalAnalysisElement cae : ca.getElements()){
				if (!displayNames.contains(cae.getElement().getSymbol())) {
					displayNames.add(cae.getElement().getSymbol());
					plottableElements.add(cae.getElement());
				}
			}
			for (ChemicalAnalysisOxide cao : ca.getOxides()){
				if (!displayNames.contains(cao.getDisplayName())) {
					displayNames.add(cao.getDisplayName());
					plottableOxides.add(cao.getOxide());
				}
			}
		}
		return displayNames;
	}
	
	private void onGraphTypeChange(final MPlot newGraph){
		currentGraph = newGraph;
		panel.getRightCol().clear();
		panel.getRightCol().add(currentGraph.createWidget(new ArrayList<ChemicalAnalysis>(),
				new ArrayList<AxisFormula>(), new HashMap<Integer,Set<Integer>>(), false));
		createAxisContainer();
	}
	
	private Widget createGraphTypeContainer(){
		final FlowPanel graphTypeContainer = new FlowPanel();
		
		final Button oneAxis = new Button("1 axis");
		final Button twoAxes = new Button("2 axes");
		final Button threeAxes = new Button("3 axes");
		final Button fourAxes = new Button("4 axes");
		final Button help = new Button("?");
		
		ClickListener graphTypeListener = new ClickListener(){
			public void onClick(final Widget sender){
				if (sender == help){
					//TODO popup with some help 
				} else {
					removeMonoStereo();
					if (sender == oneAxis){
						onGraphTypeChange(new MLinePlot());
					} else if (sender == twoAxes){
						onGraphTypeChange(new ScatterPlot());
					} else if (sender == threeAxes){
						onGraphTypeChange(new TernaryPlot(700,700,500));
					} else if (sender == fourAxes){
						onGraphTypeChange(new TetrahedralPlot(700,700,500));
						mono.addClickListener(new ClickListener(){
							public void onClick(final Widget sender){
								((TetrahedralPlot)currentGraph).setViewType(TetrahedralPlot.VIEW_TYPE.MONO);
							}
						});
						stereo.addClickListener(new ClickListener(){
							public void onClick(final Widget sender){
								((TetrahedralPlot)currentGraph).setViewType(TetrahedralPlot.VIEW_TYPE.STEREO);
							}
						});
						mono.setChecked(true);
						axisContainer.insert(mono,0);
						axisContainer.insert(stereo,1);
						
//						onGraphTypeChange(new TetrahedralPlotO3D());
					} 
				}
			}
		};
		
		oneAxis.addClickListener(graphTypeListener);
		twoAxes.addClickListener(graphTypeListener);
		threeAxes.addClickListener(graphTypeListener);
		fourAxes.addClickListener(graphTypeListener);
		help.addClickListener(graphTypeListener);
		
		graphTypeContainer.add(oneAxis);
		graphTypeContainer.add(twoAxes);
		graphTypeContainer.add(threeAxes);
		graphTypeContainer.add(fourAxes);
		graphTypeContainer.add(help);
		graphTypeContainer.add(moles);
		
		return graphTypeContainer;
	}
	
	private void removeMonoStereo(){
		if (mono.isAttached()){
			mono.removeFromParent();
			stereo.removeFromParent();
		}
	}
		
	private Button draw = new Button("Draw");	
	private Button export = new Button("Export");
	private DisclosurePanel disclosurePanel;
	private MTwoColPanel panel = new MTwoColPanel();
	private Label noticeLbl = new Label("Please note that the plotting " +
			"feature is under construction and may not work as intended. " +
			"If you have any problems please report them to the developers.");
	
	public void onClick(final Widget sender){
	
	}
	
	public void onChange(final Widget sender){
		
	}
	
	public PlotInterface(final ChemicalAnalysisList list){
		this();
		chemList = list;
	}
	
	public PlotInterface(){

		panel.setRightColWidth("60%");
		panel.setLeftColWidth("40%");
		
		export.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				String svg = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?>";
				svg += "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010904//EN\"";
				svg += "\"http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd\">";
				
				final FormPanel fp = new FormPanel();
				fp.setMethod(FormPanel.METHOD_GET);
				fp.setEncoding(FormPanel.ENCODING_URLENCODED);
				final HorizontalPanel hp = new HorizontalPanel();
				Hidden data = new Hidden("data", svg+currentGraph.getSVG());
				hp.add(data);
				fp.add(hp);
				fp.setAction(GWT.getModuleBaseURL() + "svg.svc?");
				fp.setVisible(false);
				RootPanel.get().add(fp);
				fp.submit();
				panel.getLeftCol().add(new Label(svg+currentGraph.getSVG()));
			}
		});
		
		draw.addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				final List<AxisFormula> formulas = new ArrayList<AxisFormula>();
				
				for (FlowPanel fp : axisFormulaContainers){
					AxisFormula axisForm = new AxisFormula();
					HorizontalPanel numerator = (HorizontalPanel) fp.getWidget(0);
					for (int i = 0 ; i < numerator.getWidgetCount(); i++){
						if (numerator.getWidget(i) instanceof FormulaCell){
							FormulaCell temp = (FormulaCell) numerator.getWidget(i);
							if (temp.label.getHTML().equals("")){
								if (temp.value.getText().length() > 0)
									axisForm.setConstant(Double.parseDouble(temp.value.getText()));
							} else {
								String displayName = temp.label.getHTML();
								for (Element e : plottableElements){
									if (e.getSymbol().equals(displayName)){
										if (temp.value.getText().length() > 0)
											axisForm.addElement(new AxisFormulaElement(e,Double.parseDouble(temp.value.getText())));
									}
								}
								for (Oxide o : plottableOxides){
									if (o.getDisplayName().equals(displayName)){
										if (temp.value.getText().length() > 0)
											axisForm.addOxide(new AxisFormulaOxide(o,Double.parseDouble(temp.value.getText())));
									}
								}
							}
						}
					}
					formulas.add(axisForm);
				}
				
				new ServerOp<List<ChemicalAnalysis>>() {
					public void begin() {
						final ArrayList<Integer> chemIds = new ArrayList<Integer>();
						for (Object id : chemList.getSelectedValues()){
							chemIds.add((Integer) id);
						}
						MpDb.chemicalAnalysis_svc.details(chemIds, this);
					}

					public void onSuccess(final List<ChemicalAnalysis> result) {						
						panel.getRightCol().clear();
						Map<Integer,Set<Integer>> groups = new HashMap<Integer,Set<Integer>>();
//						Set<Integer> ids = new HashSet<Integer>();
//						for (ChemicalAnalysis ca : result){
//							ids.add(ca.getId());
//						}
//						groups.put(1, ids);
		
						panel.getRightCol().add(currentGraph.createWidget(result,formulas, groups,moles.isChecked()));
					}
				}.begin();
			}
			
		});
		
		final FlowPanel leftContainer = new FlowPanel();
		leftContainer.add(createGraphTypeContainer());
		leftContainer.add(axisContainer);
		leftContainer.add(draw);
		noticeLbl.setWidth("285px");
		leftContainer.add(noticeLbl);
//		leftContainer.add(export);
		panel.getLeftCol().add(leftContainer);
		
		
/*		
		new ServerOp<List<ChemicalAnalysis>>() {
			public void begin() {
				MpDb.chemicalAnalysis_svc.allFull(1, this);
			}

			public void onSuccess(final List<ChemicalAnalysis> result) {
				
				Map<Integer, Double> elementsBottomAxis = new HashMap();
				Map<Integer, Double> elementsLeftAxis = new HashMap();
				Map<Integer, Double> elementsRightAxis = new HashMap();
				Map<Integer, Double> oxidesBottomAxis = new HashMap();
				Map<Integer, Double> oxidesLeftAxis = new HashMap();
				Map<Integer, Double> oxidesRightAxis = new HashMap();
				
				oxidesBottomAxis.put(1, 1.);
				oxidesLeftAxis.put(30, 1.);		
				oxidesRightAxis.put(15, 1.);
				test.plot(result,oxidesBottomAxis,oxidesLeftAxis,oxidesRightAxis);
			}
		}.begin();*/
				
//				getRightCol().add(p.createWidget(data,elementsXaxis,oxidesXaxis,elementsYaxis,oxidesYaxis));
//				PlotMouseListener listener = new PlotMouseListener(){
//					public void processEvent(EffectEvent e){
//						if (EffectEvent.ELEMENT_TYPE_MARKER.equals(e.getElementType())){
//							final Point pnt = (Point) e.getAssociatedObject();	
//							for(int i = 0; i < chemList.getDataTable().getRowCount()-1; i++){
//								if (((MLink)chemList.getDataTable().getWidget(i,0)).getText().equalsIgnoreCase(pnt.getTooltip())){
//									chemList.getDataTable().setHighlighted(i, true);
//								}
//							}
//						}
//					}
//				};
//				p.getPlot2D().addEffect(listener);
//				p.getChart2D().refreshChart();	
////				PlotMouseListener listener = new PlotMouseListener() {
////					public void processEvent(EffectEvent event) {
////						String label ="";
////						if(EffectEvent.ELEMENT_TYPE_MARKER.equals(event.getElementType())){
////							label = "X : " + event.getX() + " Y : " + event.getY() +
////							" X From Point object : " + ((Point)event.getAssociatedObject()).getX() + 
////							" Y From Point Object : " +((Point)event.getAssociatedObject()).getY() +
////							"Tooltip : " + ((Point)event.getAssociatedObject()).getTooltip();
////						}
////						if(event.getType().compareTo(EffectEvent.TYPE_ONCLICK) == 0){
////							Window.alert("Clicked on : " + label);
////						}else if(event.getType().compareTo(EffectEvent.TYPE_ONMOUSEOVER) == 0){
////							Window.alert("Mouse Over : " + label);
////						}else if(event.getType().compareTo(EffectEvent.TYPE_ONMOUSEOUT) == 0){
////							Window.alert("Mouse Out : " + label);
////						}
////					}
////				};
////				p.getPlot2D().addEffect(listener);
////				p.getChart2D().refreshChart();
//				
//				Window.alert("Done");
//			}
//		}.begin();
		
		final Label hideLabel = new Label("Hide");
		disclosurePanel = new DisclosurePanel(hideLabel, true);
		disclosurePanel.setStylePrimaryName("criteria-collapse");
		disclosurePanel.setAnimationEnabled(true);
		disclosurePanel.add(panel);
	}
	public Widget getWidget(){
		return disclosurePanel;
	}
}

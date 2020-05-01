package net.sf.openrocket.gui.configdialog;


import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import net.miginfocom.swing.MigLayout;
import net.sf.openrocket.document.OpenRocketDocument;
import net.sf.openrocket.gui.SpinnerEditor;
import net.sf.openrocket.gui.adaptors.BooleanModel;
import net.sf.openrocket.gui.adaptors.DoubleModel;
import net.sf.openrocket.gui.components.BasicSlider;
import net.sf.openrocket.gui.components.UnitSelector;
import net.sf.openrocket.l10n.Translator;
import net.sf.openrocket.material.Material;
import net.sf.openrocket.rocketcomponent.BodyTube;
import net.sf.openrocket.rocketcomponent.RocketComponent;
import net.sf.openrocket.startup.Application;
import net.sf.openrocket.unit.UnitGroup;

public class BodyTubeConfig extends RocketComponentConfig {
	
	private MotorConfig motorConfigPane = null;
	private static final Translator trans = Application.getTranslator();
	
	public BodyTubeConfig(OpenRocketDocument d, RocketComponent c) {
		super(d, c);
		
		JPanel panel = new JPanel(new MigLayout("gap rel unrel", "[][65lp::][30lp::][]", ""));
		
		////  Body tube length
		panel.add(new JLabel(trans.get("BodyTubecfg.lbl.Bodytubelength")));
		
		DoubleModel m = new DoubleModel(component, "Length", UnitGroup.UNITS_LENGTH, 0);
		
		JSpinner spin = new JSpinner(m.getSpinnerModel());
		spin.setEditor(new SpinnerEditor(spin));
		panel.add(spin, "growx");
		
		panel.add(new UnitSelector(m), "growx");
		panel.add(new BasicSlider(m.getSliderModel(0, 0.5, 2.0)), "w 100lp, wrap");
		

		//// Body tube diameter
		panel.add(new JLabel(trans.get("BodyTubecfg.lbl.Outerdiameter")));
		
		DoubleModel od = new DoubleModel(component, "OuterRadius", 2, UnitGroup.UNITS_LENGTH, 0);
		// Diameter = 2*Radius
		
		spin = new JSpinner(od.getSpinnerModel());
		spin.setEditor(new SpinnerEditor(spin));
		panel.add(spin, "growx");
		
		panel.add(new UnitSelector(od), "growx");
		panel.add(new BasicSlider(od.getSliderModel(0, 0.04, 0.2)), "w 100lp, wrap 0px");
		
		JCheckBox check = new JCheckBox(od.getAutomaticAction());
		//// Automatic
		check.setText(trans.get("BodyTubecfg.checkbox.Automatic"));
		panel.add(check, "skip, span 2, wrap");
		

		////  Inner diameter
		panel.add(new JLabel(trans.get("BodyTubecfg.lbl.Innerdiameter")));
		
		// Diameter = 2*Radius
		m = new DoubleModel(component, "InnerRadius", 2, UnitGroup.UNITS_LENGTH, 0);
		

		spin = new JSpinner(m.getSpinnerModel());
		spin.setEditor(new SpinnerEditor(spin));
		panel.add(spin, "growx");
		
		panel.add(new UnitSelector(m), "growx");
		panel.add(new BasicSlider(m.getSliderModel(new DoubleModel(0), od)), "w 100lp, wrap");
		

		////  Wall thickness
		panel.add(new JLabel(trans.get("BodyTubecfg.lbl.Wallthickness")));
		
		m = new DoubleModel(component, "Thickness", UnitGroup.UNITS_LENGTH, 0);
		
		spin = new JSpinner(m.getSpinnerModel());
		spin.setEditor(new SpinnerEditor(spin));
		panel.add(spin, "growx");
		
		panel.add(new UnitSelector(m), "growx");
		panel.add(new BasicSlider(m.getSliderModel(0, 0.01)), "w 100lp, wrap 0px");
		
		//// Filled
		check = new JCheckBox(new BooleanModel(component, "Filled"));
		check.setText(trans.get("BodyTubecfg.checkbox.Filled"));
		panel.add(check, "skip, span 2, wrap");
		

		//// Material
		panel.add(materialPanel(new JPanel(new MigLayout()), Material.Type.BULK),
				"cell 4 0, gapleft paragraph, aligny 0%, spany");
		
		//// General and General properties
		tabbedPane.insertTab(trans.get("BodyTubecfg.tab.General"), null, panel,
				trans.get("BodyTubecfg.tab.Generalproperties"), 0);
		motorConfigPane = new MotorConfig((BodyTube) c);
		//// Motor and Motor mount configuration
		tabbedPane.insertTab(trans.get("BodyTubecfg.tab.Motor"), null, motorConfigPane,
				trans.get("BodyTubecfg.tab.Motormountconf"), 1);
		tabbedPane.setSelectedIndex(0);
	}
	
	@Override
	public void updateFields() {
		super.updateFields();
		if (motorConfigPane != null)
			motorConfigPane.updateFields();
	}
	
}

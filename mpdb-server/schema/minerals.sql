INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Tectosilicates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Silica',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Quartz',(select mineral_id from minerals where name='Silica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Coesite',(select mineral_id from minerals where name='Silica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Tridymite',(select mineral_id from minerals where name='Silica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Cristobalite',(select mineral_id from minerals where name='Silica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Feldspar',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Plagioclase',(select mineral_id from minerals where name='Feldspar'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Albite',(select mineral_id from minerals where name='Plagioclase'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Oligoclase',(select mineral_id from minerals where name='Plagioclase'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Andesine',(select mineral_id from minerals where name='Plagioclase'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Labradorite',(select mineral_id from minerals where name='Plagioclase'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Anorthite',(select mineral_id from minerals where name='Plagioclase'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Alkali feldspar',(select mineral_id from minerals where name='Feldspar'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), (select mineral_id from minerals where name='Alkali feldspar'),'K-feldspar',0);
INSERT INTO minerals VALUES (nextval('mineral_seq'), (select mineral_id from minerals where name='Alkali feldspar'),'K feldspar',0);
INSERT INTO minerals VALUES (nextval('mineral_seq'), (select mineral_id from minerals where name='Alkali feldspar'),'Alkali-felspar',0);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Orthoclase',(select mineral_id from minerals where name='Alkali feldspar'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), (select mineral_id from minerals where name='Orthoclase'),'Microcline',0);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sanadine',(select mineral_id from minerals where name='Alkali feldspar'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Celsian',(select mineral_id from minerals where name='Feldspar'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Nepheline',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Kalsilite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Leucite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sodalite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Scapolite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Analcite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Zeolite',(select mineral_id from minerals where name='Tectosilicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sheet silicates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Mica',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Muscovite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Paragonite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Margarite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Glauconite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Biotite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Phlogopite',(select mineral_id from minerals where name='Biotite'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Annite',(select mineral_id from minerals where name='Biotite'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Wonesite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Lepidolite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clintonite',(select mineral_id from minerals where name='Mica'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Stilpnomelane',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Astrophyllite',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pyrophyllite',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Talc',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Chlorite',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Serpentine',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clay',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Kaolinite',(select mineral_id from minerals where name='Clay'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Illite',(select mineral_id from minerals where name='Clay'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Smectite',(select mineral_id from minerals where name='Clay'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Vermiculite',(select mineral_id from minerals where name='Clay'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Montmorillonite',(select mineral_id from minerals where name='Clay'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Apophyllite',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Prehnite',(select mineral_id from minerals where name='Sheet silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Chain silicates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pyroxene',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Orthopyroxene',(select mineral_id from minerals where name='Pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Enstatite',(select mineral_id from minerals where name='Orthopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ferrosilite',(select mineral_id from minerals where name='Orthopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clinopyroxene',(select mineral_id from minerals where name='Pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ca pyroxene',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Diopside',(select mineral_id from minerals where name='Ca pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Hedenbergite',(select mineral_id from minerals where name='Ca pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Augite',(select mineral_id from minerals where name='Ca pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Na pyroxene',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Jadeite',(select mineral_id from minerals where name='Na pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Acmite',(select mineral_id from minerals where name='Na pyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Omphacite',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Aegirine',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Spodumene',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clinoenstatite',(select mineral_id from minerals where name='Clinopyroxene'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Wollastonite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pectolite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Rhodonite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Bustamite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pyroxmangite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sapphirine',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Aenigmatite',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Amphibole',(select mineral_id from minerals where name='Chain silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Orthoamphibole',(select mineral_id from minerals where name='Amphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Anthophyllite',(select mineral_id from minerals where name='Orthoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Gedrite',(select mineral_id from minerals where name='Orthoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clinoamphibole',(select mineral_id from minerals where name='Amphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Cummingtonite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Grunerite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Tremolite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Actinolite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Hornblende',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Kaersutite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Glaucophane',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Riebeckite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Richterite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Katophorite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ekermannite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Arfvedsonite',(select mineral_id from minerals where name='Clinoamphibole'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ortho- and Ring Silicates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Olivine',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Humite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Zircon',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sphene',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Garnet',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Vesuvanite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sillimanite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Andalusite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Kyanite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Mullite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Topaz',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Staurolite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Chloritoid',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Larnite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Merwinite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Spurrite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Epidote',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Zoisite',(select mineral_id from minerals where name='Epidote'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Clinozoisite',(select mineral_id from minerals where name='Epidote'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Piemontite',(select mineral_id from minerals where name='Epidote'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Allanite',(select mineral_id from minerals where name='Epidote'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Lawsonite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pumpellyite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Melilite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Rankinite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Tilleyite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Beryl',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Cordierite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Tourmaline',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Axinite',(select mineral_id from minerals where name='Ortho- and Ring Silicates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Oxides',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ilmenite',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Hematite',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Rutile',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Spinel(1)',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Spinel(2)',(select mineral_id from minerals where name='Spinel(1)'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Magnetite',(select mineral_id from minerals where name='Spinel(1)'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Chromite',(select mineral_id from minerals where name='Spinel(1)'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Hercynite',(select mineral_id from minerals where name='Spinel(1)'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ghanite',(select mineral_id from minerals where name='Spinel(1)'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Periclase',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Cassiterite',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Corundum',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Anatase',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Brookite',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Perovskite',(select mineral_id from minerals where name='Oxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Hydroxides',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Brucite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Gibbsite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Diaspore',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Boehmite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Goethite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Lepidocrocite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Limonite',(select mineral_id from minerals where name='Hydroxides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sulphides',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pyrite',(select mineral_id from minerals where name='Sulphides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Pyrrhotite',(select mineral_id from minerals where name='Sulphides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Chalcopyrite',(select mineral_id from minerals where name='Sulphides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sphalerite',(select mineral_id from minerals where name='Sulphides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Galena',(select mineral_id from minerals where name='Sulphides'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Sulphates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Baryte',(select mineral_id from minerals where name='Sulphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Celestine',(select mineral_id from minerals where name='Sulphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Gypsum',(select mineral_id from minerals where name='Sulphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Anhydrite',(select mineral_id from minerals where name='Sulphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Carbonates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Calcite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Dolomite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Magnesite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Rhodochrosite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Siderite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Ankerite',(select mineral_id from minerals where name='Carbonates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Phosphates',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Apatite',(select mineral_id from minerals where name='Phosphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Monazite',(select mineral_id from minerals where name='Phosphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Xenotime',(select mineral_id from minerals where name='Phosphates'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Native Elements',null);
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Carbon',(select mineral_id from minerals where name='Native Elements'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Graphite',(select mineral_id from minerals where name='Carbon'));
INSERT INTO minerals VALUES (nextval('mineral_seq'), currval('mineral_seq'),'Diamond',(select mineral_id from minerals where name='Carbon'));
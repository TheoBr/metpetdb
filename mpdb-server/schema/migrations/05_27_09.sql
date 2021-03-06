--deletes all current relationships and adds the new correct ones, also adds 2 new minerals Chalcocite and Biopyribole/Biopyrobole
delete from mineral_relationships;
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Silica'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Silica'),(select mineral_id from minerals where name='Quartz'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Silica'),(select mineral_id from minerals where name='Coesite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Silica'),(select mineral_id from minerals where name='Tridymite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Silica'),(select mineral_id from minerals where name='Cristobalite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Feldspar'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Feldspar'),(select mineral_id from minerals where name='Plagioclase'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Plagioclase'),(select mineral_id from minerals where name='Albite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Plagioclase'),(select mineral_id from minerals where name='Oligoclase'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Plagioclase'),(select mineral_id from minerals where name='Andesine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Plagioclase'),(select mineral_id from minerals where name='Labradorite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Plagioclase'),(select mineral_id from minerals where name='Anorthite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Feldspar'),(select mineral_id from minerals where name='Alkali feldspar'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Alkali feldspar'),(select mineral_id from minerals where name='Orthoclase'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Alkali feldspar'),(select mineral_id from minerals where name='Sanidine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Feldspar'),(select mineral_id from minerals where name='Celsian'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Nepheline'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Kalsilite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Leucite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Sodalite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Scapolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Analcite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Tectosilicates'),(select mineral_id from minerals where name='Zeolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Mica'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='White Mica'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='White Mica'),(select mineral_id from minerals where name='Muscovite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='White Mica'),(select mineral_id from minerals where name='Paragonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='White Mica'),(select mineral_id from minerals where name='Phengite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='White Mica'),(select mineral_id from minerals where name='Sericite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Margarite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Glauconite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Biotite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Biotite'),(select mineral_id from minerals where name='Phlogopite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Biotite'),(select mineral_id from minerals where name='Annite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Wonesite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Lepidolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Mica'),(select mineral_id from minerals where name='Clintonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Stilpnomelane'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Astrophyllite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Pyrophyllite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Talc'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Chlorite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Serpentine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Clay'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clay'),(select mineral_id from minerals where name='Kaolinite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clay'),(select mineral_id from minerals where name='Illite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clay'),(select mineral_id from minerals where name='Smectite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clay'),(select mineral_id from minerals where name='Vermiculite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clay'),(select mineral_id from minerals where name='Montmorillonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Apophyllite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sheet silicates'),(select mineral_id from minerals where name='Prehnite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Pyroxene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Pyroxene'),(select mineral_id from minerals where name='Orthopyroxene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Orthopyroxene'),(select mineral_id from minerals where name='Enstatite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Orthopyroxene'),(select mineral_id from minerals where name='Ferrosilite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Pyroxene'),(select mineral_id from minerals where name='Clinopyroxene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Calcic pyroxene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic pyroxene'),(select mineral_id from minerals where name='Diopside'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic pyroxene'),(select mineral_id from minerals where name='Hedenbergite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic pyroxene'),(select mineral_id from minerals where name='Augite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Sodic pyroxene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sodic pyroxene'),(select mineral_id from minerals where name='Jadeite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sodic pyroxene'),(select mineral_id from minerals where name='Acmite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Omphacite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Aegirine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Spodumene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinopyroxene'),(select mineral_id from minerals where name='Clinoenstatite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Wollastonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Pectolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Rhodonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Bustamite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Pyroxmangite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Sapphirine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Aenigmatite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Chain silicates'),(select mineral_id from minerals where name='Amphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Amphibole'),(select mineral_id from minerals where name='Orthoamphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Orthoamphibole'),(select mineral_id from minerals where name='Anthophyllite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Orthoamphibole'),(select mineral_id from minerals where name='Gedrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Amphibole'),(select mineral_id from minerals where name='Clinoamphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Fe-Mg clinoamphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Mg clinoamphibole'),(select mineral_id from minerals where name='Cummingtonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Mg clinoamphibole'),(select mineral_id from minerals where name='Grunerite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Calcic amphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic amphibole'),(select mineral_id from minerals where name='Tremolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic amphibole'),(select mineral_id from minerals where name='Actinolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic amphibole'),(select mineral_id from minerals where name='Hornblende'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Calcic amphibole'),(select mineral_id from minerals where name='Kaersutite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Sodic Amphibole'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sodic Amphibole'),(select mineral_id from minerals where name='Glaucophane'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sodic Amphibole'),(select mineral_id from minerals where name='Riebeckite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sodic Amphibole'),(select mineral_id from minerals where name='Richterite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Katophorite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Ekermannite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Clinoamphibole'),(select mineral_id from minerals where name='Arfvedsonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Olivine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Humite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Zircon'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Sphene'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Garnet'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Vesuvanite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Sillimanite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Andalusite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Kyanite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Mullite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Topaz'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Staurolite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Chloritoid'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Larnite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Merwinite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Spurrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Epidote Group'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Epidote Group'),(select mineral_id from minerals where name='Zoisite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Epidote Group'),(select mineral_id from minerals where name='Clinozoisite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Epidote Group'),(select mineral_id from minerals where name='Epidote'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Epidote Group'),(select mineral_id from minerals where name='Piemontite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Epidote Group'),(select mineral_id from minerals where name='Allanite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Lawsonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Pumpellyite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Melilite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Rankinite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Tilleyite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Beryl'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Cordierite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Tourmaline'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Ortho- and Ring Silicates'),(select mineral_id from minerals where name='Axinite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Ilmenite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Hematite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Rutile'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Spinel Group'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Spinel Group'),(select mineral_id from minerals where name='Spinel'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Spinel Group'),(select mineral_id from minerals where name='Magnetite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Spinel Group'),(select mineral_id from minerals where name='Chromite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Spinel Group'),(select mineral_id from minerals where name='Hercynite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Spinel Group'),(select mineral_id from minerals where name='Ghanite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Periclase'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Cassiterite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Corundum'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Anatase'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Brookite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Oxides'),(select mineral_id from minerals where name='Perovskite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Brucite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Gibbsite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Diaspore'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Boehmite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Goethite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Lepidocrocite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Hydroxides'),(select mineral_id from minerals where name='Limonite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Pyrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Pyrrhotite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Chalcopyrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Sphalerite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Galena'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphates'),(select mineral_id from minerals where name='Baryte'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphates'),(select mineral_id from minerals where name='Celestine'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphates'),(select mineral_id from minerals where name='Gypsum'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphates'),(select mineral_id from minerals where name='Anhydrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Calcite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Dolomite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Magnesite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Rhodochrosite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Siderite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbonates'),(select mineral_id from minerals where name='Ankerite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Phosphates'),(select mineral_id from minerals where name='Apatite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Phosphates'),(select mineral_id from minerals where name='Monazite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Phosphates'),(select mineral_id from minerals where name='Xenotime'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Native Elements'),(select mineral_id from minerals where name='Carbon'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbon'),(select mineral_id from minerals where name='Graphite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Carbon'),(select mineral_id from minerals where name='Diamond'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Other'),(select mineral_id from minerals where name='Opaque'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Ilmenite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Hematite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Rutile'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Magnetite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Pyrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Pyrrhotite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Chalcopyrite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Sphalerite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Galena'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Other'),(select mineral_id from minerals where name='Fe-Ti oxide'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Ti oxide'),(select mineral_id from minerals where name='Ilmenite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Ti oxide'),(select mineral_id from minerals where name='Hematite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Ti oxide'),(select mineral_id from minerals where name='Rutile'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Fe-Ti oxide'),(select mineral_id from minerals where name='Magnetite'));
insert into minerals (mineral_id, real_mineral_id, name)  VALUES (nextval('mineral_seq'), currval('mineral_seq'), 'Chalcocite');
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Sulphides'),(select mineral_id from minerals where name='Chalcocite'));
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Opaque'),(select mineral_id from minerals where name='Chalcocite'));
insert into minerals (mineral_id, real_mineral_id, name)  VALUES (nextval('mineral_seq'), currval('mineral_seq'), 'Biopyribole');
insert into minerals (mineral_id, real_mineral_id, name)  VALUES (nextval('mineral_seq'),(select mineral_id from minerals where name='Biopyribole'), 'Biopyrobole');
insert into mineral_relationships (parent_mineral_id, child_mineral_id) VALUES((select mineral_id from minerals where name='Other'),(select mineral_id from minerals where name='Biopyribole'));
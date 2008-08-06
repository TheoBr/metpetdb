CREATE TABLE chemical_analyses
(
   chemical_analysis_id INT8 NOT NULL,
   spot_id VARCHAR(50) NOT NULL,
   subsample_id INT8 NOT NULL,
   point_x INT2,
   point_y INT2,
   image_id INT8,
   analysis_method VARCHAR(50),
   where_done VARCHAR(50),
   analyst VARCHAR(50),
   analysis_date TIMESTAMP,
   date_precision INT2,
   reference_id INT8,
   description VARCHAR(50),
   mineral_id int2,
   -- user_id INT4 NOT NULL,
   large_rock CHAR(1) CHECK (large_rock IN ('Y','N')) NOT NULL,
   total real,
   CONSTRAINT chemical_analyses_sk PRIMARY KEY (chemical_analysis_id),
   CONSTRAINT chemical_analyses_nk_spot_id UNIQUE(subsample_id, spot_id),
   CONSTRAINT chemical_analyses_fk_subsamples FOREIGN KEY (subsample_id)
      REFERENCES subsamples(subsample_id),
   --CONSTRAINT chemical_analyses_fk_user FOREIGN KEY (user_id)
  --   REFERENCES users (user_id),
  CONSTRAINT chemical_analyses_fk_reference FOREIGN KEY (reference_id)
     REFERENCES reference (reference_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
   CONSTRAINT chemical_analyses_fk_images FOREIGN KEY (image_id)
      REFERENCES images(image_id) ON DELETE SET NULL,
   CONSTRAINT chemical_analyses_fk_mineral FOREIGN KEY (mineral_id)
      REFERENCES minerals(mineral_id)
) WITHOUT OIDS;



CREATE TABLE oxides
(
   oxide_id INT2 NOT NULL,
   element_id INT2 NOT NULL,
   oxidation_state INT2,
   species  VARCHAR(20),
   weight FLOAT4,
   cations_per_oxide INT2,
   conversion_factor  FLOAT8 NOT NULL,
   CONSTRAINT oxides_sk PRIMARY KEY (oxide_id),
   CONSTRAINT oxides_fk FOREIGN KEY (element_id)
        REFERENCES elements(element_id),
   CONSTRAINT oxides_nk UNIQUE (species)
) WITHOUT OIDS ;

CREATE TABLE chemical_analysis_elements
(
    chemical_analysis_id INT8 NOT NULL,
    element_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    CONSTRAINT analysis_elements_sk PRIMARY KEY (chemical_analysis_id, element_id),
    CONSTRAINT analysis_elements_fk_chemical_analyses FOREIGN KEY (chemical_analysis_id) REFERENCES chemical_analyses(chemical_analysis_id),
    CONSTRAINT analysis_elements_fk_elements FOREIGN KEY (element_id) REFERENCES elements(element_id),
    CONSTRAINT analysis_elements_ck CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;

CREATE TABLE chemical_analysis_oxides
(
    chemical_analysis_id INT8 NOT NULL,
    oxide_id INT2 NOT NULL,
    amount     FLOAT4 NOT NULL,
    precision  FLOAT4,
    precision_type  VARCHAR(3),
    CONSTRAINT analysis_oxides_sk PRIMARY KEY (chemical_analysis_id, oxide_id),
    CONSTRAINT analysis_oxides_fk_chemical_analyses FOREIGN KEY (chemical_analysis_id) REFERENCES chemical_analyses(chemical_analysis_id),
    CONSTRAINT analysis_oxides_fk_oxides FOREIGN KEY (oxide_id) REFERENCES oxides(oxide_id),
    CONSTRAINT analysis_oxides_ck CHECK (precision_type in ('ABS', 'REL'))
) WITHOUT OIDS ;

CREATE TABLE mineral_types
(
	mineral_type_id INT2 NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT mineral_types_sk PRIMARY KEY(mineral_type_id)
) WITHOUT OIDS ;

CREATE TABLE oxide_mineral_types
(
	oxide_id INT2 NOT NULL,
	mineral_type_id INT2 NOT NULL,
	CONSTRAINT oxide_mineral_types_sk PRIMARY KEY (oxide_id, mineral_type_id),
	CONSTRAINT oxide_mineral_types_fk_oxide FOREIGN KEY (oxide_id) REFERENCES oxides (oxide_id),
	CONSTRAINT oxide_mineral_types_fk_mineral_type FOREIGN KEY (mineral_type_id) REFERENCES mineral_types (mineral_type_id)
) WITHOUT OIDS ;

CREATE TABLE element_mineral_types
(
	element_id INT2 NOT NULL,
	mineral_type_id INT2 NOT NULL,
	CONSTRAINT element_mineral_types_sk PRIMARY KEY (element_id, mineral_type_id),
	CONSTRAINT element_mineral_types_fk_element FOREIGN KEY (element_id) REFERENCES elements (element_id),
	CONSTRAINT element_mineral_types_fk_mineral_type FOREIGN KEY (mineral_type_id) REFERENCES mineral_types (mineral_type_id)
) WITHOUT OIDS ;

insert into mineral_types values(1,'Silicates');
insert into mineral_types values(2,'Oxides');
insert into mineral_types values(3,'Carbonates');
insert into mineral_types values(4,'Phosphates');
insert into mineral_types values(5,'Other');

--Oxide silicates
insert into oxide_mineral_types values((select oxide_id from oxides where species='SiO2'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='TiO2'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Al2O3'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MgO'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='FeO'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Fe2O3'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MnO'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='CaO'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Na2O'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='K2O'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='H2O'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Cl'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='F'),(select mineral_type_id from mineral_types where name='Silicates'));

--Oxide oxides
insert into oxide_mineral_types values((select oxide_id from oxides where species='SiO2'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='TiO2'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Al2O3'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MgO'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='FeO'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Fe2O3'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MnO'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Cr2O3'),(select mineral_type_id from mineral_types where name='Oxides'));

--Oxide Carbonates
insert into oxide_mineral_types values((select oxide_id from oxides where species='CaO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MgO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='FeO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MnO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='SrO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='BaO'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='CO2'),(select mineral_type_id from mineral_types where name='Carbonates'));

--Oxide Phosphates
insert into oxide_mineral_types values((select oxide_id from oxides where species='P2O5'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='SiO2'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='CaO'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='PbO'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Y2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='La2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Ce2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Pr2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Nd2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Sm2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Eu2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Gd2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Tb2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Dy2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Ho2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Er2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Tm2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Yb2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Lu2O3'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='ThO2'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='UO2'),(select mineral_type_id from mineral_types where name='Phosphates'));

--Oxide Other
insert into oxide_mineral_types values((select oxide_id from oxides where species='SiO2'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='TiO2'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Al2O3'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MgO'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='FeO'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Fe2O3'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='MnO'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='CaO'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Na2O'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='K2O'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='H2O'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='Cl'),(select mineral_type_id from mineral_types where name='Other'));
insert into oxide_mineral_types values((select oxide_id from oxides where species='F'),(select mineral_type_id from mineral_types where name='Other'));

--Elements Silicates
insert into element_mineral_types values((select element_id from elements where symbol='Si'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ti'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Al'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Mg'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Fe'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Mn'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ca'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Na'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='K'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='H'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='Cl'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='F'),(select mineral_type_id from mineral_types where name='Silicates'));
insert into element_mineral_types values((select element_id from elements where symbol='O'),(select mineral_type_id from mineral_types where name='Silicates'));

--Elements Oxides
insert into element_mineral_types values((select element_id from elements where symbol='Si'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Ti'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Al'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Mg'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Fe'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Mn'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='Cr'),(select mineral_type_id from mineral_types where name='Oxides'));
insert into element_mineral_types values((select element_id from elements where symbol='O'),(select mineral_type_id from mineral_types where name='Oxides'));

--Elements Carbonates
insert into element_mineral_types values((select element_id from elements where symbol='Ca'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='Mg'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='Fe'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='Mn'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='Sr'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ba'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='C'),(select mineral_type_id from mineral_types where name='Carbonates'));
insert into element_mineral_types values((select element_id from elements where symbol='O'),(select mineral_type_id from mineral_types where name='Carbonates'));

--Elements Phosphates
insert into element_mineral_types values((select element_id from elements where symbol='P'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Si'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ca'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Pb'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Y'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='La'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ce'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Pr'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Nd'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Sm'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Eu'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Gd'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Tb'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Dy'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Ho'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Er'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Tm'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Yb'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Lu'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='Th'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='U'),(select mineral_type_id from mineral_types where name='Phosphates'));
insert into element_mineral_types values((select element_id from elements where symbol='O'),(select mineral_type_id from mineral_types where name='Phosphates'));

--Elements Other
insert into element_mineral_types values((select element_id from elements where symbol='Si'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Ti'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Al'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Mg'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Fe'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Mn'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Ca'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Na'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='K'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='H'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='Cl'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='F'),(select mineral_type_id from mineral_types where name='Other'));
insert into element_mineral_types values((select element_id from elements where symbol='O'),(select mineral_type_id from mineral_types where name='Other'));


CREATE SEQUENCE chemical_analyses_seq;
CREATE SEQUENCE element_seq;
CREATE SEQUENCE oxide_seq;
CREATE SEQUENCE chemical_analysis_oxides_seq;
CREATE SEQUENCE chemical_analysis_elements_seq;
CREATE SEQUENCE mineral_types_seq;

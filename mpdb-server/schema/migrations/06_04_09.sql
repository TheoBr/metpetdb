--adds the PUBLICATION user to the database
INSERT INTO users VALUES (nextval('user_seq'), 1, 'PUBLICATION', 'PUBLICATION', E'\\004c]\\333\\202\\210\\320D6\\337<\\315\\360@i\\327o\\227\\220\\205\\220lo\\030q','','','','','','','','','Y',(select role_id from roles where rank=1));
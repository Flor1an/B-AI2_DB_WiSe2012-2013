INSERT INTO MA_VOGEL(ID_V, Name_Eng, Name_Lat, category,name_deu) 
SELECT g.B_ID, g.B_ENGLISH_NAME, g.B_SCIENTIFIC_NAME, g.b_category, (SELECT de_deutsch from MERLIN.birds_de deu where deu.de_latein = g.b_scientific_name)
FROM MERLIN.birds g where g.B_ID NOT IN (Select ID_V FROM ma_vogel)

delete ma_beobachtungen
delete ma_vogel
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "unidades" (
	"id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT,
	"nombre_en"	TEXT,
	"mostrar"	INTEGER DEFAULT 1,
	PRIMARY KEY("id" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "temas" (
	"id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT,
	"nombre_en"	TEXT,
	"html"	TEXT,
	"mostrar"	INTEGER DEFAULT 1,
	"unidad"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("unidad") REFERENCES "unidades"("id")
);
CREATE TABLE IF NOT EXISTS "subtemas" (
	"id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT,
	"nombre_en"	TEXT,
	"html"	TEXT,
	"mostrar"	INTEGER DEFAULT 1,
	"tema"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT),
	FOREIGN KEY("tema") REFERENCES "temas"("id")
);
CREATE TABLE IF NOT EXISTS "subsubtemas" (
	"id"	INTEGER NOT NULL UNIQUE,
	"nombre"	TEXT,
	"nombre_en"	TEXT,
	"subtema"	INTEGER,
	PRIMARY KEY("id" AUTOINCREMENT)
);
INSERT INTO "unidades" ("id","nombre","mostrar") VALUES
 (1,'Introducción',0),
 (2,'Muestreo',1),
 (3,'Estadística Descriptiva',1),
 (4,'Probabilidades',1),
 (5,'Estadística Inferencial',1);
INSERT INTO "temas" ("id","nombre","html","mostrar","unidad") VALUES
 (1,'Definición de Estadística','',0,1),
 (2,'Clasificación de la Estadística','',0,1),
 (3,'Población y Muestra','',0,1),
 (4,'Variables: Definición y Clasificaciones','',0,1),
 (5,'Escalas de Medición','',1,1),
 (6,'Tema 1: Tamaño de la Muestra','',1,2),
 (7,'Tema 2: Métodos de Muestreo','',1,2),
 (8,'Tema 3: Selección Aleatoria de una Muestra','seleccion_aleatoria_muestra.html',1,2),
 (9,'Tema 4: Distribución de la Muestra','dist_muestra.html',1,2),
 (10,'Tema 1: Análisis Estadístico Descriptivo de una Variable Cuantitativa','',1,3),
 (11,'Tema 2: Análisis Estadístico Descriptivo de una Variable Cualitativa','',1,3),
 (12,'Tema 3: Análisis Estadístico Descriptivo de 2 Variables Cuantitativas','',1,3),
 (13,'Tema 4: Análisis Estadístico Descriptivo de 2 Variables Cualitativas','',1,3),
 (14,'Tema 5: Análisis Estadístico Descriptivo de una Variable Cuantitativa y una Variable Cualitativa','',1,3),
 (15,'Tema 1: Cálculo, Reglas y Teorema de Bayes','',1,4),
 (16,'Tema 2: Distribución de Probabilidades Binomial','dist_binomial.html',1,4),
 (17,'Tema 3: Distribución de Probabilidades Poisson','dist_poisson.html',1,4),
 (18,'Tema 4: Distribución de Probabilidades Hipergeométrica','dist_hipergeometrica.html',1,4),
 (19,'Tema 5: Distribución de Probabilidades Normal','dist_normal.html',1,4),
 (20,'Tema 6: Distribución de Probabilidades t de Student','',1,4),
 (21,'Tema 7: Distribución de Probabilidades F de Fisher','',1,4),
 (22,'Tema 8: Distribución de Probabilidades Chi Cuadrado','',1,4),
 (23,'Tema 1: Intervalo de Confianza para una Proporción Poblacional','',1,5),
 (24,'Tema 2: Intervalo de Confianza para una Media Poblacional','',1,5),
 (25,'Tema 3: Prueba de Hipótesis para una Proporción Poblacional','prueba_hipotesis_proporcion_poblacional.html',1,5),
 (26,'Tema 4: Prueba de Hipótesis para una Media Poblacional','prueba_hipotesis_media_poblacional.html',1,5),
 (27,'Tema 5: Prueba de Hipótesis para comparar 2 Proporciones Poblacionales','prueba_hipotesis_2_proporciones_poblacionales.html',1,5),
 (28,'Tema 6: Prueba de Hipótesis para comparar 2 Medias Poblacionales','prueba_hipotesis_2_medias_poblacionales.html',1,5),
 (29,'Tema 7: Prueba de Hipótesis de Independencia','',0,5),
 (30,'Tema 8: Prueba de Hipótesis para el Coeficiente de Correlación y para el Coeficiente de Regresión','',0,5);
INSERT INTO "subtemas" ("id","nombre","html","mostrar","tema") VALUES
 (1,'Tamaño de Muestra para estimar una Proporción Poblacional','tamanio_muestra_proporcion.html',1,6),
 (2,'Tamaño de Muestra para estimar una Media Poblacional','tamanio_muestra_media.html',1,6),
 (3,'Tamaños de Muestras para la Diferencia de 2 Proporciones Poblacionales','tamanio_muestra_2_proporciones_poblacionales.html',1,6),
 (4,'Tamaños de Muestras para la Diferencia de 2 Medias Poblacionales','tamanio_muestra_2_medias_poblacionales.html',1,6),
 (5,'Probabilidad Teórica','probabilidad_teorica.html',1,14),
 (6,'Probabilidad Empírica','probabilidad_empirica.html',1,14),
 (7,'Regla del Complemento','regla_complemento.html',1,14),
 (8,'Regla de Adición','regla_adicion.html',1,14),
 (9,'Regla de Multiplicación','regla_multiplicacion.html',1,14),
 (10,'Teorema de Bayes (Simple)','teorema_bayes_simple.html',1,14),
 (11,'Estandarización','dist_normal_estandarizacion.html',1,18),
 (12,'Probabilidades','dist_normal_probabilidades.html',1,18),
 (13,'Intervalo de Confianza para una Proporción Poblacional (Población Desconocida)','intervalo_confianza_proporcion_poblacional_poblacion_desconocida.html',1,22),
 (14,'Intervalo de Confianza para una Proporción Poblacional (Población Conocida)','intervalo_confianza_proporcion_poblacional_poblacion_conocida.html',1,22),
 (15,'Intervalo de Confianza para una Media Poblacional (Población Desconocida)','intervalo_confianza_media_poblacional_poblacion_desconocida.html',1,23),
 (16,'Intervalo de Confianza para una Media Poblacional (Población Conocida)','intervalo_confianza_media_poblacional_poblacion_conocida.html',1,23),
 (17,'Métodos de Muestreo Probabilísticos','metodos_muestreo_probabilisticos.html',1,30),
 (18,'Métodos de Muestreo No Probabilísticos','metodos_muestreo_no_probabilisticos.html',1,30);
COMMIT;
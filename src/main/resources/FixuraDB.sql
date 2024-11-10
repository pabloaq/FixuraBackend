/*
	EXISTEN TRES ROLES:
	--ADMINISTRADOR: (1) USUARIO ENCARGADO DE REVISAR LAS INCIDENCIAS Y AGREGAR MODERADORES
	--MODERADOR:     (2) USUARIO ENCARGADO DE REVISAR LAS INCIDENCIAS
	--USUARIO:       (3) USUARIO ENCARGADO DE REPORTAR LAS INCIDENCIAS
*/
CREATE TABLE Rol (
    id_rol SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Departamento (
    id_departamento	SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Provincia (
    id_provincia SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    id_departamento INT NOT NULL,
	FOREIGN KEY (id_departamento) REFERENCES Departamento(id_departamento)
);

CREATE TABLE Distrito (
    id_distrito SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    id_provincia INT NOT NULL,
	FOREIGN KEY (id_provincia) REFERENCES Provincia(id_provincia)
);

/*
	CATEGORIAS PRINCIPALES:
	  CATEGORIA                     ID   DESCIPCIÓN
	--RED ELÉCTRICA                 (1)  Problemas como apagones, postes en mal
	                                     estado, cables caídos o cortocircuitos.
	--AGUA Y SANEAMIENTO            (2)  Fugas de agua, alcantarillas obstruidas
	                                     o rotas.
	--VÍA PÚBLICA                   (3)  Baches, calles mal señalizadas,
	                                     semáforos dañados, iluminación pública
                                         defectuosa, acumulación de basura.
	--INFRAESTRUCTURA DE TRANSPORTE (4)  Puentes en mal estado, paraderos de
	                                     buses deteriorados, señales de tránsito
                                         dañadas.
	--ESPACIO PÚBLICO               (5)  Parques sin mantenimiento, áreas
	                                     recreativas dañadas, mobiliario urbano
                                         deteriorado.
	--SEGURIDAD CIUDADANA           (6)  Cámaras de vigilancia dañadas, falta de
	                                     presencia policial.
	--VIVIENDA Y EDIFICACIONES      (7)  Edificaciones en riesgo, construcciones
	                                     informales o inseguras.
*/
CREATE TABLE Categoria (
    id_categoria SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE Usuarios (
    DNI CHAR(8) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(250) NULL,
    correo VARCHAR(50) NOT NULL,
    contrasenia VARCHAR(60) NOT NULL,
    foto_perfil VARCHAR(250) NULL,
    tiempo_ban TIMESTAMP NULL,
    id_rol INT NOT NULL,
    id_distrito INT NOT NULL,
	token_verification VARCHAR(255) NULL,
	activo BOOLEAN NULL,
	banned BOOLEAN NULL,
	FOREIGN KEY (id_rol) REFERENCES Rol(id_rol),
	FOREIGN KEY (id_distrito) REFERENCES Distrito(id_distrito)
);

/*
	EXISTEN CUATRO ESTADOS:
	--COMPLETADO:    (1) BANDERA VERDE -> INCIDENCIA RESULTA
	--EN PROCESO:    (2) BANDERA AMBAR -> INCIDENCIA EN REPARACION
	--PENDIENTE:     (3) BANDERA ROJA -> REPORTE INCIDENCIA
	--DESHABILITADO: (4) INCIDENCIA ELIMINADA
*/
CREATE TABLE Estado (
	id_estado SERIAL PRIMARY KEY,
	nombre VARCHAR(15) NOT NULL
);

CREATE TABLE Incidencia (
    id_incidencia SERIAL PRIMARY KEY,
    fecha_publicacion TIMESTAMP NOT NULL,
    descripcion VARCHAR(700) NOT NULL,
    ubicacion VARCHAR(750) NOT NULL,
    imagen VARCHAR(250) NULL,
    total_votos INT NOT NULL,
	id_estado INT NOT NULL,
    DNI CHAR(8) NOT NULL ,
    id_categoria INT NOT NULL ,
	latitud DECIMAL(9, 6) NULL,
	longitud DECIMAL(9, 6) NULL,
	FOREIGN KEY (id_estado) REFERENCES Estado(id_estado),
	FOREIGN KEY (DNI) REFERENCES Usuarios(DNI),
	FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

CREATE TABLE IncidenciaLike (
	dni CHAR(8) NOT NULL,
	id_incidencia INT NOT NULL,
	hour_liked TIMESTAMP NOT NULL,
	PRIMARY KEY (dni, id_incidencia),
	FOREIGN KEY (dni) REFERENCES Usuarios(DNI),
    FOREIGN KEY (id_incidencia) REFERENCES Incidencia(id_incidencia)
);

CREATE TABLE Coordenada_Distrito (
    id_coordenada SERIAL PRIMARY KEY,
    latitud DECIMAL(9, 6) Not NULL,
    longitud DECIMAL(9, 6) Not NULL,
	id_distrito INT NOT NULL,
	FOREIGN KEY (id_distrito) REFERENCES Distrito(id_distrito)
);

CREATE TABLE Incidencia_consolidado (
	dni CHAR(8) NOT NULL,
	id_incidencia INT NOT NULL,
	hour_consolidado TIMESTAMP NOT NULL,
	PRIMARY KEY (dni, id_incidencia),
	FOREIGN KEY (dni) REFERENCES Usuarios(DNI),
    FOREIGN KEY (id_incidencia) REFERENCES Incidencia(id_incidencia)
);

CREATE OR REPLACE FUNCTION fn_Update_total_votos()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        -- Incrementa total_votos en la tabla Incidencia
        UPDATE Incidencia
        SET total_votos = total_votos + 1
        WHERE id_incidencia = NEW.id_incidencia;
    ELSIF TG_OP = 'DELETE' THEN
        -- Decrementa total_votos en la tabla Incidencia
        UPDATE Incidencia
        SET total_votos = total_votos - 1
        WHERE id_incidencia = OLD.id_incidencia;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_insert_incidenciaLike
AFTER INSERT ON IncidenciaLike
FOR EACH ROW
EXECUTE FUNCTION fn_Update_total_votos();

CREATE TRIGGER tg_delete_incidenciaLike
AFTER DELETE ON IncidenciaLike
FOR EACH ROW
EXECUTE FUNCTION fn_Update_total_votos();

-- Función para obtener incidencias de un usuario con verificación de likes (muro-usuario)
CREATE OR REPLACE FUNCTION lista_incidente_usuario(
    p_dni VARCHAR, 
    p_limit INT, 
    p_offset INT
)
RETURNS TABLE (
    id_incidencia INT,
    fecha_publicacion TIMESTAMP,
    descripcion VARCHAR,
    ubicacion VARCHAR,
    imagen VARCHAR,
    total_votos INT,
    estado VARCHAR,
    usuario VARCHAR,
    usuario_foto VARCHAR,
    categoria VARCHAR,
    latitud DECIMAL,
    longitud DECIMAL,
    tiene_like BOOLEAN
) 
AS $$
BEGIN
    RETURN QUERY
    SELECT inc.id_incidencia, inc.fecha_publicacion, inc.descripcion,
           inc.ubicacion, inc.imagen, inc.total_votos, est.nombre AS estado,
           us.nombre AS usuario, 
           us.foto_perfil AS usuario_foto,
           cat.nombre AS categoria, inc.latitud, inc.longitud,
           CASE 
               WHEN il.id_incidencia IS NOT NULL THEN TRUE
               ELSE FALSE
           END AS tiene_like
    FROM incidencia AS inc
    INNER JOIN usuarios AS us ON us.dni = inc.dni
    INNER JOIN estado AS est ON est.id_estado = inc.id_estado
    INNER JOIN categoria AS cat ON cat.id_categoria = inc.id_categoria
    LEFT JOIN incidencialike AS il ON il.id_incidencia = inc.id_incidencia AND inc.dni = il.dni
    WHERE us.dni = p_dni AND inc.id_estado <> 4
    ORDER BY fecha_publicacion DESC
    LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener incidencias de un usuario con distrito y verificación de likes (muro-entidad)
CREATE OR REPLACE FUNCTION lista_incidente_usuario_distrito(
    p_dni VARCHAR,
	p_distrito INT,
    p_limit INT, 
    p_offset INT
)
RETURNS TABLE (
    id_incidencia INT,
    fecha_publicacion TIMESTAMP,
    descripcion VARCHAR,
    ubicacion VARCHAR,
    imagen VARCHAR,
    total_votos INT,
    estado VARCHAR,
    usuario VARCHAR,
    usuario_foto VARCHAR,
    categoria VARCHAR,
    latitud DECIMAL,
    longitud DECIMAL,
    tiene_like BOOLEAN
) 
AS $$
BEGIN
    RETURN QUERY
    SELECT inc.id_incidencia, inc.fecha_publicacion, inc.descripcion,
	   inc.ubicacion, inc.imagen, inc.total_votos, est.nombre AS estado,
	   us.nombre AS usuario, 
       us.foto_perfil AS usuario_foto,
       cat.nombre AS categoria, inc.latitud, inc.longitud,
	   CASE 
           WHEN il.id_incidencia IS NOT NULL THEN TRUE
           ELSE FALSE
       END AS tiene_like
	FROM incidencia AS inc
	INNER JOIN usuarios AS us ON us.dni = inc.dni
	INNER JOIN estado AS est ON est.id_estado = inc.id_estado
	INNER JOIN categoria AS cat ON cat.id_categoria = inc.id_categoria
	LEFT JOIN incidencialike AS il ON il.id_incidencia = inc.id_incidencia AND il.dni = p_dni
	WHERE us.id_distrito = p_distrito AND inc.id_estado <> 4
	ORDER BY fecha_publicacion DESC
	LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener incidencias de un distrito sin verificación de likes (muro-administrador)
CREATE OR REPLACE FUNCTION lista_incidente_distrito(
	p_distrito INT,
    p_limit INT, 
    p_offset INT
)
RETURNS TABLE (
    id_incidencia INT,
    fecha_publicacion TIMESTAMP,
    descripcion VARCHAR,
    ubicacion VARCHAR,
    imagen VARCHAR,
    total_votos INT,
    estado VARCHAR,
    usuario VARCHAR,
    usuario_foto VARCHAR,
    categoria VARCHAR,
    latitud DECIMAL,
    longitud DECIMAL,
    tiene_like BOOLEAN
) 
AS $$
BEGIN
    RETURN QUERY
    SELECT inc.id_incidencia, inc.fecha_publicacion, inc.descripcion,
	   inc.ubicacion, inc.imagen, inc.total_votos, est.nombre AS estado,
	   us.nombre AS usuario, 
       us.foto_perfil AS usuario_foto,
       cat.nombre AS categoria, inc.latitud, inc.longitud,
       COALESCE(tiene_like, false)
	FROM incidencia AS inc
	INNER JOIN usuarios AS us ON us.dni = inc.dni
	INNER JOIN estado AS est ON est.id_estado = inc.id_estado
	INNER JOIN categoria AS cat ON cat.id_categoria = inc.id_categoria
	WHERE us.id_distrito = p_distrito AND inc.id_estado <> 4
	ORDER BY fecha_publicacion DESC
	LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

-- Función para obtener incidencias de un distrito sin verificación de likes (muro-administrador) por orden de likes
CREATE OR REPLACE FUNCTION lista_consolidado_distrito(
	p_distrito INT,
    p_limit INT, 
    p_offset INT
)
RETURNS TABLE (
    id_incidencia INT,
    fecha_publicacion TIMESTAMP,
    descripcion VARCHAR,
    ubicacion VARCHAR,
    imagen VARCHAR,
    total_votos INT,
    estado VARCHAR,
    usuario VARCHAR,
    usuario_foto VARCHAR,
    categoria VARCHAR,
    latitud DECIMAL,
    longitud DECIMAL,
    tiene_like BOOLEAN
) 
AS $$
BEGIN
    RETURN QUERY
    SELECT inc.id_incidencia, inc.fecha_publicacion, inc.descripcion,
        inc.ubicacion, inc.imagen, inc.total_votos, est.nombre AS estado,
        us.nombre AS usuario, 
        us.foto_perfil AS usuario_foto,
        cat.nombre AS categoria, inc.latitud, inc.longitud,
	    COALESCE(tiene_like, false)
	FROM incidencia_consolidado AS incco
	INNER JOIN incidencia AS inc ON inc.id_incidencia = incco.id_incidencia
	INNER JOIN usuarios AS us ON us.dni = inc.dni
	INNER JOIN estado AS est ON est.id_estado = inc.id_estado
	INNER JOIN categoria AS cat ON cat.id_categoria = inc.id_categoria
	WHERE us.id_distrito = p_distrito AND inc.id_estado <> 4
	ORDER BY fecha_publicacion DESC
	LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

--Funcion para ordenar 
CREATE OR REPLACE FUNCTION lista_incidente_distrito_masVotos(
    p_distrito INT,
    p_limit INT, 
    p_offset INT
)
RETURNS TABLE (
    id_incidencia INT,
    fecha_publicacion TIMESTAMP,
    descripcion VARCHAR,
    ubicacion VARCHAR,
    imagen VARCHAR,
    total_votos INT,
    estado VARCHAR,
    usuario VARCHAR,
    usuario_foto VARCHAR,
    categoria VARCHAR,
    latitud DECIMAL,
    longitud DECIMAL,
    tiene_like BOOLEAN
) 
AS $$
BEGIN
    RETURN QUERY
    SELECT 
        inc.id_incidencia, 
        inc.fecha_publicacion, 
        inc.descripcion,
        inc.ubicacion, 
        inc.imagen, 
        inc.total_votos, 
        est.nombre AS estado,
        us.nombre AS usuario, 
        us.foto_perfil AS usuario_foto,
        cat.nombre AS categoria, 
        inc.latitud, 
        inc.longitud,
        COALESCE(tiene_like, false)
    FROM incidencia AS inc
    INNER JOIN usuarios AS us ON us.dni = inc.dni
    INNER JOIN estado AS est ON est.id_estado = inc.id_estado
    INNER JOIN categoria AS cat ON cat.id_categoria = inc.id_categoria
    WHERE us.id_distrito = p_distrito AND inc.id_estado <> 4
    ORDER BY total_votos DESC -- Ordenar por total_votos de forma descendente
    LIMIT p_limit OFFSET p_offset;
END;
$$ LANGUAGE plpgsql;

INSERT INTO Rol (nombre) VALUES ('ADMINISTRADOR');
INSERT INTO Rol (nombre) VALUES ('MODERADOR');
INSERT INTO Rol (nombre) VALUES ('USUARIO');

INSERT INTO Estado(nombre) VALUES('COMPLETADO');
INSERT INTO Estado(nombre) VALUES('EN PROCESO');
INSERT INTO Estado(nombre) VALUES('PENDIENTE');
INSERT INTO Estado(nombre) VALUES('DESHABILITADO');

INSERT INTO Categoria(nombre) VALUES('Red Eléctrica');
INSERT INTO Categoria(nombre) VALUES('Agua y Saneamiento');
INSERT INTO Categoria(nombre) VALUES('Vía Pública');
INSERT INTO Categoria(nombre) VALUES('Infraestructura de Transporte');
INSERT INTO Categoria(nombre) VALUES('Espacios Públicos');
INSERT INTO Categoria(nombre) VALUES('Seguridad Ciudadana');
INSERT INTO Categoria(nombre) VALUES('Vivienda y Edificaciones');

INSERT INTO Departamento (nombre) VALUES ('La Libertad');

INSERT INTO Provincia (nombre,id_departamento) VALUES ('Trujillo', 1);

INSERT INTO Distrito (nombre,id_provincia) VALUES ('Trujillo', 1);
INSERT INTO Distrito (nombre,id_provincia) VALUES ('Florencia de Mora', 1);

/*COORDENADAS PARA TRUJIYORK*/
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.103146, -79.062879, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.104493, -79.063436, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.105478, -79.063341, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.107865, -79.063813, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.110619, -79.065006, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.115746, -79.063222, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.116615, -79.061825, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.118622, -79.060946, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.120534, -79.053640, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.121214, -79.053833, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.125536, -79.046183, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.129160, -79.042614, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.127641, -79.041284, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.128353, -79.040414, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.130210, -79.035300, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.137270, -79.027466, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.142834, -79.027462, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.151245, -79.026709, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.148568, -79.021363, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.145928, -79.017765, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.142051, -79.009472, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.139866, -79.004394, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.127715, -78.992732, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.126945, -78.982755, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.120983, -78.984860, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.112947, -78.991121, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.112972, -79.000767, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.106593, -78.994017, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.103246, -79.000894, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.099318, -78.994757, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.093636, -78.995732, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.088754, -78.991438, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.087597, -79.001937, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.087267, -79.007005, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.085873, -79.014995, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.089117, -79.022809, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.088757, -79.030790, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.086954, -79.039818, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.083139, -79.049954, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.094135, -79.055036, 1);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.104484, -79.058951, 1);
/*COORDENADAS PARA FLORENCIA DE MORAZZZZZZZZ*/
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.079636, -79.031870, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.083399, -79.030571, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES (-8.083967, -79.030096 , 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.083956, -79.029777, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.084610, -79.029761, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.084641, -79.030463, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.085263, -79.030160, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.086280, -79.030021, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.086668, -79.030351, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.087557, -79.030329, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.087533, -79.032183, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088633, -79.030866, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088468, -79.029385, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088872, -79.028921, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088532, -79.028425, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088943, -79.026048, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.089121, -79.023575, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.089158, -79.023197, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088333, -79.021920, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.088423, -79.019724, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.086009, -79.016274, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.085841, -79.015872, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.085138, -79.015993, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.084899, -79.014501, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.084085, -79.014536, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.079888, -79.011909, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.075859, -79.013027, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.072649, -79.014040, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.076322, -79.025046, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.077227, -79.024948, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.077740, -79.027963, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.078212, -79.027896, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.078107, -79.028351, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.078577, -79.029037, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.078886, -79.029035, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.078911, -79.029981, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.079626, -79.029976, 2);
INSERT INTO Coordenada_Distrito (latitud, longitud, id_distrito) VALUES ( -8.079640, -79.031844, 2);

INSERT INTO Usuarios VALUES ('32542163','Mario', 'Cortez',
'ConsejoTrujillo@hotmail.com','$2a$10$B8vL0Pb3h/y9RsyVL5D9Z.sqWh6YPoS6NmV29OQy2sBjYpxp9ovRG','asdasd.jpg',NULL,1,1,NULL,true, false);
INSERT INTO Usuarios VALUES ('48264723','Juan', 'Rodrigues',
'juanrodrigues@hotmail.com','$2a$10$j3OpqR.jwe83sQSUJpBzLe9XNR7ixgZdgW8KlGGSzWnhGZyfbbno2','https://raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/foto_perfil_default.png',NULL,2,1,NULL,true, false);
INSERT INTO Usuarios VALUES ('21443227','Antony', 'Lujan',
'antonylujan@hotmail.com','$2a$10$qiFEYY..M21DKwLNWxw.dewSw5/EA/9ee8EE6pvPzKn68wHX/lXtO','https://raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/foto_perfil_default.png',NULL,3,1,NULL,true, false);
INSERT INTO Usuarios VALUES ('74390812','Harold', 'Alvarez',
'haroldalvarez@hotmail.com','$2a$10$r1ZEETRx1jIhOYqSHNM1UORaRSjn5fX9y6RpzcSsHUh/QaiMtRMvq','https://raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/foto_perfil_default.png',NULL,3,1,NULL,true, false);

INSERT INTO Incidencia (fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,DNI,id_categoria, latitud, longitud) 
VALUES ('2024-10-03 15:30:00','Se ha producido un corte de suministro eléctrico en la zona desde las 15:30 horas. Los vecinos han notado que la luz se apagó repentinamente y no ha vuelto desde entonces.','3 De Octubre 809, Florencia de Mora 13002',
'https://images-ext-1.discordapp.net/external/ulcm2eKxbYUB_Xzqp2fTdiH-695e3PoQiXBGCCG23d4/https/raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/incidentesejemplo1.jpg?format=webp',0,3,'21443227',1,-8.109726,-79.021246);

INSERT INTO Incidencia (fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,DNI,id_categoria, latitud, longitud) 
VALUES ('2024-10-04 10:02:00','se ha interrumpido el suministro de agua potable en la comunidad. Los vecinos han notado que el agua ha dejado de fluir por las tuberías, lo que ha generado preocupación por la falta de acceso a agua limpia.','9 de Octubre 896-994, Florencia de Mora 13002',
'https://images-ext-1.discordapp.net/external/dIskgpXZWaml0yX4vRiVf6XKbtxPDiqykSowk5uLNSo/https/raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/posteluz.jpg?format=webp',0,3,'74390812',2,-8.114284,-79.015999);

INSERT INTO Incidencia (fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,DNI,id_categoria, latitud, longitud) 
VALUES ('2024-10-05 16:20:15','Se ha reportado un bache peligroso en la calle Principal, a la altura del número 922-1012, que ha causado varios accidentes menores. El bache tiene aproximadamente 1 metro de diámetro y 15 cm de profundidad, lo que representa un riesgo para los vehículos y peatones.','Cesar Vallejo 922-1012, Víctor Larco Herrera 13002',
'https://images-ext-1.discordapp.net/external/dIskgpXZWaml0yX4vRiVf6XKbtxPDiqykSowk5uLNSo/https/raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/posteluz.jpg?format=webp',0,3,'21443227',3,-8.110275,-79.027882);

INSERT INTO Incidencia (fecha_publicacion,descripcion,ubicacion,imagen,total_votos,id_estado,DNI,id_categoria, latitud, longitud) 
VALUES ('2024-10-05 16:20:20','La señal de "Pare" estaba parcialmente cubierta por vegetación, lo que dificultaba su visibilidad para los conductores que se aproximaban. Además, la señal de "Ceda el Paso" estaba dañada y no era legible, lo que generó confusión entre los vehículos que circulaban por la zona.','Alfonso Ugarte 1999-1885, Florencia de Mora 13002',
'https://images-ext-1.discordapp.net/external/ulcm2eKxbYUB_Xzqp2fTdiH-695e3PoQiXBGCCG23d4/https/raw.githubusercontent.com/KevinGM02/Galeria-Imagenes-Fixura/main/imagenes/incidentesejemplo1.jpg?format=webp',0,3,'74390812',4,-8.112748,-79.033996);

INSERT INTO incidencia_consolidado VALUES('48264723', 4, '2024-10-05 16:20:20');
INSERT INTO incidencia_consolidado VALUES('48264723', 3, '2024-10-05 16:20:15');


DROP TABLE IF EXISTS usuarios_red_one;

CREATE TABLE usuarios_red_one(
  
  idusuario integer,
  nombre_completo,
  telefono integer,
  fecha_creacion date,
  usuario varchar(45),
  contraseña text,
  codigo_referido_para_usuario varchar(45),
  codigo_referido_registro varchar(45),

  primary key (idusuario,codigo_referido_para_usuario);

);

DROP TABLE IF EXISTS codigos_usuarios_red_one;
CREATE TABLE codigos_usuarios_red_one( 
 idusuario integer,
 idperfil integer,
 fichasregistradas text,
 totalfichas integer,
 estatus  varchar(45)

);

DROP TABLE IF EXISTS user_perfiles;
CREATE TABLE user_perfiles( 
 id integer,
 nombre varchar(45),
 
 primary key(id);
  
);

DROP TABLE IF EXISTS fichas_hotspot_red_one;
CREATE TABLE fichas_hotspot_red_one( 
 codigo varchar(45),
 idperfil integer,
 
 primary key(codigo,idperfil)
  
);

DROP TABLE IF EXISTS fichas_hotspot_usadas;
CREATE TABLE fichas_hotspot_usadas(
   codigo varchar(20),
   perfil varchar(20),
   fecha_uso date,

   primary key(codigo)
   

);
